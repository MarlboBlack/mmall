package com.mmall.vo;

import java.math.BigDecimal;

/**
 * @author ymj
 */
public class ProductListVo {

	private Integer id;
	private Integer categoryId;
	private String name;
	private String subtitle;
	private String mainImage;
	private BigDecimal price;
	private Integer status;
	private String imageHost;
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the categoryId
	 */
	public Integer getCategoryId() {
		return categoryId;
	}
	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the subtitle
	 */
	public String getSubtitle() {
		return subtitle;
	}
	/**
	 * @param subtitle the subtitle to set
	 */
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	/**
	 * @return the mainImage
	 */
	public String getMainImage() {
		return mainImage;
	}
	/**
	 * @param mainImage the mainImage to set
	 */
	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}
	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * @return the imageHost
	 */
	public String getImageHost() {
		return imageHost;
	}
	/**
	 * @param imageHost the imageHost to set
	 */
	public void setImageHost(String imageHost) {
		this.imageHost = imageHost;
	}
	
	
	
	
}
