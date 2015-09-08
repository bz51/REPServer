package com.rep.tea.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.rep.core.Config;
import com.rep.stu.entity.StateEntity;
import com.rep.stu.entity.StuExpEntity;
import com.rep.tea.entity.TeaExpEntity;
import com.rep.tea.service.TeaExpService;

public class TeaExpAction extends ActionSupport implements SessionAware{
	private String result = "yes";
	private String reason;
	private String keyword;
	private String name;
	private String content;
	private String tea_name;
	private String tea_id;
	private String user_id;
	private String curInfo;
	private String exp_id;
	private String ip;
	private String baud;
	private String com;
	private String data;
	private String stop;
	private String check;
	private Map<String,Object> session;
	private TeaExpService service = new TeaExpService();
	private List<TeaExpEntity> teaExpList = new ArrayList<TeaExpEntity>();
	private List<StuExpEntity> stuExpList = new ArrayList<StuExpEntity>();
	private List<StateEntity> stateList = new ArrayList<StateEntity>();
	private String lineSeparator = System.getProperty("line.separator", "\n");
	private String finish;
	private String unfinish;
	private String start_month;
	private String end_month;
	private List<String> countList;
	private String count;
	
	/**
	 * 查询所有未删除实验
	 * @return
	 */
	public String getExpTea(){
		//开始查询
		teaExpList = service.getExpTea();
		
		//查询失败
		if(teaExpList==null){
			this.result = "no";
			this.reason = "服务器发生异常，查询失败！";
			return "getExpTea";
		}
		
		//查询成功
		return "getExpTea";
	}
	
	/**
	 * 按关键字搜索实验
	 * @return
	 */
	public String serchExpTea(){
		if(keyword==null || "".equals(keyword)){
			this.result = "no";
			this.reason = "keyword不能为空！";
			return "serchExpTea";
		}
		
		//开始搜索
		teaExpList = service.serchExpTea(keyword);

		//搜索失败
		if(teaExpList==null){
			this.result = "no";
			this.reason = "服务器发生异常，搜索失败！";
			return "serchExpTea";
		}
		
		return "serchExpTea";
	}
	
	/**
	 * 按学生姓名关键字搜索学生实验信息
	 * @return
	 */
	public String serchExpStu(){
//		if(keyword==null || "".equals(keyword)){
//			this.result = "no";
//			this.reason = "keyword不能为空！";
//			return "serchExpStu";
//		}
		
		//开始搜索
		stuExpList = service.serchExpStu(keyword);

		//搜索失败
		if(stuExpList==null){
			this.result = "no";
			this.reason = "服务器发生异常，搜索失败！";
			return "serchExpStu";
		}
		return "serchExpStu";
	}
	
	/**
	 * 创建实验
	 * @return
	 */
	public String createExpTea(){
		if(name==null || "".equals(name) || content==null || "".equals(content) ||
				tea_name==null || "".equals(tea_name) || tea_id==null || "".equals(tea_id)){
			this.result = "no";
			this.reason = "name、content、tea_id、tea_name均不能为空！";
			return "createExpTea";
		}
		
		//创建失败
		if(!service.createExpTea(name,content,tea_id,tea_name)){
			this.result = "no";
			this.reason = "服务器发生异常，实验创建失败！";
			return "createExpTea";
		}
		
		//创建成功
		return "createExpTea";
	}
	
	
	/**
	 * 获取实验箱们的状态
	 * @return
	 */
	public String getState(){
		stateList = service.getState();
		if(stateList==null){
			this.result = "no";
			this.reason = "获取失败，请重试！";
		}
		return "getState";
	}
	
	/**
	 * 获取实时监控信息
	 * @return
	 * @throws IOException 
	 */
	public String getCurInfo() throws IOException{
		System.out.println("当前时间："+new Date());
		String result = service.getCurInfo(session);
		if(result!=null){
			System.out.println("result："+result);
//			this.curInfo = result;
			this.content = result;
		}else{
			this.result = "no";
			this.reason = "没有更新！";
		}
		
		return "getCurInfo";
	}
	
