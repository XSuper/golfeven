package com.golfeven.firstGolf.bean;
/**
 * 保存错误信息,show 代表是否错误错误信息提示用户
 * @author ISuper
 *
 */
public  class WrongResponse{
	public WrongResponse(){
		
	}
	
	public WrongResponse(boolean show, String msg) {
		super();
		
		this.show = show;
		this.msg = msg;
	}

	public int code;
	public boolean show;
	public String msg;
}
