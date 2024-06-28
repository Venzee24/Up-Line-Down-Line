package com.cyberz.ar7demon.exception;

import java.io.Serial;

public class CategoryNotFoundException extends RuntimeException{
  @Serial
  private static final long serialVersionUID=1;

  public CategoryNotFoundException(String message){
      super(message);
  }
}
