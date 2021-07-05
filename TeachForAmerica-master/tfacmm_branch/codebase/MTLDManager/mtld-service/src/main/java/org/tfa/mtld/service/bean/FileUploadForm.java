package org.tfa.mtld.service.bean;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author divesh.solanki
 * 
 */
public class FileUploadForm {

	private MultipartFile file;

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

}
