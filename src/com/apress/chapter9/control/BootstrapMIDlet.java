package com.apress.chapter9.control;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Command;
import javax.microedition.midlet.MIDlet;

import com.apress.chapter9.BlogException;
import com.apress.chapter9.model.BlogServer;
import com.apress.chapter9.utils.NetworkRunner;

/**
 * BootstrapMIDlet is the startup MIDlet that loads the initial data and
 * starts the service. If any initial parameters are missing it will abort.
 * It requires the URL's for posting, logging and registering defined in the
 * MIDlet JAD and manifest file.
 */
public class BootstrapMIDlet extends MIDlet {  
  
  // the display
  private Display display = null;
  
  // the controller that will drive this MIDlet  
  private Controller controller = null;
  
  // the thread in which this MIDlet will be run
  private Thread runner = null;
  
  // the thread used for creating network connections
  private NetworkRunner networkThread = null; 
  
  // the BlogServer contains the URL information
  private BlogServer blogServer = null;
  
  // flag to indicate if any error occurs
  private boolean error = false;  
 
  public void startApp() {
    
    if(display == null) {
      // get the display
      display = Display.getDisplay(this);   

      // load the initial URL parameters
      try {
        loadParameters();
      } catch(BlogException be) {
        error(be);
      }

      // create the new Controller passing this MIDlet as the reference
      controller = new Controller(this);   

      // create the runner thread
      runner = new Thread(controller);  

      // create the Network Runner thread
      networkThread = new NetworkRunner(this, controller);      
      
      runner.start();
      networkThread.start();      
    }    
  }
  
  /** 
   * loads the URL parameters from the Manifest
   */
  private void loadParameters() throws BlogException {
    
    // the registration URL
    String registerURL = getAppProperty("Blog-registerURL");
    
    // the login URL
    String loginURL = getAppProperty("Blog-loginURL");
    
    // the URL for posting entries
    String postEntryURL = getAppProperty("Blog-postURL");
    
    // if any of them is null or not defined, throw Exception
    if(registerURL == null || registerURL.length() == 0 || 
       loginURL == null || loginURL.length() == 0 || 
       postEntryURL == null || postEntryURL.length() == 0)
      throw new BlogException("One of the Blog server URL's is not defined.");    
    
    // otherwise create the BlogServer information object
    blogServer = new BlogServer(registerURL, loginURL, postEntryURL);
  }
  
  // getter for BlogServer
  public BlogServer getBlogServer() { return this.blogServer; }
  
  // getter for Display
  public Display getDisplay() { return this.display; }
  
  // getter for NetworkThread
  public NetworkRunner getNetworkRunner() { return this.networkThread; }

  public void pauseApp() {
  }
  
  public void destroyApp(boolean unconditional) {
  }
   
  /**
   * General purpose non-error message display method
   */
  public void message(String msg, int time) {
    Alert alert = new Alert("Message");
    alert.setString(msg);
    alert.setTimeout(time);
    
    display.setCurrent(alert, controller.getStartUpList());
  }
  
  /**
   * General purpose error message display method
   */
  public void error(Exception e) {
    Alert alert = new Alert("Error");
    alert.setString(e.getMessage());
    alert.setTimeout(Alert.FOREVER);
    
    display.setCurrent(alert, controller.getStartUpList());
    
    error = true;
  }
}
