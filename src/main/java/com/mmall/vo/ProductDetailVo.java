package com.mmall.vo;

import java.math.BigDecimal;

/**
 * @author ymj
 */
public class ProductDetailVo {

	private Integer id;
	private Integer categoryId;
	private String name;
	private String subtitle;
	private String mainImage;
	private String subImage;
	private String detail;
	private BigDecimal price;
	private Integer status;
	private Integer stock;
	
	private String createTime;
	private String updateTime;
	private Integer parentCategoryId;
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
	 * @return the subImage
	 */
	public String getSubImage() {
		return subImage;
	}
	/**
	 * @param subImage the subImage to set
	 */
	public void setSubImage(String subImage) {
		this.subImage = subImage;
	}
	/**
	 * @return the detail
	 */
	public String getDetail() {
		return detail;
	}
	/**
	 * @param detail the detail to set
	 */
	public void setDetail(String detail) {
		this.detail = detail;
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
	 * @return the stock
	 */
	public Integer getStock() {
		return stock;
	}
	/**
	 * @param stock the stock to set
	 */
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return the updateTime
	 */
	public String getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @return the parentCategoryId
	 */
	public Integer getParentCategoryId() {
		return parentCategoryId;
	}
	/**
	 * @param parentCategoryId the parentCategoryId to set
	 */
	public void setParentCategoryId(Integer parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
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
