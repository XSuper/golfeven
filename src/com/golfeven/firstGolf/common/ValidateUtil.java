package com.golfeven.firstGolf.common;

import com.golfeven.firstGolf.bean.WrongResponse;

public class ValidateUtil {
	public static final int UNAMEISEMPTY=-1;
	public static final int UNAMEISWRONGFUL=-2;
	public static final int UNAMEISOK=0;
	
	public static final int UPASSISEMPTY=-3;
	public static final int UPASSISWRONGFUL=-4;
	public static final int UPASSISOK=0;
	
	public static final int NICKISEMPTY=-5;
	public static final int NICKISOK=0;
	
	public static final int PASSWORDSDIFFER=-6;//两次密码不一致
	public static final int NOTAGREE=-7;//注册时没有同意协议
	/**
	 * 用户名验证
	 * @param uname
	 * @return
	 */
	public static int unameValidate(String uname){
		if(StringUtils.isEmpty(uname)){
			return UNAMEISEMPTY;
		}else if(!StringUtils.isPhone(uname)&&!StringUtils.isEmail(uname)){
			return UNAMEISWRONGFUL;
		}else{
			return UNAMEISOK;
		}
	}
	
	/**
	 * 密码验证
	 * @param upass
	 * @return
	 */
	public static int upassValidate(String upass){
		if(StringUtils.isEmpty(upass)){
			return UPASSISEMPTY;
		}else if(!StringUtils.isPassWord(upass)){
			return UPASSISWRONGFUL;
		}else{
			return UPASSISOK;
		}
	}
	/**
	 * 注册前的验证
	 * @param uname
	 * @param nick
	 * @param upass
	 * @return
	 */
	public static int registerValidate(String uname,String nick,String upass,String repeat){
		if(unameValidate(uname)<0){
			return unameValidate(uname);
		}else if(StringUtils.isEmpty(nick)){
			return NICKISEMPTY;
		}else if(upassValidate(upass)<0){
			return upassValidate(upass);
		}else {
			return upass.equals(repeat)?0:PASSWORDSDIFFER;
		}
		
	}
	
	/**
	 * 登陆前验证
	 * @param uname
	 * @param upass
	 * @return
	 */
	public static int loginValidate(String uname,String upass){
		if(unameValidate(uname)<0){
			return unameValidate(uname);
		}else{
			return upassValidate(upass);
		}
	}
	
	public static WrongResponse wrongResponse(String t){
		WrongResponse wrongResponse =new WrongResponse();
		if(t.startsWith("99")){
			wrongResponse.code = 99;
			wrongResponse.show = false;
			wrongResponse.msg = t.replaceFirst("99","").trim();
		}
		if(t.startsWith("100")){
			wrongResponse.code = 100;
			wrongResponse.show = true;
			wrongResponse.msg = "用户账号不能为空";
		}
		if(t.startsWith("101")){
			wrongResponse.code =101;
			wrongResponse.show = true;
			wrongResponse.msg = "用户已经存在";
		}
		if(t.startsWith("102")){
			wrongResponse.code =102;
			wrongResponse.show = true;
			wrongResponse.msg = "用户不存在";
		}
		if(t.startsWith("103")){
			wrongResponse.code =103;
			wrongResponse.show = true;
			wrongResponse.msg = "昵称已经存在";
		}
		if(t.startsWith("104")){
			wrongResponse.code =104;
			wrongResponse.show = true;
			wrongResponse.msg = "邮箱已经存在";
		}
		if(t.startsWith("105")){
			wrongResponse.code =105;
			wrongResponse.show = true;
			wrongResponse.msg = "密码错误";
		}
		if(t.startsWith("106")){
			wrongResponse.code =106;
			wrongResponse.show = true;
			wrongResponse.msg = "无效的用户ID";
			
		}
		if(t.startsWith("107")){
			wrongResponse.code =107;
			wrongResponse.show = false;
			wrongResponse.msg = "无效的token";
		}
		if(t.startsWith("301")){
			wrongResponse.code =301;
			wrongResponse.show = true;
			wrongResponse.msg = "一天内不能领取多次积分";
		}
		if(t.startsWith("302")){
			wrongResponse.code =302;
			wrongResponse.show = false;
			wrongResponse.msg = "无效的积分类型";
		}
		if(t.startsWith("303")){
			wrongResponse.code =303;
			wrongResponse.show = false;
			wrongResponse.msg = "无效的积分";
		}
		if(t.startsWith("401")){
			wrongResponse.code =401;
			wrongResponse.show = true;
			wrongResponse.msg = "无效的活动";
			
		}
		if(t.startsWith("402")){
			wrongResponse.code =402;
			wrongResponse.show = true;
			wrongResponse.msg = "已经报过名";
		}
		if(t.startsWith("403")){
			wrongResponse.code =403;
			wrongResponse.show = true;
			wrongResponse.msg = "已经过了报名时间";
		}
		if(t.startsWith("404")){
			wrongResponse.code =404;
			wrongResponse.show = true;
			wrongResponse.msg = "已经超过报名人数";
		}
		if(t.startsWith("501")){
			wrongResponse.code =501;
			wrongResponse.show = false;
			wrongResponse.msg = "无效的好友id";
		}
		
		return wrongResponse;
		
	}
	

}
