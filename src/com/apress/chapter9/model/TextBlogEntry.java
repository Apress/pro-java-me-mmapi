package com.apress.chapter9.model;

/**
 * TextBlogEntry implements BlogEntry and represents a TextEntry for the Blog.
 * It also acts as a base class for other media type entries.
 */
public class TextBlogEntry implements BlogEntry { 
  
  // the title of this entry, if any
  private String entryTitle = null;
  
  // the message of this entry, if any
  private String entryMessage = null;
  
  // the time this entry was posted
  private long dateTimePosted = 0;
  
  // the user who is making this entry
  private User user = null;
  
  public TextBlogEntry(User user) {
    this.user = user;
  }
  
  // getter and setters for entry title and message
  public String getEntryTitle() { return this.entryTitle; }
  public void setEntryTitle(String entryTitle) { 
    this.entryTitle = entryTitle; 
  }

  public String getEntryMessage() { return this.entryMessage; }
  public void setEntryMessage(String entryMessage) { 
    this.entryMessage = entryMessage; 
  }
  
  // time posted is immutable
  public long getDateTimePosted() {
    return this.dateTimePosted;
  }

  // the user who is posting this entry
  public User getUser() {
    return this.user;
  }  
}
