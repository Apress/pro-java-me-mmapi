package com.apress.chapter9.view;

import com.apress.chapter9.control.Controller;

import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Command;

/**
 * GenericList is a list to create lists that have commands back and exit
 * attached to them
 */
public class GenericList extends List {
  
  protected Controller controller = null;
  
  protected Command backCommand = null;
  protected Command exitCommand = null;
  
  public GenericList(String title, int listType, Controller controller) {
    this(title, listType, new String[] {}, null, controller);
  }
  
  public GenericList(String title, int listType, 
    String[] stringElements, Image[] imageElements, Controller controller) {
    
    super(title, listType, stringElements, imageElements);
    
    // controller must not be null
    if(controller == null) 
      throw new IllegalArgumentException("Controller cannot be null");
    
    this.controller = controller;
    
    // create the commands
    backCommand = new Command("Back", Command.BACK, 1);
    exitCommand = new Command("Exit", Command.EXIT, 1);
    
    // add them to this List
    addCommand(backCommand);
    addCommand(exitCommand);
    
    // and set the controller as the listener for the commands
    setCommandListener(controller);
  }
  
}
