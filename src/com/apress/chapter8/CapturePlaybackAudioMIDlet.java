package com.apress.chapter8;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import javax.microedition.media.control.RecordControl;

public class CapturePlaybackAudioMIDlet extends MIDlet 
  implements CommandListener {
  
  // the display items
  private Display display = null;
  private Alert alert = null;
  private Command exitCommand = null;
  
  // players and controls
  private Player capturePlayer = null;
  private Player playbackPlayer = null;
  private RecordControl recordControl = null;

  // buffers
  private ByteArrayOutputStream bos = new ByteArrayOutputStream();
  private ByteArrayInputStream bis = null;

  public CapturePlaybackAudioMIDlet() {
    
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
        recordControl = (RecordControl)capturePlayer.getControl(
          "javax.microedition.media.control.RecordControl");
        
        // set the alert as the current item
        display.setCurrent(alert);
        
        // if it is null throw exception
        if(recordControl == null) 
          throw new Exception("No RecordControl available");
        
        // create the buffer in which recording will be done
        bos = new ByteArrayOutputStream(1024);        
        
        // and set this buffer as the destination for recording
        recordControl.setRecordStream(bos);
        
      } else {
        throw new Exception("Capture Audio Player is not available");
      }
      
    } catch(Exception e) {
      error(e);
    }
  }
  
  public void startApp() {
    
    try {    
      
      // first start the corresponding player
      capturePlayer.start();
      
      // and then start the RecordControl
      recordControl.startRecord();     

      // now wait 10 seconds
      Thread.sleep(10000);
      
      // stop recording after time is up
      recordControl.stopRecord();
      
      // commit the recording
      recordControl.commit();
      
      // and close the Player instance
      capturePlayer.stop();

      // finished, set the message
      alert.setString("Well done! Now Processing...");
      
      // flush the buffer
      bos.flush();

      // create an inputstream of this buffer
      bis = new ByteArrayInputStream(bos.toByteArray());        

      // create the playback Player instance with this stream,
      // using the specified content type, as given by the RecordControl
      playbackPlayer = 
        Manager.createPlayer(bis, recordControl.getContentType());

      // start the playback
      playbackPlayer.start();

      // and set the message
      alert.setString("Playing back ... ");
        
    } catch(Exception e) {
      error(e);
    } finally {
      try {
        if(bos != null) bos.close();
        if(bis != null) bis.close();
      } catch(Exception ex) {
        error(ex);
      }
    }
    
  }
  
  public void pauseApp() {
  }
  
  public void destroyApp(boolean unconditional) {
  }
  
  public void commandAction(Command cmd, Displayable disp) {
    if(cmd == exitCommand) {
      notifyDestroyed();
    }
  }
  
  // general purpose error method, displays on screen as well to output
  private void error(Exception e) {
    alert.setString(e.getMessage());
    alert.setTitle("Error");
    display.setCurrent(alert);
    e.printStackTrace();
  }     
  
}
