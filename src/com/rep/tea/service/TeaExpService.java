package com.rep.tea.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.rep.core.Config;
import com.rep.stu.entity.StateEntity;
import com.rep.stu.entity.StuExpEntity;
import com.rep.tea.dao.TeaExpDao;
import com.rep.tea.entity.TeaExpEntity;

public class TeaExpService {
	private TeaExpDao dao = new TeaExpDao();
	private LogService logService = new LogService();
	
	/**
	 * 查询所有未删除实验
	 * @return
	 */
	public List<TeaExpEntity> getExpTea() {
		return dao.getExpTea();
	}

	/**
	 * 按关键字搜索实验
	 * @return
	 */
	public List<TeaExpEntity> serchExpTea(String keyword) {
		return dao.serchExpTea(keyword);
	}

	
	/**
	 * 按学生姓名关键字搜索学生实验信息
	 * @param keyword
	 * @return
	 */
	public List<StuExpEntity> serchExpStu(String keyword) {
		return dao.serchExpStu(keyword);
	}

	
	/**
	 * 创建实验
	 * @param name
	 * @param content
	 * @param tea_id
	 * @param tea_name
	 * @return
	 */
	public boolean createExpTea(String name, String content, String tea_id,String tea_name) {
		//创建实验
		boolean result = dao.createExpTea(name,content,tea_id,tea_name);

		// 添加操作日志
		if (result) {
			logService.addTeaDoLog(tea_id, tea_name + "创建了实验" + name);
		}

		return result;

	}

	
	/**
	 * 获取实验箱们的状态
	 * @return
	 */
	public List<StateEntity> getState() {
		return dao.getState();
	}

	
	public String getCurInfo(Map<String, Object> session) throws IOException {
		// 查看文件mtime
		File file = new File(Config.PATH_WTMP);
//		File file = new File("/Users/chaibozhou/abc");

		if (!file.exists()) {
			file.createNewFile();
			System.out.println("文件不存在，创建文件……");
		}

		String lastTime = session.get("lastTime") + "";
		System.out.println("map中的上次修改时间：" + lastTime);
		System.out.println("文件实际上次修改时间：" + file.lastModified());

		if (!lastTime.equals(file.lastModified() + "")) {
			//-------------------------------------------------------------------------------------
//			session.put("lastTime", file.lastModified());
			//-------------------------------------------------------------------------------------
			BufferedReader bufr = new BufferedReader(new FileReader(file));
			StringBuffer sb = new StringBuffer();
			String line = "";
			while ((line = bufr.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		}

		return null;
	}
	
	
	public String getBoxStateInfo(Map<String, Object> session) throws IOException {
		// 查看文件mtime
		File file = new File(Config.PATH_MESSAGES);
//		File file = new File("/Users/chaibozhou/abc");
		
		if (!file.exists()) {
			file.createNewFile();
			System.out.println("文件不存在，创建文件……");
		}
		
		String lastTime = session.get("lastTime") + "";
		System.out.println("map中的上次修改时间：" + lastTime);
		System.out.println("文件实际上次修改时间：" + file.lastModified());
		
		if (!lastTime.equals(file.lastModified() + "")) {
			//-------------------------------------------------------------------------------------
//			session.put("lastTime", file.lastModified());
			//-------------------------------------------------------------------------------------
			BufferedReader bufr = new BufferedReader(new FileReader(file));
			StringBuffer sb = new StringBuffer();
			String line = "";
			while ((line = bufr.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		}
		
		return null;
	}

	
	/**
	 * 查看某一个实验的学生完成情况
	 * @param exp_id
	 * @return
	 */
	public List<StuExpEntity> getExpTeaByExpId(String exp_id) {
		return dao.getExpTeaByExpId(exp_id);
	}

	
	/**
	 * ［废弃］根据实验箱的IP获取实验箱的实时命令
	 * @param ip 实验箱的IP
	 * @return
	 */
	public String getCmdByIP_old(String ip) {
		//根据IP，拼凑出该实验箱命令的文件名
		String filename = Config.PATH_HISTORY + "/history_"+ip;
		//获取里面的数据
		File file = new File(filename);
		if(!file.exists())
			return null;
		
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader bufr = new BufferedReader(new FileReader(file));
			String line;
			while((line=bufr.readLine())!=null){
				sb.append(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return sb.toString();
	}
	/**
	 * ［现用］根据实验箱的IP获取实验箱的实时命令
	 * @param ip 实验箱的IP
	 * @return
	 */
	public String getCmdByIP(String ip) {
		//读取文件REPcmd.txt
		File file = new File(Config.PATH_CMD);
		
		//初始化BufferedReader
		BufferedReader bufr;
		try {
			bufr = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
		//开始读取文件
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			//一行行地读文件
			while((line=bufr.readLine())!=null){
				//若当前行中有"###"，则将当前行切分，并判断当前行的IP是否为当前所要求的IP
				if(line.contains("###")){
					String[] strs = line.split("###");
					//若当前行地IP是所要求的IP，则将当前行的命令记录下来
					if(strs[0].equals(ip)){
						sb.append(strs[1]+"#");
					}
				}
			}
			return sb.toString();
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	/**
	 * 获取某一个实验的已完成人数、进行中人数
	 * @param exp_id
	 * @return
	 */
	public Map<String, String> getExpState(String exp_id) {
		return dao.getExpState(exp_id);
	}

	
	/**
	 * 获取实验箱的使用频率
	 * @param start_month
	 * @param end_month
	 * @return
	 */
	public List<String> getBoxUseCount(String start_month, String end_month) {
		return dao.getBoxUseCount(start_month,end_month);
	}

	
	/**
	 * 开始N个实验
	 * @param count
	 * @return
	 */
	public boolean startNExp(String count) {
		boolean result = true;
		//在state表中创建N个箱子的记录，并将箱子的状态设置为忙碌,IP:192.168.2.xxx
		result = dao.startNExp_setState(count);
		
		//在stu_exp表中生成N条学生开始实验的记录，exp_id=2,exp_name=数据结构的实现exp_id=2,exp_name=数据结构的实现,stu_id=999,stu_name=tester
		if(result){
			result = dao.startNExp_setStuExp(count);
		}
		
		return result;
	}

	
	/**
	 * 结束N个实验
	 * @return
	 */
	public boolean endNExp() {
		boolean result = true;
		//在state表中删除stu_name是test的N个箱子的记录
		result = dao.endNExp_setState();
		
		//在stu_exp表中stu_name=rester的学生的实验记录修改为：finish=1
		if(result){
			result = dao.endNExp_setStuExp();
		}
		
		return result;
	}

	
	/**
	 * 获取实时监控信息
	 * @return
	 */
//	public String getCurInfo(long lastTime) {
//		return dao.getCurInfo(lastTime);
//	}

}
