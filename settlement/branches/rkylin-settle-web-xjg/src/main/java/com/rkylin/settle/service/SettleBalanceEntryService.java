package com.rkylin.settle.service;

import java.util.List;

import com.rkylin.settle.pojo.SettleBalanceEntry;
import com.rkylin.settle.pojo.SettleBalanceEntryQuery;
import com.rkylin.settle.pojo.SettleTransAccount;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.util.PagerModel;

/***
 * 交易结果信息业务逻辑
 * @author Yang
 *
 */
public interface SettleBalanceEntryService {
	/***
	 * 分页条件查询交易结果信息
	 * @param query
	 * @return
	 */
	public PagerModel<SettleBalanceEntry> query(SettleBalanceEntryQuery query);
	/***
	 * 查询唯一对账&分润结果byID
	 * @param query
	 * @return
	 */
	public SettleBalanceEntry findById(Long id);
	/***
	 * 查询ids数组中全部的信息
	 * @param query
	 * @return
	 */
	public List<SettleBalanceEntry> queryByIds(String idsStr);
	/***
	 * 修改交易结果信息
	 * @param query
	 * @return
	 */
	public Integer edit(SettleBalanceEntry settleBalanceEntry);
	/***
	 * 修改交易结果对账状态并且修改对应的detail信息状态和account表信息状态
	 * @param query
	 * @return
	 */
	public Integer edit(SettleBalanceEntry settleBalanceEntry, SettleTransDetail transDetail, SettleTransAccount transAccount);
	/***
	 * 修改交易结果对账状态并且修改对应的detail账户系统信息状态和detail多渠道系统表信息状态
	 * @param query
	 * @return
	 */
	public Integer edit(SettleBalanceEntry settleBalanceEntry, SettleTransDetail transDetail, SettleTransDetail transDetailMuti);
	/***
	 * 通过主键获取清算transDetail交易
	 * @param id
	 * @return
	 */
	public SettleTransDetail getTransDetailById(Long id);
	/***
	 * 通过主键获取清算transAccount交易
	 * @param id
	 * @return
	 */
	public SettleTransAccount getTransAccountById(Long id);
}
