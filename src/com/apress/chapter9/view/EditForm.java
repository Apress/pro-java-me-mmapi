package com.apress.chapter9.view;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;

import com.apress.chapter9.model.BlogEntry;
import com.apress.chapter9.control.Controller;
  
/**
 * The EditForm is the super class for editing form based blog entries. 
 * Sub classes have the task of displaying the contents to the user
 */
public abstract class EditForm extends Form 
  implements EditableDisplay, CommandListener { 

  // the controller object
  protected Controller controller = null;  

  // the commands
  protected Command okCommand = null;  
  protected Command exitCommand = null;  
  
  // and the blog entry object
  protected BlogEntry entry = null;  
 
  protected EditForm(Controller controller, BlogEntry entry) {
    super("Edit Form");
    
    // controller and entry are essential, and must not be null
    if(controller == null || entry == null) 
      throw new IllegalArgumentException("Controller or entry cannot be null");
    
    this.controller = controller;
    this.entry = entry;
    
    // create commands
    okCommand = new Command("Ok", Command.OK, 1);
    exitCommand = new Command("Exit", Command.EXIT, 1);
    
    // add them to the Form
    addCommand(okCommand);
    addCommand(exitCommand);
    
    // and set the controller as the listener for commamnds
    setCommandListener(this); 
  } 
  
  /**
   * Returns the associated blog entry
   */
  public BlogEntry getBlogEntry() {
    return this.entry;
  }
}
