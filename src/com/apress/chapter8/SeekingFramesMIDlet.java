package com.apress.chapter8;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import javax.microedition.media.control.VideoControl;
import javax.microedition.media.control.FramePositioningControl;

public class SeekingFramesMIDlet extends MIDlet {
  
  private Alert alert = null;
  private Display display = null;
  
  private Player player = null;
  private FramePositioningControl fControl = null;
  private VideoControl vControl = null;
  private VideoCanvas canvas = null;
  
  private boolean error = false;
  
  public SeekingFramesMIDlet() {
    display = Display.getDisplay(this);
    alert = new Alert("Message");
    
    // load the video
    try {
      player = Manager.createPlayer(
        getClass().getResourceAsStream(
          "/media/video/chapter8/sydharbour.mp4"), "video/mp4");
      
      fControl = (FramePositioningControl)player.getControl(
        "javax.microedition.media.control.FramePositioningControl");

      vControl = (VideoControl)player.getControl(
        "javax.microedition.media.control.VideoControl");
      
      if(fControl == null || vControl == null) throw new Exception(
        "One of the controls not found!");      
      
      // uses the VideoCanvas from DisplayVideoMIDlet
      canvas = new VideoCanvas();
      
      vControl.setDisplayFullScreen(true);
      vControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, canvas);
      
      display.setCurrent(canvas);
      
    } catch(Exception e) { error(e); }
    
  }
  
  public void startApp() {
    if(error) return;
    
    try {
      player.start();
      vControl.setVisible(true);
    } catch(Exception e) { error(e); }
  }
  
  public void pauseApp() {
  }
  
  public void destroyApp(boolean unconditional) {
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

