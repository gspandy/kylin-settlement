package com.rkylin.settle.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.manager.SettleBalanceEntryManager;
import com.rkylin.settle.manager.SettleTransAccountManager;
import com.rkylin.settle.manager.SettleTransDetailManager;
import com.rkylin.settle.pojo.SettleBalanceEntry;
import com.rkylin.settle.pojo.SettleBalanceEntryQuery;
import com.rkylin.settle.pojo.SettleTransAccount;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.service.SettleBalanceEntryService;
import com.rkylin.settle.util.PagerModel;

/***
 * 交易结果信息业务逻辑
 * @author Yang
 *
 */
@Service("settleBalanceEntryService")
public class SettleBalanceEntryServiceImpl implements SettleBalanceEntryService {
	@Autowired
	private SettleBalanceEntryManager settleBalanceEntryManager;
	@Autowired
	private SettleTransAccountManager settleTransAccountManager;
	@Autowired
	private SettleTransDetailManager settleTransDetailManager;
	/***
	 * 分页条件查询交易结果信息
	 * @param query
	 * @return
	 */
	@Override
	public PagerModel<SettleBalanceEntry> query(SettleBalanceEntryQuery query) {
		//创建分页Model
		PagerModel<SettleBalanceEntry> pagerModel = new PagerModel<SettleBalanceEntry>();
		try {
			//条件查询 - 日期格式对象
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//日期格式 - 字符串
			String accountDateStr = query.getAccountDateStr();
			Date accountDate = null;
			//如果查询条件传入了日期,set日期对象
			if(accountDateStr != null && !accountDateStr.isEmpty()) {
				accountDate = sdf.parse(accountDateStr);
				query.setAccountDate(accountDate);
			}
			//初始化当前页
			if(query.getPageIndex()==null){
				query.setPageIndex(1);
			}
			//初始化显示条数
			if(query.getPageSize()==null){
				query.setPageSize(10);
			}
			//封装pageModel
			query.setOffset((query.getPageIndex() - 1) * query.getPageSize());
			pagerModel.setList(settleBalanceEntryManager.queryPage(query));
			pagerModel.setTotal(settleBalanceEntryManager.countByExample(query));
			pagerModel.setResult("ok");
		} catch (Exception e) {
			e.printStackTrace();
			pagerModel.setResult("error");
			pagerModel.setMsg("系统异常");
		}
		
		return pagerModel;
	}
	/***
	 * 查询唯一对账&分润结果byID
	 * @param query
	 * @return
	 */
	@Override
	public SettleBalanceEntry findById(Long id) {
		return settleBalanceEntryManager.findSettleBalanceEntryById(id);
	}
	/***
	 * 查询ids数组中全部的信息
	 * @param query
	 * @return
	 */
	@Override
	public List<SettleBalanceEntry> queryByIds(String idsStr) {
		String[] idsStrArr = idsStr.split(",");
		Long[] ids = stringArr2LongArr(idsStrArr);
		return settleBalanceEntryManager.queryByIds(ids);
	}
	/***
	 * 修改交易结果信息
	 * @param query
	 * @return
	 */
	@Override
	public Integer edit(SettleBalanceEntry settleBalanceEntry) {
		return settleBalanceEntryManager.updateSettleBalanceEntry(settleBalanceEntry);
	}
	/***
	 * 修改交易结果对账状态并且修改对应的detail信息状态和account表信息状态
	 * @param query
	 * @return
	 */
	@Override
	public Integer edit(SettleBalanceEntry settleBalanceEntry, SettleTransDetail settleTransDetail, SettleTransAccount settleTransAccount) {
		Integer entryStatusId = settleBalanceEntry.getStatusId();
		Integer statusId = null;
		Integer result = 1;
		Integer result01 = 1;
		Integer result02 = 1;
		switch(entryStatusId) {
			case 0 :
				statusId = 22;
				break;
			case 1 :
				statusId = 21;
				break;
			case 2 :
				statusId = 23;
				break;
			case 3 :
				statusId = 24;
				break;
			case 4 :
				statusId = 211;
				break;
		}
		if(settleBalanceEntry != null) {
			result = settleBalanceEntryManager.updateSettleBalanceEntry(settleBalanceEntry);
		}
		if(settleTransAccount != null) {
			settleTransAccount.setRemark("人工使用画面修改! STATUDS_ID 由 原:"+ settleTransAccount.getStatusId() +"改为 现:" + statusId);
			settleTransAccount.setStatusId(statusId);
			result01 = settleTransAccountManager.updateSettleTransAccount(settleTransAccount);
		}
		if(settleTransDetail != null) {
			settleTransDetail.setRemark("人工使用画面修改! STATUDS_ID 由 原:"+ settleTransDetail.getStatusId() +"改为 现:" + statusId);
			settleTransDetail.setStatusId(statusId);
			result02 = settleTransDetailManager.updateSettleTransDetail(settleTransDetail);
		}
		return result > 0 && result01 > 0 && result02 > 0 ? 1 : -1;
	}
	/***
	 * 修改交易结果对账状态并且修改对应的detail信息状态和account表信息状态
	 * @param query
	 * @return
	 */
	@Override
	public Integer edit(SettleBalanceEntry settleBalanceEntry, SettleTransDetail settleTransDetail, SettleTransDetail settleTransDetailMuti) {
		Integer entryStatusId = settleBalanceEntry.getStatusId();
		Integer statusId = null;
		Integer result = 1;
		Integer result01 = 1;
		Integer result02 = 1;
		switch(entryStatusId) {
			case 0 :
				statusId = 22;
				break;
			case 1 :
				statusId = 21;
				break;
			case 2 :
				statusId = 23;
				break;
			case 3 :
				statusId = 24;
				break;
			case 4 :
				statusId = 211;
				break;
		}
		if(settleBalanceEntry != null) {
			result = settleBalanceEntryManager.updateSettleBalanceEntry(settleBalanceEntry);
		}
		if(settleTransDetailMuti != null) {
			settleTransDetailMuti.setRemark(settleTransDetailMuti.getRemark() + " | RESERVE_1 由 原:"+ settleTransDetailMuti.getReserve1() +"改为 现:" + statusId);
			settleTransDetailMuti.setReserve1(String.valueOf(statusId));
			result01 = settleTransDetailManager.updateSettleTransDetail(settleTransDetailMuti);
		}
		if(settleTransDetail != null) {
			settleTransDetail.setRemark("人工使用画面修改! STATUDS_ID 由 原:"+ settleTransDetail.getStatusId() +"改为 现:" + statusId);
			settleTransDetail.setStatusId(statusId);
			result02 = settleTransDetailManager.updateSettleTransDetail(settleTransDetail);
		}
		return result > 0 && result01 > 0 && result02 > 0 ? 1 : -1;
	}
	/***
	 * 通过主键获取清算transDetail交易
	 * @param id
	 * @return
	 */
	@Override
	public SettleTransDetail getTransDetailById(Long id) {
		return settleTransDetailManager.findSettleTransDetailById(id);
	}
	/***
	 * 通过主键获取清算transAccount交易
	 * @param id
	 * @return
	 */
	@Override
	public SettleTransAccount getTransAccountById(Long id) {
		return settleTransAccountManager.findSettleTransAccountById(id);
	}
	/***
	 * 将字符串格式的ID数组转换成Long
	 * @param strArr
	 * @return
	 * @throws Exception
	 */
	private Long[] stringArr2LongArr(String[] strArr) {
		Long[] longArr = new Long[strArr.length];
		int index = 0;
		for(String str : strArr) {
			longArr[index ++] = Long.parseLong(str);
		}
		return longArr;
	}
}
