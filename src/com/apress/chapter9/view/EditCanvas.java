package com.apress.chapter9.view;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.CommandListener;

import com.apress.chapter9.model.BlogEntry;
import com.apress.chapter9.control.Controller;
  
/**
 * The EditCanvas is the super class for editing blog entries. Sub classes 
 * have the task of displaying the contents to the user
 */
public abstract class EditCanvas extends Canvas 
  implements EditableDisplay, CommandListener { 

  // the controller object
  protected Controller controller = null;  

  // the commands
  protected Command doneCommand = null;  
  protected Command exitCommand = null;  
  
  // the blog entry
  protected BlogEntry entry = null;  
 
  protected EditCanvas(Controller controller, BlogEntry entry) {
    
    super();
    
    // controller and entry are essential, and must not be null
    if(controller == null || entry == null) 
      throw new IllegalArgumentException("Controller or entry cannot be null");
    
    this.controller = controller;
    this.entry = entry;
    
    // create commands
    doneCommand = new Command("Done", Command.OK, 1);
    exitCommand = new Command("Exit", Command.EXIT, 1);
    
    // add them to the Canvas
    addCommand(doneCommand);
    addCommand(exitCommand);
    
    // and set the controller as the listener for commamnds
    setCommandListener(this);    
  }
  
  public void paint(Graphics g) {
    
    // first clear the screen
    g.setColor(0xFF8040);
    g.fillRect(0, 0, getWidth(), getHeight());
    
    // and then make a canvas with a border
    g.setColor(0x808040);
    g.fillRect(5, 5, getWidth() - 10, getHeight() - 10);
    
    // the rest of the work is left to subclasses
    
  }

}
