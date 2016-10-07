package com.apress.chapter8;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import javax.microedition.media.control.*;

public class DisplayVideoMIDlet extends MIDlet implements CommandListener {
  
  // the list to show the choice - form or canvas
  private List list = null;
  
  // the canvas to display the video on
  private Canvas canvas = null;
  
  // the form to add the video to
  private Form form = null;
  
  // a string item to add to form
  private StringItem descriptionItem = null;
 
  // the video player
  Player player = null;
  
  // commands
  private Command backCommand = null;
  private Command exitCommand = null;
  
  // alert to show messages on
  private Alert alert = null;
  
  // and the display
  private Display display = null;
  
  // a flag to indicate error
  private boolean error = false;
  
  public DisplayVideoMIDlet() {
    
    // create the visual elements
    display = Display.getDisplay(this);
    exitCommand = new Command("Exit", Command.EXIT, 1);
    backCommand = new Command("Back", Command.ITEM, 1);
    
    // VideoCanvas is a non public class in this file
    canvas = new VideoCanvas();
    canvas.addCommand(exitCommand);
    canvas.addCommand(backCommand);
    canvas.setCommandListener(this);
    
    // create the form and add items and commands to it
    form = new Form("Video Form", null);
    descriptionItem = new StringItem("Desc: ", "Sydney Harbour - Bad audio");
    form.append(descriptionItem);
    form.addCommand(exitCommand);
    form.addCommand(backCommand);
    form.setCommandListener(this);
    
    // create the list
    list = new List("Pick One", List.IMPLICIT);
    list.append("Play Video on Form", null);
    list.append("Play Video on Canvas", null);
    list.addCommand(exitCommand);
    list.setCommandListener(this);
    
    // and an alert for errors
    alert = new Alert("Error");   
  }
  
  public void startApp() {
    if(error) return;
    display.setCurrent(list); // show the list if no errors
  }
  
  public void pauseApp() {
  }
  
  public void destroyApp(boolean unconditional) {
    // close the player instance
    try {
      if(player != null) player.close();
    } catch(Exception e) { 
      error(e);
    }    
  }
  
  public void commandAction(Command cmd, Displayable disp) {
    
    // if exit
    if(cmd == exitCommand) {
      destroyApp(true);
      notifyDestroyed();
    } else if(cmd == backCommand) { // if the user clicks back
      
      // close the player instance
      try {
        if(player != null) player.close();
      } catch(Exception e) { 
        error(e);
      }
      
      // display the list
      display.setCurrent(list);
      
      // and return
      return;
    }
    
    // implicit list handling
    try {
      
      // first load the Player instance
      loadPlayer();
      
      if(list.getSelectedIndex() == 0) { // form video

        // extract the GUIControl
        GUIControl guiControl = (GUIControl)player.getControl(
          "javax.microedition.media.control.GUIControl");
        
        // if not found, throw error
        if(guiControl == null) throw new Exception("No GUIControl!!");
        
        // add as a video item by initializing it to use GUI Primitive
        Item videoItem = 
          (Item)guiControl.initDisplayMode(
            GUIControl.USE_GUI_PRIMITIVE, null);
        
        // insert at first place
        form.insert(0, videoItem);
        
        // show the form
        display.setCurrent(form);
        
        // finally start the player instance
        player.start();
      } else {  // canvas video        

        // grab the videocontrol
        VideoControl videoControl = (VideoControl)player.getControl(
          "javax.microedition.media.control.VideoControl");
        
        // if not found throw error
        if(videoControl == null) throw new Exception("No VideoControl!!");
        
        // initialize to use direct video and show on canvas
        videoControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, canvas);
        
        // make sure it is displayed full screen
        // not all devices support this, if your device is having trouble
        // showing the video, comment this line        
        videoControl.setDisplayFullScreen(true);
        
        // must make the control visible
        videoControl.setVisible(true);     
        
        // now show the canvas
        display.setCurrent(canvas);
        
        // and start the player
        player.start();        
      }
    } catch(Exception e) {
      error(e);
    }    
  }
  
  private void loadPlayer() throws Exception {
    
    // loads the Player on this MP4 file.
    // IMPORTANT: Change content type here for C975 to video/mp4
    // and M75 to video/mpeg4 or use Netbeans device fragmentation
    // feature
    player = Manager.createPlayer(
      getClass().getResourceAsStream(
        "/media/video/chapter8/sydharbour.mp4"), "video/mpeg4");

    player.realize();    
  }
  
  // general purpose error method, displays on screen as well to output
  private void error(Exception e) {
    alert.setString(e.getMessage());
    alert.setTimeout(Alert.FOREVER);
    display.setCurrent(alert);
    e.printStackTrace();
    error = true;
  }    
}

// VideoCanvas that is the container for the video
class VideoCanvas extends Canvas {
  public void paint(Graphics g) { 
    // does nothing..
  }
}
