package com.golfeven.firstGolf.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
/**
 * 
*Title: project_name  
*Description: XXXX   
*Copyright: Copyright (c) 2011 
*Company:www.xxx.com  
*Makedate:2013-8-19 下午1:43:07 
* @author ISuper   
* @version %I%, %G%  
* @since 1.0   
*
 */
public class XmppUtil {
	//用来保存有新消息的好友
	private  static ArrayList<String> haveMsg = new ArrayList<String>();
	
	
	public  static String id = null;//用来保存当前聊天页面的id  如果不是聊天页面 id= null;
	
	/**
	 * 
	* 描述 : <描述函数实现的功能>. <br>
	* 添加有新消息的用户 
	*<p>
	 */
	public static void addNewFriend(String mid){
		haveMsg.add(mid);
		//haveMsg.
	}
	/**
	 * 移除打开消息的用户
	* 描述 : <描述函数实现的功能>. <br> 
	*<p>
	 */
	public static void removeOldFriend(String mid){
		while(haveMsg.contains(mid)){
			
			haveMsg.remove(mid);
		}
	}
	/**
	 * 得到消息个数
	* 描述 : <描述函数实现的功能>. <br> 
	*<p>                                                                                                                                                                                                                                                      
	* @param mid
	* @return
	 */
	public static int getCount(String mid){
		if(!haveMsg.contains(mid)){
			return 0;
		}
		int i = 0;
		for (String id : haveMsg) {
			if(mid.equals(id)){
				i++;
			}
		}
		return i;
	}

}
