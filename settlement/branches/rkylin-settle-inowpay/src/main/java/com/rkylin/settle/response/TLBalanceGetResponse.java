package com.rkylin.settle.response;

public class TLBalanceGetResponse extends Response {
	
	private boolean is_success = false;
	
	private String balance;
	
	private String rtn_msg;

	public boolean isIs_success() {
		return is_success;
	}

	public void setIs_success(boolean is_success) {
		this.is_success = is_success;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getRtn_msg() {
		return rtn_msg;
	}

	public void setRtn_msg(String rtn_msg) {
		this.rtn_msg = rtn_msg;
	}
}
