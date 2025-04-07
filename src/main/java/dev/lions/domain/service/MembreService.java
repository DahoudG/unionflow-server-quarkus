package dev.lions.domain.service;

import dev.lions.domain.model.Membre;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface définissant les services de domaine pour la gestion des membres.
 * Représente un port entrant dans l'architecture hexagonale.
 *
 * @author UnionFlow Team
 */
public interface MembreService {

  /**
   * Crée un nouveau membre.
   *
   * @param membre les données du membre à créer
   * @return le membre créé avec son identifiant
   * @throws IllegalArgumentException si le membre a un email déjà utilisé
   */
  Membre creerMembre(Membre membre);

  /**
   * Met à jour les informations d'un membre existant.
   *
   * @param id l'identifiant du membre à mettre à jour
   * @param membre les nouvelles données du membre
   * @return le membre mis à jour
   * @throws IllegalArgumentException si le membre n'existe pas ou l'email est déjà utilisé
   */
  Membre mettreAJourMembre(UUID id, Membre membre);

  /**
   * Récupère un membre par son identifiant.
   *
   * @param id l'identifiant du membre
   * @return un Optional contenant le membre s'il existe
   */
  Optional<Membre> trouverMembreParId(UUID id);

  /**
   * Récupère un membre par son code unique.
   *
   * @param code le code du membre
   * @return un Optional contenant le membre s'il existe
   */
  Optional<Membre> trouverMembreParCode(String code);

  /**
   * Récupère tous les membres actifs.
   *
   * @return la liste des membres actifs
   */
  List<Membre> listerMembresActifs();

  /**
   * Récupère les membres par leur statut.
   *
   * @param statut le statut des membres à rechercher
   * @return la liste des membres ayant le statut spécifié
   */
  List<Membre> listerMembresParStatut(String statut);

  /**
   * Récupère les membres parrainés par un membre spécifique.
   *
   * @param idParrain l'identifiant du parrain
   * @return la liste des membres parrainés
   */
  List<Membre> listerFilleuls(UUID idParrain);

  /**
   * Désactive un membre (radiation ou démission).
   *
   * @param id l'identifiant du membre à désactiver
   * @param motif le motif de la désactivation
   * @return true si l'opération a réussi
   */
  boolean desactiverMembre(UUID id, String motif);

  /**
   * Change le statut d'un membre.
   *
   * @param id l'identifiant du membre
   * @param nouveauStatut le nouveau statut
   * @return le membre avec son statut mis à jour
   */
  Membre changerStatutMembre(UUID id, String nouveauStatut);
}
