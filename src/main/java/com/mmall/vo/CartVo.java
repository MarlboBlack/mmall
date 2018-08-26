package com.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ymj
 */
public class CartVo {

	private List<CartProductVo> cartProductVoList;
	private BigDecimal cartTotalPrice;
	private Boolean allChecked;
	private String imageHost;
	/**
	 * @return the cartProductVoList
	 */
	public List<CartProductVo> getCartProductVoList() {
		return cartProductVoList;
	}
	/**
	 * @param cartProductVoList the cartProductVoList to set
	 */
	public void setCartProductVoList(List<CartProductVo> cartProductVoList) {
		this.cartProductVoList = cartProductVoList;
	}
	/**
	 * @return the cartTotalPrice
	 */
	public BigDecimal getCartTotalPrice() {
		return cartTotalPrice;
	}
	/**
	 * @param cartTotalPrice the cartTotalPrice to set
	 */
	public void setCartTotalPrice(BigDecimal cartTotalPrice) {
		this.cartTotalPrice = cartTotalPrice;
	}
	/**
	 * @return the allChecked
	 */
	public Boolean getAllChecked() {
		return allChecked;
	}
	/**
	 * @param allChecked the allChecked to set
	 */
	public void setAllChecked(Boolean allChecked) {
		this.allChecked = allChecked;
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
