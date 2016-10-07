package com.apress.chapter6;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import javax.microedition.media.control.*;

public class DistributedToneMIDlet extends MIDlet {

  private Player tonePlayer;
  
  public DistributedToneMIDlet() {
    try {
      tonePlayer = Manager.createPlayer(getClass().getResourceAsStream(
        "/media/misc/happybday_hex.jts"), "audio/x-tone-seq");
    } catch(Exception e) {
      System.err.println(e);
    }
  }
  
  public void startApp() {
    try {
      if(tonePlayer != null)
        tonePlayer.start();
    } catch(Exception e) {
      System.err.println(e);
    }
  }
  
  public void pauseApp() {
  }
  
  public void destroyApp(boolean unconditional) {
  }
}
