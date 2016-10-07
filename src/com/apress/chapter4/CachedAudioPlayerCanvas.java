package com.apress.chapter4;

import java.util.*;
import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import javax.microedition.media.control.*;
  
public class CachedAudioPlayerCanvas implements ItemStateListener {
  
  // the parent MIDlet
  private CachingAudioPlayer parent;
  
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
  
  private StringItem eventInfo;
  private EventHandler handler;
  
  private Hashtable players;
  
  public CachedAudioPlayerCanvas(CachingAudioPlayer parent) {
    
    this.parent = parent;
    
    // create form and add elements and listeners
    form = new Form("");
    gauge = new Gauge("Volume: 50", true, 100, 50);    
    eventInfo = new StringItem("", null);
    form.append(gauge);
    form.append(eventInfo);
    handler = new EventHandler(eventInfo);
    form.addCommand(parent.exitCommand);
    form.addCommand(parent.backCommand);
    form.setCommandListener(parent);
    
    // a change in volume gauge will be handled by this class
    form.setItemStateListener(this);
    
    players = new Hashtable();
  }
  
  public void playMedia(String locator) {
    
    try {
      
      // first look for an existing instance
      player = (Player)players.get(locator);
      
      if(player == null) {
        
        // create the player for the specified string locator
        player = Manager.createPlayer(
          getClass().getResourceAsStream(locator), "audio/x-wav");
        
        player.addPlayerListener(handler);
        
        // fetch it
        player.prefetch();
        
        // put this instance in the Hashtable
        players.put(locator, player);
      }
      
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

      try {
        player.setMediaTime(0);
      } catch(Exception e) {}
      
      player.deallocate();
      player = null;
    }  
  }
  
  public void closeAll() {
    // iterate through the player instances and close all
    for(Enumeration e = players.elements(); e.hasMoreElements();) {
      Player p = (Player)e.nextElement();
      p.close();
    }
  }
}
