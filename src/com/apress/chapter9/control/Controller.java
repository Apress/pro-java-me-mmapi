package com.apress.chapter9.control;

// import Java ME classes
import com.apress.chapter9.view.EditCanvas;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.CommandListener;

// import application classes
import com.apress.chapter9.BlogException;

import com.apress.chapter9.model.User;
import com.apress.chapter9.model.BlogEntry;
import com.apress.chapter9.model.TextBlogEntry;
import com.apress.chapter9.model.MediaBlogEntry;
import com.apress.chapter9.model.AudioBlogEntry;
import com.apress.chapter9.model.VideoBlogEntry;
import com.apress.chapter9.model.ImageBlogEntry;

import com.apress.chapter9.utils.NetworkRunner;

import com.apress.chapter9.view.EditForm;
import com.apress.chapter9.view.GenericList;
import com.apress.chapter9.view.GenericForm;
import com.apress.chapter9.view.PreviewCanvas;
import com.apress.chapter9.view.EditableDisplay;
import com.apress.chapter9.view.impl.TextEditForm;
import com.apress.chapter9.view.impl.AudioEditCanvas;
import com.apress.chapter9.view.impl.ImageEditCanvas;
import com.apress.chapter9.view.impl.VideoEditCanvas;
import com.apress.chapter9.view.impl.TextPreviewCanvas;

/**
 * Controller is responsible for managing the flow of information between
 * the view and the model and making sure that commands are handled and
 * processed properly. It's the central class for this MIDlet
 */
public class Controller implements Runnable, CommandListener {
  
  // the base MIDlet
  private BootstrapMIDlet midlet = null;
  
  // the display that will be exposed by the base MIDlet
  private Display display = null;
  
  // the lists, startup and action
  private GenericList startUpList = null;
  private GenericList actionList = null;
  
  // the logon and register forms
  private GenericForm logonForm = null;
  private GenericForm registerForm = null;
  
  // the user who is running this MIDlet
  private User user = null;
  
  // alert that is shown when a time consuming task is being performed
  private Alert activityAlert = null;  
  
  // creates lists, forms and initializes parameters
  public Controller(BootstrapMIDlet midlet) {
    this.midlet = midlet;
    this.display = midlet.getDisplay();
    
    // create the activity alert
    activityAlert = new Alert("Please wait...");
    
    try {
      activityAlert.setImage(
        Image.createImage(
          getClass().getResourceAsStream(
            "/media/images/chapter9/working.gif")));
    } catch(Exception e) {}
    
    activityAlert.setTimeout(Alert.FOREVER);
    activityAlert.addCommand(new Command("Cancel", Command.CANCEL, 1)); 
    activityAlert.setCommandListener(this);    
    
    // create lists and forms
    createLists();
    createForms();
  }
  
  /**
   * Simply sets the startup list as current display item
   */
  public void run() {    
    display.setCurrent(startUpList);    
  } 
  
  /**
   * The main Command center
   */
  public void commandAction(Command command, Displayable disp) {
    
    // first get the command type
    int commandType = command.getCommandType();
    
    // if exiting
    if(commandType == Command.EXIT) {
      processExit();
    } else if(commandType == Command.CANCEL) {
      
      // cancel may be called when the user is trying to connect to the
      // network
      
      // first display the startup list
      display.setCurrent(startUpList);
      
      // and then try and cancel the network runner thread in the background
      midlet.getNetworkRunner().cancel();
      
    } else if(commandType == Command.BACK) {
      
      // if going back from a screen
      
      // at startup, can't go anywhere else
      if(disp == startUpList) return;
      
      else if(disp == actionList) { // if actionList
        display.setCurrent(startUpList); // go back to startup
      } else if(disp instanceof GenericForm) { // if any forms
        display.setCurrent(startUpList); // go back to statup
      }
    } else if(commandType == Command.OK) {
      
      // if command type is OK
      if(disp instanceof GenericForm) {
        
        // a generic form is used for login and register
        GenericForm genForm = (GenericForm)disp;

        // first check if all details are entered
        if(!checkForm(genForm)) return;
        
        // now get the username and password
        String uName = ((TextField)genForm.get(0)).getString();
        String password1 = ((TextField)genForm.get(1)).getString();
        
        // create a User first
        user = new User(uName, password1, this);
        
        // now, if logging in,
        if (disp == logonForm) {
          
          // show message and let the user object login
          activityMessage("Connecting to server ...");
          user.login(midlet.getBlogServer().getLoginURL());
         
        } else if(disp == registerForm) {
          
          // if instead, registering, first check if the passwords match
          String password2 = ((TextField)genForm.get(2)).getString();
          
          if(!password2.equals(password1)) {
            midlet.message("The two password strings do not match!", 3000);
            return;
          }
          
          // otherwise show activity message and try to register
          activityMessage("Connecting to server ... ");
          user.register(midlet.getBlogServer().getRegisterURL());
        }
      }       
    } else { // implicit list command handling
      
      if(disp == startUpList) { // if statupList
        
        // first get the selected Index
        int selectedIdx = startUpList.getSelectedIndex();
        
        // if Login
        if(selectedIdx == 0) {
          
          display.setCurrent(logonForm);
          
        } else if(selectedIdx == 1) { // if register
          
          display.setCurrent(registerForm);
          
        } else if(selectedIdx == 2) { // if create entry          
        
          // do we have a user who is logged in?
          if(user == null || !user.isLoggedIn()) {
            midlet.message("Please Login or Register first", 3000);
            return;
          }
          
          display.setCurrent(actionList);
        }
      } else if(disp == actionList) {
      
        // if the user wants to create an entry
        int selectedIdx = actionList.getSelectedIndex();        
       
        // initialize the display
        EditableDisplay editDisplay = null;
        
        // and the associated entry
        BlogEntry blogEntry = null;
        
        // now based on what type of entry it is, create the right
        // editable display and entry
        switch(selectedIdx) {
          case 0: // text only
          {
            blogEntry = new TextBlogEntry(user);
            editDisplay = new TextEditForm(this, blogEntry);
            break;
          }
          case 1: // audio
          {
            blogEntry = new AudioBlogEntry(user);
            editDisplay = new AudioEditCanvas(this, blogEntry);
            break;
          }
          case 2: // image
          {
            blogEntry = new ImageBlogEntry(user);
            editDisplay = new ImageEditCanvas(this, blogEntry);
            break;
          }
          case 3: // only case left, video
          {
            blogEntry = new VideoBlogEntry(user);
            editDisplay = new VideoEditCanvas(this, blogEntry);
            break;
          }
        }
        
        editDisplay.showDisplay();

      }
    }
  } 
  
