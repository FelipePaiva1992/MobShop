package br.com.thesource.mobshop.model;

public class StatusAdyen {

	private String pspReference;
	private String  resultCode;
	private String  authCode;
	private String  refusalReason;
	
	public String getPspReference() {
		return pspReference;
	}
	public void setPspReference(String pspReference) {
		this.pspReference = pspReference;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getRefusalReason() {
		return refusalReason;
	}
	public void setRefusalReason(String refusalReason) {
		this.refusalReason = refusalReason;
	}
	
	
}
