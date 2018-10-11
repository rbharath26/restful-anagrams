package com.racabe.anagrams.exception;

/**
 * The class DictionaryException.
 */
public class DictionaryException extends RuntimeException {
  
  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new dictionary exception.
   *
   * @param message the message
   */
  public DictionaryException(String message) {
    super(message);
  }

  /**
   * Instantiates a new dictionary exception.
   *
   * @param message the message
   * @param cause the cause
   */
  public DictionaryException(String message, Throwable cause) {
    super(message, cause);
  }
}
