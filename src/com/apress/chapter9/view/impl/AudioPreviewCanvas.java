package com.apress.chapter9.view.impl;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Graphics;

import javax.microedition.media.Player;
import javax.microedition.media.Manager;

import com.apress.chapter9.control.Controller;

import com.apress.chapter9.view.PreviewCanvas;

import com.apress.chapter9.model.BlogEntry;
import com.apress.chapter9.model.AudioBlogEntry;

public class AudioPreviewCanvas extends PreviewCanvas {
  
  public AudioPreviewCanvas(Controller controller, BlogEntry entry) {
    super(controller, entry);
    setTitle("Audio Entry Preview");
  }
  
  public void showCanvas() {
    
    // make this canvas as the current display item
    controller.getDisplay().setCurrent(this);
    
    // and then repaint it
    repaint();
    
    // finally playback the recording
    playbackAudioRecording();
    
  }
  
  public void paint(Graphics g) {
    
    // get the super class to clear background and repaint it first
    super.paint(g);
    
    // now draw the speaker image in the foreground
    try {
      g.drawImage(Image.createImage(
        getClass().getResourceAsStream("/media/images/chapter9/audio.gif")), 
        getWidth()/2, getHeight()/2, Graphics.VCENTER | Graphics.HCENTER);
    } catch(IOException ioex) {} // ignore exception
    
    // also draw a message in white
    g.setColor(0x000000);    
    
    g.drawString("Playing .... ", 
      (getWidth()/2), (getHeight()/2) + 30, Graphics.TOP | Graphics.HCENTER);    
  }
  
  private void playbackAudioRecording() {
    
    ByteArrayInputStream bis = new ByteArrayInputStream(
      ((AudioBlogEntry)entry).getMediaData());
    
    Player player = null;
    
    try {
      
      // create the Playback player
      player = Manager.createPlayer(
        bis, ((AudioBlogEntry)entry).getContentType());
      
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
