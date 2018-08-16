package com.mmall.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author ymj
 */
public class FTPUtil {

	private static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);
	
	private static String ftpIp = PropertiesUtil.getProperties("ftp.server.ip");
	private static String ftpUser = PropertiesUtil.getProperties("ftp.user");
	private static String ftpPwd = PropertiesUtil.getProperties("ftp.pass");
	
	private String ip;
	private int port;
	private String user;
	private String pwd;
	private FTPClient ftpClient;
	
	public FTPUtil(String ip,int port,String user,String pwd){
		this.ip = ip;
		this.port = port;
		this.user = user;
		this.pwd = pwd;
	}
	
	public static boolean uploadFile(List<File> fileList) throws IOException{
		FTPUtil ftpUtil = new FTPUtil(ftpIp,21,ftpUser,ftpPwd);
		logger.info("开始连接ftp服务器");
		boolean result = ftpUtil.uploadFile("img", fileList);//参数1：上传到img文件夹下，参数2：要批量长传的文件列表
		logger.info("结束上传，上传结果:{}");
		return result;
	}
	/**
	 * 上传的具体细节私有
	 * @param remotePath
	 * @param fileList
	 * @return
	 * @throws IOException 
	 */
	private  boolean uploadFile(String remotePath,List<File> fileList) throws IOException{
		boolean uploaded = true;//标志一下是否传了
		FileInputStream fis = null;
		//调用连接ftp服务器的具体方法
		if(connectServer(this.ip,this.port,this.user,this.pwd)){
			try {
				ftpClient.changeWorkingDirectory(remotePath);//切换文件夹，如果传的null过来，就切换不了
				ftpClient.setBufferSize(1024);//设置缓冲区大小
				ftpClient.setControlEncoding("UTF-8");//设置编码
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);//文件类型设置为二进制，防止乱码
				ftpClient.enterLocalPassiveMode();//根据在ftp服务器上配置的是被动模式，并且对外开放了服务被动端口范围
				
				//开始传
				for(File fileItem : fileList){
					fis = new FileInputStream(fileItem);
					ftpClient.storeFile(fileItem.getName(), fis);
				}
			} catch (IOException e) {
				logger.error("上传文件异常",e);
				uploaded = false;
			}finally{
				//释放资源
				fis.close();
				ftpClient.disconnect();
			}
		}
		return uploaded;
	}
	/**
	 * 连接ftp服务器的具体方法私有
	 * @param ip
	 * @param port
	 * @param user
	 * @param pwd
	 * @return
	 */
	private boolean connectServer(String ip,int port,String user,String pwd){
		boolean isSuccess = false;
		ftpClient = new FTPClient();
		try {
			ftpClient.connect(ip);
			isSuccess = ftpClient.login(user, pwd);
		} catch (IOException e) {
			logger.error("连接FTP服务器异常",e);
		}
		return isSuccess;
	}
	
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}
	/**
	 * @return the pwd
	 */
	public String getPwd() {
		return pwd;
	}
	/**
	 * @param pwd the pwd to set
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	/**
	 * @return the ftpClient
	 */
	public FTPClient getFtpClient() {
		return ftpClient;
	}
	/**
	 * @param ftpClient the ftpClient to set
	 */
	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}
	
	
	
}
