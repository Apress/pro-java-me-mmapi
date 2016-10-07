package com.apress.chapter9.view.impl;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.Displayable;

import com.apress.chapter9.view.EditForm;
import com.apress.chapter9.model.BlogEntry;
import com.apress.chapter9.control.Controller;
import com.apress.chapter9.view.PreviewCanvas;
import com.apress.chapter9.model.TextBlogEntry;

/**
 * TextEditForm extends EditForm and is used for standalone text entries and
 * for editing text of media based entries
 */
public class TextEditForm extends EditForm {
  
  // the title of this blog entry
  private TextField title = null;
  
  // the message of this blog entry
  private TextField message = null; 
  
  public TextEditForm(Controller controller, BlogEntry entry) {
    
    super(controller, entry);
    
    // cast the entry to the right type
    TextBlogEntry textEntry = (TextBlogEntry)entry;
    
    // create the TextFields
    title = 
      new TextField("Title", textEntry.getEntryTitle(), 255, TextField.ANY);
    message = new TextField(
      "Message", textEntry.getEntryMessage(), 255, TextField.ANY);    

    // and append them to this Form
    append(title);
    append(message);
  }
  
  /**
   * The showDisplay() method displays the text entry fields to the user
   */
  public void showDisplay() {         
    controller.getDisplay().setCurrent(this);
  }  
  
  public void commandAction(Command cmd, Displayable disp) {
    
    int commandType = cmd.getCommandType();
    
    if(commandType == Command.EXIT) {
      controller.processExit();
      return;
    } else if(commandType == Command.OK) {
      
      // get the values from the form, and set them in the entry
      ((TextBlogEntry)entry).setEntryTitle(title.getString());
      ((TextBlogEntry)entry).setEntryMessage(message.getString());
      
      // create a preview canvas and set the entry for it
      TextPreviewCanvas pCanvas = new TextPreviewCanvas(controller, entry);
      
      // and show it
      pCanvas.showCanvas();
    }
  }
}
