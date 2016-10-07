package com.apress.chapter7;

import javax.microedition.midlet.*;
import javax.microedition.media.*;

public class PlayMIDIMIDlet extends MIDlet {
  
  Player midiPlayer = null;
  
  public PlayMIDIMIDlet() {
    try {
      midiPlayer = Manager.createPlayer(
        getClass().getResourceAsStream(
          "/media/midi/chapter7/cabeza.mid"), "audio/midi"); 
    } catch(Exception e) {
      System.err.println(e);
    }
  }
  
  public void startApp() {
    try {
      if(midiPlayer != null) {
        midiPlayer.start();
      }      
    } catch(Exception e) {
      System.err.println(e);
    }
  }
  
  public void pauseApp() {
  }
  
  public void destroyApp(boolean unconditional) {
  }
}
