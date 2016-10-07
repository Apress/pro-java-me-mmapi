package com.apress.chapter4;

import javax.microedition.media.*;
import javax.microedition.media.control.*;
import javax.microedition.lcdui.StringItem;

public class EventHandler implements PlayerListener {
  
  private StringItem item;
  
  public EventHandler(StringItem item) {  
    this.item = item;
  }
  
  public void playerUpdate(Player player, String event, Object eventData) {   
    
    if(event == (PlayerListener.VOLUME_CHANGED)) {
      
      // a players volume has been changed
      VolumeControl vc = (VolumeControl)eventData;
      updateDisplay("Volume Changed to: " + vc.getLevel());
      
      if(vc.getLevel() > 60)  {
        updateDisplay("Volume higher than 60 is too loud");
        vc.setLevel(60);
      }      
    } else if(event == (PlayerListener.STOPPED)) {
      
      // player instance paused
      updateDisplay("Player paused at: " + (Long)eventData);
    } else if(event == (PlayerListener.STARTED)) {
      
      // player instance started (or restarted)
      updateDisplay("Player started at: " + (Long)eventData);
    } else if(event == (PlayerListener.END_OF_MEDIA)) {      
      
      // player instace reached end of loop
      updateDisplay("Player reached end of loop.");
    } else if(event == (PlayerListener.CLOSED)) {
      
      // player instance closed
      updateDisplay("Player closed.");
    } else if(event == (PlayerListener.ERROR)) {
      
      // if an error occurs, eventData contains the error message
      updateDisplay("Error Message: " + (String)eventData);
    }
  }
  
  public void updateDisplay(String text) {
    
    // update the item on the screen
    item.setText(text);
    
    // and write to error stream as well
    System.err.println(text);
  }
  
}