  /**
   * Has the responsibility of posting an entry to the server
   */
  public void postEntry(BlogEntry entry) {
    
    // first show the message to the user
    activityMessage("Connecting to server ... ");    
   
    // now get the post entry url
    String postURL = midlet.getBlogServer().getPostEntryURL();
    
    // construct the parameters in the URL
    postURL += "?title=" + entry.getEntryTitle() + 
               "&message=" + entry.getEntryMessage() +
               "&dateposted=" + System.currentTimeMillis() + 
               "&uName=" + entry.getUser().getUserName();
    
    // and use the network runner to post the url based on the
    // type of the entry
    if(entry instanceof MediaBlogEntry) {
      
      MediaBlogEntry mediaEntry = ((MediaBlogEntry)entry);
      
      String type = 
        ((mediaEntry instanceof AudioBlogEntry) ? "audio" :
        ((mediaEntry instanceof ImageBlogEntry) ? "image" : "video"));
      
      getNetworkRunner().makeConnection(
        postURL + 
        "&media=" + mediaEntry.guessFileExtension() +
        "&type=" + type +
        "&mediasize=" + mediaEntry.getMediaData().length, entry);
      
    } else {
      getNetworkRunner().makeConnection(postURL);
    }
  }

  
  /**
   * Checks Form elements to see if they have been filled
   */
  private boolean checkForm(GenericForm form) {
    
    int noOfItems = form.size();
    for(int i = 0; i < noOfItems; i++) {
      if(((TextField)form.get(i)).getString().length() == 0) {
        midlet.message("All fields must be filled!", 3000);
        return false;
      }
    }
    
    return true;
  }  

  /**
   * Creates the logon and register forms
   */
  private void createForms() {
    
    logonForm = new GenericForm("Logon", this);
    
    logonForm.append(new TextField("Username", "vikram", 25, TextField.ANY));
    logonForm.append(new TextField("Password", "vikram", 25, TextField.PASSWORD));
    
    registerForm = new GenericForm("Register", this);
    registerForm.append(new TextField("Username", "", 25, TextField.ANY));
    registerForm.append(new TextField("Password", "", 25, TextField.PASSWORD));
    registerForm.append(
      new TextField("Confirm Password", "", 25, TextField.PASSWORD));    
  } 

  /**
   * Creates the startup and action lists
   */
  private void createLists() {
    
    startUpList = new GenericList("Choose One", List.IMPLICIT, this);
    startUpList.append("Login", null);
    startUpList.append("Register", null);
    startUpList.append("Create Blog Entry", null);
    
    actionList = new GenericList("Select Entry Type", List.IMPLICIT, this);
    actionList.append("Text Only", null);
    actionList.append("Audio", null);
    actionList.append("Image", null);
    actionList.append("Video", null);    
  }   
  
  /**
   * message that is shown when a long activity takes place
   */
  public void activityMessage(String msg) {
    activityAlert.setString(msg);
    display.setCurrent(activityAlert);
  }
  
  /**
   * wrapper method called by view and model objects to show a message
   * This method simply calls the midlet's message() method
   */
  public void message(String msg) {
    midlet.message(msg, 3000);
  }    
  
  /**
   * Processes the exit command
   */  
  public void processExit() {
    midlet.destroyApp(true);
    midlet.notifyDestroyed();    
  }  
  
  /**
   * Returns the startupList
   */
  public List getStartUpList() { return this.startUpList; }
  
  /**
   * Returns the Network Runner
   */
  public NetworkRunner getNetworkRunner() { 
    return midlet.getNetworkRunner(); 
  }
  
  /**
   * Returns the MIDlet display
   */
  public Display getDisplay() { return this.display; }  
}
