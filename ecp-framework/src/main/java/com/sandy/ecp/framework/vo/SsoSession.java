package com.sandy.ecp.framework.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class SsoSession extends AbstractVO<Long> {
	
	private static final long serialVersionUID = 8995377405385560163L;

	@JsonSerialize(using=JsonSerializer.None.class, as= Void.class)
	private String account;
	
	private String mobile;
	
	private String nickName;
	
	@JsonIgnore
	private String password;
	
	private Boolean isLock;
	
	private String sotoken;
	
	private String t;
	
	public String getIdstr() {
		return super.getId() + "";
	}
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Boolean getIsLock() {
		return isLock;
	}
	public void setIsLock(Boolean isLock) {
		this.isLock = isLock;
	}
	public String getSotoken() {
		return sotoken;
	}
	public void setSotoken(String sotoken) {
		this.sotoken = sotoken;
	}
	public String getT() {
		return t;
	}
	public void setT(String t) {
		this.t = t;
	}
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder(80);
		buffer.append(getClass().getName());
		buffer.append(" [id=").append(getId());
		buffer.append(", account=").append(account);
		buffer.append(", mobile=").append(mobile);
		buffer.append(", password=").append(password);
		buffer.append(", sotoken=").append(sotoken);
		buffer.append("]");
		return buffer.toString();
	}
}
