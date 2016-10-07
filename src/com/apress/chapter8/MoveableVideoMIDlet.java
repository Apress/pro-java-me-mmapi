package com.apress.chapter8;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.media.control.VideoControl;
import javax.microedition.media.control.VolumeControl;

public class MoveableVideoMIDlet extends MIDlet implements CommandListener {
  
  // all the controls and containers
  private Player player = null;
  private VideoControl videoControl = null;
  private VolumeControl volControl = null;
  
  private MovableVideoCanvas canvas = null;
  private Command exitCommand = null;
  private Command stopAudioCommand = null;
  
  private Display display = null;
  private Alert alert = null;
  
  private boolean error = false;
    
  public MoveableVideoMIDlet() {
    
    // create the alerts, canvas and displays
    display = Display.getDisplay(this);
    
    alert = new Alert("Error");
    exitCommand = new Command("Exit", Command.EXIT, 1);
    alert.addCommand(exitCommand);
    alert.setCommandListener(this);
    
    // load the Player and then the Volume and VideoControl
    try {
      
      // change content type for different devices, mp4 for C975, mpeg4 for M75
      player = Manager.createPlayer(getClass().getResourceAsStream(
        "/media/video/chapter8/sydharbour.mp4"), "video/mp4");
      player.realize();
      
      // realize the two controls
      videoControl = (VideoControl)player.getControl(
        "javax.microedition.media.control.VideoControl");      
      volControl = (VolumeControl)player.getControl(
        "javax.microedition.media.control.VolumeControl");            
    } catch (Exception e) {
      error(e);
    }
    
    if(!error) {
      
      // if no error, create the canvas and add commands to it
      canvas = new MovableVideoCanvas();
      canvas.setVideoControl(videoControl);
      stopAudioCommand = new Command("Stop Audio", Command.SCREEN, 1);

      canvas.addCommand(exitCommand);
      canvas.addCommand(stopAudioCommand);
      canvas.setCommandListener(this);
      
      // initialize the VideoControl display
      videoControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, canvas);
      
      // and position it in the center of the canvas
      int halfCanvasWidth = canvas.getWidth()/2;
      int halfCanvasHeight = canvas.getHeight()/2;
      
      try {      
        videoControl.setDisplayFullScreen(false);
        videoControl.setDisplaySize(halfCanvasWidth, halfCanvasHeight);
        videoControl.setDisplayLocation(halfCanvasWidth/2, halfCanvasHeight/2);      
        videoControl.setVisible(true);
      } catch(Exception e) {
        error(e);
      }      
    }    
  }
  
  public void startApp() {
    
    if(error) return;   
    
    try {      
      player.start();
    } catch(Exception e) {
      error(e);
    }    
    display.setCurrent(canvas);    
  }
  
  public void pauseApp() {
  }
  
  public void destroyApp(boolean unconditional) {
    try {
      if(player != null) player.close();
    } catch(Exception e) { error(e); }
  }
  
  public void commandAction(Command cmd, Displayable disp) {
    if(cmd == exitCommand) {
      destroyApp(true);
      notifyDestroyed();      
    } else if(cmd == stopAudioCommand) { // if stop audio, mute VolumeControl
      if(volControl != null) volControl.setMute(true);
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

// MovableVideoCanvas that is the container for the video
class MovableVideoCanvas extends GameCanvas {
  
  // VideoControl that will be managed
  private VideoControl videoControl = null;
  
  // distance to move
  private int dx, dy = 2;  
  
  public MovableVideoCanvas() {
    super(false); // do not supress key events
  }
  
  void setVideoControl(VideoControl vControl) {
    this.videoControl = vControl;
  }
  
  public void paint(Graphics g) { 
		// clear the screen first
		g.setColor(0xffffff);
		g.fillRect(0, 0, getWidth(), getHeight());  
    
    // and flush off screen buffer to actual screen
    flushGraphics();
  }
  
  public void keyPressed(int keyCode) {

		// handles the user's interaction with the screen by capturing key press
    int gameAction = getGameAction(keyCode);
    int y = videoControl.getDisplayY();

    // only move in vertical direction
    if(gameAction == UP) {
			y -= dy;
		} else if(gameAction == DOWN) {
			y += dy;
		}
    
    // set the new location of the video
    videoControl.setDisplayLocation(videoControl.getDisplayX(), y);   
    
    // and repaint
    repaint();     
  } 

}
