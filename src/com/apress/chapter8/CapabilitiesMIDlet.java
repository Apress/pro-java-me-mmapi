package com.apress.chapter8;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class CapabilitiesMIDlet extends MIDlet {  
  
  // all the possible capabilities
  private String[] capabilitiesIdx = {
    "microedition.media.version",
    "supports.mixing",
    "supports.audio.capture",
    "supports.video.capture",
    "supports.recording",
    "audio.encodings",
    "video.encodings",
    "video.snapshot.encodings",
    "streamable.contents"
  };
  
  public void startApp() {
    
    // create a list
    List list = new List("Audio/Video Capabilities", Choice.IMPLICIT);
    
    // and query the device for each of the properties
    for(int i = 0; i < capabilitiesIdx.length; ++i) {
      list.append(
        System.getProperty(capabilitiesIdx[i]) + " - " +
        capabilitiesIdx[i], null);
    }
    
    // show this list    
    Display.getDisplay(this).setCurrent(list);
  }
  
  public void pauseApp() {
  }
  
  public void destroyApp(boolean unconditional) {
  }
}
