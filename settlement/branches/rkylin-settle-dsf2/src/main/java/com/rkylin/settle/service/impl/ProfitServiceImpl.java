package com.rkylin.settle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.dao.StoredProcedureDao;
import com.rkylin.settle.logic.BillLogic;
import com.rkylin.settle.logic.CorrectLogic;
import com.rkylin.settle.logic.GetMTAegisTransOrderInfo;
import com.rkylin.settle.logic.ProfitLogic;
import com.rkylin.settle.service.ProfitService;
import com.rkylin.settle.util.LogicConstantUtil;
import com.rop.response.Response;
import com.rop.service.IAPIService;

@Service("profitService")
public class ProfitServiceImpl extends BasicServiceImpl implements ProfitService, IAPIService{
	//日志对象
	protected static Logger logger = LoggerFactory.getLogger(ProfitServiceImpl.class);
	@Autowired
	private ProfitLogic profitLogic;
	@Autowired
	private BillLogic billLogic;
	@Autowired
	private CorrectLogic correctLogic;
	@Autowired
	private LogicConstantUtil logicConstantUtil;
	@Autowired
	private StoredProcedureDao storedProcedureDao;
	@Autowired
	private GetMTAegisTransOrderInfo getMTAegisTransOrderInfo;
	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		return null;
	}
	/***
	 * 开始清分 ... ...
	 * 定时任务, 执行1次/2分钟
	 */
	@Override
	public Map<String, Object> doProfit() {
		logger.info(">>> >>> >>> >>> >>>　ProfitService 开始执行'分润' ... ...");
		Map<String, Object> resultMap = new HashMap<String, Object>();							//提示信息
		List<Map<String, Object>> billTransDetailList = new ArrayList<Map<String, Object>>();	//挂账交易列表
		List<Map<String, Object>> correctTransDetailList = new ArrayList<Map<String, Object>>();//冲正交易列表
		Integer[] doProfitIds = null;															//需实时清分的交易状态
		try {
			/**
			 * 获取需实时清分的交易状态
			 */
			doProfitIds = profitLogic.getProfitIds();
		} catch (Exception e) {
			logger.error("获取需实时清分的交易状态时异常:", e);
			super.editResultMap(resultMap, "-1", "异常: 获取需实时清分的交易状态!");
			e.printStackTrace();
		}
		List<Map<String, Object>> profitTransDetailList = profitLogic.getProfitTransDetail(doProfitIds);	//获取未处理交易List
		List<String> billFuncCodes = profitLogic.getFuncCodeFromParamInfo("BILL_FUNC_CODES");					//挂账功能编号List
		List<String> correctFuncCodes = profitLogic.getFuncCodeFromParamInfo("CORRECT_FUNC_CODES");				//冲正功能编号List
		Iterator<Map<String, Object>> profitTransDetailIter = profitTransDetailList.iterator();					//获取未处理交易迭代器
		/*
		 * 遍历未处理交易
		 * 遍历完成后, 挂账交易列表只存'挂账交易', 冲正交易列表只存'冲正交易'
		 * 未处理交易List, 只保留'分润和其他(非挂账&非冲正)交易'
		 */
		while(profitTransDetailIter.hasNext()) {
			//当前交易信息
			Map<String, Object> theDetail = profitTransDetailIter.next();
			//当前交易功能编号
			String funcCode = String.valueOf(theDetail.get("FUNC_CODE"));
			/*
			 * 对当前交易分类
			 * 分为'冲正','挂账'
			 * 存入相应List, 并从'未处理交易List'中移除当前交易
			 */
			if(correctFuncCodes.contains(funcCode)) {//冲正交易
				correctTransDetailList.add(theDetail);
				//从未处理交易List,移除当前交易
				profitTransDetailIter.remove();
				profitTransDetailList.remove(theDetail);
			} else if(billFuncCodes.contains(funcCode)) {//挂账交易
				billTransDetailList.add(theDetail);
				//从未处理交易List,移除当前交易
				profitTransDetailIter.remove();
				profitTransDetailList.remove(theDetail);
			}
		}
		try {
			/**
			 * 挂账操作
			 */
			if(billTransDetailList.size() > 0) billLogic.doBill(billTransDetailList);
		} catch (Exception e) {
			logger.error(">>> >>> Service 异常: '挂账'操作失败!", e);
			super.editResultMap(resultMap, "-1", "异常: '挂账'操作失败!");
			e.printStackTrace();
		}
		try {
			/**
			 * 分润操作
			 */
			if(profitTransDetailList.size() > 0) profitLogic.doProfit(profitTransDetailList);
		} catch (Exception e) {
			logger.error(">>> >>> Service 异常: '分润'操作失败!", e);
			super.editResultMap(resultMap, "-1", "异常: '分润'操作失败");
			e.printStackTrace();
		}
		try {
			/**
			 * 冲正操作
			 */
			if(correctTransDetailList.size() > 0) correctLogic.doCorrect(correctTransDetailList);
		} catch (Exception e) {
			logger.error(">>> >>> Service 异常: '冲正'操作失败!", e);
			super.editResultMap(resultMap, "-1", "异常: '冲正'操作失败!");
			e.printStackTrace();
		}
		logger.info(">>> >>> >>> >>> >>>　ProfitService 执行'分润'结束 ... ...");
		return resultMap;
	}
	/***
	 * 清分后结算
	 * 定时任务, 执行1次/1天
	 */
	@Override
	public Map<String, Object> doProfitBalance() {
		logger.info(">>> >>> >>> >>> >>>　ProfitService 开始执行'结算' ... ...");
		Map<String, Object> resultMap = new HashMap<String, Object>();	
		try {
			profitLogic.doProfigBalance();
		} catch (Exception e) {
			logger.error(">>> >>> Service 异常: '结算'操作失败!");
			super.editResultMap(resultMap, "-1", "异常: '结算'操作失败");
			e.printStackTrace();
		}
		logger.info(">>> >>> >>> >>> >>>　ProfitService 执行'结算'结束 ... ...");
		return resultMap;
	}
	/***
	 * 刷新分润规则
	 * 定时任务, 执行1次/1天
	 * 画面使用
	 */
	public void refreshProfitKey() {
		try {
			logicConstantUtil.refreshProfitKeyList();
		} catch (Exception e) {
			logger.error(">>> >>> Service 异常: '刷新分润规则'操作失败!");
			e.printStackTrace();
		}
	}
	/***
	 * 刷新'订单号'与存放位置的对应关系
	 * 定时任务, 待定
	 * 画面使用
	 */
	public boolean refreshFuncCodeAndOrderNoRelation() {
		try {
			logicConstantUtil.refreshFuncCodeAndOrderNoRelation();
		} catch (Exception e) {
			logger.error(">>> >>> Service 异常: 刷新'订单号'与存放位置的对应关系'操作失败'!");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/***
	 * 刷新'金额'与存放位置的对应关系
	 * 定时任务, 待定
	 * 画面使用
	 */
	public boolean refreshFuncCodeAndAmountRelation() {
		try {
			logicConstantUtil.refreshFuncCodeAndAmountRelation();
		} catch (Exception e) {
			logger.error(">>> >>> Service 异常: 刷新'金额'与存放位置的对应关系'操作失败!'");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/***
	 * 读取账户系统一期交易信息
	 * 定时任务, 执行1次/2分钟
	 * @return
	 */
	public void getAccountOldTransOrderInfos() throws Exception {
		/*
		 * 开始时间为2分15秒之前 
		 */
		Date beginDate = new Date();
		beginDate.setTime(beginDate.getTime() - (60 * 2 + 15) * 1000);
		/*
		 * 截止时间为 当前时间 
		 */
		Date endDate = new Date();
		/*
		 * 读取时间段内账户系统数据
		 */
		getMTAegisTransOrderInfo.getAccountOldTransOrderInfos(beginDate, endDate);
	}
	/***
	 * 读取账户系统二期交易信息
	 * 定时任务, 执行1次/2分钟
	 * @return
	 */
	public void getAccountTransOrderInfos() throws Exception {
		/*
		 * 开始时间为2分15秒之前 
		 */
		Date beginDate = new Date();
		beginDate.setTime(beginDate.getTime() - (60 * 2 + 15) * 1000);
		/*
		 * 截止时间为 当前时间 
		 */
		Date endDate = new Date();
		/*
		 * 读取时间段内账户系统数据
		 */
		getMTAegisTransOrderInfo.getAccountTransOrderInfos(beginDate, endDate);
	}
	/***
	 * 读取账户系统T-1日全部交易信息 二期
	 * 定时任务, 执行1次/天
	 * @return
	 */
	public void readAccDateBySP() throws Exception {
		logger.info(">>> >>> >>> START 读取账户系统T-1日全部交易信息【二期 存储过程】");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap = storedProcedureDao.proSyncActTrans2(resultMap);
		logger.info(">>> >>> 读取账户系统T-1日全部交易信息运行结果 :");
		logger.info(">>> >>> on_err_code :" + String.valueOf(resultMap.get("on_err_code")));
		logger.info(">>> >>> ov_err_text :" + String.valueOf(resultMap.get("ov_err_text")));
		logger.info("<<< <<< <<< END 读取账户系统T-1日全部交易信息【二期 存储过程】");
	}
	/***
	 * 读取账户系统T-1日全部交易信息 一期
	 * 定时任务, 执行1次/天
	 * @return
	 */
	public void readAccOldDateBySP() throws Exception {
		logger.info(">>> >>> >>> START 读取账户系统T-1日全部交易信息【一期 存储过程】");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap = storedProcedureDao.proSyncActTrans(resultMap);
		logger.info(">>> >>> 读取账户系统T-1日全部交易信息运行结果 :");
		logger.info(">>> >>> on_err_code :" + String.valueOf(resultMap.get("on_err_code")));
		logger.info(">>> >>> ov_err_text :" + String.valueOf(resultMap.get("ov_err_text")));
		logger.info("<<< <<< <<< END 读取账户系统T-1日全部交易信息【一期 存储过程】");
	}
	/***
	 * 刷新'func_code'与deal_product_code的对应关系
	 * 画面使用
	 */
	public boolean refreshDealProductCodeTofuncCode() {
		try {
			logicConstantUtil.refreshDealProductCodeTofuncCode();
		} catch (Exception e) {
			logger.error(">>> >>> Service 异常: 刷新'func_code'与deal_product_code的对应关系'操作失败'!");
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
