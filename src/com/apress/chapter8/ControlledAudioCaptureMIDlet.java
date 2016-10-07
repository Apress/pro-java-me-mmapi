package com.apress.chapter8;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import javax.microedition.media.control.RecordControl;

public class ControlledAudioCaptureMIDlet extends MIDlet 
  implements CommandListener, PlayerListener {
  
  // the display items
  private Display display = null;
  private Alert alert = null;
  private Command exitCommand = null;
  private Command startCommand = null;
  private Command pauseCommand = null;
  private Command doneCommand = null;
  private Command playbackCommand = null;
  
  // players and controls
  private Player capturePlayer = null;
  private Player playbackPlayer = null;
  private RecordControl recordControl = null;

  // buffers
  private ByteArrayOutputStream bos = new ByteArrayOutputStream();
  private ByteArrayInputStream bis = null; 
  
  // boolean flag to indicate when recording is being done
  private boolean recording = false;

  public ControlledAudioCaptureMIDlet() {
    
    // create the display
    display = Display.getDisplay(this);
    alert = new Alert("Message");
    alert.setTimeout(Alert.FOREVER);
    alert.setString("Press Start Recording to capture audio");
    
    // create the various commands
    exitCommand = new Command("Exit", Command.EXIT, 1);
    startCommand = new Command("Start", Command.SCREEN, 1);
    pauseCommand = new Command("Pause", Command.SCREEN, 1);
    doneCommand = new Command("Done", Command.SCREEN, 1);
    playbackCommand = 
      new Command("Playback", Command.SCREEN, 1);       
    
    // and initialize the commands with the alert
    alert.addCommand(exitCommand);
    alert.addCommand(startCommand);
    
    // set this class as the PlayerListener for command actions
    alert.setCommandListener(this);
    
    // now set the alert as the current item
    display.setCurrent(alert);
    
    try {
      
      // create the capture player
      capturePlayer = Manager.createPlayer("capture://audio");

      if (capturePlayer != null) {
        
        // if created, realize it
        capturePlayer.realize();
      
        // and grab the RecordControl
        recordControl = (RecordControl)capturePlayer.getControl(
          "javax.microedition.media.control.RecordControl");
        
        // if RecordControl is null throw exception
        if(recordControl == null) 
          throw new Exception("No RecordControl available");
        
        // create the buffer in which recording will be done
        bos = new ByteArrayOutputStream(1024);       
      
      } else {
        throw new Exception("Capture Audio Player is not available");
      }      
    } catch(Exception e) {
      error(e);
    }
  }
  
  public void startApp() {    
    display.setCurrent(alert);    
  }
  
  public void pauseApp() {
  }
  
  public void destroyApp(boolean unconditional) {
    
    // close any open player instances, underlying controls will be
    // closed by calling the close method
    if(capturePlayer != null)  {
      capturePlayer.close(); // releases the microphone
      capturePlayer = null;
    }
    if(playbackPlayer != null)  {
      playbackPlayer.close();
      playbackPlayer = null;
    }    
  }
  
  public void commandAction(Command cmd, Displayable disp) {

    // if exit ..
    if(cmd == exitCommand) {
      destroyApp(true);
      notifyDestroyed();
    }
    
    // now based on what command is called, take the right action
    try {
      
      // if starting or restarting recording
      if(cmd == startCommand) {
        
        // remove other commands
        alert.removeCommand(startCommand);
        alert.removeCommand(playbackCommand);
        
        // add pause and done commands
        alert.addCommand(pauseCommand);
        alert.addCommand(doneCommand);        
        
        // now, if a playback was being done, close it to preserve
        // system resources
        if(playbackPlayer != null) { 
          playbackPlayer.close();
          playbackPlayer = null;
        }
        
        // are we restarting an existing recording or a new one?
        if(!recording) { // this means a new one
          
          // set the ByteArrayInputStream to null
          bis = null;
          
          // initialilze the ByteArrayOutputStream
          bos = new ByteArrayOutputStream(1024); 
          
          // set the output of recording
          recordControl.setRecordStream(bos);
          
          // and start the underlying player
          capturePlayer.start(); 
        }
        
        // now start the recording
        recordControl.startRecord();   
        
        // set the flag
        recording = true;
        
        // and show the message
        alert.setString("Recording now. Say something nice" + 
          " and then press Pause or Done Recording");        
          
      } else if(cmd == pauseCommand) {
        
        // pausing an existing recording
        
        // so remove the pause command and add the start (or restart command)
        alert.removeCommand(pauseCommand);
        alert.addCommand(startCommand);
        
        // stop the control, this only pauses the recording
        recordControl.stopRecord();
        
        // and show message
        alert.setString("Recording paused. " + 
          " Press Start Recording to restart");        

      } else if(cmd == doneCommand) {
        
        // done recording, so remove commands
        alert.removeCommand(doneCommand);
        alert.removeCommand(pauseCommand);
        
        // add command to playback or start a new recording
        alert.addCommand(startCommand);
        alert.addCommand(playbackCommand);       
        
        // complete the recording
        completeRecording();
        
        // show message
        alert.setString("Press Start Recording to capture new audio or " +
          " Playback Recording to playback recorded audio");
        
      } else if(cmd == playbackCommand) {
        
        // remove the start recording command
        alert.removeCommand(startCommand);   
        
        // start the playback
        playbackPlayer.start();
        
        // and set the message
        alert.setString("Playing back recorded audio");
      }
    } catch(Exception e) {
      error(e);
    }
  }
  
  private void completeRecording() throws Exception {
    
    // flush the output buffer
    bos.flush();
    
    // commit the recording
    recordControl.commit();
    
    // create the input buffer from the output buffer
    bis = new ByteArrayInputStream(bos.toByteArray());
    
    // create the playback player
    playbackPlayer = 
      Manager.createPlayer(bis, recordControl.getContentType());
    
    // add a listener on it
    playbackPlayer.addPlayerListener(this);
    
    // and initialize the recording flag
    recording = false;

  }
  
  public void playerUpdate(Player player, String event, Object data) {
    
    // only listening on the playback player
    if(event.equals(PlayerListener.END_OF_MEDIA)) {
      
      // add the commands back
      alert.addCommand(startCommand);
      alert.addCommand(playbackCommand);
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
