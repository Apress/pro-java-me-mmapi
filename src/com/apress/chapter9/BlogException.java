package com.apress.chapter9;

/**
 * BlogException is a catchall exception class for this application
 */
public class BlogException extends Exception {  

  public BlogException() {
    super();
  }
  
  public BlogException(String message) {
    super(message);
  }
  
  
}
