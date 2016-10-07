package com.apress.chapter3;

import javax.microedition.midlet.MIDlet;

import javax.microedition.media.Manager;
import javax.microedition.media.Player;

public class SimplePlayer extends MIDlet {  
  
  public void startApp() { 
    try {
      Player player = Manager.createPlayer(
        getClass().getResourceAsStream(
        "/media/audio/chapter3/baby.wav"), "audio/x-wav");
      player.start();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  public void pauseApp() {
  }
  
  public void destroyApp(boolean unconditional) {
  }
}