	/**
	 * 获取实验箱状态信息
	 * @return
	 * @throws IOException 
	 */
	public String getBoxStateInfo() throws IOException{
		System.out.println("当前时间："+new Date());
		String result = service.getBoxStateInfo(session);
		if(result!=null){
			System.out.println("result："+result);
//			this.curInfo = result;
			this.content = result;
		}else{
			this.result = "no";
			this.reason = "没有更新！";
		}
		
		return "getBoxStateInfo";
	}
	
	
	/**
	 * 查看某一个实验的学生完成情况
	 * @return
	 */
	public String getExpTeaByExpId(){
		if(exp_id==null || "".equals(exp_id)){
			this.result = "no";
			this.reason = "exp_id不能为空！";
			return "getExpTeaByExpId";
		}
		
		this.stuExpList = service.getExpTeaByExpId(exp_id);
		
		if(stuExpList==null){
			this.result = "no";
			this.reason = "服务器发生异常！请重试";
			return "getExpTeaByExpId";
		}
		return "getExpTeaByExpId";
	}
	
	
	/**
	 * 根据实验箱的IP获取实验箱的实时命令
	 * @return
	 */
	public String getCmdByIP(){
		if(ip==null || "".equals(ip)){
			this.result = "no";
			this.reason = "IP不能位空！";
			return "getCmdByIP";
		}
		
		this.content = service.getCmdByIP(this.ip);
		if(content == null){
			this.result = "no";
			this.reason = "获取数据失败！请重试";
		}else{
			this.result = "yes";
		}
		
		return "getCmdByIP";
	}
	
