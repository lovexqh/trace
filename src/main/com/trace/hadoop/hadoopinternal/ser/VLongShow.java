package com.trace.hadoop.hadoopinternal.ser;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.hadoop.io.WritableUtils;

public class VLongShow {
	static void showVLong(long value) {
	    try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
		    DataOutputStream dout=new DataOutputStream(out);
			WritableUtils.writeVLong(dout, value);
			dout.close();
			
			System.out.print("VLong ser result of "+value+": length "+out.size()+", ");
			
			for(int ii=0; ii<out.size(); ii++) {
				byte abyte=out.toByteArray()[ii];
				
				if(ii==0 && out.size()>1 ) {
                    if ( abyte>=-120 && abyte<=-113  ) {
                    	System.out.print(", positive, number of bytes "+ (-(abyte+112)) +", ");
                    } else if( abyte>=-128 && abyte<=-121 ) {
                    	System.out.print(", negative, number of bytes "+ (-(abyte+120)) +", ");
                    } else {
                    	System.out.print(" ERROR!!");
                    }
                    
					System.out.print(" output: ");
				}
				
				for(int jj=0x80; jj>0; jj>>=1) {
					if ( (abyte&jj) > 0 ) {
						System.out.print( "1" );
					} else {
						System.out.print( "0" );
					} 
				}
				
				System.out.print(" ");
			}
			
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		VLongShow.showVLong(127);
		VLongShow.showVLong(-112);
		VLongShow.showVLong(128);
		VLongShow.showVLong(-114);
		VLongShow.showVLong(128);
		VLongShow.showVLong(-113);
		VLongShow.showVLong(Long.MAX_VALUE);
		VLongShow.showVLong(Long.MIN_VALUE);
	}

}
