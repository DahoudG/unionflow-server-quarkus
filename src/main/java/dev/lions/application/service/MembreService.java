// src/main/java/dev/lions/application/service/MembreService.java
package dev.lions.application.service;

import dev.lions.application.mapper.MembreMapper;
import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import dev.lions.application.dto.request.CreationMembreRequest;
import dev.lions.application.dto.response.MembreResponse;
import dev.lions.application.exception.EmailDejaUtiliseException;
import dev.lions.domain.model.Membre;
import dev.lions.domain.model.StatutMembre;
import dev.lions.domain.repository.MembreRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service pour gérer les opérations liées aux membres.
 */
@ApplicationScoped
public class MembreService {

  private static final Logger logger = LoggerFactory.getLogger(MembreService.class);

  @Inject
  MembreRepository membreRepository;

  @Inject
  MembreMapper membreMapper;

  /**
   * Crée un nouveau membre.
   *
   * @param request DTO contenant les informations du nouveau membre
   * @return Le membre créé avec son ID et code générés
   * @throws EmailDejaUtiliseException Si l'email est déjà utilisé
   */
  @Transactional
  public MembreResponse creerMembre(CreationMembreRequest request) {
    logger.info("Création d'un nouveau membre : {} {}", request.getPrenom(), request.getNom());

    // Vérifier si l'email n'est pas déjà utilisé
    if (request.getEmail() != null && !request.getEmail().isEmpty()) {
      if (membreRepository.emailExiste(request.getEmail())) {
        logger.warn("Tentative de création avec un email déjà utilisé : {}", request.getEmail());
        throw new EmailDejaUtiliseException("L'email " + request.getEmail() + " est déjà utilisé par un autre membre");
      }
    }

    // Convertir la requête en objet du domaine
    Membre membre = membreMapper.requestToDomaine(request);  // Utilisation de la méthode renommée

    // Associer le parrain si nécessaire
    if (request.getIdParrain() != null) {
      Membre parrain = membreRepository.chercherParId(request.getIdParrain())
                                       .orElseThrow(() -> new NotFoundException("Parrain non trouvé avec l'ID : " + request.getIdParrain()));
      membre.setParrain(parrain);
    }

    // Sauvegarder le membre
    Membre membreSauvegarde = membreRepository.sauvegarder(membre);
    logger.info("Membre créé avec succès. Code : {}, ID : {}", membreSauvegarde.getCode(), membreSauvegarde.getId());

    // Retourner la réponse
    return membreMapper.toResponse(membreSauvegarde);
  }

  /**
   * Récupère un membre par son ID.
   *
   * @param id L'ID du membre à récupérer
   * @return Les informations du membre
   * @throws NotFoundException Si le membre n'est pas trouvé
   */
  public MembreResponse getMembre(UUID id) {
    logger.info("Recherche du membre avec l'ID : {}", id);

    Membre membre = membreRepository.chercherParId(id)
                                    .orElseThrow(() -> {
                                      logger.warn("Membre non trouvé avec l'ID : {}", id);
                                      return new NotFoundException("Membre non trouvé avec l'ID : " + id);
                                    });

    return membreMapper.toResponse(membre);
  }

  /**
   * Liste tous les membres.
   *
   * @return La liste des membres
   */
  public List<MembreResponse> listerTousMembres() {
    logger.info("Récupération de la liste de tous les membres");

    List<Membre> membres = membreRepository.listerTous();
    return membreMapper.toResponseList(membres);
  }

  /**
   * Liste les membres filtrés par statut.
   *
   * @param statut Le statut pour filtrer
   * @return La liste des membres avec ce statut
   */
  public List<MembreResponse> listerMembresParStatut(StatutMembre statut) {
    logger.info("Récupération de la liste des membres avec le statut : {}", statut);

    List<Membre> membres = membreRepository.listerParStatut(statut);
    return membreMapper.toResponseList(membres);
  }

