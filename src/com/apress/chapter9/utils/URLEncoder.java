package com.apress.chapter9.utils;

public class URLEncoder {
  
  private static char getHex(int val) {    
    if (val < 10) return (char)('0' + val);
    else return (char)('A' + (val - 10));
}

  public static String encodeURL(String url) {
    
    // the return encoded URL
    StringBuffer returnURL = new StringBuffer();
    
    // the length
    int size = url.length();
    
    for(int i = 0; i < size; i++) {
      
      // iterate over each character
      char ch = url.charAt(i);
      
      // if alphnumeric, it remains as such
      if ((ch >= '0' && ch <= '9') ||
          (ch >= 'a' && ch <= 'z') ||
          (ch >= 'A' && ch <= 'Z'))
            returnURL.append(ch);
      else { 
        
        // non alphanumeric
        
        // see first if this is one of the special characters.
        int spec = special.indexOf(ch);
        
        if (spec >= 0) {
          
          // this character is not in the special chars
          // String defined later
          returnURL.append(ch);
          
        } else {
          
          // use the hex converter for the rest of the characters
          
          // first add the % character
          returnURL.append('%');
          
          // next convert the high bits
          returnURL.append(getHex((ch & 0xF0) >> 4));
          
          // and finally the low bits
          returnURL.append(getHex(ch & 0x0F));
        }
      }
    }
    
    // the final encoded url
    return returnURL.toString(); 
  }
  
  private static String special = "=&:/?\".-!~*_'()";  
}
