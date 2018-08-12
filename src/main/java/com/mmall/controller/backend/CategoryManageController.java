package com.mmall.controller.backend;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;

/**
 * @author ymj
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

	 @Autowired
	 private IUserService iUserService;
	 
	 @Autowired
	 private ICategoryService iCategoryService;
	 
	 /**
	  * 添加商品品类
	  * @param session
	  * @param categoryName
	  * @param parentId
	  * @return
	  */
	 @RequestMapping("add_category.do")
	 @ResponseBody
	 public ServerResponse addCategory(HttpSession session,String categoryName,@RequestParam(value="parentId", defaultValue="0")int parentId){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
		 if(user == null){
			 return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		 }
		 //判断是不是管理员
		 if(iUserService.checkAdminRole(user).isSuccess()){
			 //是管理员,增加处理分类的逻辑
			 return iCategoryService.addCategory(categoryName, parentId);
		 }else{
			 return ServerResponse.createByErrorMessage("无管理员权限");
		 }
		 
	 }
	 
	 /**
	  * 更新商品品类名称
	  * @param session
	  * @param categoryId
	  * @param categoryName
	  * @return
	  */
	 @RequestMapping("set_category_name.do")
	 @ResponseBody
	 public ServerResponse setCategoryName(HttpSession session,Integer categoryId,String categoryName){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
		 if(user == null){
			 return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		 }
		 //判断是不是管理员
		 if(iUserService.checkAdminRole(user).isSuccess()){
			 //是管理员,更新categoryName
			 return iCategoryService.updateCategoryName(categoryId, categoryName);
		 }else{
			 return ServerResponse.createByErrorMessage("无管理员权限");
		 }
	 }
	 /**
	  * 获取当前品类的子品类，不递归
	  * @param session
	  * @return
	  */
	 @RequestMapping("get_children_parallel_category.do")
	 @ResponseBody
	 public ServerResponse getChildrenParallelCategory(HttpSession session,@RequestParam(value="categoryId",defaultValue="0")Integer categoryId){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
		 if(user == null){
			 return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		 }
		 //判断是不是管理员
		 if(iUserService.checkAdminRole(user).isSuccess()){
			 //是管理员,获取子品类信息，不递归
			return iCategoryService.getChildrenParallelCategory(categoryId);
		 }else{
			 return ServerResponse.createByErrorMessage("无管理员权限");
		 }
	 }
	 /**
	  * 查询本节点的id及所有孩子节点的id
	  * @param session
	  * @param categoryId
	  * @return
	  */
	 @RequestMapping("get_children_deep_category.do")
	 @ResponseBody
	 public ServerResponse getChildrenDeepCategory(HttpSession session,@RequestParam(value="categoryId",defaultValue="0")Integer categoryId){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
		 if(user == null){
			 return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		 }
		 //判断是不是管理员
		 if(iUserService.checkAdminRole(user).isSuccess()){
			 //是管理员,获取所有子品类信息，递归0-->10000-->100000
			return iCategoryService.getChildrenDeepCategory(categoryId);
		 }else{
			 return ServerResponse.createByErrorMessage("无管理员权限");
		 }
	 }
	 
	 
	 
	 
	 
}











