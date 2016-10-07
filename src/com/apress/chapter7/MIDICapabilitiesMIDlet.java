package com.apress.chapter7;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import javax.microedition.media.control.MIDIControl;

public class MIDICapabilitiesMIDlet extends MIDlet {
  
  public void startApp() {
    try {
      
      // create Player using MIDI Device locator
      Player p = Manager.createPlayer(Manager.MIDI_DEVICE_LOCATOR);
      
      // must prefetch before extracting controls
      p.prefetch();
      
      // get the MIDIControl
      MIDIControl mControl = 
        (MIDIControl)p.getControl(
          "javax.microedition.media.control.MIDIControl");
      
      // create a message based on whether advanced capabilities are supported
      String msg = 
        mControl.isBankQuerySupported() ? 
          "MIDIControl is fully supported" : "Minimum MIDIControl is provided";
      
      // and display message as alert
      Display.getDisplay(this).setCurrent(
        new Alert("Message", msg, null, AlertType.INFO));
      
    } catch(Exception e) {
      System.err.println(e);
    }
  }
  
  public void pauseApp() {
  }
  
  public void destroyApp(boolean unconditional) {
  }
}
