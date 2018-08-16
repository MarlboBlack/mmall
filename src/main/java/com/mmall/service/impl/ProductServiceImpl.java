package com.mmall.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.jasper.tagplugins.jstl.core.ForEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;

/**
 * @author ymj
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService{

	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	private ICategoryService iCategoryService; 
	
	
	/**
	 * 新增或更新商品
	 */
	@Override
	public ServerResponse saveOrUpdateProduct(Product product){
		if(product != null){
			if(StringUtils.isNotBlank(product.getSubImages())){
				String[] subImageArray = product.getSubImages().split(",");
				if(subImageArray.length > 0){
					product.setMainImage(subImageArray[0]);
				}
			}
			if(product.getId() != null){
				int rowCount = productMapper.updateByPrimaryKey(product);
				if(rowCount > 0){
					return ServerResponse.createBySuccessMessage("更新商品成功");
				}
				return ServerResponse.createByErrorMessage("更新商品失败");
			}else{
				int rowCount = productMapper.insert(product);
				if(rowCount > 0){
					return ServerResponse.createBySuccessMessage("新增商品成功");
				}
				return ServerResponse.createByErrorMessage("新增商品失败");
			}
		}
		return ServerResponse.createByErrorMessage("新增或更新商品参数错误");
	}
	/**
	 * 修改商品售卖状态
	 * @param productId
	 * @param status
	 * @return
	 */
	@Override
	public ServerResponse<String> setSaleStatus(Integer productId,Integer status){
		if(productId == null || status == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		Product product = new Product();
		product.setId(productId);
		product.setStatus(status);
		int rowCount = productMapper.updateByPrimaryKeySelective(product);
		if(rowCount > 0){
			return ServerResponse.createBySuccessMessage("修改售卖状态成功");
		}
		return ServerResponse.createByErrorMessage("修改售卖状态失败");
	}
	
	/**
	 * 获取商品详情
	 */
	@Override
	public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId){
		if(productId == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		Product product = productMapper.selectByPrimaryKey(productId);
		if(product == null){
			return ServerResponse.createByErrorMessage("产品已下架或者删除");
		}
		ProductDetailVo productDetailVo = assembleProductDetailVo(product);
		return ServerResponse.createBySuccess(productDetailVo);
	}
	/**
	 * 装配productDetailVo方法
	 * @param product
	 */
	private ProductDetailVo assembleProductDetailVo(Product product){
		ProductDetailVo productDetailVo = new ProductDetailVo();
		productDetailVo.setId(product.getId());
		productDetailVo.setName(product.getName());
		productDetailVo.setCategoryId(product.getCategoryId());
		productDetailVo.setSubtitle(product.getSubtitle());
		productDetailVo.setSubImage(product.getSubImages());
		productDetailVo.setMainImage(product.getMainImage());
		productDetailVo.setStatus(product.getStatus());
		productDetailVo.setStock(product.getStock());
		productDetailVo.setPrice(product.getPrice());
		productDetailVo.setDetail(product.getDetail());
		
		//parentCategoryId
		Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
		if(category == null){
				productDetailVo.setParentCategoryId(0);//默认根节点
		}else{
				productDetailVo.setParentCategoryId(category.getParentId());
		}
				
		//imageHost0000000000000000000000000000000000000000使用了Properties000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
		//productDetailVo.setImageHost(PropertiesUtil.getProperties("ftp.server.http.prefix", "value"));
		productDetailVo.setImageHost("/upload/");
		//createTime
		//updateTime
		productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
		productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
		
		return productDetailVo;
	}
	
	/**
	 * 后台商品列表(按id升序)
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Override
	public ServerResponse<PageInfo> getProductList(int pageNum,int pageSize){
		//startPage--start
		//填充自己的sql查询逻辑
		//pageHelper-收尾
		PageHelper.startPage(pageNum, pageSize);
		List<ProductListVo> productListVoList = Lists.newArrayList();
		List<Product> productList = productMapper.selectList();
		for (Product productItem : productList) {
			ProductListVo productListVo = assembleProductListVo(productItem);
			productListVoList.add(productListVo);
		}
		PageInfo pageResult = new PageInfo(productListVoList);
		return ServerResponse.createBySuccess(pageResult);
	}
	
	/**
	 * 装配productListVo方法
	 * @param product
	 * @return
	 */
	private ProductListVo assembleProductListVo(Product product){
		ProductListVo productListVo = new ProductListVo();
		productListVo.setId(product.getId());
		productListVo.setName(product.getName());
		productListVo.setCategoryId(product.getCategoryId());
		productListVo.setMainImage(product.getMainImage());
		productListVo.setSubtitle(product.getSubtitle());
		productListVo.setPrice(product.getPrice());
		productListVo.setStatus(product.getStatus());
		//imageHost0000000000000000000000000000000000000000使用了Properties000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
		//productListVo.setImageHost(PropertiesUtil.getProperties("ftp.server.http.prefix", "D:/myFtp"));
		productListVo.setImageHost("/upload/");
		
		return productListVo;
	}
	
	/**
	 * 后台商品搜索
	 * @param productId
	 * @param productName
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Override
	public ServerResponse<PageInfo> searchProduct(Integer productId,String productName,int pageNum,int pageSize){
		//startPage--start
		PageHelper.startPage(pageNum,pageSize);
		//填充自己的sql查询逻辑
		if(StringUtils.isNotBlank(productName)){
			productName = new StringBuilder().append("%").append(productName).append("%").toString();
		}
		List<Product> productList = productMapper.selectByNameAndId(productName, productId);
		List<ProductListVo> productListVoList = Lists.newArrayList();
		for (Product productItem : productList) {
			ProductListVo productListVo = assembleProductListVo(productItem);
			productListVoList.add(productListVo);
		}
		//pageHelper-收尾
		PageInfo pageResult = new PageInfo(productListVoList);
		return ServerResponse.createBySuccess(pageResult);
	}
	//---------------------------------------------------------------前台------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 前台获取商品详情
	 */
	@Override
	public ServerResponse<ProductDetailVo> getProductDetail(Integer productId){
		if(productId == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		Product product = productMapper.selectByPrimaryKey(productId);
		if(product == null){
			return ServerResponse.createByErrorMessage("产品已下架或者删除");
		}
		if(product.getStatus()!=Const.ProductStatusEnum.ON_SALE.getCode()){
			return ServerResponse.createByErrorMessage("产品已下架或者删除");
		}
		ProductDetailVo productDetailVo = assembleProductDetailVo(product);
		return ServerResponse.createBySuccess(productDetailVo);
	}
	/**
	 * 前台搜索商品
	 * @param keyword
	 * @param categoryId
	 * @param pageNum
	 * @param pageSize
	 * @param orderBy
	 * @return
	 */
	@Override
	public ServerResponse<PageInfo> getProductListByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy){
		if(StringUtils.isBlank(keyword) && categoryId == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		List<Integer> categoryIdList = new ArrayList<Integer>();
		if(categoryId != null){
			Category category = categoryMapper.selectByPrimaryKey(categoryId);
			if(category == null && StringUtils.isBlank(keyword)){
				//没有该分类，并且还没有关键字，说明没有命中数据，返回一个空的结果集,不报错
				PageHelper.startPage(pageNum,pageSize);
				List<ProductListVo> productListVoList = Lists.newArrayList();
				PageInfo pageInfo = new PageInfo(productListVoList);
				return ServerResponse.createBySuccess(pageInfo);
			}
			categoryIdList = iCategoryService.getChildrenDeepCategory(category.getId()).getData();
		}
		if(StringUtils.isNotBlank(keyword)){
			keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
		}
		PageHelper.startPage(pageNum,pageSize);
		//排序处理
		if(StringUtils.isNotBlank(orderBy)){
			if(Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
				String[] orderByArray = orderBy.split("_");//例如price_desc得到[0]price,[1]desc
				PageHelper.orderBy(orderByArray[0]+" "+orderByArray[1]);//得到price desc符合sql语句规则
			}
		}
		//根据名称或分类id查询所有符合条件商品
		List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword,categoryIdList.size()==0?null:categoryIdList);
		//将符合条件的商品组装成Vo，再返回Vo集合。，放入pageInfo
		List<ProductListVo> productListVoList = Lists.newArrayList();
		for(Product product:productList){
			ProductListVo productListVo = assembleProductListVo(product);
			productListVoList.add(productListVo);
		}
		PageInfo pageInfo = new PageInfo(productListVoList);
		return ServerResponse.createBySuccess(pageInfo);
	}
}










