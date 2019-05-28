package ykhfree.dev.transfile.socketmodel;

import java.io.Serializable;
import java.util.List;

public class EchoTimeStamp implements Serializable {

	private static final long serialVersionUID = -5098260754414505842L;

	/**
	 * 파일명
	 */
	private String fileNm;

	/**
	 * 타임스탬프
	 */
	private Long timeStamp;

	/**
	 * 파일유무
	 */
	private boolean fileAt;

	/**
	 * 파일리스트
	 */
	private List<String> fileList;

	public String getFileNm() {
		return fileNm;
	}

	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public boolean isFileAt() {
		return fileAt;
	}

	public void setFileAt(boolean fileAt) {
		this.fileAt = fileAt;
	}

	public List<String> getFileList() {
		return fileList;
	}

	public void setFileList(List<String> fileList) {
		this.fileList = fileList;
	}

}
