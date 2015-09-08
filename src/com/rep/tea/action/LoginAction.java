package com.rep.tea.action;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.rep.core.Tools;
import com.rep.tea.entity.SMSEntity;
import com.rep.tea.entity.TeacherEntity;
import com.rep.tea.service.LogService;
import com.rep.tea.service.LoginService;

public class LoginAction extends ActionSupport implements ApplicationAware {
	private String username;
	private String password;
	private String tea_id;
	private String tea_name;
	private String type;
	private String result = "yes";
	private String reason;
	private String access_token;
	private String phone;
	private String authcode;
	private List<SMSEntity> list;
	private String id;
	private LoginService service = new LoginService();
	private Map<String,Object> session;
	private Map<String,Object> application;
	//验证码图片
	private ByteArrayInputStream imageStream;
	private String checkCode;
	
	/**
	 * 判断用户名username是否已经被注册
	 */
	public String isLogin(){
		System.out.println("被执行");
		if(username==null || "".equals(username)){
			this.result = "no";
			this.reason = "用户名不得为空！";
			return "signin";
		}
		
		//判断用户名是否被注册
		if(!service.isLogin(username)){
			this.result = "no";
			this.reason = "用户名已经被注册！";
			return "signin";
		}
		
		return "isLogin";
	}
	
	/**
	 * 登陆
	 */
	public String signin(){
		if(username==null || "".equals(username) || password==null || "".equals(password)){
			this.result = "no";
			this.reason = "用户名或密码不得为空！";
			return "signin";
		}
		
		//如果该IP访问超过了5次，则需要输入验证码
		////////////////////////
		String ip = ServletActionContext.getRequest().getRemoteAddr();
		System.out.println("IP:"+ip);
		int count = 0;
		if(session.get(ip)==null)
			++count;
		else
			count = (int) session.get(ip) + 1;
		session.put(ip, count);
		
		System.out.println("当前IP:"+ip+"登录次数："+session.get(ip));
		//若该IP已经失败登录了5次，则判断提交的验证码是否正确
		if(count>5){
			//判断验证码是否为空
			if(checkCode==null || "".equals(checkCode)){
				this.result = "no";
				this.reason = "checkCode不能为空";
				return "signin";
			}
			//判断验证码是否正确
			System.out.println("用户输入的code："+this.checkCode+",服务器中的code："+session.get(ip+"checkCode")+"");
			if(!this.checkCode.equals(session.get(ip+"checkCode")+"")){
				this.result = "no";
				this.reason = "验证码不正确";
				return "signin";
			}
		}
		////////////////////////
		
		//用户鉴权
		TeacherEntity teaEntity = new TeacherEntity();
		teaEntity.setUsername(username);
		teaEntity.setPassword(password);
		
		teaEntity = service.signin(teaEntity);
		
		//登陆失败
		if("no".equals(teaEntity.getResult())){
			this.result = "no";
			this.reason = teaEntity.getReason();
		}else{//登陆成功
			//返回数据
			this.tea_id = teaEntity.getId()+"";
			this.tea_name = teaEntity.getUsername();
			this.type = teaEntity.getType()+"";
			//添加登陆日志
			LogService service = new LogService();
			service.addTeaLoginLog(teaEntity.getId(),teaEntity.getUsername(),teaEntity.getType());
			//去掉session中该IP登录失败的次数 和 验证码
			session.remove(ip+"checkCode");
			session.remove(ip);
		}
		
		return "signin";
	}
	
