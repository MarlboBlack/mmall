package com.mmall.service.impl;


import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;

/**
 * @author ymj
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService{

	@Autowired
	private CartMapper cartMapper;
	@Autowired
	private ProductMapper productMapper;
	
	/**
	 * 购物车添加功能
	 * @param userId
	 * @param productId
	 * @param count
	 * @return
	 */
	
	@Override
	public ServerResponse<CartVo> add(Integer userId,Integer productId,Integer count){
		if(productId == null||count == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
		if(cart == null){
			//这个商品不在购物车里,需要新增一个这个商品的记录
			Cart cartItem = new Cart();
			cartItem.setQuantity(count);
			cartItem.setChecked(Const.Cart.CHECKED);
			cartItem.setUserId(userId);
			cartItem.setProductId(productId);
			
			cartMapper.insert(cartItem);
		}else{
			//这个商品已经在购物车了，我们把数量相加
			count = cart.getQuantity()+count;
			cart.setQuantity(count);
			cartMapper.updateByPrimaryKeySelective(cart);
		}
		return this.list(userId);//用的list方法，其中封装好了
	}
	
	/**
	 * 购物车更新功能
	 */
	@Override
	public ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count){
		if(productId == null||count == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
		if(cart != null){
			cart.setQuantity(count);
		}
		cartMapper.updateByPrimaryKeySelective(cart);
		return this.list(userId);//用的list方法，其中封装好了
	}
	/**
	 * 购物车删除商品功能
	 * @param userId
	 * @param productIds
	 * @return
	 */
	@Override
	public ServerResponse<CartVo> deleteProduct(Integer userId,String productIds){
		List<String> productList = Splitter.on(",").splitToList(productIds);
		if(CollectionUtils.isEmpty(productList)){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		cartMapper.deleteByUserIdProductIds(userId,productList);
		return this.list(userId);//用的list方法，其中封装好了
	}
	/**
	 * 购物车查询功能
	 * @param userId
	 * @return
	 */
	@Override
	public  ServerResponse<CartVo> list(Integer userId){
		CartVo cartVo = this.getCartVoLimit(userId); 
		return ServerResponse.createBySuccess(cartVo);
	}
	/**
	 * 购物车全选或全不选，单选或单不选
	 * @param userId
	 * @param checked
	 * @return
	 */
	@Override
	public ServerResponse<CartVo> selectOrUnSelect(Integer userId,Integer checked,Integer productId){
		cartMapper.checkedOrUnCheckedProduct(userId, checked,productId);
		return this.list(userId);//用的list方法，其中封装好了
	}
	/**
	 * 购物车商品数量
	 */
	@Override
	public ServerResponse<Integer> gettCartProductCount(Integer userId){
		if(userId == null){
			return ServerResponse.createBySuccess(0);
		}
		return ServerResponse.createBySuccess(cartMapper.selectCartProductCount(userId));
	}
	
	
	/**
	 * 购物车是否是全选，单选，反选，所有的都会用这个计算方法
	 * @param userId
	 * @return
	 */
	private CartVo getCartVoLimit(Integer userId){
		CartVo cartVo = new CartVo();
		List<Cart> cartList = cartMapper.selectCartByUserId(userId);
		List<CartProductVo> cartProductVoList = Lists.newArrayList();
		
		BigDecimal cartTotalPrice = new BigDecimal("0");
		
		if(CollectionUtils.isNotEmpty(cartList)){
			for(Cart cartItem:cartList){
				CartProductVo cartProductVo = new CartProductVo();
				cartProductVo.setId(cartItem.getId());
				cartProductVo.setUserId(cartItem.getUserId());
				cartProductVo.setProductId(cartItem.getProductId());
				
				Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
				if(product != null){
					cartProductVo.setProductName(product.getName());
					cartProductVo.setProductMainImage(product.getMainImage());
					cartProductVo.setProductStatus(product.getStatus());
					cartProductVo.setProductSubtitle(product.getSubtitle());
					cartProductVo.setProductStock(product.getStock());
					cartProductVo.setProductPrice(product.getPrice());
					//判断库存
					int buyLimitCount = 0;
					if(product.getStock() >= cartItem.getQuantity()){
						buyLimitCount = cartItem.getQuantity();
						cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCESS);
					}else{
						buyLimitCount = product.getStock();
						cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
						//更新购物车有效库存
						Cart cartForQuantity = new Cart();
						cartForQuantity.setId(cartItem.getId());
						cartForQuantity.setQuantity(buyLimitCount);
						cartMapper.updateByPrimaryKeySelective(cartForQuantity);
					}
					cartProductVo.setQuantity(buyLimitCount);
					//计算总价
					cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity()));
					cartProductVo.setProductChecked(cartItem.getChecked());
					
				}
				if(cartItem.getChecked() == Const.Cart.CHECKED){
					//如果已经勾选，增加到整个的购物车总价中
					cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());
				}
					
				cartProductVoList.add(cartProductVo);				
			}	
		}
		cartVo.setCartTotalPrice(cartTotalPrice);
		cartVo.setCartProductVoList(cartProductVoList);
		cartVo.setAllChecked(this.getAllCheckedStatus(userId));
		cartVo.setImageHost(PropertiesUtil.getProperties("ftp.server.http.prefix"));
		return cartVo;
	}
	/**
	 * 是否是全选状态
	 * @param userId
	 * @return
	 */
	private boolean getAllCheckedStatus(Integer userId){
		if(userId == null){
			return false;
		}
		return cartMapper.selectCartProductCheckedStatusByUserId(userId)==0;
	}
	
	
	
	
	
}








