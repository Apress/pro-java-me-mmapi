package com.apress.chapter8;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import javax.microedition.media.control.MIDIControl;

public class MixingAudioMIDlet extends MIDlet implements CommandListener {
  
  // define Players and Controls
  private Player backgroundPlayer = null;
  private Player firePlayer = null;
  private Player jumpPlayer = null;
  private MIDIControl mControl = null;
  
  // define commands and display items
  private Display display = null;
  private Alert alert = null;
  private Command exitCommand = null;
  private Command fireCommand = null;
  private Command jumpCommand = null;
  
  public MixingAudioMIDlet() {
    
    // create the display items
    display = Display.getDisplay(this);    
    
    // this is the alert that will be displayed
    alert = new Alert("Message");    
    alert.setString("Playing background score. " +
      " Use menu to mix sampled audio and/or other midi"); 
    alert.setTimeout(Alert.FOREVER);
    
    // create the commands
    exitCommand = new Command("Exit", Command.EXIT, 1);
    fireCommand = new Command("Fire!", Command.SCREEN, 1);
    jumpCommand = new Command("Jump!", Command.SCREEN, 1);
    
    // attach the commands to the alerts
    alert.addCommand(exitCommand);
    alert.addCommand(fireCommand);
    alert.addCommand(jumpCommand);
    
    // and set the command listener
    alert.setCommandListener(this);
    
    // finally create the Player instances
    initPlayers();    
  }
  
  private void initPlayers() {
    
    try {
     
      // the background player that will play a midi file throughout
      backgroundPlayer = 
        Manager.createPlayer(getClass().getResourceAsStream(
            "/media/midi/chapter8/cabeza.mid"), "audio/midi");
      
      // prefetch it and make sure it repeats if it finishes
      backgroundPlayer.prefetch();
      backgroundPlayer.setLoopCount(-1);

      // create another MIDI Player for the firing sound
      firePlayer = Manager.createPlayer(Manager.MIDI_DEVICE_LOCATOR);
      firePlayer.prefetch();
      
      // extract its MIDIControl
      mControl = (MIDIControl)firePlayer.getControl(
        "javax.microedition.media.control.MIDIControl");
      
      // set program to the Gunshot sound (See Figure 7-6 in Chapter 7)
      mControl.setProgram(11, -1, 127);
      
      // create another sampled audio player for jump sound
      jumpPlayer = Manager.createPlayer(getClass().getResourceAsStream(
            "/media/audio/chapter8/jump.wav"), "audio/x-wav");
      
      // prefetch it as well
      jumpPlayer.prefetch();      
      
    } catch (Exception e) {
      error(e);
    }     
  }
  
  public void startApp() {
    
    // start background player
    try {
      if(backgroundPlayer != null) {
        backgroundPlayer.start();
      }
    } catch(Exception e) {
      error(e);
    }
    
    // and show message
    display.setCurrent(alert);    
  }
  
  public void commandAction(Command cmd, Displayable disp) {
    
    // if closing
    if(cmd == exitCommand) {
      destroyApp(true);
      notifyDestroyed();
      return;
    }   
    
    try {
      
      // if fire command is issued, send the NOTE_ON command
      if(cmd == fireCommand) {        
        mControl.shortMidiEvent(MIDIControl.NOTE_ON | 11, 60, 100);        
      }

      // if jump command is issued, start the jump player
      if(cmd == jumpCommand) {
        jumpPlayer.start();
      }
    } catch(Exception e) {
      error(e);
    }
    
  }
  
  public void pauseApp() {
    
    // pause any players if MIDlet is paused
    try {
      if(backgroundPlayer != null) backgroundPlayer.stop();
      if(firePlayer != null) firePlayer.stop();
      if(jumpPlayer != null) jumpPlayer.stop();
    } catch(Exception e) {
      error(e);
    }
  }
  
  public void destroyApp(boolean unconditional) {
    
    // close players once application is destroyed
    try {
      if(backgroundPlayer != null) { 
        backgroundPlayer.close(); 
        backgroundPlayer = null; 
      }
      if(firePlayer != null) { firePlayer.close(); firePlayer = null; }
      if(jumpPlayer != null) { jumpPlayer.close(); jumpPlayer = null; }
    } catch(Exception e) {
      error(e);
    }
  }
  
  // general purpose error method, displays on screen as well to output
  private void error(Exception e) {
    alert.setString(e.getMessage());
    alert.setTitle("Error");
    display.setCurrent(alert);
    e.printStackTrace();
  }   
}