	/**
	 * 注册
	 */
	public String login(){
		if(username==null || "".equals(username) || password==null || "".equals(password)
				|| access_token==null || "".equals(access_token)
				|| authcode==null || "".equals(authcode)
				|| phone==null || "".equals(phone)){
			this.result = "no";
			this.reason = "用户名、密码、access_token、authcode、phone不得为空！";
			return "login";
		}
		
		
		//判断验证码是否正确
		System.out.println("access_token:"+access_token);
		System.out.println(session.size());
		Set<String> sets = session.keySet();
		for(String str : sets){
			System.out.println("key:"+str);
		}
		Collection<Object> values = session.values();
		for(Object o : values){
			System.out.println("value:"+o+"");
		}
		String code = (String) session.get(access_token);
		System.out.println("服务器中的code："+code);
		if(!authcode.equals(code)){
			this.result = "no";
			this.reason = "验证码不正确";
			return "login";
		}
		//验证码正确，把验证码从session中remove掉
		session.remove(access_token);
		
		//进行注册
		TeacherEntity teaEntity = new TeacherEntity();
		teaEntity.setUsername(username);
		teaEntity.setPassword(password);
		teaEntity.setPhone(phone);
		
		teaEntity = service.login(teaEntity);
		
		if("no".equals(teaEntity.getResult())){
			this.result = "no";
			this.reason = teaEntity.getReason();
		}else{
			this.tea_id = teaEntity.getId()+"";
			this.tea_name = teaEntity.getUsername();
			this.type = teaEntity.getType()+"";
		}
		
		return "login";
	}
	
	
	/**
	 * 获取验证码，并向手机发送
	 */
	public String getAuthCode(){
		if(phone==null || "".equals(phone)){
			this.result = "no";
			this.reason = "phone不能为空！";
			return "getAuthCode";
		}
		
		//生成 验证码 ＋ access_token
		String code = Tools.getFixLenthString(4);
		this.access_token = Tools.getTenLengthRandom();
		System.out.println("验证码："+code);
		System.out.println("access_token："+access_token);
	    
	    //将 验证码 ＋ access_token存入session
	    session.put(access_token, code);
	    
	    /** ----------------------------------*/
	    //向该手机发送验证码
	    boolean result = service.sendSMS(phone,code);
	    if(!result){
	    	this.result = "no";
	    	this.reason = "验证码发送失败，请重试!";
	    	return "getAuthCode";
	    }
	    /** ----------------------------------*/
	    
	    /**----------*/
		Set<String> sets = session.keySet();
		for(String set : sets)
			System.out.println("key1:"+set);
		
		Collection<Object> values = session.values();
		for(Object o : values)
			System.out.println("value1:"+o);
		/**----------*/
	    
	    this.result = "yes";
	    this.reason = "验证码发送成功";
		return "getAuthCode";
	}
	
	
	/**
	 * 获取所有未发送的验证码(Android客户端使用)
	 */
	public String getAllUnSendAuthCode(){
		list = service.getAllUnSendAuthCode();
		
		if(list==null){
			this.result = "no";
			this.reason = "获取失败，请重试！";
			return "getAllUnSendAuthCode";
		}
		
		this.result = "yes";
		this.reason = "获取成功！";
		return "getAllUnSendAuthCode";
	}
	
	
	/**
	 * 将某一条验证码设为已发送(Android客户端使用)
	 */
	public String setHasSend(){
		if(this.id==null || "".equals(id)){
			this.result = "no";
			this.reason = "id不能为空！";
			return "setHasSend";
		}
		
		boolean result = service.setHasSend(this.id);
		if(!result){
			this.result = "no";
			this.reason = "服务器发生异常，请重试";
			return "setHasSend";
		}
		
		this.result = "yes";
		this.reason = "设置成功！";
		return "setHasSend";
	}
	
	/**
	 * 找回密码－获取验证码(废弃) 
	 */
	public String getPass(){
		if(phone==null || "".equals(phone)  || tea_id==null || "".equals(tea_id)){
			this.result = "no";
			this.reason = "phone、tea_id不能为空！";
			return "getPass";
		}
		
		//判断该用户tea_id和phone是否匹配
		if(!service.matchPhone(tea_id,phone)){
			this.result = "no";
			this.reason = "该号码不是注册时填写的号码！";
			return "getPass";
		}
		
		//生成验证码
		String code = Tools.getFixLenthString(4);
		System.out.println(code);
		//发送验证码
		
		//将验证码存入session
		session.put(tea_id, code);
		
		this.result = "yes";
		this.reason = "验证码发送成功！";
		return "getPass";
	}
	
