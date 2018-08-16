package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

/**
 * @author ymj
 */
public interface IProductService {

	//后台
	ServerResponse saveOrUpdateProduct(Product product);
	
	ServerResponse<String> setSaleStatus(Integer productId,Integer status);
	
	ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);
	
	ServerResponse<PageInfo> getProductList(int pageNum,int pageSize);
	
	ServerResponse<PageInfo> searchProduct(Integer productId,String productName,int pageNum,int pageSize);
	
	//前台
	ServerResponse<ProductDetailVo> getProductDetail(Integer productId);
	
	ServerResponse<PageInfo> getProductListByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);
}
