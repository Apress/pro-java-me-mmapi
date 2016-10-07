package com.apress.chapter4;

import javax.microedition.media.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

public class EchoEventsMIDlet extends MIDlet implements PlayerListener {  
  
  private StringItem stringItem;
  
  public void startApp() { 
    
    try {      
      
      Form form = new Form("Player State");
      stringItem = new StringItem("", null);
      form.append(stringItem);
      Display.getDisplay(this).setCurrent(form);

      Player player = Manager.createPlayer(
        getClass().getResourceAsStream(
        "/media/audio/chapter4/baby.wav"), "audio/x-wav");
      player.addPlayerListener(this);           
      
      player.start();      
      
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  public void pauseApp() {
  }
  
  public void destroyApp(boolean unconditional) {
  }
  
  public void playerUpdate(Player player, String event, Object eventData) {
    stringItem.setText(event);
    System.err.println(event);
  }
}
