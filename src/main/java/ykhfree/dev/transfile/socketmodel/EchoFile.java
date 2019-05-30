package ykhfree.dev.transfile.socketmodel;

import java.io.Serializable;

public class EchoFile implements Serializable {

	private static final long serialVersionUID = 8953150675564212795L;

	/**
	 * 파일 분할 번호 합계
	 */
	private int sumCountPackage;
	/**
	 * 파일 분할 번호
	 */
	private int countPackage;
	/**
	 * 원본 파일경로
	 */
	private String srcFilePath;
	/**
	 * 파일 바이트
	 */
	private byte[] bytes;
	/**
	 * 활용시스템 ID
	 */
	private String systemId;
	/**
	 * 복사 파일경로
	 */
	private String destFilePath;

	public int getSumCountPackage() {
		return sumCountPackage;
	}

	public void setSumCountPackage(int sumCountPackage) {
		this.sumCountPackage = sumCountPackage;
	}

	public int getCountPackage() {
		return countPackage;
	}

	public void setCountPackage(int countPackage) {
		this.countPackage = countPackage;
	}

	public String getSrcFilePath() {
		return srcFilePath;
	}

	public void setSrcFilePath(String srcFilePath) {
		this.srcFilePath = srcFilePath;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getDestFilePath() {
		return destFilePath;
	}

	public void setDestFilePath(String destFilePath) {
		this.destFilePath = destFilePath;
	}
}
