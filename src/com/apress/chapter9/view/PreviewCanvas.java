package com.apress.chapter9.view;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.CommandListener;

import com.apress.chapter9.model.BlogEntry;
import com.apress.chapter9.control.Controller;
import com.apress.chapter9.view.impl.TextEditForm;
  
/**
 * The PreviewCanvas is the super class for previewing blog entries to the
 * user. Sub classes have the task of displaying the contents to the user
 */
public abstract class PreviewCanvas extends Canvas 
  implements CommandListener { 

  // the controller object
  protected Controller controller = null;  
  
  // the BlogEntry
  protected BlogEntry entry = null;

  // the commands
  protected Command postEntryCommand = null;
  protected Command discardEntryCommand = null;
  protected Command editTextCommand = null;  
  protected Command exitCommand = null;  
  
  protected PreviewCanvas(Controller controller, BlogEntry entry) {
    super();
    
    // controller and entry are essential and must not be null
    if(controller == null || entry == null) 
      throw new IllegalArgumentException("Controller or entry cannot be null");
    
    this.controller = controller;
    this.entry = entry;
    
    // create commands
    postEntryCommand = new Command("Post Entry", Command.OK, 2);
    discardEntryCommand = new Command("Discard Entry", Command.CANCEL, 2);
    editTextCommand = new Command("Edit Text", Command.SCREEN, 2);
    exitCommand = new Command("Exit", Command.EXIT, 1);
    
    // add them to the Canvas
    addCommand(postEntryCommand);
    addCommand(discardEntryCommand);
    addCommand(editTextCommand);
    addCommand(exitCommand);
    
    // and set the controller as the listener for commamnds
    setCommandListener(this);    
  }
  
  /**
   * Partially done paint method, which only paints the background
   */
  public void paint(Graphics g) {   
   
    // first clear the screen
    g.setColor(0xFF8040);
    g.fillRect(0, 0, getWidth(), getHeight());
    
    // and then make a canvas with a border
    g.setColor(0x808040);
    g.fillRect(5, 5, getWidth() - 10, getHeight() - 10);
    
    // the rest of the work is left to subclasses
        
  }
  
  public void commandAction(Command cmd, Displayable disp) {
    
    // first grab the type of the command
    int commandType = cmd.getCommandType();
    
    if(commandType == Command.EXIT) { 
      
      // let the controller process the exit
      controller.processExit(); 
      
    } else if(commandType == Command.SCREEN) { // means editing text
      
      // create a new text edit form with the BlogEntry
      TextEditForm form = new TextEditForm(controller, entry);
      
      // and show it
      form.showDisplay();
      
    } else if(commandType == Command.CANCEL) { // means discard entry
      
      // simply show the startup list
      controller.getDisplay().setCurrent(controller.getStartUpList());
      
    } else if(commandType == Command.OK) { // means post entry to server
      
      controller.postEntry(entry);
    }
  }  
  
  /**
   * Subclasses must implement this method as they seem fit to show the contents
   * of this canvas
   */
  public abstract void showCanvas();
}
