package com.rkylin.settle.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.logic.CollectLogic;
import com.rkylin.settle.manager.SettleTransDetailManager;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.service.CollectService;
/***
 * 清分系统通用汇总业务service
 * @author Yang
 */
@Component("collectService")
public class CollectServiceImpl implements CollectService {
	//日志对象
	protected static Logger logger = LoggerFactory.getLogger(CollectService.class);
	//代收付结果返回需要汇总的交易类型
	String[] collectFuncCodeArr = new String[]{"4014","4016"};
	@Autowired
	private CollectLogic collectLogic;
	@Autowired
	private SettleTransDetailManager settleTransDetailManager;
	/**
	 * 汇总
	 * @param collectType	汇总类型
	 * @return
	 * @throws Exception
	 */
	@Override
	public void doCollect(Integer collectType) throws Exception {
		collectLogic.doCollect(collectType);
	}
	/**
	 * 汇总
	 * @param collectType	汇总类型
	 * @param 交易类型			交易类型
	 * @return
	 * @throws Exception
	 */
	@Override
	public void doCollect(Integer collectType, String funcCode) throws Exception {
		collectLogic.doCollect(collectType, funcCode);
	}
	/**
	 * 汇总
	 * @param accountDate
	 * @param collectType	汇总类型
	 * @return
	 * @throws Exception
	 */
	@Override
	public void doCollect(Integer collectType, Date accountDate) throws Exception {
		collectLogic.doCollect(collectType, accountDate);
	}
	/**
	 * 从交易画面发起汇总
	 * @param accountDate	//账期
	 * @param collectType	//汇总类型
	 * @return
	 * @throws Exception
	 */
	@Override
	public void doCollect(Integer collectType, Integer[] idsArr) throws Exception {
		logger.info(">>> >>> >>> >>> 开始执行 从‘交易’画面发起汇总");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(idsArr == null || idsArr.length < 1) throw new Exception("交易信息ID数组长度为0, trans_detail_id数组长度为0");
		paramMap.put("idsArr", idsArr);
		paramMap.put("transType", collectType);
		List<SettleTransDetail> settleTransDetailList = settleTransDetailManager.selectByIdsArr(paramMap);
		if(settleTransDetailList == null || settleTransDetailList.size() < 1) {
			logger.error("<<< <<< <<< <<< <<< <<< <<< 结束执行 从‘交易’画面发起汇总 获取复核条件的交易信息的条数为0");
			return;
		}
		Iterator<SettleTransDetail> settleTransDetailIter = settleTransDetailList.iterator();
		SettleTransDetail settleTransDetail = null;
		while(settleTransDetailIter.hasNext()) {
			settleTransDetail = settleTransDetailIter.next();
			collectLogic.doCollect(collectType, settleTransDetail);
		}
		logger.info("<<< <<< <<< <<< 结束执行 从‘交易’画面发起汇总");
	}
	/**
	 * 从汇总画面发起汇总
	 * @param accountDate	//账期
	 * @param collectType	//汇总类型
	 * @return
	 * @throws Exception
	 */
	@Override
	public void doCollect(Integer collectType, Date accountDate, String merchantCode, String payChannelId, String funcCode) throws Exception {
		collectLogic.doCollect(collectType, accountDate, merchantCode, payChannelId, funcCode);
	}
	/** 汇总信息发送MQ
	 * @param ske
	 * @throws Exception
	 */
	@Override
	public void doCollectBySettleKernelEntry(Long id) throws Exception {
		collectLogic.doCollectBySettleKernelEntry(id);
	}
	/**
	 * 汇总前回写10014机构号渠道号
	 */
	@Override
	public void updatePayChannelIdBy4015Trans() throws Exception {
		settleTransDetailManager.updatePayChannelIdBy4015Trans();
	}
	/**
	 * 代收付结果返回后汇总
	 * @param funcCode	交易类型
	 * @return
	 * @throws Exception
	 */
//	@Override
//	public void doCollectAfterDsfReturn(final String funcCode) throws Exception {
//		logger.info(">>> >>> >>> 开始:会计记账汇总 代收付结果返回后 funcCode:" + funcCode);
//		if(!Arrays.asList(collectFuncCodeArr).contains(funcCode)) {
//	   		logger.info("<<< <<< <<< 结束:" + funcCode + "交易不参加代收付结果返回后汇总记账!");
//	   		return;
//	   	}
//		Thread t = new Thread(
//			new Runnable() {
//				public void run() {
//					try {
//						doCollect(SettleConstants.COLLECT_TYPE_2, funcCode);
//					} catch(Exception e) {
//						e.printStackTrace();
//						logger.info(">>> >>> >>> 异常 【"+ funcCode +"】计会计账 ： ", e);
//					}
//				}
//			}		
//		);  
//        t.start();
//        logger.info("<<< <<< <<< 结束:会计记账汇总 代收付结果返回后 funcCode:" + funcCode);
//	}
}
