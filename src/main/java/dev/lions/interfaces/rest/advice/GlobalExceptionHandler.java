// src/main/java/dev/lions/interfaces/rest/advice/GlobalExceptionHandler.java
package dev.lions.interfaces.rest.advice;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.lions.application.exception.EmailDejaUtiliseException;

/**
 * Gestionnaire global des exceptions pour l'API REST.
 */
@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @Override
  public Response toResponse(Exception exception) {
    logger.error("Exception interceptée: ", exception);

    if (exception instanceof NotFoundException) {
      return Response.status(Response.Status.NOT_FOUND)
                     .entity(new ErrorResponse("RESOURCE_NOT_FOUND", exception.getMessage()))
                     .build();
    }

    if (exception instanceof EmailDejaUtiliseException) {
      return Response.status(Response.Status.CONFLICT)
                     .entity(new ErrorResponse("EMAIL_ALREADY_EXISTS", exception.getMessage()))
                     .build();
    }

    if (exception instanceof ConstraintViolationException) {
      return Response.status(Response.Status.BAD_REQUEST)
                     .entity(new ErrorResponse("VALIDATION_ERROR", "Erreur de validation des données"))
                     .build();
    }

    // Erreur générique
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                   .entity(new ErrorResponse("INTERNAL_ERROR", "Une erreur interne est survenue"))
                   .build();
  }

  /**
   * Classe interne pour représenter une réponse d'erreur standardisée.
   */
  @Getter
  public static class ErrorResponse {
    private final String code;
    private final String message;

    public ErrorResponse(String code, String message) {
      this.code = code;
      this.message = message;
    }

  }
}
