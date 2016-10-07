package com.apress.chapter9.model;

import com.apress.chapter9.control.Controller;

/**
 * A simple user class to represent a user of this MIDlet. Contains only 
 * userName and password. Once created, these values cannot be 
 * changed
 **/
public class User {
  
  // parameters that define this user
  private String userName = null;
  private String password = null;
  
  // flag to indicate if the user has been successfully logged in
  private boolean loggedIn = false;
  
  // the controller
  private Controller controller = null; 
  
  public User(String userName, String password, Controller controller) {
    
    // check for invalid values
    if(userName == null || userName.length() == 0 || 
       password == null || password.length() == 0)
      throw new IllegalArgumentException("One of the arguments is invalid");
    
    this.userName = userName;
    this.password = password;
    
    this.controller = controller;
  }
  
  // getters for the parameters
  public String getUserName() { return this.userName; }
  public String getPassword() { return this.password; }
  
  /**
   * Tries to log the user into the blog server.
   */
  public void login(String loginURL) {
    
    // use the Network Runner to make the connection
    controller.getNetworkRunner().makeConnection(
      loginURL + "?uName=" + getUserName() + "&pWord=" + getPassword());  
  }
  
  /**
   * Returns true if the user is logged in, false otherwise
   */
  public boolean isLoggedIn() {    
    
    if(controller.getNetworkRunner().isLoggedIn()) {
      this.loggedIn = true;      
    }
    
    return this.loggedIn;
  }
  
  /**
   * Registers this user with the blog server. 
   * Successful registration doesn't log the user in, and login must be
   * done separately. 
   */
  public void register(String registerURL) {
    
    // use the Network runner to make the connection
    controller.getNetworkRunner().makeConnection(
      registerURL + "?uName=" + getUserName() + "&pWord=" + getPassword());    
  }  
}
