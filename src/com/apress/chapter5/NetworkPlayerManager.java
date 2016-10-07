package com.apress.chapter5;

import javax.microedition.lcdui.*;
import javax.microedition.media.*;

public class NetworkPlayerManager implements Runnable {
  
  private Display display;
  private Form form;
  private StringItem msg;
  
  private boolean cancel = false;
  
  private Player player = null;
  
  public NetworkPlayerManager(
    Display display, Command cancelCmd, ThreadedMIDlet parent) {
    
    this.display = display;
    
    form = new Form("Network Player Manager");
    msg = new StringItem("Please Wait ... ", null);
    form.append(msg);
    form.addCommand(cancelCmd);
    
    form.setCommandListener(parent);
  }
  
  public void run() {

    display.setCurrent(form);
    boolean connected = false;
      
    try {    
      player = 
        Manager.createPlayer(
          "http://www.mmapibook.com/resources/media/audio/chapter5/siren.wav");

      player.realize();
      
      connected = true;
    } catch (Exception e) {
      msg.setText("Failed: " + e.getMessage());
      System.err.println(e);
      return;
    }

    if(connected && !cancel) 
      msg.setText("Connected. Starting playback...");
    else {
      msg.setText("Unable to connect.");
      return;
    }

    try {
      player.start();
    } catch(Exception pe) {
      msg.setText(pe.getMessage());
      System.err.println(pe);
    }
  }
  
  public void cancel() {
    cancel = true;
    if(player!= null) player.deallocate();
  }
}
