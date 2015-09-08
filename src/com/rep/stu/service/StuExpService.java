package com.rep.stu.service;

import java.util.List;

import com.rep.stu.dao.StuExpDao;
import com.rep.stu.entity.StateEntity;
import com.rep.stu.entity.StuExpEntity;

public class StuExpService {
	private StuExpDao dao = new StuExpDao();
	
	/**
	 * 开始实验
	 * @param exp_name
	 * @param exp_id
	 * @param stu_name
	 * @param stu_id
	 * @return
	 */
	public StateEntity startExp(String exp_name, String exp_id, String stu_name,String stu_id) {
		//连通性测试＋更新实验箱状态表
		StateEntity stateEntity  = dao.connectTest(exp_name,exp_id,stu_name,stu_id);
		
		//更新学生实验表
		if(stateEntity!=null){
			if(dao.updatestuExp(exp_name,exp_id,stu_name,stu_id))
				return stateEntity;
			else
				return null;
		}
		
		//没有找到连通且空闲的箱子
		return null;
	}

	
	/**
	 * 结束实验
	 * @param exp_name
	 * @param exp_id
	 * @param stu_name
	 * @param stu_id
	 * @return
	 */
	public boolean endExp(String exp_name, String exp_id, String stu_name,String stu_id,String ip) {
		//更新表state，将busy设为空闲，并将学生、实验的信息删掉
		boolean result = dao.updateState(ip);
		
		//更新表stu_exp
		if(result)
			return dao.endExp(exp_name,exp_id,stu_name,stu_id);
		
		return false;
	}


	
	/**
	 * 获取当前登录学生所做过的所有实验
	 * @param stu_id
	 * @return
	 */
	public List<StuExpEntity> getMyExp(String stu_id) {
		return dao.getMyExp(stu_id);
	}

}
