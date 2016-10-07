package com.apress.chapter5;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class ThreadTest extends MIDlet implements CommandListener {
  
  private Form form;
  private StringItem text;
  private Display display;
  
  public ThreadTest() {
    form = new Form("Thread Test");
    
    // displays the name of the current system thread
    text = new StringItem(
      "Current Thread:",
      Thread.currentThread().getName());
    
    form.append(text);
    
    // commands to exit and create new threads
    Command exit = new Command("Exit", Command.EXIT, 1);
    Command newThread = new Command("New Thread", Command.SCREEN, 1);
    
    form.addCommand(exit);
    form.addCommand(newThread);
    
    form.setCommandListener(this);
    
    display = Display.getDisplay(this);
  }
  
  public void startApp() {
    display.setCurrent(form);
  }
  
  public void pauseApp() {
  }
  
  public void destroyApp(boolean unconditional) {
  }
  
  public void commandAction(Command cmd, Displayable disp) {
    if(cmd.getLabel().equals("Exit")) {
      notifyDestroyed();
    } else {
      System.err.print("Starting new thread ... ");
      
      // create a new thread
      Thread runner = new Thread(new ThreadRunner(display));
      
      // and start it
      runner.start();
      
      System.err.println("Done");
    }
  }
}

class ThreadRunner implements Runnable {
  
  // the parent MIDlet's display
  Display display;
  
  ThreadRunner(Display display) {
    this.display = display;
  } 
  
  public void run() {
    
    System.err.print(
      "New thread ( " + Thread.currentThread().getName() + " ) running .. ");
    
    display.setCurrent(
      new Alert(Thread.currentThread().getName()));
    
    try {
      Thread.sleep(3000);
    } catch(Exception e) {}
    
    System.err.println("Done");
  }
}
