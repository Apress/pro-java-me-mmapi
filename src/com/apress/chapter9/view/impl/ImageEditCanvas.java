package com.apress.chapter9.view.impl;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Displayable;

import javax.microedition.media.Player;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.control.VideoControl;

import com.apress.chapter9.BlogException;

import com.apress.chapter9.model.BlogEntry;
import com.apress.chapter9.model.ImageBlogEntry;

import com.apress.chapter9.view.EditCanvas;
import com.apress.chapter9.view.PreviewCanvas;

import com.apress.chapter9.control.Controller;

/**
 * ImageEditCanvas is used to edit/record an image
 */
public class ImageEditCanvas extends EditCanvas implements Runnable {
  
  private Player capturePlayer = null;
  private VideoControl vControl = null;
  
  public ImageEditCanvas(Controller controller, BlogEntry entry) {
    super(controller, entry);
  }
  
  public void showDisplay() {   
 
    // make this canvas the current display item
    controller.getDisplay().setCurrent(this);
    
    // repaint
    repaint();
    
    // and start the viewfinder
    startViewFinder();

 }

  public void paint(Graphics g) {
    
    // just call the super class method
    super.paint(g);
    
  }
  
  private void startViewFinder() {
    
    try {
      
      // create the capture player
      capturePlayer = Manager.createPlayer("capture://video");

      if (capturePlayer != null) {
        
        // if created, realize it
        capturePlayer.realize();
      
        // and grab the VideoControl
        vControl = (VideoControl)capturePlayer.getControl(
          "javax.microedition.media.control.VideoControl");        
       
        // if VideoControl is null throw exception
        if(vControl == null) 
          throw new BlogException("VideoControl not available for snapshots");
        
        // now add this video control to this Canvas and initialize it
        vControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, this);
        
        vControl.setDisplayLocation(5, 5);
        
        try {
          vControl.setDisplaySize(getWidth() - 10, getHeight() - 10);
        } catch (MediaException me) {} // ignore
        
        vControl.setVisible(true);
        
        // start the underlying player
        capturePlayer.start();        
      
      } else {
        throw new Exception("Viewfinder video player is not available");
      }      
    } catch(Exception e) {
      cleanUp(e);
    }
    
  }
  
  public void commandAction(Command cmd, Displayable disp) {
    
    int commandType = cmd.getCommandType();
    
    if(commandType == Command.EXIT) {
      controller.processExit();
      return;
    } else if(commandType == Command.OK) {
      
      Thread runner = new Thread(this);
      runner.start();
    }    
  }
  
  public void run() {
    
    // first cast the entry to the right type
    ImageBlogEntry imgEntry = ((ImageBlogEntry)entry);

    // get the snapshot from the control in the default format      
    byte[] mediaData = null;
    try {

      // first try and capture a jpeg shot which is the width
      // and height of this canvas
      mediaData = vControl.getSnapshot(
        "encoding=jpeg&width=" + 
        (getWidth() - 10) + "&height=" + (getHeight() - 10));

      // set the content-type
      imgEntry.setContentType("image/jpeg");

    } catch (MediaException me) {

      // if that doesn't work, try to get the default
      try {

        mediaData = vControl.getSnapshot(null);

        // try and get the default encoding content type
        String encodings = System.getProperty("video.snapshot.encodings");
        String defaultContentType = 
          encodings.substring(
          encodings.indexOf("=") + 1, 
          (encodings.indexOf(" ") == -1) ? 
            encodings.length() : encodings.indexOf(" "));

        imgEntry.setContentType(defaultContentType);

      } catch(MediaException mex) { cleanUp(mex); return; }
    }

    // once data is available set it on this entry
    ((ImageBlogEntry)entry).setMediaData(mediaData);

    // now close the capture Player and the vControl
    vControl.setVisible(false);
    vControl = null;
    capturePlayer.close();
    capturePlayer = null;

    // create a preview canvas and set this entry for it
    ImagePreviewCanvas pCanvas = new ImagePreviewCanvas(controller, entry);

    // and show it
    pCanvas.showCanvas();    
    
  }
  
  
  /**
   * This method is used to cleanup and release resources on error
   */
  private void cleanUp(Exception e) {

    // release resources on error
    if(vControl != null) { vControl = null; }
    if(capturePlayer != null) { 
      capturePlayer.close(); 
      capturePlayer = null; 
    }

    // and show the user the message
    controller.message("Error: " + e.getMessage());    
  }
  
}
