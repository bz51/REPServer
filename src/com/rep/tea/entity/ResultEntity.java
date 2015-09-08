package com.rep.tea.entity;

/**
 *  服务器返回结果的实体
 * @author chaibozhou
 *
 */
public class ResultEntity {
	//返回的结果yes/no
	private String result;
	//若发生错误这个字段才有值，表示发生错误的原因；若未发生错误，则这个字段为空
	private String reason;
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
}
