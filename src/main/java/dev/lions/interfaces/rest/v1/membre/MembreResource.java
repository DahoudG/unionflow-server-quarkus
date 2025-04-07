// src/main/java/dev/lions/interfaces/rest/v1/membre/MembreResource.java
package dev.lions.interfaces.rest.v1.membre;

import java.util.List;
import java.util.UUID;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import dev.lions.application.dto.request.CreationMembreRequest;
import dev.lions.application.dto.response.MembreResponse;
import dev.lions.application.service.MembreService;
import dev.lions.domain.model.StatutMembre;

/**
 * Point d'entrée REST pour la gestion des membres.
 */
@Path("/api/v1/membres")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityScheme(securitySchemeName = "oidc", type = SecuritySchemeType.OAUTH2)
@Tag(name = "Membre", description = "Opérations liées à la gestion des membres")
public class MembreResource {

  @Inject
  MembreService membreService;

  @POST
  @Operation(summary = "Créer un nouveau membre",
             description = "Crée un nouveau membre avec les informations fournies")
  @APIResponse(responseCode = "201",
               description = "Membre créé avec succès",
               content = @Content(schema = @Schema(implementation = MembreResponse.class)))
  @APIResponse(responseCode = "400", description = "Requête invalide")
  @APIResponse(responseCode = "409", description = "Email déjà utilisé")
  @SecurityRequirement(name = "oidc")
  @RolesAllowed({"admin", "secretaire"})
  public Response creerMembre(@Valid CreationMembreRequest request) {
    MembreResponse membre = membreService.creerMembre(request);
    return Response.status(Status.CREATED)
                   .entity(membre)
                   .build();
  }

  @GET
  @Operation(summary = "Lister tous les membres",
             description = "Récupère la liste de tous les membres")
  @APIResponse(responseCode = "200",
               description = "Liste des membres récupérée avec succès")
  @SecurityRequirement(name = "oidc")
  @RolesAllowed({"admin", "secretaire", "membre"})
  public Response listerMembres(@QueryParam("statut") StatutMembre statut) {
    List<MembreResponse> membres;

    if (statut != null) {
      membres = membreService.listerMembresParStatut(statut);
    } else {
      membres = membreService.listerTousMembres();
    }

    return Response.ok(membres).build();
  }

  @GET
  @Path("/{id}")
  @Operation(summary = "Récupérer un membre par ID",
             description = "Récupère les informations détaillées d'un membre par son ID")
  @APIResponse(responseCode = "200",
               description = "Membre récupéré avec succès",
               content = @Content(schema = @Schema(implementation = MembreResponse.class)))
  @APIResponse(responseCode = "404", description = "Membre non trouvé")
  @SecurityRequirement(name = "oidc")
  @RolesAllowed({"admin", "secretaire", "membre"})
  public Response getMembre(
      @Parameter(description = "ID du membre", required = true)
      @PathParam("id") UUID id) {
    MembreResponse membre = membreService.getMembre(id);
    return Response.ok(membre).build();
  }

  @PUT
  @Path("/{id}")
  @Operation(summary = "Mettre à jour un membre",
             description = "Met à jour les informations d'un membre existant")
  @APIResponse(responseCode = "200",
               description = "Membre mis à jour avec succès",
               content = @Content(schema = @Schema(implementation = MembreResponse.class)))
  @APIResponse(responseCode = "404", description = "Membre non trouvé")
  @APIResponse(responseCode = "409", description = "Email déjà utilisé")
  @SecurityRequirement(name = "oidc")
  @RolesAllowed({"admin", "secretaire"})
  public Response mettreAJourMembre(
      @Parameter(description = "ID du membre", required = true)
      @PathParam("id") UUID id,
      @Valid CreationMembreRequest request) {
    MembreResponse membre = membreService.mettreAJourMembre(id, request);
    return Response.ok(membre).build();
  }

  @PUT
  @Path("/{id}/statut")
  @Operation(summary = "Changer le statut d'un membre",
             description = "Met à jour le statut d'un membre existant")
  @APIResponse(responseCode = "200",
               description = "Statut du membre mis à jour avec succès",
               content = @Content(schema = @Schema(implementation = MembreResponse.class)))
  @APIResponse(responseCode = "404", description = "Membre non trouvé")
  @SecurityRequirement(name = "oidc")
  @RolesAllowed({"admin", "secretaire"})
  public Response changerStatutMembre(
      @Parameter(description = "ID du membre", required = true)
      @PathParam("id") UUID id,
      @Parameter(description = "Nouveau statut", required = true)
      @QueryParam("statut") StatutMembre nouveauStatut) {
    MembreResponse membre = membreService.changerStatutMembre(id, nouveauStatut);
    return Response.ok(membre).build();
  }

  @DELETE
  @Path("/{id}")
  @Operation(summary = "Supprimer un membre",
             description = "Supprime un membre existant")
  @APIResponse(responseCode = "204", description = "Membre supprimé avec succès")
  @APIResponse(responseCode = "404", description = "Membre non trouvé")
  @SecurityRequirement(name = "oidc")
  @RolesAllowed({"admin"})
  public Response supprimerMembre(
      @Parameter(description = "ID du membre", required = true)
      @PathParam("id") UUID id) {
    membreService.supprimerMembre(id);
    return Response.noContent().build();
  }
}
