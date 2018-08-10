package com.mmall.service.impl;


import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ServletCookieValueMethodArgumentResolver;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;

/**
 * @author ymj
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService{

	@Autowired
	private UserMapper userMapper;
	
	/**
	 * 登录
	 */
	@Override
	public ServerResponse<User> login(String username, String password) {
		int resultCount = userMapper.checkUsername(username);
		if (resultCount ==0){
			return ServerResponse.createByErrorMessage("用户名不存在");
		}
		
		//md5密码登录
		String md5Password = MD5Util.MD5EncodeUtf8(password);
		
		User user = userMapper.selectLogin(username, md5Password);
		if(user == null){
			return ServerResponse.createByErrorMessage("密码错误");
		}
		
		user.setPassword(StringUtils.EMPTY);
		return ServerResponse.createBySuccess("登录成功", user);
			
	}
	/**
	 * 注册
	 */
	@Override
	public ServerResponse<String> register(User user){
//		int resultCount = userMapper.checkUsername(user.getUsername());
//		if (resultCount > 0){
//			return ServerResponse.createByErrorMessage("用户名已存在");
//		}
//		resultCount = userMapper.checkEmail(user.getEmail());
//		if(resultCount > 0){
//			return ServerResponse.createByErrorMessage("email已存在");
//		}
//因为为了以后校验的拓展性，我们把校验方法单独写出来，减少方法之间的耦合性
		ServerResponse validResponse = this.checkVaild(user.getUsername(),Const.USERNAME);
		if(!validResponse.isSuccess()){
			return validResponse;
		}
		validResponse = this.checkVaild(user.getEmail(),Const.EMAIL);
		if(!validResponse.isSuccess()){
			return validResponse;
		}
		user.setRole(Const.Role.ROLE_CUSTOMER);
		//MD5加密
		user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
		
		int resultCount = userMapper.insert(user);
		if(resultCount == 0){
			return ServerResponse.createByErrorMessage("注册失败");
		}
		return ServerResponse.createBySuccessMessage("注册成功");
	}
	/**
	 * 校验方法
	 */
	@Override
	public ServerResponse<String> checkVaild(String str,String type){
		if(StringUtils.isNotBlank(type)){
			//开始校验
			if(Const.USERNAME.equals(type)){
				int resultCount = userMapper.checkUsername(str);
				if (resultCount > 0){
					return ServerResponse.createByErrorMessage("用户名已存在");
				}
			}
			if(Const.EMAIL.equals(type)){
				int resultCount = userMapper.checkEmail(str);
				if(resultCount > 0){
					return ServerResponse.createByErrorMessage("email已存在");
				}
			}
			
		}else{
			return ServerResponse.createByErrorMessage("参数错误");
		}
		return ServerResponse.createBySuccessMessage("校验成功");
	}
	/**
	 * 获取密保问题
	 */
	@Override
	public ServerResponse selectQuestion(String username){
		ServerResponse validResponse = this.checkVaild(username,Const.USERNAME);
		if(validResponse.isSuccess()){
			//用户名不存在
			return ServerResponse.createByErrorMessage("用户不存在");
		}
		String question = userMapper.selectQuestionByUsername(username);
		if(StringUtils.isNotBlank(question)){
			return ServerResponse.createBySuccess(question);
		}
		return ServerResponse.createByErrorMessage("找回密码的问题是空的");
	}
	/**
	 * 验证密保答案
	 */
	@Override
	public ServerResponse<String> checkAnswer(String username,String question,String answer){
		int resultCount = userMapper.checkAnswer(username,question,answer);
		if(resultCount > 0){
			//问题答案是这个用户的，并且正确
			String forgetToken = UUID.randomUUID().toString();
			TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
			return ServerResponse.createBySuccess(forgetToken);
		}
		return ServerResponse.createByErrorMessage("问题答案错误");
	}
	/**
	 * 忘记密码情况下修改
	 */
	@Override
	public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
		if(StringUtils.isBlank(forgetToken)){
			return ServerResponse.createByErrorMessage("参数错误，需要传递token");
		}
		ServerResponse validResponse = this.checkVaild(username,Const.USERNAME);
		if(validResponse.isSuccess()){
			//用户名不存在
			return ServerResponse.createByErrorMessage("用户不存在");
		} 
		String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
		if(StringUtils.isBlank(token)){
			return ServerResponse.createByErrorMessage("token已过期");
		}
		if(StringUtils.equals(token,forgetToken)){
			String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
			int rowCount = userMapper.updatePasswordByUsername(username, md5Password);
			if(rowCount > 0){
				return ServerResponse.createBySuccessMessage("更改密码成功");
			}
		}else{
			return ServerResponse.createByErrorMessage("token错误，请重新获取重置密码的token");
		}
		return ServerResponse.createByErrorMessage("更改密码失败");
	}
	
	/**
	 * 登录状态修改密码
	 */
	@Override
	public ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user){
		//防止横向越权，要校验一下这个用户的旧密码，一定要指定是这个用户
		int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
		if(resultCount == 0){
			return ServerResponse.createByErrorMessage("旧密码错误");
		}
		user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
		int updateCount = userMapper.updateByPrimaryKeySelective(user);
		if(updateCount > 0){
			return ServerResponse.createBySuccessMessage("更新密码成功");
		}
		return ServerResponse.createByErrorMessage("更新密码失败");
	}
	/**
	 * 登录状态下更新个人信息
	 * @param user
	 * @return
	 */
	@Override
	public ServerResponse<User> updateInformation(User user){
		//email要被校验,校验是否重复
		int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
		if(resultCount > 0){
			return ServerResponse.createByErrorMessage("邮箱已存在");
		}
		User updateUser = new User();
		updateUser.setId(user.getId());
		updateUser.setEmail(user.getEmail());
		updateUser.setPhone(user.getPhone());
		updateUser.setQuestion(user.getQuestion());
		updateUser.setAnswer(user.getAnswer());
		
		int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
		if(updateCount > 0){
			return ServerResponse.createBySuccessMessage("更新个人信息成功");
		}
		return ServerResponse.createByErrorMessage("更新个人信息失败");
	}
	/**
	 * 登录状态下获取用户详细信息
	 */
	@Override
	public ServerResponse<User> getInformation(Integer userId){
		User user = userMapper.selectByPrimaryKey(userId);
		if(user == null){
			return ServerResponse.createByErrorMessage("找不到当前用户");
		}
		user.setPassword(StringUtils.EMPTY);
		return ServerResponse.createBySuccess(user);
	}
}














