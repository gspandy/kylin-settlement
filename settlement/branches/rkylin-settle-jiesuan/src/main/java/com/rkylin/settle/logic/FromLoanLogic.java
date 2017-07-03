package com.rkylin.settle.logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.settle.constant.Constants;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.LoanSettleLoanDetailManager;
import com.rkylin.settle.manager.SettleLoanDetailManager;
import com.rkylin.settle.pojo.LoanSettleLoanDetail;
import com.rkylin.settle.pojo.LoanSettleLoanDetailQuery;
import com.rkylin.settle.pojo.SettleLoanDetail;
import com.rkylin.settle.pojo.SettleLoanDetailQuery;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransDetailQuery;
import com.rkylin.settle.pojo.TransDetailInfo;
import com.rkylin.settle.pojo.TransDetailInfoQuery;
import com.rkylin.settle.util.SettlementUtil;

@Component("fromLoanLogic")
public class FromLoanLogic extends BasicLogic {
	//日志对象
	protected static Logger logger = LoggerFactory.getLogger(FromLoanLogic.class);

	@Autowired
	private SettlementUtil settlementUtil;							//清算工具类
	@Autowired
	private LoanSettleLoanDetailManager orderSettleLoanDetailManager;
	@Autowired
	private SettleLoanDetailManager settleLoanDetailManager;
	/**
	 * 从贷款系统读取交易信息并存入'清算'DB
	 * @return 提示信息
	 */
	public Map<String, Object> getLoanDetailFromLoan() throws Exception {
		return this.getLoanDetailFromLoan(null);
	}
	/**
	 * 从贷款系统读取交易信息并存入'清算'DB
	 * @param accountDate
	 * @return 提示信息
	 */
	public Map<String, Object> getLoanDetailFromLoan(Date accountDate) throws Exception {
		logger.info(">>> >>> >>> >>> 从贷款系统读取交易信息并存入'清算'DB START <<< <<< <<< <<<");
		Map<String, Object> resultMap = new HashMap<String, Object>();							//提示信息
		List<LoanSettleLoanDetail> loanSettleLoanDetailList = new ArrayList<LoanSettleLoanDetail>();
		List<SettleLoanDetail> settleLoanDetailList = new ArrayList<SettleLoanDetail>();
		SimpleDateFormat formatYYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
		
		//验证账期
		if(accountDate == null) {//如果账期为null, 即是未传入账期
			//从DB中过去账期信息, 账期在DB中每日更新,获取T-1日账期  	
			accountDate = (Date) settlementUtil.getAccountDate("yyyyMMdd", 0, "Date");	
		}
		logger.info("操作账期为："+accountDate);
		
		LoanSettleLoanDetailQuery query = new LoanSettleLoanDetailQuery();
		query.setUpdatedTime(accountDate);
		//查询'贷款系统'交易信息
		try {
			loanSettleLoanDetailList = orderSettleLoanDetailManager.selectByTime(query);
			if(loanSettleLoanDetailList == null || loanSettleLoanDetailList.size() < 1) {
				String msg = "查询'贷款系统'交易信息  0条";
				logger.error(">>> " + msg);
				return super.editResultMap(resultMap, "0", msg);
			}
		} catch (Exception e) {
			String msg = "异常:查询'贷款系统'交易信息  异常";
			logger.error(">>> " + msg);
			e.printStackTrace();
			return super.editResultMap(resultMap, "-1", msg);
		}	
		
		logger.info(">>> >>> >>> 把'多渠道'交易信息封装成'清算'交易信息");
		// 当前日期
		String nwdate = formatYYYYMMDD.format(new Date());
		String repdate = "";
		String tardate = "";
		Calendar cloDate = Calendar.getInstance();
		
		cloDate.setTime(new Date());
		// 前月20日
		cloDate.setTime(new Date());
		cloDate.add(Calendar.MONTH, -1);
		cloDate.set(Calendar.DATE, 20);
		String qm20date = formatYYYYMMDD.format(cloDate.getTime());
		// 当月15日
		cloDate.add(Calendar.MONTH, 1);
		cloDate.set(Calendar.DATE, 15);
		String dm15date = formatYYYYMMDD.format(cloDate.getTime());
		// 当月20日
		cloDate.set(Calendar.DATE, 20);
		String dm20date = formatYYYYMMDD.format(cloDate.getTime());
		
		//遍历上游交易信息
		Iterator<LoanSettleLoanDetail> iter = loanSettleLoanDetailList.iterator();
		String settle_flg = "";
		String date_flg = "9";
		SettleLoanDetail settleLoanDetail = null;
		String onePaymenyFlg = "";
		String repaymentCnt = "";
		String repaymentResult = "";
		String overdueFlg = "";
		String prepaymentFlg = "";
		String dropClassFlg = "";
		String specialFlg1 = "";
		String specialFlg2 = "";
		String specialFlg3 = "";
		String indate = "";
		String shrepdate = "";
		int daysu = 0;
		while(iter.hasNext()) {
			//当前'贷款系统'交易信息
			LoanSettleLoanDetail loanLoanDetail = iter.next();
			settle_flg = "";
			date_flg = "9";
			repdate = "";
			indate = "";
			shrepdate = "";
			settleLoanDetail = new SettleLoanDetail();
			// 首付标识
			onePaymenyFlg = loanLoanDetail.getOnePaymenyFlg()==null ? "0":loanLoanDetail.getOnePaymenyFlg().toString();
			// 还款期数
			repaymentCnt = loanLoanDetail.getRepaymentCnt()==null ? "0":loanLoanDetail.getRepaymentCnt();
			// 还款结果
			repaymentResult = loanLoanDetail.getRepaymentResult()==null ? " ":loanLoanDetail.getRepaymentResult();
			// 逾期标识
			overdueFlg = loanLoanDetail.getOverdueFlg()==null ? "0":loanLoanDetail.getOverdueFlg();
			// 提前还款标识
			prepaymentFlg = loanLoanDetail.getPrepaymentFlg()==null ? "0":loanLoanDetail.getPrepaymentFlg();
			// 退课标识
			dropClassFlg = loanLoanDetail.getDropClassFlg()==null ? "0":loanLoanDetail.getDropClassFlg();
			// 特殊标识1
			specialFlg1 = "0";
			// 特殊标识2
			specialFlg2 = "0";
			// 特殊标识3
			specialFlg3 = "0";
			// 时间标识
			if (Constants.KZ_ID.equals(loanLoanDetail.getRootInstCd()) && "LOAN0".equals(loanLoanDetail.getProductId())) {
				if (loanLoanDetail.getRepaymentDate()==null || "".equals(loanLoanDetail.getRepaymentDate())) {
					repdate = "";
				} else {
					repdate = formatYYYYMMDD.format(loanLoanDetail.getRepaymentDate());
				}
				if (!"".equals(repdate)) {
					if (qm20date.compareTo(repdate) > 0) { // 还款日期 < 前月20日
						
					} else if (dm15date.compareTo(repdate) > 0) { // 当月14日 >= 还款日期>= 前月20日
						date_flg = "20`14";
					} else if (dm20date.compareTo(repdate) > 0) { // 当月19日 >= 还款日期>= 当月15日
						date_flg = "15`19";
					} else if (dm20date.compareTo(repdate) <= 0) { // 还款日期>= 当月20日
						date_flg = "20`14";
					} else {

					}
				}
				
				if (repaymentCnt.compareTo("1") > 0) {
					repaymentCnt = "2";
				}
			} else if (Constants.KZ_ID.equals(loanLoanDetail.getRootInstCd()) 
					&& ("LOAN1".equals(loanLoanDetail.getProductId()) ||"LOAN2".equals(loanLoanDetail.getProductId()))) {
				if (loanLoanDetail.getRepaymentDate()==null || "".equals(loanLoanDetail.getRepaymentDate())) {
					repdate = "";
				} else {
					repdate = formatYYYYMMDD.format(loanLoanDetail.getRepaymentDate());
				}
				if (loanLoanDetail.getSpecialFlg1()==null || "".equals(loanLoanDetail.getSpecialFlg1()) || "0".equals(loanLoanDetail.getSpecialFlg1())) {
					indate = "";// 计息开始日
				} else {
					indate = loanLoanDetail.getSpecialFlg1();
				}
				if (loanLoanDetail.getSpecialFlg2()==null || "".equals(loanLoanDetail.getSpecialFlg2()) || "0".equals(loanLoanDetail.getSpecialFlg2())) {
					shrepdate = "";// 应还款日
				} else {
					shrepdate = loanLoanDetail.getSpecialFlg2();
				}
				if ("1".equals(onePaymenyFlg)) {// 首付
					
				} else if ("1".equals(prepaymentFlg)) { // 提前还款
					if ("".equals(repdate) || "".equals(indate)) {
						continue;
					}
					daysu = daysOfTwo(indate, repdate) + 1;// 把被减去的第一天加回来
					if (daysu <= 30) {
						date_flg = "30";
					} else if (daysu >= 31 && daysu <= 60) {
						date_flg = "60";
					} else if (daysu >= 61 && daysu <= 90) {
						date_flg = "90";
					} else if (daysu >= 91) {
						date_flg = "100";
					}
				} else { // 还款结果
					if ("".equals(repdate) || "".equals(shrepdate)) {
						continue;
					}
					daysu = daysOfTwo(shrepdate, repdate) + 1;// 把被减去的第一天加回来
					if (daysu < 0) {
						continue;
					} else if (daysu<=4) {
						date_flg = "DQ";
					} else {
						date_flg = "WQ";
					}
				}
			}
			
			settle_flg = onePaymenyFlg+repaymentCnt+repaymentResult+overdueFlg+prepaymentFlg+dropClassFlg+specialFlg1+specialFlg2+specialFlg3;
			
			settleLoanDetail = editLoanDate(loanLoanDetail,settle_flg,date_flg);
			
			logger.info(">>> >>> >>> >>> 添加或修改'清算'方交易信息");
			//当前'清算'交易信息
			//查询此信息在DB中是否存在
			SettleLoanDetailQuery loanquery = new SettleLoanDetailQuery();
			loanquery.setLoanId(settleLoanDetail.getLoanId());
			settleLoanDetailList = settleLoanDetailManager.queryList(loanquery);
			if(settleLoanDetailList==null || settleLoanDetailList.size() == 0) {//不存在
				settleLoanDetailManager.saveSettleLoanDetail(settleLoanDetail);
			} else {//存在
				if (settleLoanDetailList.get(0).getStatusId() != 11) {// 已清分交易，不再更新
					settleLoanDetailManager.updateSettleLoanDetail(settleLoanDetail);
				}
			}
			
		}
		return resultMap;
	}
	