	/**
	 * 用户提交验证码
	 */
	public String verfiyCode(){
		if(authcode==null || "".equals(authcode) || access_token==null || "".equals(access_token)){
			this.result = "no";
			this.reason = "authcode、access_token不能为空";
			return "verfiyCode";
		}
		
		
		/**----------*/
		Set<String> sets = session.keySet();
		System.out.println("session大小："+sets.size());
		for(String set : sets)
			System.out.println("key:"+set);
		
		Collection<Object> values = session.values();
		for(Object o : values)
			System.out.println("value:"+o);
		/**----------*/
		
		
		//根据access_token获取session中的code
		String code = (String) session.get(access_token);
		//判断code与authcode是否一致
		if(!authcode.equals(code)){
			this.result = "no";
			this.reason = "验证码不正确";
			return "verfiyCode";
		}
		
		session.remove(access_token);
		this.result = "yes";
		this.reason = "验证成功";
		return "verfiyCode";
	}
	
	/**
	 * 修改密码
	 */
	public String modifyPass(){
		if(password==null || "".equals(password) || phone==null || "".equals(phone)){
			this.result = "no";
			this.reason = "password、phone不能为空";
			return "modifyPass";
		}
		
		//修改密码
		boolean result = service.modifyPass(password,phone);
		if(!result){
			this.result = "no";
			this.reason = "服务器发生异常，请重试!";
			return "modifyPass";
		}
		return "modifyPass";
	}
	
	/**
	 * 获取验证码图片
	 */
	private static int WIDTH = 60;
    private static int HEIGHT = 20;
	public String getCheckCodeImage() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
        // 设置浏览器不要缓存此图片
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        String rands = createRandom();
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT,BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // 产生图像
        drawBackground(g);
        drawRands(g, rands);
        // 结束图像的绘制过程，完成图像
        g.dispose();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", outputStream);
        ByteArrayInputStream input = new ByteArrayInputStream(outputStream.toByteArray());
        this.setImageStream(input);

//        HttpSession session = ServletActionContext.getRequest().getSession();
        System.out.println("验证码："+rands);
        String ip = ServletActionContext.getRequest().getRemoteAddr();
        session.put(ip+"checkCode",rands);
        
        input.close();
        outputStream.close();
        
		return "getCheckCodeImage";
	}
    private static String createRandom()
    {
        String str = "0123456789qwertyuiopasdfghjklzxcvbnm";

        char[] rands = new char[4];

        Random random = new Random();

        for (int i = 0; i < 4; i++)
        {
            rands[i] = str.charAt(random.nextInt(36));
        }

        return new String(rands);
    }

    private void drawBackground(Graphics g)
    {
        // 画背景
        g.setColor(new Color(0xDCDCDC));

        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 随机产生 120 个干扰点

        for (int i = 0; i < 120; i++)
        {
            int x = (int) (Math.random() * WIDTH);

            int y = (int) (Math.random() * HEIGHT);

            int red = (int) (Math.random() * 255);

            int green = (int) (Math.random() * 255);

            int blue = (int) (Math.random() * 255);

            g.setColor(new Color(red, green, blue));

            g.drawOval(x, y, 1, 0);
        }
    }

    private void drawRands(Graphics g, String rands)
    {
        g.setColor(Color.BLACK);

        g.setFont(new Font(null, Font.ITALIC | Font.BOLD, 18));

        // 在不同的高度上输出验证码的每个字符

        g.drawString("" + rands.charAt(0), 1, 17);

        g.drawString("" + rands.charAt(1), 16, 15);

        g.drawString("" + rands.charAt(2), 31, 18);

        g.drawString("" + rands.charAt(3), 46, 16);

        System.out.println(rands);

    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getTea_id() {
		return tea_id;
	}

	public void setTea_id(String tea_id) {
		this.tea_id = tea_id;
	}

	public String getTea_name() {
		return tea_name;
	}

	public void setTea_name(String tea_name) {
		this.tea_name = tea_name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Map<String, Object> getApplication() {
		return session;
	}
	public void setApplication(Map<String, Object> session) {
		this.session = session;
	}

	public String getAuthcode() {
		return authcode;
	}

	public void setAuthcode(String authcode) {
		this.authcode = authcode;
	}

	public ByteArrayInputStream getImageStream() {
		return imageStream;
	}

	public void setImageStream(ByteArrayInputStream imageStream) {
		this.imageStream = imageStream;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public List<SMSEntity> getList() {
		return list;
	}

	public void setList(List<SMSEntity> list) {
		this.list = list;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
