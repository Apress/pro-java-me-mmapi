package com.apress.chapter8;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import javax.microedition.media.control.RecordControl;

public class SaveCapturedAudioMIDlet extends MIDlet 
  implements CommandListener {
  
  // the display items
  private Display display = null;
  private Alert alert = null;
  private Command exitCommand = null;
  
  // players and controls
  private Player capturePlayer = null;
  private Player playbackPlayer = null;
  private RecordControl rControl = null;  
  
  private boolean error = false;

  public SaveCapturedAudioMIDlet() {
    
    // create the display
    display = Display.getDisplay(this);
    alert = new Alert("Message");
    alert.setTimeout(Alert.FOREVER);
    alert.setString("Capturing for 10 seconds. Say something intelligent!");
    exitCommand = new Command("Exit", Command.EXIT, 1);
    alert.addCommand(exitCommand);
    alert.setCommandListener(this);
    
    try {
      
      // create the capture player
      capturePlayer = Manager.createPlayer("capture://audio");
      
      if (capturePlayer != null) {
        
        // if created, realize it
        capturePlayer.realize();
      
        // and grab the RecordControl
        rControl = (RecordControl)capturePlayer.getControl(
          "javax.microedition.media.control.RecordControl");
        
        // set the alert as the current item
        display.setCurrent(alert);
        
        // if it is null throw exception
        if(rControl == null) throw new Exception("No RecordControl available");        
      
        // and set the destination for this captured data
        rControl.setRecordLocation("file:///test.wav"); 
        
      } else {
        throw new Exception("Capture Audio Player is not available");
      }
      
    } catch(Exception e) {
      error(e);
    }
  }
  
  public void startApp() {
    
    if(error) return;
    
    try {    
      
      // first start the corresponding recording player
      capturePlayer.start();
      
      // and then start the RecordControl
      rControl.startRecord();     

      // now record for 10 seconds
      Thread.sleep(10000);
      
      // stop recording after time is up
      rControl.stopRecord();
      
      // commit the recording
      rControl.commit();
      
      // stop the Player instance
      capturePlayer.stop();
      
      // and close it to release the microphone
      capturePlayer.close();
      
      // finally, create a Player instance to playback
      // check your device documentation to find out the root.
      // The following will work on devices that have the root
      // specified as shown      
      playbackPlayer = Manager.createPlayer("file:///test.wav");
      
      // and start it
      playbackPlayer.start();
        
    } catch(Exception e) {
      error(e);
    }    
  }
  
  public void pauseApp() {
  }
  
  public void destroyApp(boolean unconditional) { 
    if(capturePlayer != null) capturePlayer.close();
    if(playbackPlayer != null) playbackPlayer.close();
  }
  
  public void commandAction(Command cmd, Displayable disp) {
    if(cmd == exitCommand) {
      destroyApp(true);
      notifyDestroyed();
    }
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
