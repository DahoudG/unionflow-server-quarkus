// src/main/java/dev/lions/application/dto/request/CreationMembreRequest.java
package dev.lions.application.dto.request;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la création d'un nouveau membre.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreationMembreRequest {

  @NotBlank(message = "Le nom est obligatoire")
  @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
  private String nom;

  @NotBlank(message = "Le prénom est obligatoire")
  @Size(min = 2, max = 100, message = "Le prénom doit contenir entre 2 et 100 caractères")
  private String prenom;

  @Email(message = "L'email doit être valide")
  @Size(max = 150, message = "L'email ne peut pas dépasser 150 caractères")
  private String email;

  @Size(max = 20, message = "Le numéro de téléphone ne peut pas dépasser 20 caractères")
  private String telephone;

  @Past(message = "La date de naissance doit être dans le passé")
  private LocalDate dateNaissance;

  private String adresse;

  @Size(max = 100, message = "La profession ne peut pas dépasser 100 caractères")
  private String profession;

  private String photoUrl;

  private UUID idParrain;
}
