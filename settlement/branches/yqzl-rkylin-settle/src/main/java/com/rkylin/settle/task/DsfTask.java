package com.rkylin.settle.task;

import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.settle.service.DsfService;

public class DsfTask {
	
	@Autowired
	private DsfService dsfService;
	/***
	 * 汇总代付定时任务
	 * 每天7点执行一次,汇总代付和提现的交易
	 * rootInstCds 机构号可能多个，用逗号分隔
	 */
	public void dealDf(){
		//汇总T-1日的交易
		dsfService.dealDsf(null,"0",new String[]{"M000001"},null,null,null);
	}
	
	/***
	 * 汇总课栈T日的提现交易 
	 * 课栈的特殊需求0~7点会生成一批贷款，要求当天到账，便于计息
	 */
	public void kzTxSummary(){
		dsfService.dealDsf(null,"2",new String[]{"M000004"},0,new String[]{"4016"},new String[]{"1"});
	}
	
	/***
	 * 汇总代收付定时任务
	 * 每天16:00执行一次,汇总代收和T0的标记的代付
	 */
	public void dealDs(){
		dsfService.dealDsf(null,"1",new String[]{"M000001"},null,new String[]{"4013"},null);
	}
	
	/***
	 * 还未确定，临时设置成16:00
	 * 每天16:00执行一次,汇总一分钱T0和T0之前10个账期内的数据
	 */
	public void dealYFQ(){
		dsfService.dealDsf(null,"1",new String[]{"M000001"},null,new String[]{"4014_1"},null);
		dsfService.dealDsf(null,"1",new String[]{"M000001"},null,new String[]{"4014"},new String[]{"YFYZ"});
	}
	
	/***
	 * 发送代收付交易给代收付系统定时任务
	 * 每天执行一次
	 */
	public void sendDsf(){
		dsfService.sendDsf(null,new String[]{"M000001"},null,null);
	}
	
	/***
	 * 对于代收付返回的由于银行系统忙等导致的失败单，定时任务重发
	 */
	public void sendDsfAgain(){
		dsfService.sendDsfAgain(null);
	}
	
	/***
	 * 将失败单发送给订单系统
	 */
	public void sendFailToAccount(){
		dsfService.returnAccount(null,new Integer[]{4,6,40,60});
	}
	
	/***
	 * 汇总代付定时任务
	 * 每天11点执行一次,汇总丰年T0代付 0:00~11:00之间的数据
	 * rootInstCds 机构号可能多个，用逗号分隔
	 */
	public void fnT0DfSummary(){
		//汇总T日的交易
		dsfService.dealDsf(null,"0",new String[]{"M000001"},0,new String[]{"4014","4014_1"},null);
	}
	
	/***
	 * 银企直连汇总定时任务
	 * rootInstCds 机构号可能多个，用逗号分隔，暂时限制只有丰年
	 */
	public void yqzlDfSummary(){
		//汇总T日的交易
		dsfService.dealDsf(null,"1",new String[]{"M000001"},null,new String[]{"40143","40144"},null);
	}

	/***
	 * 汇总代收付定时任务
	 * 每天11:00,17:00执行,汇总代收和T0的标记的代付
	 */
	public void dealFile(){
		dsfService.dealDsf(null,"5",new String[]{"M000029"},0,new String[]{"40147"},null);
		dsfService.sendDsf(null,new String[]{"M000029"},new Integer[]{8},null);
	}
}
