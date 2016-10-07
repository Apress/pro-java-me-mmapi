package com.apress.chapter9.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import com.apress.chapter9.model.BlogEntry;
import com.apress.chapter9.control.Controller;
import com.apress.chapter9.model.MediaBlogEntry;
import com.apress.chapter9.control.BootstrapMIDlet;

public class NetworkRunner extends Thread {

  // the calling MIDlet
  private BootstrapMIDlet midlet = null;

  // the controller
  private Controller controller = null;

  // the reusable connection
  private HttpConnection connection = null;

  // flags
  private boolean cancel = false;
  private boolean running = false;

  // the url to connect to, this will change
  private String url = null;

  // the BlogEntry that may be sent
  private BlogEntry entry = null;

  // flags that indicate the success or failure of logging in,
  // registration and posting
  private boolean logInSuccess = false;
  private boolean registerSuccess = false;
  private boolean postSuccess = false;

  public NetworkRunner(BootstrapMIDlet midlet, Controller controller) {
    this.midlet = midlet;
    this.controller = controller;
    running = true;
  }

  /**
   * Waits till a request is made for making a connection
   */
  public synchronized void run() {
    while(running) {
      try {
        wait();
      } catch(InterruptedException ie) {}

      if(running) connect();
    }
  }

  /**
   * Calling threads must use this method to make a connection
   * providing the URL to connect to
   */
  public synchronized void makeConnection(String url) {
    makeConnection(url, null);
  }

  public synchronized void makeConnection(String url, BlogEntry entry) {
    this.url = url;
    this.entry = entry;
    notify();
  }

  /**
   * Tries to make a connection with the network server, using the
   * URL that is provided to the makeConnection method
   */
  private void connect() {

   InputStream in = null;
   OutputStream out = null;

    try {

      // first encode the URL String
      controller.activityMessage("Encoding URL ...");
      String encodedURL = URLEncoder.encodeURL(this.url);

      // next get an HttpConnection on this URL
      controller.activityMessage("Opening connection ...");
      connection =
        (HttpConnection)Connector.open(encodedURL);

      // now check if this connection contains a media object by way of an Entry object
      // if yes, then make this connection via HTTP POST
      if(this.entry != null) {

        // get the media data from this entry
        byte[] mediaData = ((MediaBlogEntry)entry).getMediaData();

        // set connection type and content length
        connection.setRequestMethod(HttpConnection.POST);
        connection.setRequestProperty(
          "Content-Length", (mediaData.length) + "");

        // open the OutputStream over which the media data will be sent
        out = connection.openOutputStream();

        // write the media data over this stream
        controller.activityMessage("Sending media data ...");
        out.write(mediaData);

        // to actually flush the stream check the response code
        int responseCode = connection.getResponseCode();

        if(responseCode != HttpConnection.HTTP_OK)
          throw new IOException("Transmission failed as server " +
            "responded with response code: " + responseCode);
      }

      // now irrespective of whether it was a POST or GET,
      // simply display to the user the response from the server

      // grab the input stream
      controller.activityMessage("Reading response ...");
      in = connection.openInputStream();

      // this can take a long time.. so check if the user has cancelled
      // before this
      if(cancel) return;

      // see the response and make sure it is not more than
      // 255 characters long
      int contentLength = (int)connection.getLength();
      if (contentLength == -1) contentLength = 255;
      byte[] data = new byte[contentLength];
      int length = in.read(data);

      // create a response message
      String message = new String(data, 0, length);
      midlet.message(message, 3000);

      // and process the type of the message
      processMessage(message);
    }
    catch (IOException ioe) {
      midlet.error(ioe); // show error
    } finally {

      // close connections
      if (!cancel) {
        try {
          if (in != null) in.close();
          if (out != null) out.close();
          if (connection != null) connection.close();
        }
        catch (IOException ie) {}
      }

      this.cancel = false;

    }
  }

  /**
   * This method is a way for the NetworkRunner to inform the rest of the
   * classes about the state of logging, registering and posting
   */
  private void processMessage(String message) {
    if(message == null) return;

    message = message.trim();

    if(message.equals("Logged In!")) { logInSuccess = true; }
    else if(message.equals("Registered")) registerSuccess = true;
    else if(message.equals("Posted")) postSuccess = true;
  }

  public boolean isLoggedIn() { return this.logInSuccess; }
  public boolean isRegistered() { return this.registerSuccess; }
  public boolean isPosted() { return this.postSuccess; }

  /**
   * Cancel the network attempt
   */
  public void cancel() {
    try {
      cancel = true;
      if (connection != null) connection.close();
    } catch (IOException io) {} // ignore
  }

}
