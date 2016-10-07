package com.apress.chapter8;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

import javax.microedition.media.*;
import javax.microedition.media.control.MetaDataControl;

public class MetaDataControlMIDlet extends MIDlet implements CommandListener {
  
  // define the display items
  private Display display = null;
  private List list = null;
  private Command exitCommand = null;
  private Alert alert = null;
  
  // the player instance
  private Player player = null;
  
  public MetaDataControlMIDlet() {

    // create the display items
    display = Display.getDisplay(this);
    alert = new Alert("Message");
    exitCommand = new Command("Exit", Command.EXIT, 1);    
    alert.addCommand(exitCommand);
    alert.setCommandListener(this);
    
    list = new List("Message", List.IMPLICIT);
    list.addCommand(exitCommand);
    
    list.setCommandListener(this);
    
    // create and prefetch the player instance
    try {
      player = Manager.createPlayer(
        getClass().getResourceAsStream(
        "/media/audio/chapter8/frogs.mp3"), "audio/mp3");
      
      player.prefetch();
    } catch(Exception e) {
      error(e);
    }
  }
  
  public void startApp() {
    
    // if player was created, extract control
    if(player != null) {
    
      MetaDataControl mControl = 
        (MetaDataControl)player.getControl(
          "javax.microedition.media.control.MetaDataControl");

      // if control is provided, show information on screen
      if(mControl == null) {
        
        // no info
        alert.setString("No Meta Information");
        display.setCurrent(alert);
      } else {   
        
        // get all the keys of this control
        String[] keys = mControl.getKeys();
        
        // and append the key and its value to the list
        for(int i = 0; i < keys.length; i++) {
          list.append(keys[i] + " -- " + mControl.getKeyValue(keys[i]), null);
        }

        // show the list
        display.setCurrent(list);
      }
    }
  }
  
  public void commandAction(Command cmd, Displayable disp) {
    if(cmd == exitCommand) {
      notifyDestroyed();
    }
  }
  
  public void pauseApp() {
  }
  
  public void destroyApp(boolean unconditional) {
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
