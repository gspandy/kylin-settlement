package com.rkylin.settle.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.rkylin.settle.pojo.SettleBalanceEntry;
import com.rkylin.settle.pojo.SettleBalanceEntryQuery;
import com.rkylin.settle.pojo.SettleTransAccount;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.service.SettleBalanceEntryService;
import com.rkylin.settle.service.SettleTransAccountService;
import com.rkylin.settle.service.SettleTransDetailService;
import com.rkylin.settle.util.PagerModel;

/**
 * @Description: 交易结果管理
 * @author Yang
 * @Create Time: 2015-6-12下午12:59:48
 * @version V1.00
 */
@Controller
@RequestMapping("/entry")
@Scope("prototype")
public class SettleBalanceEntryController extends BaseController {
	//日志对象
	protected static Logger logger = LoggerFactory.getLogger(SettleBalanceEntryController.class);
	@Autowired
	private SettleBalanceEntryService settleBalanceEntryService;
	@Autowired
	private SettleTransAccountService settleTransAccountService;
	@Autowired
	private SettleTransDetailService settleTransDetailService;
	/***
	 * 分页查询
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/query_ajax")
	public void queryAjax(SettleBalanceEntryQuery query) throws Exception {
		try {
			PagerModel<SettleBalanceEntry> pagerModel = settleBalanceEntryService.query(query);
			returnJsonList(pagerModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/***
	 * 打开查询页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/entry_manager")
	public String openQuery() {
		return "/settlement/entry/query";
	}
	/***
	 * 打开修改页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/entry_open_edit")
	public ModelAndView openEdit(Long id) {
		ModelAndView modelAndView = new ModelAndView("/settlement/entry/edit");
		modelAndView.addObject("settleBalanceEntry", settleBalanceEntryService.findById(id));
		return modelAndView;
	}
	/***
	 * 查询详情信息返回json
	 * @param ids: detail表交易ID,account表交易ID
	 * @param entryId: balance_entry表交易ID
	 * @return
	 */
	@RequestMapping("/entry_open_title")
	public void openTitle(String ids, Long entryId) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String[] idsArr = ids.split(",");
		Long detailId = idsArr.length <= 0 || "".equals(idsArr[0]) || null == idsArr[0] ? -1l : Long.parseLong(idsArr[0]);
		Long accountId = idsArr.length <= 1 || "".equals(idsArr[1]) || null == idsArr[1] ? -1l : Long.parseLong(idsArr[1]);
		
