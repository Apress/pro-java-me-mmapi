/*
 * CreateJTSFileFromHexString reads a file containing MMAPI Tone data in
 * Hex format and converts it into JTS binary file, ready for distribution and
 * playing
 */

package com.apress.chapter6;

import java.io.*;

public class CreateJTSFileFromHexString {
  
  public static void main(String[] args) {
    if(args[0] == null) System.err.println("Usage: java <filename>");
    
    // initialize streams that we will need
    FileReader fr = null;
    BufferedReader br = null;
    ByteArrayOutputStream bos = null;
    FileOutputStream fos = null;
    
    try {
      // the argument must be the file containing tone format in hex string
      File hexStringFile = new File(args[0]);
      
      // create text based readers on it
      fr = new FileReader(hexStringFile);
      br = new BufferedReader(fr);
      
      String lineRead;
      String hexString = "";
      
			// read the file, a line at a time into the hexString variable
      while((lineRead = br.readLine()) != null) {
				hexString += lineRead;
			}

			// remove the spaces within this hexString
      StringBuffer buf = new StringBuffer();
			for(int i = 0; i < hexString.length(); i++) {
				if(!Character.isWhitespace(hexString.charAt(i))) 
          buf.append(hexString.charAt(i));
			}

			// the final hexString with spaces removed
      hexString = buf.toString(); 
      
			// now write the hexString out to a file as a byte array making it a 
      // binary file
      byte[] txtInByte = new byte[hexString.length()/2];
			int j = 0;
			for (int i = 0; i < hexString.length(); i += 2)
			{
				txtInByte[j++] = 
          (byte)Integer.parseInt(hexString.substring(i, i + 2), 16);
			}

			// the byte array created, write it to a ByteArrayOutputStream
      bos = new ByteArrayOutputStream();
			bos.write(txtInByte, 0, txtInByte.length);

			// and create a physical file with the extension jts containing this data
      fos = new FileOutputStream(
        new File(args[0].substring(0, args[0].indexOf(".")) + ".jts"));
			bos.writeTo(fos);      
      
    } catch (Exception e) {
      System.err.println(e);
    } finally {
      try {
        if(fos != null) fos.close();
        if(bos != null) bos.close();
        if(br != null) br.close();
        if(fr != null) fr.close();
      } catch(Exception e) {
        System.err.println(e);
      }
    }
  }
  
}
