// src/main/java/dev/lions/domain/model/Membre.java
package dev.lions.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entité représentant un membre de l'association ou de la mutuelle.
 * Contient toutes les informations personnelles et de statut d'un membre.
 *
 * @author UnionFlow
 * @version 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"photo", "parrain"})
public class Membre {

  private UUID id;
  private String code;
  private String nom;
  private String prenom;
  private String email;
  private String telephone;
  private LocalDate dateNaissance;
  private String adresse;
  private String profession;
  private byte[] photo;
  private String photoUrl;
  private StatutMembre statut;
  private LocalDate dateAdhesion;
  private LocalDateTime dateModification;
  private Membre parrain;
  private boolean actif;

  /**
   * Indique si le membre est actif et à jour de ses cotisations.
   *
   * @return true si le membre est en règle
   */
  public boolean estEnRegle() {
    return actif && statut == StatutMembre.ACTIF;
  }

  /**
   * Obtient le nom complet du membre (prénom + nom).
   *
   * @return Le nom complet formaté
   */
  public String getNomComplet() {
    return prenom + " " + nom;
  }

  /**
   * Effectue une mise à jour de la date de modification.
   */
  public void mettreAJourDateModification() {
    this.dateModification = LocalDateTime.now();
  }
}