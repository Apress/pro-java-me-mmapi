package com.apress.chapter8;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.media.*;

public class StreamingMediaMIDlet extends MIDlet {
  
  private Player player = null;
  private Display display = null;
  private Alert alert = null;
  
  private boolean error = false;
  
  public StreamingMediaMIDlet() {
    
    display = Display.getDisplay(this);
    alert = new Alert("Message");
    
    // try and create a Player instance for rtsp
    try {
      
      player = Manager.createPlayer(
        "rtsp://rmv8.bbc.net.uk/radio1/lockup.ra");
      
      if(player == null) 
        throw new Exception("Could not create player for streaming");
      
      player.realize();
      
    } catch(Exception e) {
      error(e);
    }
  }
  
  public void startApp() {
    if(error) return;
    
    try {
      player.start();
    } catch(Exception e) {
      error(e);
    }
    
    alert.setString("Playing streaming radio from BBC");    
    display.setCurrent(alert);
  }
  
  public void pauseApp() {
  }
  
  public void destroyApp(boolean unconditional) {
    if(player != null) player.close();
  }
  
  // general purpose error method, displays on screen as well to output
  private void error(Exception e) {
    alert.setString(e.getMessage());
    alert.setTitle("Error");
    display.setCurrent(alert);
    e.printStackTrace();
    error = true;
  }   
}
