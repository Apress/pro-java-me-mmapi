package com.apress.chapter3;

import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import javax.microedition.media.control.*;
  
public class AudioPlayerCanvas implements ItemStateListener {
  
  // the parent MIDlet
  private AudioPlayer parent;
  
  // form that contains canvas elements
  private Form form;
  
  // gauge to allow user to manipulate volume
  private Gauge gauge;
  
  // the volume control
  private VolumeControl volume;
  
  // the player used to play media
  private Player player;
  
  // is the player paused?
  private boolean paused = false;
  
  public AudioPlayerCanvas(AudioPlayer parent) {
    
    this.parent = parent;
    
    // create form and add elements and listeners
    form = new Form("");
    gauge = new Gauge("Volume: 50", true, 100, 50);    
    form.append(gauge);
    form.addCommand(parent.exitCommand);
    form.addCommand(parent.backCommand);
    form.setCommandListener(parent);
    
    // a change in volume gauge will be handled by this class
    form.setItemStateListener(this);
  }
  
  public void playMedia(String locator) {
    
    try { 
      
      // create the player for the specified string locator
      player = Manager.createPlayer(
        getClass().getResourceAsStream(locator), "audio/x-wav");
      
      // realize it
      player.realize();
      
      // get the volume control
      volume = (VolumeControl)player.getControl("VolumeControl");
      
      // initialize it to 50
      volume.setLevel(50);
      
      // initialize the gauge
      gauge.setValue(volume.getLevel());
      gauge.setLabel("Volume: " + volume.getLevel());
      
      // play it twice
      player.setLoopCount(2);
      
      // start the player
      player.start();      
     
      // set the title of the form
      form.setTitle("Playing " + locator);
      
    } catch(Exception e) {
      e.printStackTrace();
    } 
    
  }
  
  public void pauseMedia() {
    
    // if the player needs to be paused, either due to an incoming call,
    // or due to user actions
    if(player != null) {
      try {
        player.stop();
        paused = true;
      } catch(Exception e) {}
    }
  }
  
  public void restartMedia() {
    
    // restarting after player was paused
    if(player != null) {
      try {
        player.start();
        paused = false;
      } catch(Exception e) {}
    }
  }
  
  public boolean isPlayerPaused() {
    return paused;
  }
  
  public Form getForm() {
    return this.form;
  }
  
  public void itemStateChanged(Item item) {
    
    // there is only one item on the form, the gauge, and any change in its
    // value means the user wants to increase or decrease the playback volume
    volume.setLevel(gauge.getValue());
    gauge.setLabel("Volume: " + volume.getLevel());
  }
  
  public void cleanUp() {
    
    // clean up, either due to user action or AMS call
    if(player != null) {
      player.close();
      player = null;
    }  
  }
}