		if(detailId != -1l) returnMap.put("settleTransDetailAmount", settleTransDetailService.findById(detailId).getAmount());
		if(accountId != -1l) returnMap.put("settleTransAccountAmount", settleTransAccountService.findById(accountId).getTransAmount());
		writeJson(returnMap);
	}
	/***
	 * 打开详情页面
	 * @param ids: detail表交易ID,account表交易ID
	 * @param entryId: balance_entry表交易ID
	 * @return
	 */
	@RequestMapping("/entry_open_view")
	public ModelAndView openView(String ids, Long entryId) {
		ModelAndView modelAndView = new ModelAndView("/settlement/entry/view");
		String[] idsArr = ids.split(",");
		Long detailId = idsArr.length <= 0 || "".equals(idsArr[0]) || null == idsArr[0] ? -1l : Long.parseLong(idsArr[0]);
		Long accountId = idsArr.length <= 1 || "".equals(idsArr[1]) || null == idsArr[1] ? -1l : Long.parseLong(idsArr[1]);
		if(detailId != -1l) modelAndView.addObject("settleTransDetail", settleTransDetailService.findById(detailId));
		if(accountId != -1l) modelAndView.addObject("settleTransAccount", settleTransAccountService.findById(accountId));
		modelAndView.addObject("settleBalanceEntry", settleBalanceEntryService.findById(entryId));
		return modelAndView;
	}
	/***
	 * 修改
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/edit_ajax")
	public void editAjax(SettleBalanceEntry settleBalanceEntry) {
		SettleTransDetail transDetail = null;
		SettleTransDetail transDetailMuti = null;
		SettleTransAccount transAccount = null;
		String msg = "";
		Integer resultNum = 0;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String detIdAndAccId = settleBalanceEntryService.findById(settleBalanceEntry.getBalanceEntryId().longValue()).getObligate2();		
		if(detIdAndAccId == null || detIdAndAccId.isEmpty()) {
			logger.info(">>> >>> 此条交易结果信息没有记录相应SETTLE_TRANS_DETAIL & SETTLE_TRANS_ACCOUNT的主键ID, 所以无法自动更新SETTLE_TRANS_DETAIL & SETTLE_TRANS_ACCOUNT状态, 请核对后手动操作!");
			msg = "无法[自动]更新SETTLE_TRANS_DETAIL & SETTLE_TRANS_ACCOUNT状态, 请核对后手动操作!";
		} else {
			Long detId = null;
			Long accId = null;
			if(settleBalanceEntry.getBalanceType() == 1) {//外部对账, 渠道对账
				if(detIdAndAccId.startsWith("#")) {
					logger.info(">>> >>> 查询[长款] BALANCE_ENTRY_ID:" + settleBalanceEntry.getBalanceEntryId() + "; 词条对账结果对应的SETTLE_TRANS_ACCOUNT交易... ...");
					accId = Long.parseLong(detIdAndAccId.substring(1));
					transAccount = settleBalanceEntryService.getTransAccountById(accId);
				} else if(detIdAndAccId.endsWith("#")) {
					logger.info(">>> >>> 查询[短款] BALANCE_ENTRY_ID:" + settleBalanceEntry.getBalanceEntryId() + "; 词条对账结果对应的SETTLE_TRANS_DETAIL交易... ...");
					detId = Long.parseLong(detIdAndAccId.substring(0, detIdAndAccId.length() - 1));
					transDetail = settleBalanceEntryService.getTransDetailById(detId);
				} else {
					logger.info(">>> >>> 查询[平||错 ] BALANCE_ENTRY_ID:" + settleBalanceEntry.getBalanceEntryId() + "; 词条对账结果对应的SETTLE_TRANS_DETAIL & SETTLE_TRANS_ACCOUNT交易... ...");
					detId = Long.parseLong(detIdAndAccId.substring(0, detIdAndAccId.indexOf("#")));
					accId = Long.parseLong(detIdAndAccId.substring(detIdAndAccId.indexOf("#") + 1));
					transAccount = settleBalanceEntryService.getTransAccountById(accId);
					transDetail = settleBalanceEntryService.getTransDetailById(detId);
				}
				resultNum = settleBalanceEntryService.edit(settleBalanceEntry, transDetail, transAccount);
			} else if(settleBalanceEntry.getBalanceType() == 2) {//内部对账, 系统间对账
				if(detIdAndAccId.startsWith("#")) {
					logger.info(">>> >>> 查询[长款] BALANCE_ENTRY_ID:" + settleBalanceEntry.getBalanceEntryId() + "; 词条对账结果对应【多渠道系统】交易... ...");
					accId = Long.parseLong(detIdAndAccId.substring(1));
					transDetailMuti = settleBalanceEntryService.getTransDetailById(accId);
				} else if(detIdAndAccId.endsWith("#")) {
					logger.info(">>> >>> 查询[短款] BALANCE_ENTRY_ID:" + settleBalanceEntry.getBalanceEntryId() + "; 词条对账结果对应【账户系统】交易... ...");
					detId = Long.parseLong(detIdAndAccId.substring(0, detIdAndAccId.length() - 1));
					transDetail = settleBalanceEntryService.getTransDetailById(detId);
				} else {
					logger.info(">>> >>> 查询[平||错 ] BALANCE_ENTRY_ID:" + settleBalanceEntry.getBalanceEntryId() + "; 词条对账结果对应的【账户系统】 &【多渠道系统】交易... ...");
					detId = Long.parseLong(detIdAndAccId.substring(0, detIdAndAccId.indexOf("#")));
					accId = Long.parseLong(detIdAndAccId.substring(detIdAndAccId.indexOf("#") + 1));
					transDetailMuti = settleBalanceEntryService.getTransDetailById(accId);
					transDetail = settleBalanceEntryService.getTransDetailById(detId);
				}
				resultNum = settleBalanceEntryService.edit(settleBalanceEntry, transDetail, transDetailMuti);
			}
		}
		
		Boolean isSuccess = false;
		
		if(resultNum > 0) {
			isSuccess = true;
			msg = "修改成功!" + msg;
		} else {
			isSuccess = false;
			msg = "修改失败!" + msg;
		}
		resultMap.put("isSuccess", isSuccess);
		resultMap.put("msg", msg);
		
		try {
			writeJson(resultMap);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