	private SettleLoanDetail editLoanDate(LoanSettleLoanDetail loanLoanDetail,String settle_flg,String date_flg) {
		SettleLoanDetail settleLoanDetail = new SettleLoanDetail();
		
		BeanUtils.copyProperties(loanLoanDetail,settleLoanDetail);
		
		settleLoanDetail.setOnePaymentFlg(loanLoanDetail.getOnePaymenyFlg()==null ? 0:loanLoanDetail.getOnePaymenyFlg());
		settleLoanDetail.setRepaymentCnt(loanLoanDetail.getRepaymentCnt()==null ? "0":loanLoanDetail.getRepaymentCnt());
		settleLoanDetail.setRepaymentResult(loanLoanDetail.getRepaymentResult()==null ? " ":loanLoanDetail.getRepaymentResult());
		settleLoanDetail.setOverdueFlg(loanLoanDetail.getOverdueFlg()==null ? "0":loanLoanDetail.getOverdueFlg());
		settleLoanDetail.setPrepaymentFlg(loanLoanDetail.getPrepaymentFlg()==null ? "0":loanLoanDetail.getPrepaymentFlg());
		settleLoanDetail.setDropClassFlg(loanLoanDetail.getDropClassFlg()==null ? "0":loanLoanDetail.getDropClassFlg());;
		settleLoanDetail.setSpecialFlg1(loanLoanDetail.getSpecialFlg1()==null ? "0":loanLoanDetail.getSpecialFlg1());;
		settleLoanDetail.setSpecialFlg2(loanLoanDetail.getSpecialFlg2()==null ? "0":loanLoanDetail.getSpecialFlg2());
		settleLoanDetail.setSpecialFlg3(loanLoanDetail.getSpecialFlg3()==null ? "0":loanLoanDetail.getSpecialFlg3());
		settleLoanDetail.setSettleFlg(settle_flg);
		settleLoanDetail.setDateFlg(date_flg);
		settleLoanDetail.setStatusId(1); // 初始状态为1
		
		return settleLoanDetail;
	}
	
	public static int daysOfTwo(String date1, String date2) throws ParseException {
	     SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	        int result = 0;

	        Calendar calst = Calendar.getInstance();;
	        Calendar caled = Calendar.getInstance();

	        calst.setTime(sdf.parse(date1));
	        caled.setTime(sdf.parse(date2));
	 
	         //设置时间为0时   
	         calst.set(Calendar.HOUR_OF_DAY, 0);   
	         calst.set(Calendar.MINUTE, 0);   
	         calst.set(Calendar.SECOND, 0);   
	         caled.set(Calendar.HOUR_OF_DAY, 0);   
	         caled.set(Calendar.MINUTE, 0);   
	         caled.set(Calendar.SECOND, 0);   
	        //得到两个日期相差的天数   
	         int days = ((int)(caled.getTime().getTime()/1000)-(int)(calst.getTime().getTime()/1000))/3600/24;   
	         
	        return days;   
	    }
}
