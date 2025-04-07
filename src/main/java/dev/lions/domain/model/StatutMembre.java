// src/main/java/dev/lions/domain/model/StatutMembre.java
package dev.lions.domain.model;

/**
 * Représente les différents statuts possibles pour un membre.
 */
public enum StatutMembre {

  /**
   * Membre à jour de ses cotisations et pleinement actif.
   */
  ACTIF,

  /**
   * Membre n'ayant pas payé ses cotisations.
   */
  INACTIF,

  /**
   * Membre temporairement suspendu.
   */
  SUSPENDU,

  /**
   * Membre radié définitivement de l'association.
   */
  RADIE;
}
