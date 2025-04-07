// src/main/java/dev/lions/application/dto/response/MembreResponse.java
package dev.lions.application.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import dev.lions.domain.model.StatutMembre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour retourner les informations d'un membre.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembreResponse {

  private UUID id;
  private String code;
  private String nom;
  private String prenom;
  private String nomComplet;
  private String email;
  private String telephone;
  private LocalDate dateNaissance;
  private String adresse;
  private String profession;
  private String photoUrl;
  private StatutMembre statut;
  private LocalDate dateAdhesion;
  private LocalDateTime dateModification;
  private UUID idParrain;
  private String nomParrain;
  private boolean actif;
  private boolean enRegle;
}
