package com.apress.chapter9.view.impl;

import java.io.IOException;

// import javax microedition packages
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Displayable;

// import application packages
import com.apress.chapter9.model.BlogEntry;
import com.apress.chapter9.model.TextBlogEntry;
import com.apress.chapter9.model.MediaBlogEntry;
import com.apress.chapter9.model.AudioBlogEntry;

import com.apress.chapter9.control.Controller;
import com.apress.chapter9.view.PreviewCanvas;
import com.apress.chapter9.view.impl.TextEditForm;

/**
 * This class allows the user to preview a text based entry
 */
public class TextPreviewCanvas extends PreviewCanvas {
  
  public TextPreviewCanvas(Controller controller, BlogEntry entry) {
    super(controller, entry);
    setTitle("Text Preview");
  }
  
  public void showCanvas() {
    
    // make this canvas as the current display item
    controller.getDisplay().setCurrent(this);
    
    // and then repaint it
    repaint();
  }
  
  public void paint(Graphics g) {
    
    // get the super class to clear background and repaint it first
    super.paint(g);
    
    // set the font color to white
    g.setColor(0xFFFFFF);

    // grab the title and message from the entry
    String title = ((TextBlogEntry)entry).getEntryTitle();
    String message = ((TextBlogEntry)entry).getEntryMessage(); 
  
    // draw the title
    g.drawString(title, 10, 10, Graphics.TOP | Graphics.LEFT);
    
    // draw a line
    g.drawLine(5, 40, getWidth() - 8, 40);
    
    // see if this is a media entry, let the user know by drawing an image
    if(entry instanceof MediaBlogEntry) {
      
      if(entry instanceof AudioBlogEntry) {
      
      // draw the speaker image in the foreground
      try {
        
        g.drawImage(Image.createImage(
          getClass().getResourceAsStream("/media/images/chapter9/audio.gif")), 
          getWidth() - 10, getHeight() - 10, 
          Graphics.VCENTER | Graphics.HCENTER);
        
      } catch(IOException ioex) {} // ignore exception        
      }
      
      
    }
    
    // then draw the message
    g.drawString(message, 10, 60, Graphics.TOP | Graphics.LEFT);    
    
  }

}
