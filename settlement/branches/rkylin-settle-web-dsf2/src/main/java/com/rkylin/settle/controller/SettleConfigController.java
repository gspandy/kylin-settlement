package com.rkylin.settle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rkylin.settle.settleInterface.SettleWebInterface;

/***
 * 
 * @author CaoYang
 *
 */
@Controller
@RequestMapping("/config")
@Scope("prototype")
public class SettleConfigController extends BaseController {
	@Autowired
	private SettleWebInterface settleWebInterface;
	/***
	 * 打开配置页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/open_refresh_config")
	public String openConfig() {
		return "/settlement/config/refresh_config";
	}
	/***
	 * 刷新分润规则
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/refresh_profit_rule")
	public void refreshProfitRule() throws Exception {
		logger.info(">>> >>> >>> 刷新分润规则 start");
		settleWebInterface.refreshProfitKeyTask();
		logger.info("<<< <<< <<< 刷新分润规则 end");
		writeText("更新成功!");
	}
	/***
	 * 刷新订单号对应关系规则
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/refresh_order_no")
	public void refreshOrderNo() throws Exception {
		logger.info(">>> >>> >>> 刷新订单号对应关系规则 start");
		settleWebInterface.refreshFuncCodeAndOrderNoRelationTask();
		logger.info("<<< <<< <<< 刷新订单号对应关系规则 end");
		writeText("更新成功!");
	}
	/***
	 * 刷新金额对应关系规则
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/refresh_amount")
	public void refreshAmount() throws Exception {
		logger.info(">>> >>> >>> 刷新金额对应关系规则 start");
		settleWebInterface.refreshFuncCodeAndAmountRelationTask();
		logger.info("<<< <<< <<< 刷新金额对应关系规则 end");
		writeText("更新成功!");
	}
	/***
	 * 刷新功能编码对应关系规则
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/refresh_func_code")
	public void refreshFuncCode() throws Exception {
		logger.info(">>> >>> >>> 刷新功能编码对应关系规则 start");
		settleWebInterface.refreshDealProductCodeTofuncCode();
		logger.info("<<< <<< <<< 刷新功能编码对应关系规则 end");
		writeText("更新成功!");
	}
	/***
	 * 刷新功能编码对应关系规则
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/refresh_multigatechannelhome_2_paychannelid")
	public void refresh_multigatechannelhome_2_paychannelid() throws Exception {
		logger.info(">>> >>> >>> 渠道名称（channelHome & payChannelId）和渠道编码对应关系 start");
		settleWebInterface.refreshMultiGateChannelHome2PayChannelId();
		logger.info("<<< <<< <<< 渠道名称（channelHome & payChannelId）和渠道编码对应关系 end");
		writeText("更新成功!");
	}
	/***
	 * 'func_code'与deal_product_code的对应关系
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("refresh_deal_product_code_to_funccode")
	public void refresh_deal_product_code_to_funccode() throws Exception {
		logger.info(">>> >>> >>> 刷新'func_code'与deal_product_code的对应关系 start");
		settleWebInterface.refreshDealProductCodeTofuncCode();
		logger.info("<<< <<< <<< 刷新'func_code'与deal_product_code的对应关系  end");
		writeText("更新成功!");
	}
}
