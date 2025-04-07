// src/main/java/dev/lions/domain/repository/MembreRepository.java
package dev.lions.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import dev.lions.domain.model.Membre;
import dev.lions.domain.model.StatutMembre;

/**
 * Interface du repository pour la gestion des membres.
 */
public interface MembreRepository {

  /**
   * Enregistre un nouveau membre ou met à jour un membre existant.
   *
   * @param membre Le membre à sauvegarder
   * @return Le membre sauvegardé avec son ID
   */
  Membre sauvegarder(Membre membre);

  /**
   * Recherche un membre par son identifiant unique.
   *
   * @param id L'identifiant du membre
   * @return Un Optional contenant le membre s'il existe
   */
  Optional<Membre> chercherParId(UUID id);

  /**
   * Recherche un membre par son code unique.
   *
   * @param code Le code du membre
   * @return Un Optional contenant le membre s'il existe
   */
  Optional<Membre> chercherParCode(String code);

  /**
   * Recherche un membre par son adresse email.
   *
   * @param email L'email du membre
   * @return Un Optional contenant le membre s'il existe
   */
  Optional<Membre> chercherParEmail(String email);

  /**
   * Liste tous les membres.
   *
   * @return La liste des membres
   */
  List<Membre> listerTous();

  /**
   * Liste les membres selon leur statut.
   *
   * @param statut Le statut recherché
   * @return La liste des membres ayant ce statut
   */
  List<Membre> listerParStatut(StatutMembre statut);

  /**
   * Vérifie si un email est déjà utilisé par un membre.
   *
   * @param email L'email à vérifier
   * @return true si l'email est déjà utilisé
   */
  boolean emailExiste(String email);

  /**
   * Supprime un membre (logiquement ou physiquement selon l'implémentation).
   *
   * @param id L'identifiant du membre à supprimer
   * @return true si la suppression a réussi
   */
  boolean supprimer(UUID id);
}
