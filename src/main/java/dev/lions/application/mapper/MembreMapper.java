// src/main/java/dev/lions/application/mapper/MembreMapper.java
package dev.lions.application.mapper;

import java.util.List;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import dev.lions.application.dto.request.CreationMembreRequest;
import dev.lions.application.dto.response.MembreResponse;
import dev.lions.domain.model.Membre;
import dev.lions.infrastructure.persistence.entity.MembreEntity;

/**
 * Mapper pour convertir entre les différentes représentations d'un membre.
 */
public interface MembreMapper {

  // Mappings du domaine vers l'entity
  MembreEntity toEntity(Membre membre);

  // Mappings de l'entity vers le domaine
  Membre entityToDomaine(MembreEntity entity);

  // Mappings de la request vers le domaine
  Membre requestToDomaine(CreationMembreRequest request);

  // Mappings du domaine vers la response
  MembreResponse toResponse(Membre membre);

  // Transforme une liste de membres en liste de réponses
  List<MembreResponse> toResponseList(List<Membre> membres);

  // Met à jour un membre existant avec les valeurs d'une requête
  void updateMembreFromRequest(CreationMembreRequest request, @MappingTarget Membre membre);
}
