package com.trace.common.util;
public abstract class ByteUtil {
    /**
     * @title  将int转为低字节在前，高字节在后的byte数组
     * @param  int
     * @return byte[]
     */
   public static byte[] toLH(int n) {
         byte[] b = new byte[4];
         b[0] = (byte) (n & 0xff);
         b[1] = (byte) (n >> 8 & 0xff);
         b[2] = (byte) (n >> 16 & 0xff);
         b[3] = (byte) (n >> 24 & 0xff);
         return b;
   }
   
   /**
    * @title  将long转为低字节在前，高字节在后的byte数组
    * @param  long
    * @return byte[]
    */
   public static byte[] toLH(long n) {
       byte[] b = new byte[4];
       b[0] = (byte) (n & 0xff);
       b[1] = (byte) (n >> 8 & 0xff);
       b[2] = (byte) (n >> 16 & 0xff);
       b[3] = (byte) (n >> 24 & 0xff);
       return b;
   } 
   
	/**
	 * @title 将int类型转化为低字节在前，高字节在后，大小为四个字节的byte数组
	 * @param number
	 * @return
	 */
	public byte[] intToByte(int number) {
		long temp = number;
		byte[] b = new byte[4];
		for (int i = 0; i < b.length; i++) {
			// 将最高位保存在最低位
			b[i] = new Long(temp & 0xff).byteValue();
			// 向右移8位
			temp = temp >> 8;
		}
		return b;
	}
	/**
	 * 将float类型转化为低字节在前，高字节在后，大小为四个字节的byte数组
	 * @param f
	 * @return
	 */
	public static byte[] floatToByte(float f) {
	    return toLH(Float.floatToRawIntBits(f));
	} 
	
	/**
	 * 将double类型转化为低字节在前，高字节在后，大小为四个字节的byte数组
	 * @param d
	 * @return
	 */
	public static byte[] doubleToByte(double d) {
	    return toLH(Double.doubleToRawLongBits(d));
	} 
	
	/**
	  * 低字节数组转换为float
	  * @param b byte[]
	  * @return float
	  */
	@SuppressWarnings("static-access")
	public static float byteToFloat(byte[] b) {
    	  	int i = 0;
    	  	Float F = new Float(0.0);
    	  	i = ((((b[3]&0xff)<<8 | (b[2]&0xff))<<8) | (b[1]&0xff))<<8 | (b[0]&0xff);
    	  	return F.intBitsToFloat(i);
	} 
	/**
	 * 字符转化为字节数组
	 * @param c
	 * @return
	 */
	public byte[] charToByte(char c){
		byte[] b = new byte[1];
		b[0] = (byte) ((byte) c & 0xff);
		return b;
	}
	
	/**
	 * 将int类型转化为低字节在前，高字节在后，大小为一个字节的byte数组
	 * @param number
	 * @return
	 */
	public byte[] intToOneByte(int number){
		int temp = number;
		byte[] b = new byte[1];
		// 将最高位保存在最低位
		b[0] = new Integer(temp & 0xff).byteValue();
		return b;
	}
	
	/**
	 * 将short类型转化为低字节在前，高字节在后，大小为两个个字节的byte数组
	 * @param number
	 * @return
	 */
	public byte[] shortToByte(int number) {
		int temp = number;
		byte[] b = new byte[2];
		for (int i = 0; i < b.length; i++) {
			// 将最高位保存在最低位
			b[i] = new Integer(temp & 0xff).byteValue();
			// 向右移8位
			temp = temp >> 8;
		}
		return b;
	}
	
	/**
	 * 先进行高低位转换,再将四个字节数组，转化为int类型
	 * @param a
	 * @return
	 */
	public int byteToInt(byte[] a) {
		byte[] b = new byte[4];
		for(int j=0,k=3;j < 4&&k >-1;j ++,k--){
			b[j]=a[k];
		}
		int s = 0;
		for (int i = 0; i < 3; i++) {
			if (b[i] >= 0)
				s = s + b[i];
			else
				s = s + 256 + b[i];
			s = s * 256;
		}
		if (b[3] >= 0) // 最后一个之所以不乘，是因为可能会溢出
			s = s + b[3];
		else
			s = s + 256 + b[3];
		return s;
	}
	
