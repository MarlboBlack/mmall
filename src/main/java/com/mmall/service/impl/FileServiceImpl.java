package com.mmall.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;

/**
 * @author ymj
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService{
	
	private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class); 

	/**
	 * 上传文件到服务器
	 * @param file
	 * @param path
	 * @return
	 */
	@Override
	public String upload(MultipartFile file,String path){
		String fileName = file.getOriginalFilename();
		//扩展名
		String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
		//上传文件名
		String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
		logger.info("开始上传文件，上传文件名:{},上传路径:{},新文件名:{}",fileName,path,uploadFileName);
		
		//创建目录（这次的路径path是在tomcat服务器的webapp下创建upload文件夹目录）
		File fileDir = new File(path);
		if(!fileDir.exists()){
			fileDir.setWritable(true);
			fileDir.mkdirs();
		}
		//创建文件
		File targetFile = new File(path,uploadFileName);
		
		try {
			//上传文件(springMvc封装的MultipartFile的transferTo方法)
			file.transferTo(targetFile);//文件已经上传到upload文件夹下面
			
			//将targetFile上传到FTP服务器上
			//FTPUtil.uploadFile(Lists.newArrayList(targetFile));
			
			//上传到FTP服务器之后，删除tomcat服务器upload下面的文件
			//targetFile.delete();
			
		} catch (IOException e) {
			logger.error("上传文件异常",e);
			return null;
		}
		return targetFile.getName();
	}
	
}
