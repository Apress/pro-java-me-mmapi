package com.apress.chapter6;

import javax.microedition.midlet.MIDlet;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;

public class CSharpMIDlet extends MIDlet {
  public CSharpMIDlet() {    
  }
  
  public void startApp() {
    try {
      // plays C# (frequency of 277.183 for 5 seconds at max volume)
      Manager.playTone(61, 5000, 100);
    } catch(MediaException me) { System.err.println(me); }
  }
  
  public void pauseApp() {
  }
  
  public void destroyApp(boolean unconditional) {
  }
  
}
