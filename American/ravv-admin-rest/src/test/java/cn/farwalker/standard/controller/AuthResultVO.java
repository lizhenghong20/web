package cn.farwalker.standard.controller;

public class AuthResultVO {
	
	private Long memberId;
	
	private String account;
	
	private String token;
	
	private String randomKey;
	
	private int Code;

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRandomKey() {
		return randomKey;
	}

	public void setRandomKey(String randomKey) {
		this.randomKey = randomKey;
	}

	public int getCode() {
		return Code;
	}

	public void setCode(int code) {
		Code = code;
	}
	
	

}
