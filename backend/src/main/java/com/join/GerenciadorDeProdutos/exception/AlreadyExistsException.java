package com.join.GerenciadorDeProdutos.exception;

public class AlreadyExistsException extends RuntimeException{
  public AlreadyExistsException(String message) {
    super(message);
  }
}
