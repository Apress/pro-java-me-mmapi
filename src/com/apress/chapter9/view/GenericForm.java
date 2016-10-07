package com.apress.chapter9.view;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.Command;

import com.apress.chapter9.control.Controller;

/**
 * GenericForm creates Forms that have back, exit and ok commands attached to 
 * it.
 */
public class GenericForm extends Form {
  
  // the controller
  protected Controller controller = null;
  
  // the commands
  protected Command backCommand = null;
  protected Command exitCommand = null;
  protected Command okCommand = null;

  public GenericForm(String title, Controller controller) {
    this(title, null, controller);
  }
  
  public GenericForm(String title, Item[] items, Controller controller) {
    super(title, items);
    
    // controller can't be null
    if(controller == null) 
      throw new IllegalArgumentException("Controller cannot be null");
    
    this.controller = controller;
    
    // create commands
    backCommand = new Command("Back", Command.BACK, 1);
    exitCommand = new Command("Exit", Command.EXIT, 1);
    okCommand = new Command("Ok", Command.OK, 1);
    
    // add them to this Form
    addCommand(backCommand);
    addCommand(exitCommand);
    addCommand(okCommand);
    
    // and set the controller as the listener    
    setCommandListener(controller);
  }
  
}