  /**
   * Met à jour les informations d'un membre existant.
   *
   * @param id L'ID du membre à mettre à jour
   * @param request DTO contenant les nouvelles informations
   * @return Les informations mises à jour du membre
   * @throws NotFoundException Si le membre n'est pas trouvé
   * @throws EmailDejaUtiliseException Si le nouvel email est déjà utilisé
   */
  @Transactional
  public MembreResponse mettreAJourMembre(UUID id, CreationMembreRequest request) {
    logger.info("Mise à jour du membre avec l'ID : {}", id);

    Membre membre = membreRepository.chercherParId(id)
                                    .orElseThrow(() -> {
                                      logger.warn("Tentative de mise à jour d'un membre inexistant avec l'ID : {}", id);
                                      return new NotFoundException("Membre non trouvé avec l'ID : " + id);
                                    });

    // Vérifier si le nouvel email n'est pas déjà utilisé par un autre membre
    if (request.getEmail() != null && !request.getEmail().equals(membre.getEmail()) &&
        !request.getEmail().isEmpty() && membreRepository.emailExiste(request.getEmail())) {
      logger.warn("Tentative de mise à jour avec un email déjà utilisé : {}", request.getEmail());
      throw new EmailDejaUtiliseException("L'email " + request.getEmail() + " est déjà utilisé par un autre membre");
    }

    // Mettre à jour le membre
    membreMapper.updateMembreFromRequest(request, membre);

    // Mettre à jour le parrain si nécessaire
    if (request.getIdParrain() != null &&
        (membre.getParrain() == null || !request.getIdParrain().equals(membre.getParrain().getId()))) {
      Membre parrain = membreRepository.chercherParId(request.getIdParrain())
                                       .orElseThrow(() -> new NotFoundException("Parrain non trouvé avec l'ID : " + request.getIdParrain()));
      membre.setParrain(parrain);
    }

    // Mettre à jour la date de modification
    membre.mettreAJourDateModification();

    // Sauvegarder les modifications
    Membre membreMisAJour = membreRepository.sauvegarder(membre);
    logger.info("Membre mis à jour avec succès. ID : {}", membreMisAJour.getId());

    return membreMapper.toResponse(membreMisAJour);
  }

  /**
   * Change le statut d'un membre.
   *
   * @param id L'ID du membre
   * @param nouveauStatut Le nouveau statut
   * @return Les informations mises à jour du membre
   * @throws NotFoundException Si le membre n'est pas trouvé
   */
  @Transactional
  public MembreResponse changerStatutMembre(UUID id, StatutMembre nouveauStatut) {
    logger.info("Changement du statut du membre {} vers {}", id, nouveauStatut);

    Membre membre = membreRepository.chercherParId(id)
                                    .orElseThrow(() -> new NotFoundException("Membre non trouvé avec l'ID : " + id));

    membre.setStatut(nouveauStatut);
    membre.mettreAJourDateModification();

    Membre membreMisAJour = membreRepository.sauvegarder(membre);
    logger.info("Statut du membre mis à jour avec succès. ID : {}, Nouveau statut : {}",
                membreMisAJour.getId(), membreMisAJour.getStatut());

    return membreMapper.toResponse(membreMisAJour);
  }

  /**
   * Supprime un membre.
   *
   * @param id L'ID du membre à supprimer
   * @return true si la suppression a réussi
   * @throws NotFoundException Si le membre n'est pas trouvé
   */
  @Transactional
  public boolean supprimerMembre(UUID id) {
    logger.info("Suppression du membre avec l'ID : {}", id);

    if (!membreRepository.chercherParId(id).isPresent()) {
      logger.warn("Tentative de suppression d'un membre inexistant avec l'ID : {}", id);
      throw new NotFoundException("Membre non trouvé avec l'ID : " + id);
    }

    boolean resultat = membreRepository.supprimer(id);
    if (resultat) {
      logger.info("Membre supprimé avec succès. ID : {}", id);
    } else {
      logger.warn("Échec de la suppression du membre. ID : {}", id);
    }

    return resultat;
  }
}
