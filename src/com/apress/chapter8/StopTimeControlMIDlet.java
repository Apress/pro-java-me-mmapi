package com.apress.chapter8;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import javax.microedition.media.control.*;

public class StopTimeControlMIDlet extends MIDlet 
  implements CommandListener, PlayerListener {
  
  // the display items
  private Display display = null;  
  private Alert alert = null;
  private List list = null;
  
  // commands
  private Command exitCommand = null;
  private Command okCommand = null;
  
  // player and controls
  private Player player = null;
  private StopTimeControl stControl = null;

  public StopTimeControlMIDlet() {
  
    // create all the basic stuff
    display = Display.getDisplay(this);
    
    alert = new Alert("Message");
    
    exitCommand = new Command("Exit", Command.EXIT, 1);
    okCommand = new Command("Ok", Command.OK, 1);
    
    alert.addCommand(exitCommand);
    
    list = new List("Pick One", List.IMPLICIT);
    list.append("Play full", null);
    list.append("Use StopTimeControl", null);   
    
    list.addCommand(exitCommand);    
    alert.addCommand(okCommand);
    
    alert.setCommandListener(this);  
    list.setCommandListener(this);  
    
    // create the player and the stoptimecontrol
    try {
      player = Manager.createPlayer(
        getClass().getResourceAsStream(
        "/media/audio/chapter8/printer.wav"), "audio/x-wav");
      
      player.addPlayerListener(this);
      
      player.prefetch();
      
      stControl = (StopTimeControl)player.getControl(
        "javax.microedition.media.control.StopTimeControl");
      
      // no point continuing if stoptimecontrol is not supported
      if(stControl == null) 
        throw new Exception("StopTimeControl is not supported");      
    } catch(Exception e) {
      error(e);      
    }    
  }
    
  public void startApp() {
    // show the list of items
    display.setCurrent(list);
  }
  
  public void pauseApp() {
  }
  
  public void destroyApp(boolean unconditional) {
  } 
  
  public void commandAction(Command cmd, Displayable disp) {
    
    // if exiting
    if(cmd == exitCommand) {
      notifyDestroyed();
      return;
    }
    
    // if ok command
    if(cmd == okCommand) { 
      try {
        // pause the player, so that it can be reused
        player.stop();
        
        // and redisplay the list
        display.setCurrent(list);
      } catch(Exception e) {
        error(e);
      }
    }
    
    // implicit list handling
    if(disp == list) {
      
      // the selected idx
      int selectedIdx = list.getSelectedIndex();
      
      // show the message
      display.setCurrent(alert);
      
      try {
        if(selectedIdx == 0) {         
          // start player without stoptimecontrol
          if(player != null) player.start();
          alert.setString("Started without StopTimeControl ...");          
        } else {
          
          // start with stoptimecontrol
          
          // set the stop time as half of the length
          stControl.setStopTime(player.getDuration()/2);
          
          // start the player
          if(player != null) player.start();
          
          alert.setString("Started WITH StopTimeControl ...");          
        }
      } catch(Exception e) {
        error(e);
      }
    }
  }
  
  public void playerUpdate(Player player, String event, Object eventData) {
    
    // only interested in the STOPPED_AT_TIME event
    if(event == STOPPED_AT_TIME) {
      // give the message
      alert.setString("Stopped at: " + eventData + " microseconds" +
        ", actual duration: " + player.getDuration() + " microseconds");
      
      // and show the alert
      alert.setTimeout(Alert.FOREVER);
      display.setCurrent(alert);
    }
  }
  
  // general purpose error method, displays on screen as well to output
  private void error(Exception e) {
    alert.setString(e.getMessage());
    alert.setTitle("Error");
    alert.setTimeout(Alert.FOREVER);
    display.setCurrent(alert);
    e.printStackTrace();
  }    
}
