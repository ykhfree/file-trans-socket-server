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
	 * 파일명
	 */
	private String fileNm;
	/**
	 * 파일 바이트
	 */
	private byte[] bytes;
	/**
	 * 활용시스템 ID
	 */
	private String systemId;
	/**
	 * 복사 장소
	 */
	private String file_to;

	/**
	 * @return the sumCountPackage
	 */
	public int getSumCountPackage() {
		return sumCountPackage;
	}

	/**
	 * @param sumCountPackage
	 *            the sumCountPackage to set
	 */
	public void setSumCountPackage(int sumCountPackage) {
		this.sumCountPackage = sumCountPackage;
	}

	/**
	 * @return the countPackage
	 */
	public int getCountPackage() {
		return countPackage;
	}

	public void setCountPackage(int countPackage) {
		this.countPackage = countPackage;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public String getFileNm() {
		return fileNm;
	}

	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getFile_to() {
		return file_to;
	}

	public void setFile_to(String file_to) {
		this.file_to = file_to;
	}
}
