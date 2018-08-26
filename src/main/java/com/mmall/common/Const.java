package com.mmall.common;

import java.util.Set;

import com.google.common.collect.Sets;

/**
 * @author ymj
 */
public class Const {

	public static final String CURRENT_USER = "currentUser";
	
	public static final String USERNAME = "username";
	public static final String EMAIL = "email";
	
	public interface ProductListOrderBy{
		Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
	}
	
	public interface Cart{
		int CHECKED = 1;//购物车选中状态
		int UN_CHECKED = 0;//购物车未选中状态
		
		String LIMIT_NUM_FAIL="LIMIT_NUM_FAIL";//数量限制失败
		String LIMIT_NUM_SUCESS="LIMIT_NUM_SUCCESS";//数量限制成功
		
	}
	public interface Role{
		int ROLE_CUSTOMER = 0;//普通用户
		int ROLE_ADMIN = 1;//管理员
	}
	
	public enum ProductStatusEnum{
		ON_SALE(1,"在线");
		private String value;
		private int code;
		
		ProductStatusEnum(int code,String value){
			this.code = code;
			this.value = value;
		}

		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @return the code
		 */
		public int getCode() {
			return code;
		}
		
		
	}
}
