package com.administrator.bean;

import java.io.Serializable;

/**  
* @Description: TODO
* @author lin
* @date 2016年3月3日 下午4:26:23 
* @version V1.0  
 */
public class ThumbnailPhoto implements Serializable{

	private String thumbnail;//缩略图路径
	private String original;//原图路径
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getOriginal() {
		return original;
	}
	public void setOriginal(String original) {
		this.original = original;
	}
	
}
