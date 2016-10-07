package com.apress.chapter6;

import javax.microedition.midlet.*;
import javax.microedition.media.*;
import javax.microedition.media.control.*;

import java.io.*;

public class ConverterMIDlet extends MIDlet {  
  
  public void startApp() {
    try {     
      
      // Load the RTTTL format of the file as an InputStream
      RingToneConverter rtc = 
        new RingToneConverter(
          getClass().getResourceAsStream(
            "/media/misc/happybday.rtttl"), "Happy Birthday");
      
      // get the equivalent sequence
      byte[] seq = rtc.getSequence();
      
      // print it
      printSequence(seq);
      
      // play it
      playSequence(seq);
      
      // print hex sequence
      rtc.dumpSequence();
      
    } catch (Exception e) {
      System.err.println(e);
    }
  }
  
  private void playSequence(byte[] seq) throws Exception {
    
    // create a Tone Player
    Player player = Manager.createPlayer(Manager.TONE_DEVICE_LOCATOR);
    
    // must realize it before getting ToneControl
    player.realize();
    
    // now get the ToneControl
    ToneControl toneControl = (ToneControl)player.getControl("ToneControl");
    
    // set the sequence
    toneControl.setSequence(seq);
    
    // and start the player - this will play the sequence
    player.start();
  }
  
  private void printSequence(byte[] seq) {
    
    // print the sequence in a user friendly format
    byte bite = 0;
    for(int i = 0; i < seq.length; i++) {
      // the first control structure or the note value
      bite = seq[i];
      if(i % 2 == 0) {
        if(bite == ToneControl.VERSION)
          System.err.print("ToneControl.VERSION, ");
        else if(bite == ToneControl.TEMPO)
          System.err.print("ToneControl.TEMPO, ");
        else if(bite == ToneControl.SILENCE)
          System.err.print("ToneControl.SILENCE, ");
        else if(bite == ToneControl.RESOLUTION)
          System.err.print("ToneControl.RESOLUTION, ");
        else if(bite == ToneControl.SET_VOLUME)
          System.err.print("ToneControl.SET_VOLUME, ");
        else
          System.err.print(bite + ", ");
      }
      else {        
        // the value of a control structure or the note duration
        if(i != (seq.length - 1))
          System.err.println(bite + ", ");
        else
          System.err.println(bite);
      }
    }    
    
  }
  
  public void pauseApp() {
  }
  
  public void destroyApp(boolean unconditional) {
  }
}
