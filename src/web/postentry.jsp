<%@ page contentType="text/plain;charset=UTF-8" language="java" import="java.io.File, java.io.*" %>

<%--
  Simple postentry.jsp tries to post an entry to the server
--%>

<%

  String title = (String)request.getParameter("title");
  String message = (String)request.getParameter("message");
  String datePosted = (String)request.getParameter("dateposted");
  String username = (String)request.getParameter("uName");
  String media = (String)request.getParameter("media");
  String type = (String)request.getParameter("type");
  String mediaSize = (String)request.getParameter("mediasize");
  
  // only username is a required value
  if(username == null || username.length() == 0) {
    
    out.println("Required parameter username is missing");
    return;
  }  

  // now find out the working dir
  String workingDir = System.getProperty("user.dir");

  // try to get to the user directory with the given username
  // this assumes a specific working directory, change is you put files in
  // different places
  File userDir = new File(workingDir + "/webapps/MMAPI/deviceblog/users/" + username);
  
  if(!userDir.exists()) {
    
    out.println("Internal error: no user directory");
    return;
    
  }
  
  // set the title if it is missing
  if(title == null || title.equals("null") || title.length() == 0) {
    title = "Untitled " + (type == null ? "Text" : type) + " entry";
  }
  
  // try and figure out the date posted value
  long datePostedLong = 0L; 

  if(datePosted == null) datePostedLong = System.currentTimeMillis();
  else {
    try {
      datePostedLong = Long.parseLong(datePosted, 10);
    } catch (Exception e) {
      datePostedLong = System.currentTimeMillis();
    }
  }
  
  // now the file name for this entry will be based on the datePosted value
  // but first create, if it doesn't exist, the entries directory for this user
  // and also the media directories
  
  File entriesDir = new File(userDir, "entries");
  
  if(!entriesDir.exists()) {
    if(!entriesDir.mkdir()) {
      
      out.println("Internal error: could not create entries directory");
      return;
      
    } else { // entries directory created, now create the individual media directories
      
      new File(entriesDir, "audio").mkdir();
      new File(entriesDir, "video").mkdir();
      new File(entriesDir, "image").mkdir();
      
    }
  }
  
  // now see if this is a media entry, before saving the actual text entry
  if(media != null) {
    
    // this is a media entry
    
    InputStream in = null;
    FileOutputStream fos = null;
    
    try {
    
      // open up an inputstream for media data
      in = request.getInputStream();
      
      // create a byte array to recieve this data in
      byte[] rcdData = new byte[new Integer(mediaSize).intValue()];

      // read from the inputstream in this byte array    
      in.read(rcdData);
      
      fos = new FileOutputStream(new File(entriesDir, type + "/" + datePostedLong + "." + media));
      
      fos.write(rcdData, 0, rcdData.length);
      fos.flush();
      
    } catch(IOException ioex) {
      
      out.println("Internal error: " + ioex.getMessage());
      return;
    } finally {
      
      try {
        if(fos != null) fos.close();
        if(in != null)  in.close();
      } catch(IOException ix) { return; }
      
    } 
    
  }  
  
  // now save this entry
  File entryFile = new File(entriesDir, datePostedLong + ".txt");
  
  if(!entryFile.createNewFile()) {
    
    out.println("Internal error: could not create the entry file");
  }
  
  // now open this file, and write the title and message to it
  FileWriter fw = null;
  try {
    fw = new FileWriter(entryFile);
  
    // first the title
    fw.write(title, 0, title.length());
    fw.write("\r\n");
    
    // now the media file location, if any, if there is no media file,
    // still write an empty line
    if(media == null) fw.write("\r\n");
    else { 
      fw.write(type + "/" + datePostedLong + "." + media);
      fw.write("\r\n");
    }
    
    // see if there is a message, if yes, write it
    if(message != null && !message.equals("null"))
      fw.write(message, 0, message.length());
  
    // flush the buffer
    fw.flush();

  } catch(IOException ioex) {
    
    out.println("Internal error: " + ioex.getMessage());
    return;
    
  } finally {

    if(fw != null) {
      try {
        fw.close();
      } catch(IOException io) { return; }
    }
                
  }
  
  out.println("Posted");  
%>