package com.mmall.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;

/**
 * @author ymj
 */
@Controller
@RequestMapping("/product/")
public class ProductController {

	@Autowired
	private IProductService iProductService;
	
	/**
	 * 前台获取商品详情
	 * @param productId
	 * @return
	 */
	@RequestMapping("detail.do")
	@ResponseBody
	public ServerResponse<ProductDetailVo> detail(Integer productId){
		return iProductService.getProductDetail(productId);
	}
	/**
	 * 前台搜索商品功能
	 * @param keyword
	 * @param categoryId
	 * @param pageNum
	 * @param pageSize
	 * @param orderBy
	 * @return
	 */
	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse<PageInfo> list(@RequestParam(value="keyword",required=false)String keyword,
										@RequestParam(value="categoryId",required=false)Integer categoryId,
										@RequestParam(value="pageNum",defaultValue="1")int pageNum,
										@RequestParam(value="pageSize",defaultValue="10")int pageSize,
										@RequestParam(value="orderBy",defaultValue="")String orderBy){
		return iProductService.getProductListByKeywordCategory(keyword, categoryId, pageNum, pageSize, orderBy);
	} 
	
}







