package com.mmall.vo;

import java.math.BigDecimal;

/**
 * @author ymj
 */
public class CartProductVo {

	//介个商品和购物车的一个抽象对象
	private Integer id;
	private Integer userId;
	private Integer productId;
	private Integer quantity;
	private String productName;
	private String productSubtitle;
	private String productMainImage;
	private BigDecimal productPrice;
	private Integer productStatus;
	private BigDecimal productTotalPrice;
	private Integer productStock;
	private Integer productChecked;
	
	private String limitQuantity;//限制数量的一个返回结果

	
	
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
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @return the productId
	 */
	public Integer getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	/**
	 * @return the quantity
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * @return the productSubtitle
	 */
	public String getProductSubtitle() {
		return productSubtitle;
	}

	/**
	 * @param productSubtitle the productSubtitle to set
	 */
	public void setProductSubtitle(String productSubtitle) {
		this.productSubtitle = productSubtitle;
	}

	/**
	 * @return the productMainImage
	 */
	public String getProductMainImage() {
		return productMainImage;
	}

	/**
	 * @param productMainImage the productMainImage to set
	 */
	public void setProductMainImage(String productMainImage) {
		this.productMainImage = productMainImage;
	}

	/**
	 * @return the productPrice
	 */
	public BigDecimal getProductPrice() {
		return productPrice;
	}

	/**
	 * @param productPrice the productPrice to set
	 */
	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	/**
	 * @return the productStatus
	 */
	public Integer getProductStatus() {
		return productStatus;
	}

	/**
	 * @param productStatus the productStatus to set
	 */
	public void setProductStatus(Integer productStatus) {
		this.productStatus = productStatus;
	}

	/**
	 * @return the productTotlePrice
	 */
	public BigDecimal getProductTotalPrice() {
		return productTotalPrice;
	}

	/**
	 * @param productTotlePrice the productTotlePrice to set
	 */
	public void setProductTotalPrice(BigDecimal productTotlePrice) {
		this.productTotalPrice = productTotlePrice;
	}

	/**
	 * @return the productStock
	 */
	public Integer getProductStock() {
		return productStock;
	}

	/**
	 * @param productStock the productStock to set
	 */
	public void setProductStock(Integer productStock) {
		this.productStock = productStock;
	}

	/**
	 * @return the productChecked
	 */
	public Integer getProductChecked() {
		return productChecked;
	}

	/**
	 * @param productChecked the productChecked to set
	 */
	public void setProductChecked(Integer productChecked) {
		this.productChecked = productChecked;
	}

	/**
	 * @return the limitQuantity
	 */
	public String getLimitQuantity() {
		return limitQuantity;
	}

	/**
	 * @param limitQuantity the limitQuantity to set
	 */
	public void setLimitQuantity(String limitQuantity) {
		this.limitQuantity = limitQuantity;
	}
	
	
}
