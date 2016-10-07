package com.apress.chapter9.view.impl;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Graphics;

import javax.microedition.media.Player;
import javax.microedition.media.Manager;

import com.apress.chapter9.control.Controller;

import com.apress.chapter9.view.PreviewCanvas;

import com.apress.chapter9.model.BlogEntry;
import com.apress.chapter9.model.ImageBlogEntry;

public class ImagePreviewCanvas extends PreviewCanvas {
  
  public ImagePreviewCanvas(Controller controller, BlogEntry entry) {
    super(controller, entry);
    setTitle("Image Entry Preview");
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
    
    // now simply create an Image from the byte array, and show it on 
    // the canvas
    Image snapshot = null;
    
    try {
      
      ImageBlogEntry imgEntry = ((ImageBlogEntry)entry);
      byte[] imgData = imgEntry.getMediaData();
      snapshot = Image.createImage(imgData, 0, imgData.length);  
      
    } catch(Exception e) {     
      
      // show the error message to the user
      controller.message(e.getMessage());
      
      // and return
      return;
    }
    
    // now draw the image on this canvas
    g.drawImage(snapshot, getWidth()/2, getHeight()/2, 
      Graphics.HCENTER | Graphics.VCENTER);
  }
  
}
