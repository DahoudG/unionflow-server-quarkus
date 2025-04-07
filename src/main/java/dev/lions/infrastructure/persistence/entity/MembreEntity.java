// src/main/java/dev/lions/infrastructure/persistence/entity/MembreEntity.java
package dev.lions.infrastructure.persistence.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import dev.lions.domain.model.StatutMembre;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entité JPA représentant un membre dans la base de données.
 */
@Entity
@Table(name = "membre", schema = "unionflow",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_membre_code", columnNames = "code"),
           @UniqueConstraint(name = "uk_membre_email", columnNames = "email")
       })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembreEntity extends PanacheEntityBase {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @NotBlank
  @Size(min = 2, max = 50)
  @Column(name = "code", nullable = false, length = 50)
  private String code;

  @NotBlank
  @Size(min = 2, max = 100)
  @Column(name = "nom", nullable = false, length = 100)
  private String nom;

  @NotBlank
  @Size(min = 2, max = 100)
  @Column(name = "prenom", nullable = false, length = 100)
  private String prenom;

  @Email
  @Size(max = 150)
  @Column(name = "email", length = 150)
  private String email;

  @Size(max = 20)
  @Column(name = "telephone", length = 20)
  private String telephone;

  @Past
  @Column(name = "date_naissance")
  private LocalDate dateNaissance;

  @Column(name = "adresse")
  private String adresse;

  @Size(max = 100)
  @Column(name = "profession", length = 100)
  private String profession;

  @Lob
  @Column(name = "photo")
  private byte[] photo;

  @Column(name = "photo_url", length = 255)
  private String photoUrl;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "statut", nullable = false, length = 20)
  private StatutMembre statut;

  @NotNull
  @Column(name = "date_adhesion", nullable = false)
  private LocalDate dateAdhesion;

  @Column(name = "date_modification")
  private LocalDateTime dateModification;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_parrain")
  private MembreEntity parrain;

  @NotNull
  @Column(name = "actif", nullable = false)
  private boolean actif;

  @PrePersist
  public void prePersist() {
    if (dateAdhesion == null) {
      dateAdhesion = LocalDate.now();
    }
    dateModification = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    dateModification = LocalDateTime.now();
  }
}