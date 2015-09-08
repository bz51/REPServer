package com.rep.tea.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.struts2.ServletActionContext;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.opensymphony.xwork2.ActionSupport;
import com.rep.core.Config;
import com.rep.core.HibernateSessionFactory;

public class UploadAction extends ActionSupport{
	private File file;
	private String fileFileName;
	private String fileContentType;
	private String result;
	private String reason;
	private String id;
	
	/**
	 * 上传文件
	 * @return
	 * @throws IOException 
	 */
	public String upload(){
		if(id==null || "".equals(id)){
			this.result = "no";
			this.reason = "id不能为空！";
			return "upload";
		}
		
		System.out.println("id:"+id);
		
		//基于myFile创建一个文件输入流  
        InputStream is;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			this.result = "no";
			this.reason = "创建输入流失败";
			return "upload";
		}  
          
        // 设置上传文件目录  
        String uploadPath = "/usr/tomcat/webapps/upload";  
//        String uploadPath = ServletActionContext.getServletContext().getRealPath("/upload");
        File uploadPathDir  = new File(uploadPath);
        if(!uploadPathDir.exists()){
        	uploadPathDir.mkdirs();
        }
        System.out.println(uploadPath);
        // 设置目标文件
        System.out.println("filename:"+this.getFileFileName());
        System.out.println("fileContentType:"+this.getFileContentType());
        //生成新的文件名，防止服务器中文件名重复
        //将目标文件名以.切成两端
        System.out.println("filename1:"+this.fileFileName);
        String[] strs = this.getFileFileName().split("\\.");
        System.out.println("strs[0]:"+strs[0]);
        this.fileFileName = strs[0]+new Date().getTime()+"."+strs[1];
        System.out.println("filename2:"+this.fileFileName);
        File toFile = new File(uploadPath, this.getFileFileName());  
        if(!toFile.exists()){
        	try {
				toFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				this.result = "no";
				this.reason = "创建目标文件失败";
				return "upload";
			}
        }
        
        // 创建一个输出流  
        OutputStream os;
		try {
			os = new FileOutputStream(toFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			this.result = "no";
			this.reason = "创建输出流失败";
			return "upload";
		}
  
        //设置缓存  
        byte[] buffer = new byte[1024];  
  
        int length = 0;  
  
        //读取myFile文件输出到toFile文件中  
        try {
			while ((length = is.read(buffer)) > 0) {  
			    os.write(buffer, 0, length);  
			}
		} catch (IOException e) {
			e.printStackTrace();
			this.result = "no";
			this.reason = "读取输入流，写入文件时发生异常";
			return "upload";
		}  
        //关闭输入流、输出流
        try {
			is.close();
	        os.close(); 
		} catch (IOException e) {
			e.printStackTrace();
			this.result = "no";
			this.reason = "流关闭异常";
			return "upload";
		}  
          
        System.out.println("文件名:"+this.getFileFileName());
        System.out.println("文件类型:"+this.getFileContentType());
        
        //将文件的路径写入exp_stu表的path字段中
//        try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String hql = "update StuExpEntity set path=:pathString where id=:idString";
			session.createQuery(hql)
//				.setString("pathString", Config.IP+":"+Config.PORT+ServletActionContext.getServletContext().getContextPath()+"/upload/"+this.fileFileName)
				.setString("pathString", Config.IP+"/upload/"+this.fileFileName)
				.setString("idString", id)
				.executeUpdate();
			session.getTransaction().commit();
//			HibernateSessionFactory.closeSession();
//		} catch (HibernateException e) {
//			this.result = "no";
//	        this.reason = "更新数据库时发生异常";
//			return "upload";
//		}finally{
			HibernateSessionFactory.closeSession();
//		}
        
        this.result = "yes";
        this.reason = "上传成功";
		return "upload";
	}
	
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getFileFileName() {
		return fileFileName;
	}


	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}


	public String getFileContentType() {
		return fileContentType;
	}
	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}


	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}


	public String getReason() {
		return reason;
	}


	public void setReason(String reason) {
		this.reason = reason;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}






	
	

}
