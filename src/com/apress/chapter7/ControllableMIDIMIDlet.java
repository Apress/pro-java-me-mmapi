package com.apress.chapter7;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import javax.microedition.media.*;
import javax.microedition.media.control.*;

public class ControllableMIDIMIDlet extends MIDlet implements ItemStateListener {
  
  // the midi player
  Player midiPlayer = null;
  
  // the controls
  VolumeControl volControl = null;
  PitchControl pitchControl = null;
  TempoControl tempoControl = null;
  
  // the visual elements
  Form form = null;
  Gauge volGauge = null;
  Gauge pitchGauge = null;
  Gauge tempoGauge = null;
  
  public ControllableMIDIMIDlet() {
    try {
      
      // load the midi file
      midiPlayer = Manager.createPlayer(
        getClass().getResourceAsStream(
          "/media/midi/chapter7/cabeza.mid"), "audio/midi");
      
      // you must prefetch it to get the controls
      midiPlayer.prefetch();
      
      // extract the controls
      volControl = (VolumeControl) midiPlayer.getControl(
        "javax.microedition.media.control.VolumeControl");
      pitchControl = (PitchControl) midiPlayer.getControl(
        "javax.microedition.media.control.PitchControl");
      tempoControl = (TempoControl) midiPlayer.getControl(
        "javax.microedition.media.control.TempoControl");
      
      // create the visual elements
      form = new Form("MIDI Player", null);
      
      // volume is set at a max of 100 with initial value of 50
      volGauge = new Gauge("Volume: 50", true, 100, 50);
      
      // tempo is set at a max of 30 with 12 (default) as initial value
      tempoGauge = new Gauge("Tempo: 120", true, 30, 12);
      
      // pitch is set at a max of 5 with initial value of 0, and a min of -5
      // note that since pitch can be negative and positive, the starting value
      // is at 5 (which is pitch 0), with +5 being 10 and -5 being 0.
      pitchGauge = new Gauge("Pitch: 0", true, 10, 5);
     
      // add the gauges to the form
      form.append(volGauge);
      form.append(tempoGauge);
      form.append(pitchGauge);
      
      // add the listener to listen to gauge changes     
      form.setItemStateListener(this);     
      
      // and set this form as the current display
      Display.getDisplay(this).setCurrent(form);
      
    } catch(Exception e) {
      System.err.println(e);
    }
  }
  
  /**
   * Listens to changes in control gauges and applies them to actual controls
   */
  public void itemStateChanged(Item item) {
    
    // we are only interested in item state changes of Gauges
    if(!(item instanceof Gauge)) return;
    
    // get the new value of this gauge
    Gauge gauge = (Gauge)item;
    int val = gauge.getValue();
   
    // and change the control and label accordingly
    
    // changing volume?
    if(item == volGauge) { 
      volControl.setLevel(val); 
      volGauge.setLabel("Volume: " + val);
    }
    
    // changing tempo? make sure that tempoControl is available
    if(item == tempoGauge && tempoControl != null) {
      tempoControl.setTempo((val) * 10 * 1000);
      tempoGauge.setLabel("Tempo: " + (val * 10));
    }
    
    // changing pitch? make sure that pitchControl is available
    // remember, actual pitch is (val - 5)
    if(item == pitchGauge && pitchControl != null) {
      pitchControl.setPitch((val - 5) * 12 * 1000);
      pitchGauge.setLabel("Pitch: " + (val - 5));
    }
  }
  
  public void startApp() {
    try {
      // start the MIDI player if it was created
      if(midiPlayer != null) {
        midiPlayer.start();
      }      
    } catch(Exception e) {
      System.err.println(e);
    }
  }
  
  
  // standard pause and destroy methods
  public void pauseApp() {
    try {
      if(midiPlayer != null) {
        midiPlayer.stop();
      }
    } catch(Exception e) {
      System.err.println(e);
    }
  }
  
  public void destroyApp(boolean unconditional) {
    try {
      if(midiPlayer != null) {
        midiPlayer.close();
      }
    } catch(Exception e) {
      System.err.println(e);
    }    
  }  
}
