package com.hylanda.util;

public class SystemUtil {

	/**
	 * 获取单钱手机系统版本号
	 * @return
	 */
	public static String getSystemVersion()
	{
		return android.os.Build.VERSION.RELEASE;
	}
	/**
	 * int 转ip
	 * @param i
	 * @return
	 */
	public static String intToIp(int i) {       
        
        return (i & 0xFF ) + "." +       
      ((i >> 8 ) & 0xFF) + "." +       
      ((i >> 16 ) & 0xFF) + "." +       
      ( i >> 24 & 0xFF) ;  
   }   
}
