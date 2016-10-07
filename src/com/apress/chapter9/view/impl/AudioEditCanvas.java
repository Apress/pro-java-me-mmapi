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
import javax.microedition.media.control.RecordControl;

import com.apress.chapter9.BlogException;

import com.apress.chapter9.model.BlogEntry;
import com.apress.chapter9.model.AudioBlogEntry;

import com.apress.chapter9.view.EditCanvas;
import com.apress.chapter9.view.PreviewCanvas;

import com.apress.chapter9.control.Controller;

/**
 * AudioEditCanvas is used to edit/record audio blog entry
 */
public class AudioEditCanvas extends EditCanvas implements Runnable {
  
  private Player capturePlayer = null;
  private RecordControl recordControl = null;
  private ByteArrayOutputStream bos = null;
  
  public AudioEditCanvas(Controller controller, BlogEntry entry) {
    super(controller, entry);
  }
  
  public void showDisplay() {
    
    // make this canvas the current display item
    controller.getDisplay().setCurrent(this);
    
    // repaint
    repaint();
    
    // and start the audio recording process
    new Thread(this).start();
  }
  
  public void run() {
    startAudioRecording(); 
  }
  
  public void paint(Graphics g) {
    
    // first call the super class method
    super.paint(g);
    
    // now draw the mic image in the foreground
    try {
      g.drawImage(Image.createImage(
        getClass().getResourceAsStream("/media/images/chapter9/mic.gif")), 
        getWidth()/2, getHeight()/2, Graphics.VCENTER | Graphics.HCENTER);
    } catch(IOException ioex) {} // ignore exception
    
    // also draw a message in white
    g.setColor(0x000000);    
    
    g.drawString("Recording .... ", 
      (getWidth()/2), (getHeight()/2) + 30, Graphics.TOP | Graphics.HCENTER);
    
  }
  
  private void startAudioRecording() {
    
    try {
      
      // create the capture player
      capturePlayer = Manager.createPlayer("capture://audio");

      if (capturePlayer != null) {
        
        // if created, realize it
        capturePlayer.realize();
      
        // and grab the RecordControl
        recordControl = (RecordControl)capturePlayer.getControl(
          "javax.microedition.media.control.RecordControl");        
       
        // if RecordControl is null throw exception
        if(recordControl == null) 
          throw new BlogException("RecordControl not available for audio");
        
        // create the buffer in which recording will be done
        bos = new ByteArrayOutputStream();
        
        // set the output of recording
        recordControl.setRecordStream(bos);

        // start the underlying player
        capturePlayer.start();
        
        // and the actual recorder
        recordControl.startRecord();
        
      } else {
        throw new Exception("Capture Audio Player is not available");
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
      
      // complete the recording
      
      try {
        // flush the output buffer
        bos.flush();

        // commit the recording
        recordControl.commit();

        // close the player
        capturePlayer.close();
        
        capturePlayer = null;
        
      } catch(Exception ex) {
        cleanUp(ex);
        return;
      }
      
      // set the media data on this entry
      ((AudioBlogEntry)entry).setMediaData(bos.toByteArray());
      
      // set the media content type
      ((AudioBlogEntry)entry).setContentType(recordControl.getContentType());
      
      // release the resources
      bos = null;
      recordControl = null;
    
      // create a preview canvas and set this entry for it
      AudioPreviewCanvas pCanvas = new AudioPreviewCanvas(controller, entry);
      
      // and show it
      pCanvas.showCanvas();
    }    
  }
  
  /**
   * This method is used to cleanup and release resources on error
   */
  private void cleanUp(Exception e) {

    // release resources on error
    if(recordControl != null) { recordControl = null; }
    if(capturePlayer != null) { 
      capturePlayer.close(); 
      capturePlayer = null; 
    }

    // and show the user the message
    controller.message(e.getMessage());    
  }  
}