	/**
	 * 先进行高低位转换,再将两个字节数组，转化为short类型
	 * @param a
	 * @return
	 */
	public short byteToShort(byte[] a) {
		byte[] b = new byte[2];
		for(int j=0,k=1;j < 2&&k >-1;j ++,k--){
			b[j]=a[k];
		}
		int s = 0;
		if (b[0] >= 0)
			s = s + b[0];
		else
			s = s + 256 + b[0];
		s = s * 256;
		if (b[1] >= 0) // 最后一个之所以不乘，是因为可能会溢出
			s = s + b[1];
		else
			s = s + 256 + b[1];
		return (short)s;
	}
	
	/**
	 * 将两个字节数组，转化为int类型
	 * @param b
	 * @return
	 */
	public int byte2Int(byte[] b) {
		int s = 0;
		for (int i = 0; i < 3; i++) {
			if (b[i] >= 0)
				s = s + b[i];
			else
				s = s + 256 + b[i];
			s = s * 256;
		}
		if (b[3] >= 0) // 最后一个之所以不乘，是因为可能会溢出
			s = s + b[3];
		else
			s = s + 256 + b[3];
		return s;
	}
	
	/**
	 * 一个字节数组，转化为int类型
	 * @param b
	 * @return
	 */
	public int byteToOneInt(byte[] b){
		int s = 0;
		if (b[0] >= 0)
			s = b[0];
		else
			s = 256 + b[0];
		return s;
	}
	
	/**
	 * 将字节数组，转化为char类型
	 * @param b
	 * @return
	 */
	public char byteToChar(byte[] b){
		String s = new String(b);
		return s.charAt(0);
	}
	
	/**
	 * 将long类型转化为低字节在前，高字节在后，大小为8个字节的byte数组
	 * @param iSource
	 * @return
	 */
	public byte[] longToByte(long iSource){
		byte[] bLocalArr = new byte[8]; 
        for ( int i = 0;i < 8; i++) { 
            bLocalArr[i] = (byte)( iSource>>8*i & 0xFF ); 
        } 
        return bLocalArr; 
	}
	
	/**
	 * 将八个字节数组，转化为long类型
	 * @param bRefArr
	 * @return
	 */
	public long byteToLong(byte[] bRefArr) { 
        long iOutcome = 0; 
        byte bLoop; 
        for ( int i =0; i<8 ; i++) { 
            bLoop = bRefArr[i]; 
            iOutcome+= (bLoop & 0xFF) << (8 * i); 
        }   
        return iOutcome; 
	} 
	
	public String blockIdToStr(long blockId){
		StringBuffer sb = new StringBuffer();
		byte[] bIdByte = longToByte(blockId);
		for(int i=0;i<8;i++){
			sb.append(bIdByte[i]);
		}
		return new Long(blockId).toString();
	}
	
	public long blockIdToLong(String blockIdStr){
		return new Long(blockIdStr);
	}
	
	/**
	 * 将UINT64变为十六进制字符串
	 * @param a
	 * @return
	 */
	public String byteToString(byte[] a){
		byte[] b = new byte[8];
		for(int j=0,k=7;j < 8&&k >-1;j++,k--){
			b[j]=a[k];
		}
		String hs="";   
	    String stmp="";   
	    for (int n=0;n<b.length;n++){   
	    	stmp=(java.lang.Integer.toHexString(b[n] & 0XFF));   
	    	if (stmp.length()==1)   
	    		hs=hs+"0"+stmp;   
	        else 
	        	hs=hs+stmp;   
	      }   
	    return hs.toUpperCase(); 
	}
	
	/**
	 * @param h
	 * @return
	 */
	public byte[] StringToByte(String h){
		byte[] ret = new byte[h.length()/2];   
	    for(int i=0; i<ret.length; i++){   
	        ret[i] = Integer.decode("#"+h.substring(2*i, 2*i+2)).byteValue();   
	    }   
	    byte[] b = new byte[8];
		for(int j=0,k=7;j < 8&&k >-1;j++,k--){
			b[j]=ret[k];
		}
	    return b;
	}
}
