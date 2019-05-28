package ykhfree.dev.transfile.socketmodel;

import java.io.Serializable;

public class ResultMsg implements Serializable {

	private static final long serialVersionUID = -755288712044917381L;

	/**
	 * 결과코드
	 */
	private String resultCode;

	/**
	 * 상세 메시지
	 */
	private String detailMsg;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getDetailMsg() {
		return detailMsg;
	}

	public void setDetailMsg(String detailMsg) {
		this.detailMsg = detailMsg;
	}

}
