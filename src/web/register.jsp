<%@ page contentType="text/plain;charset=UTF-8" language="java" import="java.io.File" %>

<%--
  Simple registration JSP file that allows users to register given username and password
--%>
<%
  
  // get the username and password from request
  String username = (String)request.getParameter("uName");
  String password = (String)request.getParameter("pWord");

  // if any of them is not present, raise error
  if(username == null || username.length() == 0 ||
     password == null || password.length() == 0) {
    
    out.println("Required parameter missing");
    return;
  }
  
  // now find out the working dir
  String workingDir = System.getProperty("user.dir");

  // try to create a user directory with the given username
  File userDir = new File(workingDir + "/webapps/MMAPI/deviceblog/users/" + username);

  // if this directory exists, then the user is present
  if(userDir.exists()) { 
    
    out.println("Username exists.");
    return;

  } else {

    // otherwise, first try and create a user directory
    if(userDir.mkdir()) {
      
      // and then a password file
      File pWordFile = new File(userDir, password + ".password");
      
      if(pWordFile.createNewFile()) {
        
        // if directory and file are created, return simple message
        out.println("Registered");
        return;

      }
    }

    // in all other cases, return error
    out.println("User account could not be created");
    return;
    
  }


%>