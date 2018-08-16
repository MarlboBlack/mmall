package com.mmall.controller.backend;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;

/**
 * @author ymj
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {
	
	@Autowired
	private IUserService iUserService;
	@Autowired
	private IProductService iProductService;
	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private IFileService iFileService;
	
	/**
	 * 新增或更新商品
	 * @param session
	 * @param product
	 * @return
	 */
	@RequestMapping("save.do")
	@ResponseBody
	public ServerResponse productSave(HttpSession session,Product product){
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，请登录管理员");
		}
		if(iUserService.checkAdminRole(user).isSuccess()){
			return iProductService.saveOrUpdateProduct(product);
		}else{
			return ServerResponse.createByErrorMessage("无权限操作");
		}
	}
	
	/**
	 * 修改售卖状态
	 * @param productId
	 * @param status
	 * @return
	 */
	@RequestMapping("set_sale_status.do")
	@ResponseBody
	public ServerResponse setSaleStatus(HttpSession session,Integer productId,Integer status){
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，请登录管理员");
		}
		if(iUserService.checkAdminRole(user).isSuccess()){
			return iProductService.setSaleStatus(productId,status);
		}else{
			return ServerResponse.createByErrorMessage("无权限操作");
		}
	}
	/**
	 * 后台获取商品详情
	 * @param session
	 * @param productId
	 * @return
	 */
	@RequestMapping("detail.do")
	@ResponseBody
	public ServerResponse getDetail(HttpSession session,Integer productId){
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，请登录管理员");
		}
		if(iUserService.checkAdminRole(user).isSuccess()){
			return iProductService.manageProductDetail(productId);
		}else{
			return ServerResponse.createByErrorMessage("无权限操作");
		}
	}
	
	/**
	 * 后台查询商品列表
	 * @param session
	 * @return
	 */
	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse getList(HttpSession session,@RequestParam(value="pageNum" ,defaultValue="1")int pageNum,@RequestParam(value="pagesize" ,defaultValue="10")int pageSize){
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		if(iUserService.checkAdminRole(user).isSuccess()){
			return iProductService.getProductList(pageNum, pageSize);
		}else{
			return ServerResponse.createByErrorMessage("无权限操作");
		}
	}
	/**
	 * 后台商品搜索
	 * @param session
	 * @param productId
	 * @param productName
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("search.do")
	@ResponseBody
	public ServerResponse productSearch(HttpSession session,Integer productId,String productName,@RequestParam(value="pageNum" ,defaultValue="1")int pageNum,@RequestParam(value="pagesize" ,defaultValue="10")int pageSize){
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		if(iUserService.checkAdminRole(user).isSuccess()){
			return iProductService.searchProduct(productId, productName, pageNum, pageSize);
		}else{
			return ServerResponse.createByErrorMessage("无权限操作");
		}
	}
	/**
	 * 后台商品文件上传
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping("upload.do")
	@ResponseBody
	public ServerResponse upload(HttpSession session,@RequestParam(value="upload_file",required=false)MultipartFile file,HttpServletRequest request){

		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		if(iUserService.checkAdminRole(user).isSuccess()){
			String path = request.getSession().getServletContext().getRealPath("upload");
			String targetFileName = iFileService.upload(file, path);
			//String url = PropertiesUtil.getProperties("ftp.server.http.prefix")+targetFileName;//000000000000000000000000000000用了properties配置0000000000000000000000000000000000000000
			String url = "/upload/"+targetFileName;
			Map fileMap = Maps.newHashMap();
			fileMap.put("uri",targetFileName);
			fileMap.put("url",url);
			return ServerResponse.createBySuccess(fileMap);
		}else{
			return ServerResponse.createByErrorMessage("无权限操作");
		}
	}
	
	/**
	 * 后台商品富文本上传
	 * 富文本对于返回值有自己的要求，我们使用的是simditor所以按照simditor的要求进行返回
	 * 我们还需要对HttpServletResponse的头部进行修改
	 * 如下：
	 * {
	 * 		"success":true/false;
	 * 		"msg":"上传消息";
	 * 		"file_path":"[real file path]"
	 * }
	 * @param session
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping("richtext_image_upload.do")
	@ResponseBody
	public Map richtextImageUpload(HttpSession session,@RequestParam(value="upload_file",required=false)MultipartFile file,HttpServletRequest request,HttpServletResponse response){

		Map resultMap = Maps.newHashMap();
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			resultMap.put("success",false);
			resultMap.put("msg", "未登录，请登录管理员");
			return resultMap;
		}
		if(iUserService.checkAdminRole(user).isSuccess()){
			String path = request.getSession().getServletContext().getRealPath("upload");
			String targetFileName = iFileService.upload(file, path);
			if(StringUtils.isBlank(targetFileName)){
				resultMap.put("success",false);
				resultMap.put("msg", "上传失败");
				return resultMap;
			}
			//String url = PropertiesUtil.getProperties("ftp.server.http.prefix")+targetFileName;//000000000000000000000000000000用了properties配置0000000000000000000000000000000000000000
			String url = "/upload/"+targetFileName;
			resultMap.put("success",true);
			resultMap.put("msg", "上传成功");
			resultMap.put("file_path", url);
			response.addHeader("Access-Control-Allow-Headers","X-File-Name");//当上传成功之后我们修改response的header，这也是与前端的约定
			return resultMap;
		}else{
			resultMap.put("success",false);
			resultMap.put("msg", "无权限操作");
			return resultMap;
		}
	}
	
	
}















