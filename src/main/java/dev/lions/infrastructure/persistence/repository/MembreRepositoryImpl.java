package dev.lions.infrastructure.persistence.repository;

import dev.lions.application.mapper.MembreMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import dev.lions.domain.model.Membre;
import dev.lions.domain.model.StatutMembre;
import dev.lions.domain.repository.MembreRepository;
import dev.lions.infrastructure.persistence.entity.MembreEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

/**
 * Implémentation du repository pour la gestion des membres.
 * Utilise l'approche PanacheRepositoryBase pour une meilleure typification.
 */
@ApplicationScoped
public class MembreRepositoryImpl implements MembreRepository, PanacheRepositoryBase<MembreEntity, UUID> {

  @Inject
  MembreMapper membreMapper;

  @Override
  @Transactional
  public Membre sauvegarder(Membre membre) {
    MembreEntity entity;

    if (membre.getId() != null) {
      entity = findById(membre.getId());
      if (entity == null) {
        entity = membreMapper.toEntity(membre);
      } else {
        // Mettre à jour l'entité existante
        entity.setNom(membre.getNom());
        entity.setPrenom(membre.getPrenom());
        entity.setEmail(membre.getEmail());
        entity.setTelephone(membre.getTelephone());
        entity.setDateNaissance(membre.getDateNaissance());
        entity.setAdresse(membre.getAdresse());
        entity.setProfession(membre.getProfession());
        entity.setPhoto(membre.getPhoto());
        entity.setPhotoUrl(membre.getPhotoUrl());
        entity.setStatut(membre.getStatut());
        entity.setActif(membre.isActif());

        if (membre.getParrain() != null && membre.getParrain().getId() != null) {
          entity.setParrain(findById(membre.getParrain().getId()));
        }
      }
    } else {
      entity = membreMapper.toEntity(membre);

      // Génération du code unique si non existant
      if (entity.getCode() == null || entity.getCode().isEmpty()) {
        entity.setCode(genererCodeUnique(entity.getNom(), entity.getPrenom()));
      }

      // Liaison avec le parrain si nécessaire
      if (membre.getParrain() != null && membre.getParrain().getId() != null) {
        entity.setParrain(findById(membre.getParrain().getId()));
      }
    }

    persist(entity);
    flush();
    return membreMapper.entityToDomaine(entity);
  }

  @Override
  public Optional<Membre> chercherParId(UUID id) {
    return Optional.ofNullable(findById(id))
                   .map(membreMapper::entityToDomaine);
  }

  @Override
  public Optional<Membre> chercherParCode(String code) {
    return find("code", code)
        .firstResultOptional()
        .map(membreMapper::entityToDomaine);
  }

  @Override
  public Optional<Membre> chercherParEmail(String email) {
    return find("email", email)
        .firstResultOptional()
        .map(membreMapper::entityToDomaine);
  }

  @Override
  public List<Membre> listerTous() {
    return streamAll()
        .map(membreMapper::entityToDomaine)
        .collect(Collectors.toList());
  }

  @Override
  public List<Membre> listerParStatut(StatutMembre statut) {
    return list("statut", statut)
        .stream()
        .map(membreMapper::entityToDomaine)
        .collect(Collectors.toList());
  }

  @Override
  public boolean emailExiste(String email) {
    return count("email", email) > 0;
  }

  @Override
  @Transactional
  public boolean supprimer(UUID id) {
    return deleteById(id);
  }

  /**
   * Génère un code unique pour un nouveau membre.
   * Format: M-[Initiales]-[Année][Séquence]
   *
   * @param nom Le nom du membre
   * @param prenom Le prénom du membre
   * @return Un code unique généré
   */
  private String genererCodeUnique(String nom, String prenom) {
    String initiales = (prenom.substring(0, 1) + nom.substring(0, 1)).toUpperCase();
    String annee = String.valueOf(java.time.Year.now().getValue()).substring(2);

    // Trouver la dernière séquence utilisée cette année
    String prefixeCode = "M-" + initiales + "-" + annee;
    long sequence = count("code LIKE ?1", prefixeCode + "%") + 1;

    // Formater la séquence sur 4 chiffres
    return prefixeCode + String.format("%04d", sequence);
  }
}
