<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.io.*, java.util.*" %>

<HTML>
<HEAD>
<TITLE>Pro MMAPI Book</TITLE>
<META content="MSHTML 5.50.4616.200" name=GENERATOR>
<link rel="stylesheet" href="/css/styles.css" type="text/css">
</HEAD>
<BODY text=black bgColor=#9999FF marginheight="0" 
marginwidth="0" topmargin="0" leftmargin="20" link="#666666">
<table width="690" border="0" cellspacing="0" cellpadding="0" vspace="0" align="center" height="60">
  <tr> 
    <td width="596" valign="bottom"> 
      <div align="left"><font face="Georgia, Times New Roman, Times, serif" size="6" color="#FFFFFF"><b>&nbsp;&nbsp;&nbsp;&nbsp;Pro 
        Mobile Media API</b></font></div>
    </td>
  </tr>
</table>
<TABLE cellSpacing=0 cellPadding=0 width=690 bgColor=#FFFFFF border=0 vspace="0" align="center">
  <TR> 
    <TD vAlign=top align=left width=30 bgColor=#9999FF>&nbsp;</TD>
    <TD vAlign=top colSpan=3 bgcolor="#9999FF"> 
      <table border="0" cellspacing="0" cellpadding="0" align="right" bgcolor="#9999FF" height="20">
        <tr bgcolor="#FFFFFF"> 
          <td width="20" height="19"><img src="/images/left1.gif" width="20" height="20"></td>
          <td height="19" width="45"> 
            <div align="center"><font color="#666666" face="Arial, Helvetica, sans-serif" size="2"><b><a href="http://www.mmapibook.com">Home</a></b></font></div>
          </td>
          <td width="41" height="19" bgcolor="#FFFFFF"><img src="/images/right1.gif" width="20" height="20"><img src="/images/left1.gif" width="20" height="20"></td>		
          <td height="19" width="45"> 
            <div align="center"><font color="#666666" face="Arial, Helvetica, sans-serif" size="2"><b><a href="/deviceblog/index.jsp">DeviceBlog</a></b></font></div>
          </td>
          <td width="41" height="19" bgcolor="#FFFFFF"><img src="/images/right1.gif" width="20" height="20"><img src="/images/left1.gif" width="20" height="20"></td>
          <td height="19" width="74"> 
            <div align="center"><font color="#666666" face="Arial, Helvetica, sans-serif" size="2"><b><a href="#">Reviews</a></b></font></div>
          </td>
          <td width="40" height="19" bgcolor="#66CC00"><img src="/images/right1.gif" width="20" height="20"><img src="/images/left1.gif" width="20" height="20"></td>
          <td height="19" width="67"> 
            <div align="center"><font color="#666666" face="Arial, Helvetica, sans-serif" size="2"><b><a href="/about.htm">About</a></b></font></div>
          </td>
          <td width="20" height="19" bgcolor="#9999FF"><img src="/images/right1.gif" width="20" height="20"></td>
          <td width="20" height="19" bgcolor="#9999FF"><img src="/images/left1.gif" width="20" height="20"></td>
          <td height="19" width="88"> 
            <div align="center"><font color="#666666" face="Arial, Helvetica, sans-serif" size="2"><b><a href="#">Errata</a></b></font></div>
          </td>
          <td width="20" height="19" bgcolor="#9999FF"><img src="/images/right1.gif" width="20" height="20"></td>
        </tr>
        <tr bgcolor="#9999FF"> 
          <td colspan="10" height="1".><img src="/images/line1.gif" width="1" height="1"></td>
        </tr>
      </table>
    </TD>
    <TD vAlign=top align=right bgColor=#9999FF width="30">&nbsp;</TD>
  </TR>
  <TBODY> 
  <TR> 
    <TD vAlign=top align=left width=30 bgColor=#FFFFFF height=439><img src="/images/top_left.gif" width="30" height="30"><BR>
    </TD>
    <TD vAlign=top colSpan=3 bgcolor="#FFFFFF"> <br>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td height="18" width="9"><img src="/images/gph2.gif" width="9" height="18"></td>
          <td height="18" bgcolor="#9999FF"><font size="2" face="Verdana, Arial, Helvetica, sans-serif" color="#FFFFFF"><b>Latest 
            Entries </b></font></td>
          <td height="18" width="9"><img src="/images/gph1.gif" width="9" height="18"></td>
        </tr>
      </table>
      <br>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">      

      
      
      <%
      
        // first get the working dir
        String workingDir = System.getProperty("user.dir");

        // this is the users directory
        File userDir = new File(workingDir + "/webapps/MMAPI/deviceblog/users");
        
        // a list of users
        File users[] = userDir.listFiles();
        
        if(!userDir.exists() || users.length == 0) {
          
          out.println("No entries are present at this point");
          return;
          
        } else {
          
          // and a sorted map of entries
          TreeMap entryList = new TreeMap();
        
          // traverse the users directories and look for the entries
          // folder in each, which will contain a list of entries          
          for(int i = 0; i < users.length; i++) {
            
            File entriesFolder = new File(users[i], "entries");
            
            if(!entriesFolder.exists()) continue;
            
            File[] entries = entriesFolder.listFiles();
            
            for(int j = 0; j < entries.length; j++) {      
              entryList.put(entries[j].getName(), entries[j].getPath());              
            }
          }
          
          Collection entryFiles = entryList.values();
          Iterator itr = entryFiles.iterator();
          
          String title = "";
          String str = "";
          String user = "";
          String postedOn = "";
          String fileName = "";
          String mediaFileName = "";
          String mediaFilePath = "";
          
          boolean image = false;
          boolean audio = false;
          boolean video = false;
       
          while(itr.hasNext()) {
            
            BufferedReader in = null;
            String message = "";
            
            try {
              
              String path = (String)itr.next();
              
              File file = new File(path);
              
              if(file.isDirectory()) continue;
              
              user = path.substring(
                path.indexOf("users/") + 6, 
                path.indexOf("/entries")); 
              
              fileName = file.getName();
              
              postedOn = fileName.substring(0, fileName.indexOf("."));
         
              // read each entry file
              in = new BufferedReader(new FileReader(file));
              
              // the first line is the title
              title = in.readLine();
              
              // the second line is the media file, blank if no media file is there
              mediaFileName = in.readLine();
              
              // the next lines are the message
              while ((str = in.readLine()) != null) {
                  message += str;
              }
              
              if(mediaFileName.length() != 0)  { // means that this entry has media data
                
                if(mediaFileName.startsWith("image")) image = true;
                else if(mediaFileName.startsWith("audio")) audio = true;
                else video = true;
                
                mediaFilePath = "users/" + user + "/entries/" + mediaFileName;
                        
              }
      
      %>
      
        <tr> 
          <td valign="top" width="70%"> 
            <p><b><%= title %></b></p>
            
            <p><% if(image) { %>
            <img src="<%= mediaFilePath %>" />
            <% image = false; } %></p>
            
            <p><% if(audio) { %>
            <a href="<%= mediaFilePath %>" > <img src="/images/audio.gif" border="0"/> Click here to hear audio!! </a>
            <% audio = false; } %></p>        
    
            <p><% if(video) { %>
            <a href="<%= mediaFilePath %>" > <img src="/images/qcam.gif" border="0"/> Click here to see video!!! </a>
            <% video = false; } %></p>               

            <p><%= message %></p>
            <p><i><font size="2">Posted by: <%= user %> on <%= new Date((new Long(postedOn)).longValue()).toString() %></font></i></p>
            <hr>
          </td>
        </tr>
       
        <%
              } catch(IOException io) {
                out.println(io.getMessage());
                continue;
              } finally {
                if(in != null) try { in.close(); } catch(IOException ix) {}
              }
            }
          }
        %>
        
      </table>
      
    </TD>
    <TD vAlign=top align=right bgColor=#FFFFFF width="30" height="439"><img src="/images/top_right.gif" width="30" height="30"> 
      <BR>
      <BR>
    </TD>
  </TR>
  <TR> 
    <TD vAlign=top align=left width=30 bgColor=#ffffff><img src="/images/bottom_left.gif" width="30" height="30"></TD>
    <TD colSpan=3>&nbsp;</TD>
    <TD vAlign=top align=right bgColor=#ffffff width="30"><img src="/images/bottom_right.gif" width="30" height="30"></TD>
  </TR>
  </TBODY> 
</TABLE>
<table width="690" border="0" cellspacing="0" cellpadding="0" vspace="0" align="center">
  <tr> 
    <td height="30"> 
      <div align="center"><font face="Arial, Helvetica, sans-serif" size="2" color="#FFFFFF">Copyright 
        2006 Vikram Goyal. All Rights Reserved</font></div>
    </td>
  </tr>
</table>
</BODY></HTML>
