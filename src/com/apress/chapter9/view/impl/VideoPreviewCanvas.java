package com.apress.chapter9.view.impl;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Graphics;

import javax.microedition.media.Player;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.control.VideoControl;

import com.apress.chapter9.control.Controller;

import com.apress.chapter9.view.PreviewCanvas;

import com.apress.chapter9.model.BlogEntry;
import com.apress.chapter9.model.VideoBlogEntry;

public class VideoPreviewCanvas extends PreviewCanvas  {
  
  public VideoPreviewCanvas(Controller controller, BlogEntry entry) {
    super(controller, entry);
    setTitle("Video Entry Preview");
  }
  
  public void showCanvas() {
    
    // make this canvas as the current display item
    controller.getDisplay().setCurrent(this);
    
    // and then repaint it
    repaint();
    
    // finally playback the recording
    playbackVideoRecording();
    
  }
  
  public void paint(Graphics g) {
    
    // get the super class to clear background and repaint it first
    super.paint(g);  
  }
  
  private void playbackVideoRecording() {
    
    VideoBlogEntry vEntry = (VideoBlogEntry)entry;
    
    ByteArrayInputStream bis = 
      new ByteArrayInputStream(vEntry.getMediaData());
    
    Player player = null;
    VideoControl vControl = null;
    
    try {
      
      // create the Playback player
      player = Manager.createPlayer(bis, vEntry.getContentType());
      
      // realize it
      player.realize();
      
      // create the playback video control
      vControl = (VideoControl)player.getControl(
        "javax.microedition.media.control.VideoControl");
      
      // initialize it
      vControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, this);
      
      vControl.setDisplayLocation(5, 5);

      try {
        vControl.setDisplaySize(getWidth() - 10, getHeight() - 10);
      } catch (MediaException me) {} // ignore
        
      vControl.setVisible(true); 
      
      // start it
      player.start();
      
    } catch(Exception e) {
      
      // release this player instance
      if(player != null) { player.close(); player = null; }
      
      // show the error message to the user
      controller.message(e.getMessage());
      
      // and return
      return;
    }
  }
}
