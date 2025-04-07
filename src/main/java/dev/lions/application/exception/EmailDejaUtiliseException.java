// src/main/java/dev/lions/application/exception/EmailDejaUtiliseException.java
package dev.lions.application.exception;

/**
 * Exception lancée lorsqu'un email est déjà utilisé par un autre membre.
 */
public class EmailDejaUtiliseException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public EmailDejaUtiliseException(String message) {
    super(message);
  }
}