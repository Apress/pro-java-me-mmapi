package com.apress.chapter6;

public class NoteCalculator {
  
  public static final double SEMITONE_CONST = 17.31234049066755;
  public static final double ARBIT_CONST = 8.176;
  
  public static int getNoteAsInt(double freq) {
    return (int)Math.round((Math.log(freq/ARBIT_CONST) * SEMITONE_CONST));
  }
  
  public static double getNoteFreq(int note) {
    return (ARBIT_CONST * Math.exp(note/SEMITONE_CONST));
  }
  
  public static void printAllNoteFreq() {
    for(int i = 0; i < 128; i++) {
      System.err.println(i + " : " + getNoteFreq(i));
    }
  }
  
  public static void main(String[] args) {
    
    try {
      if(args.length == 2) {
        if(args[0].equals("1")) {
          double freq = new Double(args[1]).doubleValue();
          System.err.println(args[1] + " Hz : " + getNoteAsInt(freq));
        } else {
          int note = new Integer(args[1]).intValue();
          System.err.println(args[1] + ": " + getNoteFreq(note));
        }
      } else {
        System.err.println("Usage: NoteCalculator convType convVal \r\n" +
          " where convType: 1 for freq in Hz and 2 for note value in int \r\n" +
          " and convVal is the actual value");
      }    
    } catch(Exception e) {
      System.err.println(e);
    }
    
    // print all notes from 0 to 127
    printAllNoteFreq();
  }
}
