package com.trace.hadoop.hadoopinternal.ser;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Calendar;

import lava.clib.Ctype;

public class SerializationExample {
  public static void print16(byte[] buffer, int length) {
	int iPos = 0;
	while (iPos < length) {
	  int iLinePos = 0;
	  String output = "";
	  String charOutput = "";

	  while (iPos < length && iLinePos < 16) {
		String hexString = Integer.toHexString(0x0FF & buffer[iPos]);
		output += ((hexString.length() < 2 ? "0" : "") + hexString + " ");
		charOutput += (Ctype.isprint(buffer[iPos]) ? Character
				.valueOf((char) buffer[iPos]) : ".");
		
		if (iLinePos == 7) {
		  output += " ";
		  charOutput += " ";
		}
		
		iLinePos++;
		iPos++;
	  }

	  while (iLinePos < 16) {
		output += "   ";
		iLinePos++;
	  }

	  System.out.println(output.toUpperCase() + "   " + charOutput);
    }
  }

  public static void main(String[] args) {
    try {
      Block block1=new Block(7806259420524417791L, 39447755L, 56736651L);
      Block block2=new Block(5547099594945187683L, 67108864L, 56736828L);
 
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      ObjectOutputStream objOut = new ObjectOutputStream(out);
      objOut.writeObject(block1);
      objOut.close();

      System.out.println("writeObject (block1) with " + out.size() + " bytes:");
      SerializationExample.print16(out.toByteArray(), out.size());

      BlockMetaDataInfo blockMetaData = new BlockMetaDataInfo(block1, Calendar.getInstance().getTimeInMillis());

      out = new ByteArrayOutputStream();
      objOut = new ObjectOutputStream(out);
      objOut.writeObject(block1);
      objOut.writeObject(block2);
      objOut.close();

      System.out.println();
      System.out.println("writeObject (block1 & block2) with " + out.size() + " bytes:");
      SerializationExample.print16(out.toByteArray(), out.size());
      
      out = new ByteArrayOutputStream();
      objOut = new ObjectOutputStream(out);
      objOut.writeObject(blockMetaData);
      objOut.close();

      System.out.println();
      System.out.println("BlockMetaDataInfo writeObject with "
    	+ out.size() + " bytes:");
      SerializationExample.print16(out.toByteArray(), out.size());

      System.out.println();
      System.out.println("=======================================================");
      
      ByteArrayOutputStream bout = new ByteArrayOutputStream();
      DataOutputStream dout=new DataOutputStream(bout);
      block1.write(dout);
      dout.close();
      System.out.println("writeObject (block1) with " + bout.size() + " bytes:");
      SerializationExample.print16(out.toByteArray(), bout.size());  
      
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
