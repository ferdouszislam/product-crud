package com.dbl.nsl.productcrud.common;

import javax.persistence.Embeddable;

@Embeddable
public class Photo {

	private String photoName;
	
	private String originalPath;
	
	private String v300x300Path;
	
	private String v50x50Path;

	public Photo() {
		super();
	}

	public Photo(String photoName, String originalPath, String v300x300Path, String v50x50Path) {
		super();
		this.photoName = photoName;
		this.originalPath = originalPath;
		this.v300x300Path = v300x300Path;
		this.v50x50Path = v50x50Path;
	}

	public String getPhotoName() {
		return photoName;
	}
	
	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}
	
	public String getOriginalPath() {
		return originalPath;
	}
	
	public void setOriginalPath(String originalPath) {
		this.originalPath = originalPath;
	}
	
	public String getV300x300Path() {
		return v300x300Path;
	}
	
	public void setV300x300Path(String v300x300Path) {
		this.v300x300Path = v300x300Path;
	}
	
	public String getV50x50Path() {
		return v50x50Path;
	}
	
	public void setV50x50Path(String v50x50Path) {
		this.v50x50Path = v50x50Path;
	}

	@Override
	public String toString() {
		return "Photo [photoName=" + photoName + ", originalPath="
				+ originalPath + ", v300x300Path=" + v300x300Path
				+ ", v50x50Path=" + v50x50Path + "]";
	}

}
