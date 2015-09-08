package com.rep.core;

import java.util.Random;

public class Tools {
	
	/**
	 * 获取指定长度的随机数
	 * BUG：当随机数长度超过7位时，这个方法每次产生随机数都一样
	 * @param strLength
	 * @return
	 */
	public static String getFixLenthString(int strLength) {  
	      
	    Random rm = new Random();  
	      
	    // 获得随机数  
	    int pross = (int)((1 + rm.nextDouble()) * Math.pow(11, strLength));
	  
	    // 将获得的获得随机数转化为字符串  
	    String fixLenthString = String.valueOf(pross);  
	    // 返回固定的长度的随机数  
	    return fixLenthString.substring(0, strLength);
	} 
	
	public static String getTenLengthRandom(){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<10;i++){
			sb.append((int)(10*(Math.random())));
		}
		return sb.toString();
	}
}
