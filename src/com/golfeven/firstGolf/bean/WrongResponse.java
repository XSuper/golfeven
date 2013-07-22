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
	public String data;
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
