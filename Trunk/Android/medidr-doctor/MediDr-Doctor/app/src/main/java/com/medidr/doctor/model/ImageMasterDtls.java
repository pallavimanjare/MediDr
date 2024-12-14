package com.medidr.doctor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ImageMasterDtls {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)	
	
	@JsonProperty("image_id")
	private Long imageId;
	
	@JsonProperty("image_url")
	private String imageUrl;
	
	@JsonProperty("image_type")
	private String imageType;
	
	@JsonProperty("modified")
	private boolean modified;		
	
	@JsonProperty("deleted")
	private boolean deleted;

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}



	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	@Override
	public String toString() {
		return String.format("ImageMasterDtls [imageId=%s, imageUrl=%s, imageType=%s, modified=%s, deleted=%s]",
				imageId, imageUrl, imageType, modified, deleted);
	}

	
}
