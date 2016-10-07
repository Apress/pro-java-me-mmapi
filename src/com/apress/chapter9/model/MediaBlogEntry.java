package com.apress.chapter9.model;

/**
 * This abstract class encapsulates all the blog entries that have a media
 * component along with any text component. This is why it extends the 
 * TextBlogEntry class
 */
public abstract class MediaBlogEntry extends TextBlogEntry {
  
  // the mediaData
  protected byte[] mediaData = null;
  
  // the contentType of the media
  protected String contentType = null;
  
  protected MediaBlogEntry(User user) {
    super(user);
  }
  
  public byte[] getMediaData() { return this.mediaData; }
  public void setMediaData(byte[] mediaData) { this.mediaData = mediaData; }
  
  public String getContentType() { return this.contentType; }
  public void setContentType(String contentType) { 
    this.contentType = contentType; 
  }
  
  /**
   * This method tries to guess the extension of the likely media data
   * file that will be gauged from the type of content type this data is
   */
  public String guessFileExtension() {    
    
    if(contentType == null || contentType.length() == 0) return "";
    
    contentType = contentType.toLowerCase();
    
    if(contentType.equals("audio/x-wav")) return "wav";
    if(contentType.equals("audio/amr") || 
       contentType.equals("audio/amr-nb") ||
       contentType.equals("audio/amr-wb")) return "amr";
    
    if(contentType.equals("jpeg") ||
       contentType.equals("jpg") ||
       contentType.equals("image/jpeg") || 
       contentType.equals("image/jpg")) return "jpg";
    if(contentType.equals("gif") || 
       contentType.equals("image/gif")) return "gif";
    
    if(contentType.equals("video/mpeg")) return "mpg";
    if(contentType.equals("video/3gpp") ||
       contentType.equals("video/3gp")) return "3gp";
    
    return "unknown";    
  }
}
