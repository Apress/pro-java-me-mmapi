package com.apress.chapter6;

import javax.microedition.midlet.MIDlet;
import javax.microedition.media.*;
import javax.microedition.lcdui.*;

public class AllTonesPlayer extends MIDlet implements CommandListener {
  
  Form displayForm = new Form("Playing all tones");
  StringItem info = new StringItem("", "");
  Command exit = new Command("Exit", Command.EXIT, 1);
  Thread runner;
  boolean stop = false;
 
  public void startApp() {
    
    displayForm.append(info);
    Display.getDisplay(this).setCurrent(displayForm);
    displayForm.addCommand(exit);
    displayForm.setCommandListener(this);
    
    // create and start a new thread to play all the notes
    runner = new Thread(new TonePlayer(info, this));
    runner.start();
  }
  
  public void pauseApp() {
  }
  
  public void destroyApp(boolean unconditional) {
    if(runner != null) stop = true;
  }
  
  public void commandAction(Command cmd, Displayable disp) {
    // only exit command defined in this MIDlet
    destroyApp(true);
    notifyDestroyed();
  }
  
}

// Plays all notes sequentially
class TonePlayer implements Runnable {
  
  StringItem info;
  AllTonesPlayer midlet;
  
  public TonePlayer(StringItem info, AllTonesPlayer midlet) {
    this.info = info;
    this.midlet = midlet;
  }
  
  public void run() {
    try {
      for(int i = 0; i < 128; i++) {
        // wait a second before playing the next note
        Thread.sleep(1000); 
        info.setText("Playing: " + i);
        Manager.playTone(i, 500, 100); // play for 500 milliseconds at max vol
        if(midlet.stop) break;
      }
      
    } catch(Exception me) { System.err.println(me); }    
  }
}
