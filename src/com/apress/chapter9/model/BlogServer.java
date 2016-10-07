package com.apress.chapter9.model;

/**
 * BlogServer represents the server on which the blog is hosted, and includes
 * URL's for various actions, like registration, login and posting entries
 */
public class BlogServer {
  
  // the URL for registration
  private String registerURL = null;
  
  // the URL for logging in  
  private String loginURL = null;
  
  // the URL for posting a blog entry
  private String postEntryURL = null;
  
  public BlogServer(String registerURL, String loginURL, String postEntryURL) {
    this.registerURL = registerURL;
    this.loginURL = loginURL;
    this.postEntryURL = postEntryURL;
  }
  
  public String getRegisterURL() { return this.registerURL; }
  public String getLoginURL() { return this.loginURL; }
  public String getPostEntryURL() { return this.postEntryURL; }
  
}
