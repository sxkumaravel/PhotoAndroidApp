package com.photoandroidapp;

public class Details {

	private String imagePath;
	private String userHashtag;
	private String imagePathBig;
	
	public Details(String imagePath, String userHashtag,String imagePathBig) {
		super();
		this.imagePath = imagePath;
		this.userHashtag = userHashtag;
		this.setImagePathBig(imagePathBig);
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getUserHashtag() {
		return userHashtag;
	}

	public void setUserHashtag(String userHashtag) {
		this.userHashtag = userHashtag;
	}

	public String getImagePathBig() {
		return imagePathBig;
	}

	public void setImagePathBig(String imagePathBig) {
		this.imagePathBig = imagePathBig;
	}
	
	
	
	
}
