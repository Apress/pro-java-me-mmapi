package com.apress.chapter4;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class CachingAudioPlayer extends MIDlet implements CommandListener {
  
  // the list of media names
  private String[] audioDisplayList = 
    {"Baby Crying", "Applause", "Laughter"};
  
  // the list of media locations
  private String[] audioList        = 
    {"/media/audio/chapter4/baby.wav", 
     "/media/audio/chapter4/applause.wav", 
     "/media/audio/chapter4/laughter.wav"};
  
  protected Display display;
  private CachedAudioPlayerCanvas canvas;
  private List list;
  
  protected Command exitCommand;
  protected Command backCommand;
  
  public CachingAudioPlayer() {

    // initialize the list and add the exit command
    list = new List(
      "Pick an Audio file", List.IMPLICIT, audioDisplayList, null);
    exitCommand = new Command("Exit", Command.EXIT, 1);
    list.addCommand(exitCommand);    
    list.setCommandListener(this);
    
    // the back command
    backCommand = new Command("Back", Command.BACK, 1);
    
    // create the canvas
    canvas = new CachedAudioPlayerCanvas(this);
    
    // and initialize the display
    display = Display.getDisplay(this);
  }
  
  public void startApp() {    
    
    // if startApp() is called after MIDlet has been paused
    if(canvas.isPlayerPaused()) { 
      
      // restart the player
      canvas.restartMedia(); 
      display.setCurrent(canvas.getForm()); 
    } else { 
      
      // else display the audio list
      display.setCurrent(list);
    }
  }
  
  public void pauseApp() {
    // pauses the playing of the player, if any
    canvas.pauseMedia();
  }
  
  public void destroyApp(boolean unconditional) {    
    // closes all players before shutdown
    canvas.closeAll();
  }
  
  public void commandAction(Command command, Displayable disp) {
    
    // exiting the MIDlet
    if(command == exitCommand) {      
      canvas.closeAll(); // close all players
      notifyDestroyed(); // let AMS know clean up is done
      return;
    } else if(command == backCommand) { // back to the list
      canvas.cleanUp();
      display.setCurrent(list);
      return;
    }
    
    // the implicit list handling
    if(disp == list) {
      
      long t1 = System.currentTimeMillis();
      
      // play the current selected file
      canvas.playMedia(audioList[list.getSelectedIndex()]);
      
      long t2 = System.currentTimeMillis();
      
      canvas.getForm().setTitle("Time taken: " + (t2-t1) + " ms");

      // display the canvas's form
      display.setCurrent(canvas.getForm());
    }
  }
}
