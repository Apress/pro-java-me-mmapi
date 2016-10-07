package com.apress.chapter8;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.microedition.media.control.VideoControl;
import javax.microedition.media.control.RecordControl;

public class CaptureVideoAndImageMIDlet extends MIDlet 
  implements CommandListener {
  
  // initialize the player and the canvas
  private CaptureVideoCanvas canvas = null;
  Player capturePlayer = null;
  
  // and the other variables
  private Alert alert = null;
  private Command exitCommand = null;
  Display display = null;
  private boolean error = false;  
 
  public CaptureVideoAndImageMIDlet() {
    
    // create the display items
    alert = new Alert("Message");
    display = Display.getDisplay(this);
    exitCommand = new Command("Exit", Command.EXIT, 1);  
    
    // create the video capture player
    try {
      capturePlayer = Manager.createPlayer("capture://video");      
      capturePlayer.realize();
      
      // now create the canvas
      canvas = new CaptureVideoCanvas(this);
      canvas.addCommand(exitCommand);
      canvas.setCommandListener(this);
    } catch(Exception e) {
      error(e);
    }
  }
  
  public void startApp() {
    
    // if error, return
    if(error) return; 
    
    // start the player
    try {
      capturePlayer.start();
    } catch(Exception e) { error(e); }    
    
    // and set the canvas as the current item on display
    display.setCurrent(canvas);
  }
  
  public void pauseApp() {
    try {
      if(capturePlayer != null) capturePlayer.stop();
    } catch(Exception e) {}
  }
  
  public void destroyApp(boolean unconditional) {
    if(capturePlayer != null) capturePlayer.close();
  }
  
  public void commandAction(Command cmd, Displayable disp) {
    if(cmd == exitCommand) {
      destroyApp(true);
      notifyDestroyed();
    }
  }
  
  // general purpose error method, displays on screen as well to output
  void error(Exception e) {
    alert.setString(e.getMessage());
    alert.setTitle("Error");
    display.setCurrent(alert);
    e.printStackTrace();
    error = true;
  }   
}

// the canvas that holds the video
class CaptureVideoCanvas extends Canvas {
  
  // the base midlet
  CaptureVideoAndImageMIDlet midlet = null;
  
  // the video control
  private VideoControl videoControl = null;
  
  // the record control
  private RecordControl rControl = null;
  
  // the output stream
  ByteArrayOutputStream bos = null;
  
  // flag to keep track of when recording is started
  private boolean recording = false;
  
  public CaptureVideoCanvas(CaptureVideoAndImageMIDlet midlet) 
    throws Exception {
    
    this.midlet = midlet;
    
    // initialize the video control
    videoControl = 
      (VideoControl)midlet.capturePlayer.getControl(
        "javax.microedition.media.control.VideoControl");
    
    // if not present, throw error
    if(videoControl == null) 
      throw new Exception("No Video Control for capturing!");
    
    // init display mode to use direct video and this canvas
    videoControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, this);
    
    try { // try and set to full screen
      videoControl.setDisplayFullScreen(true);
    } catch(MediaException me) { 
      
      // but some devices may not support full screen mode
      videoControl.setDisplayLocation(5, 5);
      try {
        videoControl.setDisplaySize(getWidth() - 10, getHeight() - 10);
      } catch(Exception e) {}
      
      repaint();
    } 
    
    // and make the video control visible
    videoControl.setVisible(true);
  }
  
  public void keyPressed(int keyCode) {
    
    // see what game action key the user has pressed
    int key = getGameAction(keyCode);
    
    // if the camera is recording at the moment, then just return
    // unless the key code is DOWN to stop recording
    if(recording && (key != DOWN)) return;
    
    // if fire, take a snapshot
    if(key == FIRE) {      
      
      try {
        // use the control to take the picture, using the default encoding
        byte[] imageArray = videoControl.getSnapshot(null);
        
        // create an image using the LCDUI Image class
        Image image = Image.createImage(imageArray, 0, imageArray.length);
        
        // make this image a part of an Alert
        Alert imageAlert = 
          new Alert("Snapshot", "", image, AlertType.INFO);
        
        // show this alert for 5 seconds
        imageAlert.setTimeout(5000);
        
        // and show this alert
        midlet.display.setCurrent(imageAlert);
        
      } catch(Exception e) {
        midlet.error(e);
      }
    } else if(key == UP) {
      
      // start recording
      try {
        
        // grab a record control
        rControl = (RecordControl)midlet.capturePlayer.getControl(
            "javax.microedition.media.control.RecordControl");
        
        // if not found, throw exception
        if(rControl == null) throw new Exception("No RecordControl found!");
        
        // create a ByteArrayOutputStream to store this recorded data in
        bos = new ByteArrayOutputStream(); 
        
        // set up the stream
        rControl.setRecordStream(bos);
        
        // and start recording - no need to start the underlying player
        // as it is already started
        rControl.startRecord();
        
        // set flag
        recording = true;
      } catch(Exception e) {
        midlet.error(e);
      } 
    } else if(key == DOWN) {      
      
      try {
     
        rControl.stopRecord();
        rControl.commit();
        
        // do what is required with the byte array now, save it, display to user
        // or discard        
        
        // reset the recording flag
        recording = false;
      } catch(Exception e) {
        midlet.error(e);
      }     
    }
  }
  
  public void paint(Graphics g) {
    
		// clear background
    g.setColor(0xffffff);
		g.fillRect(0, 0, getWidth(), getHeight()); 
    
    // and draw a rectangle with a different color
    g.setColor(0x44ff66);
    g.drawRect(2, 2,  getWidth() - 4, getHeight() - 4);       
  }
}
