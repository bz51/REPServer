package com.rep.tea.service;

import com.rep.tea.dao.BackupDao;

public class BackupService {
	private BackupDao dao = new BackupDao();
	
	
	/**
	 * 立即备份
	 * @param lists 
	 * @return
	 */
	public boolean backUpNow(String[] lists) {
		return dao.backUpNow(lists);
	}

}
