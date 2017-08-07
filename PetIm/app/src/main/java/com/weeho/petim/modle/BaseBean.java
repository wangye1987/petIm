package com.weeho.petim.modle;

import java.io.Serializable;

/** 
 * @author 作者 E-mail: wk
 * @version 创建时间：2015-9-29 上午10:13:52 
 * 类说明 
 * @param
 */
public class BaseBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String status;//状态码
	private ResultBean result;//返回结果
	private ErrorBean error;//返回失败结果
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ErrorBean getError() {
		return error;
	}
	public void setError(ErrorBean error) {
		this.error = error;
	}
	public ResultBean getResult() {
		return result;
	}
	public void setResult(ResultBean result) {
		this.result = result;
	}
	public class ResultBean implements Serializable{
	private String userId;
	private String hxusername;
	private String hxpassword;
	private String headSculpturePath;

		public String getHeadSculpturePath() {
			return headSculpturePath;
		}

		public void setHeadSculpturePath(String headSculpturePath) {
			this.headSculpturePath = headSculpturePath;
		}

		public String getHxusername() {
			return hxusername;
		}

		public void setHxusername(String hxusername) {
			this.hxusername = hxusername;
		}

		public String getHxpassword() {
			return hxpassword;
		}

		public void setHxpassword(String hxpassword) {
			this.hxpassword = hxpassword;
		}

		public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}

}
 