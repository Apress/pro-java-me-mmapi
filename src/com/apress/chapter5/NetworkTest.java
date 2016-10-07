package com.apress.chapter5;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.media.*;

public class NetworkTest extends MIDlet implements CommandListener {
  
  private List list;
  private StringItem text;
  private Display display;
  
  public NetworkTest() {
    
    list = new List("Press Play", List.IMPLICIT);
    
    // commands to exit and play
    Command exit = new Command("Exit", Command.EXIT, 1);
    Command play = new Command("Play", Command.SCREEN, 1);
    
    list.addCommand(exit);
    list.addCommand(play);
    
    list.setCommandListener(this);       
    display = Display.getDisplay(this);
  }
  
  public void startApp() {
    display.setCurrent(list);
  }
  
  public void pauseApp() {
  }
  
  public void destroyApp(boolean unconditional) {
  }
  
  public void commandAction(Command cmd, Displayable disp) {
    if(cmd.getLabel().equals("Exit")) {
      notifyDestroyed();
    } else {
      try {
        Player player = 
          Manager.createPlayer(
          "http://www.mmapibook.com/resources/media/audio/chapter5/siren.wav");
        player.start();      
      } catch(Exception e) { System.err.println(e); }
    }
  }
}
