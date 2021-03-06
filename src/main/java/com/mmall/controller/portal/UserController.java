package com.mmall.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;

/**
 * @author ymj
 */
@Controller
@RequestMapping("/user/")
public class UserController {
	
	@Autowired
	private IUserService iUserService;
	/**
	 * 用户登录
	 * @param username
	 * @param password
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "login.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> login(String username,String password,HttpSession session){
		//service -->mybatis -->dao
		ServerResponse<User> response = iUserService.login(username, password);
		if(response.isSuccess()){
			session.setAttribute(Const.CURRENT_USER, response.getData());
		}
		return response;
		
	}
	/**
	 * 用户退出
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "logout.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> logout(HttpSession session){
		session.removeAttribute(Const.CURRENT_USER);
		return ServerResponse.createBySuccess();
	}
	
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "register.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> register(User user){
		return iUserService.register(user);
	}
	/**
	 * 校验
	 * @param str
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "check_valid.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> checkValid(String str,String type){
		return iUserService.checkVaild(str, type);
	}
	/**
	 * 获取用户登录信息
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "get_user_info.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> getUserInfo(HttpSession session){
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user  != null){
			return ServerResponse.createBySuccess(user);
		}
		return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
	}
	/**
	 * 忘记密码获取密保问题
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "forget_get_question.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetGetQuestion(String username){
		return iUserService.selectQuestion(username);
	}
	/**
	 * 验证密保
	 * @param username
	 * @param question
	 * @param answer
	 * @return
	 */
	@RequestMapping(value = "forget_check_answer.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
		return iUserService.checkAnswer(username, question, answer);
	}
	/**
	 * 忘记密码修改密码
	 * @param username
	 * @param passwordNew
	 * @param forgetToken
	 * @return
	 */
	@RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetRsetPassword(String username,String passwordNew,String forgetToken){
		return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
	}
	/**
	 * 登录状态修改密码
	 * @param session
	 * @param passwordOld
	 * @param passwordNew
	 * @return
	 */
	@RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> RsetPassword(HttpSession session,String passwordOld,String passwordNew){
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorMessage("用户未登录");
		}
		return iUserService.resetPassword(passwordOld, passwordNew, user);
	}
	/**
	 * 登录状态更新个人信息
	 * @param session
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "update_information.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> upadateInformation(HttpSession session,User user){
		User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
		if(currentUser == null){
			return ServerResponse.createByErrorMessage("用户未登录");
		}
		//username不能被更新
		user.setId(currentUser.getId());
		user.setUsername(currentUser.getUsername());
		ServerResponse<User> response = iUserService.updateInformation(user);
		if(response.isSuccess()){
			session.setAttribute(Const.CURRENT_USER,response.getData());
		}
		return response;
	}
	/**
	 * 登录状态下获取用户详细信息
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "get_information.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> getInformation(HttpSession session){
		User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
		if(currentUser == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，需要强制登录");
		}
		return iUserService.getInformation(currentUser.getId());
	}
	

}








