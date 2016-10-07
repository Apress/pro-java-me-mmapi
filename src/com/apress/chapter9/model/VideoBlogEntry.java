package com.apress.chapter9.model;

/**
 * The VideoBlogEntry extends MediaBlogEntry and represents an entry that
 * has a video component in addition to a text component
 */
public class VideoBlogEntry extends MediaBlogEntry {
  
  public VideoBlogEntry(User user) {
    super(user);
  }  
}