	/**
	 * 设置串口通信参数
	 * @return
	 */
	public String setConfig(){
		if(this.baud==null || "".equals(baud) ||
				this.check==null || "".equals(check) ||
				this.com==null || "".equals(com) ||
				this.data==null || "".equals(data) ||
				this.stop==null || "".equals(stop)){
			this.result = "no";
			this.reason = "baud、check、com、stop、data均不能为空！";
			return "setConfig";
		}
		
		File file = new File(Config.PATH_CHUANKOU);
		if(!file.exists()){
			try {
				boolean r= file.createNewFile();
				System.out.println("文件创建是否成功："+r);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//文件存在就将参数写入文件
		try {
			BufferedWriter bufw = new BufferedWriter(new FileWriter(file));
			String fileContent = "[portName]"+lineSeparator+"name="+this.com+lineSeparator+lineSeparator+"[baudRate]"+lineSeparator+"rate="+this.baud+lineSeparator+lineSeparator+"[dataBits]"+lineSeparator+"data="+this.data+lineSeparator+lineSeparator+"[Parity]"+lineSeparator+"parity="+this.check+lineSeparator+lineSeparator+"[stopBits]"+lineSeparator+"stop="+this.stop;
//			String fileContent = "baud="+this.baud+lineSeparator+"check="+check+lineSeparator+"com="+com+lineSeparator+"data="+data+lineSeparator+"stop="+stop+lineSeparator;
			bufw.write(fileContent);
			bufw.flush();
			bufw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "setConfig";
	}
	
	
	/**
	 * 获取某一个实验的已完成人数、进行中人数
	 */
	public String getExpState(){
		if(exp_id==null || "".equals(exp_id)){
			this.result = "no";
			this.reason = "exp_id不能为空！";
			return "getExpState";
		}
		
		//根据exp_id查这个实验所有学生已完成的数量 和 未完成的数量
		Map<String,String> map = service.getExpState(exp_id);
		
		if(map==null){
			this.result = "no";
			this.reason = "服务器发生异常！";
			return "getExpState";
		}
		
		this.finish = map.get("finish");
		this.unfinish = map.get("unfinish");
		this.result = "yes";
		this.reason = "查询成功！";
		return "getExpState";
	}
	
	
	/**
	 * 获取实验箱的使用频率
	 */
	public String getBoxUseCount(){
		if(start_month==null || "".equals(start_month) ||end_month==null || "".equals(end_month)){
			this.result = "no";
			this.reason = "start_month、end_month不能为空！";
			return "getBoxUseCount";
		}
		if(Integer.parseInt(start_month)>Integer.parseInt(end_month)){
			this.result = "no";
			this.reason = "start_month不能大于end_month";
			return "getBoxUseCount";
		}
		if(Integer.parseInt(start_month)>12 || Integer.parseInt(start_month)<1 || Integer.parseInt(end_month)>12 || Integer.parseInt(end_month)<1){
			this.result = "no";
			this.reason = "月份必须在1-12之间";
			return "getBoxUseCount";
		}
		
		countList = service.getBoxUseCount(start_month,end_month);
		if(countList==null){
			this.result = "no";
			this.reason = "服务器发生异常，请重试";
			return "getBoxUseCount";
		}
		
		this.result = "yes";
		this.reason = "查询成功";
		return "getBoxUseCount";
	}
	
	
	/**
	 * 开始N个实验
	 */
	public String startNExp(){
		if(count==null || "".equals(count)){
			this.result = "no";
			this.reason = "count不能为空";
			return "startNExp";
		}
		
		if(Integer.parseInt(count)<=0){
			this.result = "no";
			this.reason = "count必须为自然数";
			return "startNExp";
		}
		
		//开启N个实验
		boolean result = service.startNExp(count);
		if(!result){
			this.result = "no";
			this.reason = "服务器发生异常，请重试";
			return "startNExp";
		}
		
		this.result = "yes";
		this.reason = "开启成功！";
		return "startNExp";
	}
	
	
	/**
	 * 结束N个实验
	 */
	public String endNExp(){
		
		//结束N个实验
		boolean result = service.endNExp();
		if(!result){
			this.result = "no";
			this.reason = "服务器发生异常，请重试";
			return "endNExp";
		}
		
		this.result = "yes";
		this.reason = "关闭成功！";
		return "endNExp";
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

	public List<TeaExpEntity> getTeaExpList() {
		return teaExpList;
	}

	public void setTeaExpList(List<TeaExpEntity> teaExpList) {
		this.teaExpList = teaExpList;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<StuExpEntity> getStuExpList() {
		return stuExpList;
	}

	public void setStuExpList(List<StuExpEntity> stuExpList) {
		this.stuExpList = stuExpList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTea_name() {
		return tea_name;
	}

	public void setTea_name(String tea_name) {
		this.tea_name = tea_name;
	}

	public String getTea_id() {
		return tea_id;
	}

	public void setTea_id(String tea_id) {
		this.tea_id = tea_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public List<StateEntity> getStateList() {
		return stateList;
	}

	public void setStateList(List<StateEntity> stateList) {
		this.stateList = stateList;
	}

	public void setCurInfo(String curInfo) {
		this.curInfo = curInfo;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getExp_id() {
		return exp_id;
	}

	public void setExp_id(String exp_id) {
		this.exp_id = exp_id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getBaud() {
		return baud;
	}

	public void setBaud(String baud) {
		this.baud = baud;
	}

	public String getCom() {
		return com;
	}

	public void setCom(String com) {
		this.com = com;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getStop() {
		return stop;
	}

	public void setStop(String stop) {
		this.stop = stop;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public String getFinish() {
		return finish;
	}

	public void setFinish(String finish) {
		this.finish = finish;
	}

	public String getUnfinish() {
		return unfinish;
	}

	public void setUnfinish(String unfinish) {
		this.unfinish = unfinish;
	}

	public String getStart_month() {
		return start_month;
	}

	public void setStart_month(String start_month) {
		this.start_month = start_month;
	}

	public String getEnd_month() {
		return end_month;
	}

	public void setEnd_month(String end_month) {
		this.end_month = end_month;
	}

	public List<String> getCountList() {
		return countList;
	}

	public void setCountList(List<String> countList) {
		this.countList = countList;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	
	
}
