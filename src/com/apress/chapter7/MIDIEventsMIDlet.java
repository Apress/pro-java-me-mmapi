package com.apress.chapter7;

import javax.microedition.midlet.*;
import javax.microedition.media.*;
import javax.microedition.lcdui.*;
import javax.microedition.media.control.MIDIControl;

public class MIDIEventsMIDlet extends MIDlet {

  Display display = null;
  Alert alert = null;
  
  public MIDIEventsMIDlet() {
    display = Display.getDisplay(this);
    alert = new Alert("Message");
    alert.setString("Working...");
    alert.setTimeout(Alert.FOREVER);
  }

  public void startApp() {
    
    // show the alert at startup
    display.setCurrent(alert);
    
    try {      
      
      // create a MIDI player
      Player p = Manager.createPlayer(Manager.MIDI_DEVICE_LOCATOR);
      
      // prefetch
      p.prefetch();
      
      // extract MIDI Control
      MIDIControl mControl = 
        (MIDIControl)p.getControl(
          "javax.microedition.media.control.MIDIControl");
      
      if(mControl == null) throw new Exception("MIDIControl not available");
      
      // send Note ON for Channel 12 for note MIDDLE C at 100 velocity
      mControl.shortMidiEvent(MIDIControl.NOTE_ON | 11, 60, 100);
      
      Thread.sleep(100);
      
      // send Note OFF
      mControl.shortMidiEvent(MIDIControl.NOTE_ON | 11, 60, 0);
      
      Thread.sleep(100);
      
      // send program change to Xylophone (14)
      // alternatively, use setProgram(11, -1, 14);
      mControl.shortMidiEvent(192 | 11, 14, 0);
      // mControl.setProgram(11, -1, 14);
      
      Thread.sleep(100);      
      
      // set volume change to 50
      // alternatively, use setChannelVolume(11, 50);
      mControl.shortMidiEvent(MIDIControl.CONTROL_CHANGE | 11, 7, 50);
      
      Thread.sleep(100);

      // send Note ON for Channel 12 for note MIDDLE C at 100 velocity
      mControl.shortMidiEvent(MIDIControl.NOTE_ON | 11, 60, 100);
      
      Thread.sleep(100);
      
      // send Note OFF
      mControl.shortMidiEvent(MIDIControl.NOTE_ON | 11, 60, 0);
      
      Thread.sleep(100);     
     
    } catch(Exception e) {
      alert.setString(e.getMessage());
      System.err.println(e);
    }
  }
  
  public void pauseApp() {
  }
  
  public void destroyApp(boolean unconditional) {
  }
}
