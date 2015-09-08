package test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.mysql.jdbc.log.Log;

public class MainClass {
	
	public static void main(String[] argus){
		try {
//            String requestUrl = "http://127.0.0.1:8080/REPServer/tea/uploadAction!upload";
//            String requestUrl = "http://115.28.217.42/REPServer2.4_test/tea/uploadAction!upload";
            String requestUrl = "http://115.28.217.42/REP/tea/uploadAction!upload";
            //请求普通信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("id", "3");
//            params.put("pwd", "zhangsan");
//            params.put("age", "21");
//            params.put("fileName", "文件名啊啊啊啊啊啊啊");
            //上传文件
            File file = new File("/Users/chaibozhou/11003231.docx");
            FormFile formfile = new FormFile(file.getName(), file, "file", "application/octet-stream");
            
            System.out.println(SocketHttpRequester.post(requestUrl, params, formfile));
        } catch (Exception e) {
            e.printStackTrace();
            return ;
        }
		System.out.println("上传完成");
	}
}
