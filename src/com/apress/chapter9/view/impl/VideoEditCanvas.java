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
import javax.microedition.media.control.RecordControl;

import com.apress.chapter9.BlogException;

import com.apress.chapter9.model.BlogEntry;
import com.apress.chapter9.model.VideoBlogEntry;

import com.apress.chapter9.view.EditCanvas;
import com.apress.chapter9.view.PreviewCanvas;

import com.apress.chapter9.control.Controller;

/**
 * VideoEditCanvas is used to edit/record a video blog entry
 */
public class VideoEditCanvas extends EditCanvas implements Runnable {
  
  private Player capturePlayer = null;
  private VideoControl vControl = null;
  private RecordControl rControl = null;
  private ByteArrayOutputStream bos = null;
  
  public VideoEditCanvas(Controller controller, BlogEntry entry) {
    super(controller, entry);
  }
  
  public void showDisplay() {
    
    // make this canvas the current display item
    controller.getDisplay().setCurrent(this);
    
    // repaint
    repaint();
    
    startViewFinder();
  }
  
  public void paint(Graphics g) {
    
    // just call the super class method
    super.paint(g);
    
  }
  
  /**
   * This method is the same as the ImageEditCanvas, these two could be
   * combined into one super class
   */
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
          throw new BlogException("VideoControl not available for video");
        
        // now add this video control to this Canvas and initialize it
        vControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, this);
        
        vControl.setDisplayLocation(5, 5);
        
        try {
          vControl.setDisplaySize(getWidth() - 10, getHeight() - 10);
        } catch (MediaException me) {} // ignore
        
        vControl.setVisible(true);       
       
        // start the underlying player
        capturePlayer.start();    
        
        // now locate the RecordControl to start recording as well
        rControl = (RecordControl)capturePlayer.getControl(
          "javax.microedition.media.control.RecordControl");
        
        if(rControl == null) 
          throw new Exception("Video recording not supported");
        
        // create the buffer in which recording will be done
        bos = new ByteArrayOutputStream();
        
        // set the output of recording
        rControl.setRecordStream(bos);
        
        // start the actual recorder
        rControl.startRecord();        
        
      } else {
        throw new Exception("Viewfinder video player is not available");
      }      
    } catch(Exception e) {
      System.err.println(e.getMessage());
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
      VideoBlogEntry vEntry = ((VideoBlogEntry)entry);
      
      // now complete the recording      
      try {
        
        // flush the output buffer
        bos.flush();
        
        rControl.stopRecord();

        // commit the recording
        rControl.commit();

        // close the player
        vControl.setVisible(false);
        vControl = null;
        
        capturePlayer.deallocate(); 
       
      } catch(Exception ex) {
        cleanUp(ex);
        // return;
      }
      
      // set the media data on this entry
      vEntry.setMediaData(bos.toByteArray());
      
      // set the media content type
      vEntry.setContentType(rControl.getContentType());
      
      // release the resources
      bos = null;
      rControl = null;
      
      // force reclamation of unused objects
      System.gc();
      
      // create a preview canvas and set this entry for it
      VideoPreviewCanvas pCanvas = new VideoPreviewCanvas(controller, entry);     
      
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
    controller.message(e.getMessage());    
  }
  
}
