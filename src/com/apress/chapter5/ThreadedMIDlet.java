package com.apress.chapter5;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.media.*;

public class ThreadedMIDlet extends MIDlet implements CommandListener {
  
  private List list;
  private StringItem text;
  private Display display;
  
  private NetworkPlayerManager mgr;
  private Command cancel = new Command("Cancel", Command.CANCEL, 1);
  
  public ThreadedMIDlet() {
    
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
    } else if(cmd.getLabel().equals("Cancel")) {
      mgr.cancel();
      display.setCurrent(list);
    } else {
      try {
        mgr = new NetworkPlayerManager(display, cancel, this); 
        Thread runner = new Thread(mgr);
        runner.start();
      } catch(Exception e) { System.err.println(e); }
    }
  }
}
