// src/main/java/dev/lions/application/mapper/MembreMapperImpl.java
package dev.lions.application.mapper;

import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;

import dev.lions.application.dto.request.CreationMembreRequest;
import dev.lions.application.dto.response.MembreResponse;
import dev.lions.domain.model.Membre;
import dev.lions.domain.model.StatutMembre;
import dev.lions.infrastructure.persistence.entity.MembreEntity;

/**
 * Implémentation manuelle du MembreMapper.
 */
@ApplicationScoped
public class MembreMapperImpl implements MembreMapper {

  @Override
  public MembreEntity toEntity(Membre membre) {
    if (membre == null) {
      return null;
    }

    return MembreEntity.builder()
                       .nom(membre.getNom())
                       .prenom(membre.getPrenom())
                       .email(membre.getEmail())
                       .telephone(membre.getTelephone())
                       .dateNaissance(membre.getDateNaissance())
                       .adresse(membre.getAdresse())
                       .profession(membre.getProfession())
                       .photo(membre.getPhoto())
                       .photoUrl(membre.getPhotoUrl())
                       .statut(membre.getStatut())
                       .dateAdhesion(membre.getDateAdhesion())
                       .dateModification(membre.getDateModification())
                       .actif(membre.isActif())
                       .code(membre.getCode())
                       .build();
  }

  @Override
  public Membre entityToDomaine(MembreEntity entity) {
    if (entity == null) {
      return null;
    }

    Membre membre = Membre.builder()
                          .id(entity.getId())
                          .code(entity.getCode())
                          .nom(entity.getNom())
                          .prenom(entity.getPrenom())
                          .email(entity.getEmail())
                          .telephone(entity.getTelephone())
                          .dateNaissance(entity.getDateNaissance())
                          .adresse(entity.getAdresse())
                          .profession(entity.getProfession())
                          .photo(entity.getPhoto())
                          .photoUrl(entity.getPhotoUrl())
                          .statut(entity.getStatut())
                          .dateAdhesion(entity.getDateAdhesion())
                          .dateModification(entity.getDateModification())
                          .actif(entity.isActif())
                          .build();

    // Gestion de la relation parrain si elle existe
    if (entity.getParrain() != null) {
      membre.setParrain(entityToDomaine(entity.getParrain()));
    }

    return membre;
  }

  @Override
  public Membre requestToDomaine(CreationMembreRequest request) {
    if (request == null) {
      return null;
    }

    return Membre.builder()
                 .nom(request.getNom())
                 .prenom(request.getPrenom())
                 .email(request.getEmail())
                 .telephone(request.getTelephone())
                 .dateNaissance(request.getDateNaissance())
                 .adresse(request.getAdresse())
                 .profession(request.getProfession())
                 .photoUrl(request.getPhotoUrl())
                 .statut(StatutMembre.ACTIF)
                 .dateAdhesion(java.time.LocalDate.now())
                 .actif(true)
                 .build();
  }

  @Override
  public MembreResponse toResponse(Membre membre) {
    if (membre == null) {
      return null;
    }

    MembreResponse.MembreResponseBuilder response = MembreResponse.builder()
                                                                  .id(membre.getId())
                                                                  .code(membre.getCode())
                                                                  .nom(membre.getNom())
                                                                  .prenom(membre.getPrenom())
                                                                  .nomComplet(membre.getNomComplet())
                                                                  .email(membre.getEmail())
                                                                  .telephone(membre.getTelephone())
                                                                  .dateNaissance(membre.getDateNaissance())
                                                                  .adresse(membre.getAdresse())
                                                                  .profession(membre.getProfession())
                                                                  .photoUrl(membre.getPhotoUrl())
                                                                  .statut(membre.getStatut())
                                                                  .dateAdhesion(membre.getDateAdhesion())
                                                                  .dateModification(membre.getDateModification())
                                                                  .actif(membre.isActif())
                                                                  .enRegle(membre.estEnRegle());

    if (membre.getParrain() != null) {
      response.idParrain(membre.getParrain().getId());
      response.nomParrain(membre.getParrain().getNomComplet());
    }

    return response.build();
  }

  @Override
  public List<MembreResponse> toResponseList(List<Membre> membres) {
    if (membres == null) {
      return null;
    }

    List<MembreResponse> list = new ArrayList<>(membres.size());
    for (Membre membre : membres) {
      list.add(toResponse(membre));
    }

    return list;
  }

  @Override
  public void updateMembreFromRequest(CreationMembreRequest request, Membre membre) {
    if (request == null) {
      return;
    }

    membre.setNom(request.getNom());
    membre.setPrenom(request.getPrenom());
    membre.setEmail(request.getEmail());
    membre.setTelephone(request.getTelephone());
    membre.setDateNaissance(request.getDateNaissance());
    membre.setAdresse(request.getAdresse());
    membre.setProfession(request.getProfession());
    membre.setPhotoUrl(request.getPhotoUrl());
  }
}
