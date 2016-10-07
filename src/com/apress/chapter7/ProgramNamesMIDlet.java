package com.apress.chapter7;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import javax.microedition.media.control.MIDIControl;

public class ProgramNamesMIDlet extends MIDlet implements CommandListener {
  
  // MIDlet's display
  private Display display = null;

  // the bank and program lists
  private List bankList = null;
  private List programList = null;
  
  // the actual banks and programs (instruments)
  private int[] banks = null;
  private int[] programs = null;
  
  // the player and MIDIControl
  private Player player = null;
  private MIDIControl mControl = null;
  
  public ProgramNamesMIDlet() {
    
    display = Display.getDisplay(this);

    try {
      // create a MIDI player
      player = Manager.createPlayer(Manager.MIDI_DEVICE_LOCATOR);

      // prefetch
      player.prefetch();
      
      // extract MIDI Control
      mControl = (MIDIControl)player.getControl(
        "javax.microedition.media.control.MIDIControl"); 
      
    } catch (Exception e) {
      error(e);
    }
  }
  
  public void startApp() {
    
    try {
      
      // check if we can query the banks
      if(mControl.isBankQuerySupported()) {
        
        // get the list of banks
        banks = mControl.getBankList(false);  
        
        // create a list to display the banks
        bankList = new List("Installed Banks", Choice.IMPLICIT);
        
        // and populate the list
        for(int i = 0; i < banks.length; i++) {
          bankList.append("Bank " + banks[i], null);
        }
        
        // add this MIDlet as the CommandListener
        bankList.setCommandListener(this);
        
        // and show the list
        display.setCurrent(bankList);
        
      } else { // Bank Query not supported
        display.setCurrent(new Alert("Bank Query not supported"));
      }
      
    } catch(Exception e) {
      error(e);
    }
  }
  
  public void commandAction(Command cmd, Displayable disp) {
    
    // only bankList and programList commands are being listened for
    if(disp != bankList && disp != programList) return; 
    
    try {
      
      // for bankList
      if(disp == bankList) {
        
        // the selected bank
        int selectedBank = banks[bankList.getSelectedIndex()];
        
        // get the programs for this selected bank
        programs = mControl.getProgramList(selectedBank);

        // and create a programList
        programList = 
          new List("Programs for Bank: " + selectedBank, Choice.IMPLICIT);

        // populate this list
        for(int i = 0; i < programs.length; i++) {
          programList.append("Program " + programs[i] + " - " +
            mControl.getProgramName(selectedBank, programs[i]), null);
        }

        // add this MIDlet as the CommandListener
        programList.setCommandListener(this);

        // and show this list
        display.setCurrent(programList);
        
      } else { // can be programList only        
       
        // send command to change program
        mControl.setProgram(
          11, // on channel 11
          banks[bankList.getSelectedIndex()], // the selected bank
          programs[programList.getSelectedIndex()]); // the selected program
        
        // sleep
        Thread.sleep(100); 
        
        // send a Note ON command
        mControl.shortMidiEvent(MIDIControl.NOTE_ON | 11, 60, 100);
        
        // sleep
        Thread.sleep(100);
        
        // send a Note OFF command
        mControl.shortMidiEvent(MIDIControl.NOTE_ON | 11, 60, 0);
        
        // shorter sleep
        Thread.sleep(20);        
      }
      
    } catch(Exception e) {
      error(e);
    }
  }
  
  // general purpose error method, displays on screen as well to output
  private void error(Exception e) {
    display.setCurrent(new Alert(e.getMessage()));
    e.printStackTrace();
  }
  
  public void pauseApp() {
  }
  
  public void destroyApp(boolean unconditional) {
  }
}
