package dev.lions.infrastructure.persistence.mapper;

import dev.lions.domain.model.Membre;
import dev.lions.infrastructure.persistence.entity.MembreEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.UUID;

/**
 * Mapper pour convertir entre l'entité de domaine Membre et l'entité JPA MembreEntity.
 *
 * @author UnionFlow Team
 */
@Mapper(componentModel = "cdi")
public interface MembreEntityMapper {

  /**
   * Convertit une entité JPA en entité de domaine.
   *
   * @param entity l'entité JPA à convertir
   * @return l'entité de domaine correspondante
   */
  @Mapping(source = "parrain.id", target = "idParrain")
  Membre toDomain(MembreEntity entity);

  /**
   * Convertit une entité de domaine en entité JPA.
   *
   * @param domain l'entité de domaine à convertir
   * @return l'entité JPA correspondante
   */
  @Mapping(target = "parrain", source = "idParrain", qualifiedByName = "idToParrain")
  MembreEntity toEntity(Membre domain);

  /**
   * Convertit une liste d'entités JPA en liste d'entités de domaine.
   *
   * @param entities les entités JPA à convertir
   * @return les entités de domaine correspondantes
   */
  List<Membre> toDomainList(List<MembreEntity> entities);

  /**
   * Met à jour une entité JPA existante avec les données d'une entité de domaine.
   *
   * @param domain l'entité de domaine contenant les nouvelles données
   * @param entity l'entité JPA à mettre à jour
   */
  @Mapping(target = "parrain", source = "idParrain", qualifiedByName = "idToParrain")
  void updateEntityFromDomain(Membre domain, @MappingTarget MembreEntity entity);

  /**
   * Convertit un UUID en entité Parrain.
   * Ne crée qu'une référence par l'ID pour éviter de charger l'objet complet.
   *
   * @param id l'UUID du parrain
   * @return l'entité parrain avec uniquement l'ID défini
   */
  @Named("idToParrain")
  default MembreEntity idToParrain(UUID id) {
    if (id == null) {
      return null;
    }
    MembreEntity parrain = new MembreEntity();
    parrain.setId(id);
    return parrain;
  }
}
