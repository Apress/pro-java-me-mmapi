<%@ page contentType="text/plain;charset=UTF-8" language="java" import="java.io.File" %>

<%--
  Simple login.jsp tries to log the user, after accepting username and password
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

  // if this directory does not exist, then the user was not found
  if(!userDir.exists()) { 
    
    out.println("Username not found. Please register");
    return;

  } else {

    // user present, has he given the right password?
    
    // check by looking for a password file for this user in his directory
    File pWordFile = new File(userDir, password + ".password");

    if(pWordFile.exists()) {

      // send message back
      out.println("Logged In!");
      return;

    } else {
      
      // if password file not found, return erro
      out.println("Invalid password!");
      return;

    }
    
  }

%>