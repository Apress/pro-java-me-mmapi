package com.apress.chapter9.model;

/**
 * BlogEntry is the interface that represents the blog entries as a model
 */
public interface BlogEntry {
  
  /**
   * The user who is posting this entry
   */
  public User getUser();

  /**
   * The DateTime when the entry was posted
   */
  public long getDateTimePosted();

  /**
   * The title of this entry, if any
   */
  public String getEntryTitle();
  
  /**
   * The message of this entry, if any
   */
  public String getEntryMessage();  

}
