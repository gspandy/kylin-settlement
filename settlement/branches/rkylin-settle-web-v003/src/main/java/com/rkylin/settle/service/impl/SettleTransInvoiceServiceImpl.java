package com.rkylin.settle.service.impl;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.common.RedisIdGenerator;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.manager.SettleTransBillManager;
import com.rkylin.settle.manager.SettleTransInvoiceManager;
import com.rkylin.settle.manager.SettleTransProfitManager;
import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;
import com.rkylin.settle.pojo.SettleTransBill;
import com.rkylin.settle.pojo.SettleTransBillQuery;
import com.rkylin.settle.pojo.SettleTransInvoice;
import com.rkylin.settle.pojo.SettleTransInvoiceQuery;
import com.rkylin.settle.service.SettleTransInvoiceService;
import com.rkylin.settle.util.PagerModel;
import com.rkylin.settle.util.SettlementUtil;

/***
 * 下游对账交易信息业务逻辑
 * @author Yang
 *
 */
@Service("settleTransInvoiceService")
public class SettleTransInvoiceServiceImpl implements SettleTransInvoiceService {
	private static Logger logger = LoggerFactory.getLogger(SettleTransInvoiceServiceImpl.class);
	@Autowired
	private SettleTransInvoiceManager settleTransInvoiceManager;
	@Autowired
	private SettleParameterInfoManager parameterInfoManager;
	@Autowired
	private RedisIdGenerator redisIdGenerator;
	@Autowired
    private SettleTransBillManager settleTransBillManager;
	@Autowired
	private SettleTransProfitManager settleTransProfitManager;
	
	@Autowired
	private SettlementUtil settlementUtil;
	
	//挂账交易状态
	//private Map<String,String> billOrderStatusMap = new HashMap<String, String>();
	/***
	 * 分页条件查询下游对账交易信息
	 * @param query
	 * @return
	 */
	@Override
	public PagerModel<SettleTransInvoice> query(SettleTransInvoiceQuery query) {
		//创建分页Model
		PagerModel<SettleTransInvoice> pagerModel = new PagerModel<SettleTransInvoice>();
		try {
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
			pagerModel.setList(settleTransInvoiceManager.queryPage(query));
			pagerModel.setTotal(settleTransInvoiceManager.countByExample(query));
			pagerModel.setResult("ok");
		} catch (Exception e) {
			e.printStackTrace();
			pagerModel.setResult("error");
			pagerModel.setMsg("系统异常");
		}
		
		return pagerModel;
	}
	
	/***
	 * 查询结算表交易byID
	 * @param query
	 * @return
	 */
	@Override
	public SettleTransInvoice findById(Long id) {
		return settleTransInvoiceManager.findSettleTransInvoiceById(id);
	}
	
	/***
	 * 修改下结算表信息
	 * @param query
	 * @return
	 */
	@Override
	public Integer edit(SettleTransInvoice SettleTransInvoice) {
		return settleTransInvoiceManager.updateSettleTransInvoice(SettleTransInvoice);
	}
	
	 @Override
	 public  String importInvoice(InputStream stream){
		List<SettleTransInvoice> invoiceList =null;
		int rowNum = 1;
		try{
			invoiceList = new ArrayList<SettleTransInvoice>();
			//获取机构号映射关系
		    SettleParameterInfoQuery paramQuery = new SettleParameterInfoQuery();
		    paramQuery.setParameterType(SettleConstants.PARAMETER_TYPE_DSF_MERCHANT);
		    List<SettleParameterInfo> paramList = parameterInfoManager.queryList(paramQuery);  
			  
		    XSSFWorkbook wb = null;
		    wb = new XSSFWorkbook(stream);
		    XSSFSheet sheet1 = wb.getSheetAt(0);
		    Boolean check = true;
		    SettleTransInvoiceQuery query = new SettleTransInvoiceQuery();
		    List<SettleTransInvoice> invoiceReturnList = null;
		    String warnMessage = null;
		    for (Row row : sheet1) {
		      if(row.getRowNum() >=1){
			      SettleTransInvoice invoice = new SettleTransInvoice();
			      for (Cell cell : row) {
			    	  String value = null;
			    	  switch (cell.getCellType()) {
			    	    case HSSFCell.CELL_TYPE_FORMULA:
				    	   try {
				    	      value = String.valueOf(cell.getNumericCellValue());
				    	      if(value !=null){
				    	    	  value = value.substring(0,value.length()-2); 
				    	      }
				    	   } catch (IllegalStateException e) {
				    	      value = String.valueOf(cell.getRichStringCellValue());
				    	   }
				    	   break;
				    	   case HSSFCell.CELL_TYPE_NUMERIC:
				    	      value = String.valueOf(cell.getNumericCellValue());
				    	      if(value !=null){
				    	    	  value = value.substring(0,value.length()-2); 
				    	      }
				    	   break;
				    	   case HSSFCell.CELL_TYPE_STRING:
				    	   value = String.valueOf(cell.getRichStringCellValue());
				    	   break;
			    	   }
			    	  int index = cell.getColumnIndex();//单元格所在的列的索引
			    	  
			    	  if(index == 0){
			    		  invoice.setMerchantCode(value);//机构号
			    		  if(StringUtils.isNotBlank(value)){
			    			  for(SettleParameterInfo bean : paramList){
				  				if(bean.getParameterValue().contains(value)){
				  					invoice.setRootInstCd(bean.getParameterCode());//机构号转换
				  					break;
				  				}
			    		  }
			  			}
			    	  }else if(index == 1){
			    		  invoice.setUserId(value);//用户ID
			    	  }else if(index == 2){
			    		  //交易类型
			    		  if("代收".equals(value)){
			    			  value = "5";
			    			  invoice.setFuncCode("4013");
			    		  }else if("代付".equals(value)){
			    			  value = "6";
			    			  invoice.setFuncCode("4014");
			    		  }
			    		  if(StringUtils.isNotBlank(value)){
			    			  invoice.setOrderType(Integer.parseInt(value));//交易类型
			    		  }
			    	  }else if(index == 3){
			    		  invoice.setBankCode(value);//银行代码
			    	  }else if(index == 4){
			    		  invoice.setOpenBankName(value);//开户行名称
			    	  }else if(index == 5){
			    		  if(StringUtils.isNotBlank(value)){
			    			  value = value.substring(0,2);
			    		  }
			    		  invoice.setAccountType(value);//账户类型
			    	  }else if(index == 6){
			    		  invoice.setAccountNo(value);//账号
			    	  }else if(index == 7){
			    		  invoice.setAccountName(value);//账号名称
			    	  }else if(index == 8){
			    		  if(StringUtils.isNotBlank(value)){
			    			  if("对公".equals(value)){
			    				  value = "1";
			    			  }else if("对私".equals(value)){
			    				  value = "2";
			    			  }
			    		  }
			    		  invoice.setAccountProperty(value);//账号属性
			    	  }else if(index == 9){
			    		  invoice.setProvince(value);//开户行所在省
			    	  }else if(index == 10){
			    		  invoice.setCity(value);//开户行所在市
			    	  }else if(index == 11){
			    		  invoice.setOpenBankName(value);//支行名称
			    	  }else if(index == 12){
			    		  invoice.setPayBankCode(value);//支行行号
			    	  }else if(index == 13){
			    		  if(StringUtils.isNotBlank(value)){
			    			  invoice.setAmount(Long.parseLong(value));//金额：分
			    		  }
			    	  }else if(index == 14){
			    		  invoice.setCurrency(value);//币种
			    	  }else if(index == 15){
			    		  if(StringUtils.isNotBlank(value)){
			    			  String[] tempArr = value.split(":");
			    			  if(tempArr !=null && tempArr.length>0){
			    				  value = tempArr[0];
			    			  }
			    		  }
			    		  invoice.setCertificateType(value);//证件类型
			    	  }else if(index == 16){
			    		  invoice.setCertificateNumber(value);//证件号
			    	  }else if(index == 17){
			    		  //查询批次号在数据库中是否存在
			    		  if(StringUtils.isNotBlank(value)){
			    			  query.setBatchNo(value);
				    		  invoiceReturnList = settleTransInvoiceManager.queryPage(query);
				    		  if(invoiceReturnList !=null && invoiceReturnList.size()>0){
				    			  warnMessage = "0";
				    			  check =false;
				    			  break;
				    		  }
			    		  }
			    		  invoice.setBatchNo(value);//批次号
			    	  }else if(index == 18){
			    		  invoice.setRemark(value);//备注
			    	  }
			      }
			      
			      if(!check) break;
			      
			      if(StringUtils.isNotBlank(invoice.getMerchantCode())){
			    	  if(StringUtils.isBlank(invoice.getRootInstCd())){
			    		  invoice.setRootInstCd(invoice.getMerchantCode());//机构号
					  }
			    	  invoice.setRequestNo(this.createBatchNo("DSF"));
			    	  invoice.setOrderNo(this.createBatchNo("O"));
			    	  invoice.setDataSource(0);//文件导入
			    	  invoice.setSendType(0);
			    	  invoice.setStatusId(0);
			    	  invoice.setRealTimeFlag(0);
			    	  if(StringUtils.isBlank(invoice.getRemark())){
			    		  invoice.setRemark("文件导入");
			    	  }
			    	 
			    	  if(invoice.getOrderType() !=null && invoice.getOrderType() == 6){//代付
			    		  invoice.setBussinessCode("11101");
					   }else if(invoice.getOrderType() !=null  && invoice.getOrderType() == 5){
						  invoice.setBussinessCode("09100");
					   }
			    	   String settleDate;
					   try {
						 settleDate = (String) settlementUtil.getAccountDate("yyyy-MM-dd", 0, "");
						 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
				    	 invoice.setAccountDate(sdf.parse(settleDate));
					   } catch (Exception e) {
						 logger.error("获取账期异常，异常信息==="+e);
					   }
					
					   invoiceList.add(invoice);  
			      }
			  }
		      
		      rowNum = row.getRowNum()+1;
		    }
		    
		    if(warnMessage !=null) return warnMessage;
		    
		}catch(Exception e){
			logger.error("解析excel发生异常"+e.getMessage());
			return "import excel error,please check row "+rowNum+" data";
		}  
		
		String userIds = "";
		try{
			for(SettleTransInvoice invoice : invoiceList){
				
				SettleTransInvoiceQuery invoiceQuery = new SettleTransInvoiceQuery();
				invoiceQuery.setBatchNo(invoice.getBatchNo());
				invoiceQuery.setUserId(invoice.getUserId());
				invoiceQuery.setAmount(invoice.getAmount());
				List<SettleTransInvoice>  tmpList = settleTransInvoiceManager.queryPage(invoiceQuery);
				if(tmpList ==null || tmpList.size()==0){
					settleTransInvoiceManager.saveSettleTransInvoice(invoice);
				}else{
					userIds += invoice.getUserId()+",";
				}
				
			}
		}catch(Exception e){
			logger.info("导入excel,往结算表写入数据时发生异常！！！");
			try{
				for(SettleTransInvoice invoice1 : invoiceList){
					settleTransInvoiceManager.deleteSettleTransInvoiceByInvoiceNo(new Long(invoice1.getInvoiceNo()));
				}
			}catch(Exception e2){
				logger.info(e2.getMessage());
			}
		}
		if(StringUtils.isNotBlank(userIds)){
			return userIds; 
		}
		
        return "1";
	  }
	
	/***
     * 挂账
     * 1、更新detail表状态为99，插入挂账表信息状态为3
     * 2、清分成功交易，需要更新清分表信息为2不处理
     * 3、清算成功交易，不做挂账处理，将ID返回前端
     * @return
     */
    @Override
    public Map<String, Object> doBill(Integer[] ids) throws Exception {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        //清算成功的交易
        StringBuffer settleSb = new StringBuffer();
        //inavoic表中需要更新的交易
        List<Integer> updateIdList = new ArrayList<Integer>();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("ids", ids);
        List<SettleTransInvoice> settleTransInvoicList = settleTransInvoiceManager.selectByIds(queryMap);
        
        for(SettleTransInvoice settleTransInvoice : settleTransInvoicList){
        	boolean dontBill = settleTransInvoice.getStatusId() != SettleConstants.DSF_NOT_DEAL;
            if(dontBill){
                //记录交易信息，返回前端
                settleSb.append(settleTransInvoice.getInvoiceNo()).append(",");
                logger.debug("清算成功交易，不做挂账,ID为" + settleTransInvoice.getInvoiceNo());
                continue;
            }
            
            SettleTransBill settleTransBill = new SettleTransBill();
            SettleTransBillQuery query = new SettleTransBillQuery();
            query.setOrderNo(settleTransInvoice.getOrderNo());
            List<SettleTransBill> billList = settleTransBillManager.queryList(query);
            if(!billList.isEmpty()){
                settleTransBill = billList.get(0);
            }
            //订单号=订单系统订单号
            settleTransBill.setOrderNo(settleTransInvoice.getOrderNo());
            //批次号
            //settleTransBill.setBatchNo(batchNo);
            //挂账条目=订单系统订单号 + 0001
            settleTransBill.setBillNo(settleTransInvoice.getOrderNo() + "0001");
            //管理机构代码=商户编码
            settleTransBill.setRootInstCd(settleTransInvoice.getRootInstCd());
            //产品号
            //settleTransBill.setProductId(settleTransInvoice.get);
            //用户ID
            //settleTransBill.setUserId(settleTransDetail.get);
            //第三方账户ID
            //settleTransBill.setReferUserId(settleTransDetail.getInterMerchantCode());
            //挂账金额=入账金额
            settleTransBill.setBillAmount(settleTransInvoice.getAmount());
            //挂账类型0差错处理,1退款
            settleTransBill.setBillType(0);
            //状态,0未进行,1已进行,2不处理,3挂账中,4取消挂账
            settleTransBill.setStatusId(3);
            //记账日期
            settleTransBill.setAccountDate(settleTransInvoice.getAccountDate());
            settleTransBill.setRemark("invoice 【结算单】 挂账中");
            //如果挂账记录不为空，则更新挂账表信息
            if(!billList.isEmpty()){
                settleTransBillManager.updateSettleTransBill(settleTransBill);
            }else{
                settleTransBillManager.saveSettleTransBill(settleTransBill);
            }
            //invoice表中需要更新的ID
            updateIdList.add(settleTransInvoice.getInvoiceNo());
        }
        if(!updateIdList.isEmpty()){
            //批量更新交易信息状态
            queryMap.clear();
            queryMap.put("idList", updateIdList);
            queryMap.put("statusId", SettleConstants.INVOICE_ISBILLED);
            settleTransInvoiceManager.updateTransStatusId(queryMap);
        }
        //封装返回值
        if(settleSb.length() != 0){
            returnMap.put("error", "error");
            settleSb.insert(0, "ID为");
            settleSb.insert(settleSb.length(), "，不允许挂账操作，操作失败！");
            returnMap.put("msg", settleSb.toString());
        }
        return returnMap;
    }
    /***
     * 取消挂账
     * @return
     */
    @Override
    public Map<String, Object> doCancelBill(Integer[] ids) throws Exception {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        StringBuffer returnSb = new StringBuffer();
        for(Integer id:ids){
            //查询交易信息
        	SettleTransInvoice settleTransInvoice = settleTransInvoiceManager.findSettleTransInvoiceById(Long.valueOf(id));
            //交易不是挂账状态，不处理
            if(SettleConstants.INVOICE_ISBILLED != settleTransInvoice.getStatusId()){
                returnSb.append(id).append(",");
                logger.debug("交易不是挂账状态，不处理, ID为"+settleTransInvoice.getInvoiceNo());
                continue;
            }
            //更新交易信息状态
            SettleTransInvoice updateInvoice = new SettleTransInvoice();
            updateInvoice.setInvoiceNo(id);
            updateInvoice.setStatusId(SettleConstants.DSF_NOT_DEAL);
            updateInvoice.setRequestNo((new SettlementUtil()).createBatchNo("BILL"));
            settleTransInvoiceManager.updateSettleTransInvoice(updateInvoice);
            //更新挂账表信息状态
            SettleTransBill settleTransBill = new SettleTransBill();
            settleTransBill.setOrderNo(settleTransInvoice.getOrderNo());
            //取消挂账后的状态
            settleTransBill.setStatusId(4);
            settleTransBill.setRemark("invoice 【结算单】挂账取消");
            settleTransBillManager.updateSettleTransBillByOrderNo(settleTransBill);
        }
        //封装返回值
        if(returnSb.length()!=0){
            returnMap.put("error", "error");
            returnSb.insert(0, "本操作仅限取消挂账，ID为");
            returnSb.insert(returnSb.length(), "交易状态不是挂账中，操作失败！");
            returnMap.put("msg", returnSb.toString());
        }
        return returnMap;
    }
	/***
	 * 输入流转文件 
	 * @param ins
	 * @param file
	 */
//	public static void inputstreamtofile(InputStream ins, File file) {
//		try {
//			OutputStream os = new FileOutputStream(file);
//			int bytesRead = 0;
//			byte[] buffer = new byte[8192];
//			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
//				os.write(buffer, 0, bytesRead);
//			}
//			os.close();
//			ins.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
    
    /**
     * 生成批次号
     * 算法：根据头信息+毫秒级时间戳+1000以内的随机数生成批次号
     * @param head 批次号头部
     * @return 如：head传的DSF,则返回DSF14507744680760438
     * @throws Exception
     */
     public synchronized String createBatchNo(String head){
    	 String batchNo ="";
    	 try{
			 batchNo=UUID.randomUUID().toString();
			 batchNo=batchNo.replace("-", "");
    	   }catch(Exception e){
    			logger.error("<<<<<<<<生成批次号异常>>>>>>>>>>>>>");
    	   }
    	   return batchNo;
     }
     
     /***
 	 * 批量查询交易信息by主键数组
 	 * @param queryMap
 	 * @return
 	 */
 	@Override
 	public List<SettleTransInvoice> queryByIds(Integer[] ids) {
 		Map<String, Object> queryMap = new HashMap<String, Object>();
 		queryMap.put("ids", ids);
 		return settleTransInvoiceManager.selectByIds(queryMap);
 	}
 	
 	public Map<String,Long> queryAmountAndTotalNum(String dataSource,String[] inRootInstCds,Integer[] orderTypes){
 		Map<String,Long> resultMap = new HashMap<String,Long>();
 		
 		SettleTransInvoiceQuery query = new SettleTransInvoiceQuery();
 		query.setDataSource(Integer.parseInt(dataSource));
 		query.setRootInstCds(inRootInstCds);
 		query.setOrderTypes(orderTypes);
 		query.setStatusId(0);
 		int count = settleTransInvoiceManager.countByExample(query);
 		long summaryMoney = 0;
 		try{
 			summaryMoney = settleTransInvoiceManager.summaryMoneyByExample(query);
 		}catch(Exception e){
 			logger.info("查询到的总的金额为null!!!"+e);
 		}
 		resultMap.put("count", Long.parseLong(count+""));
 		resultMap.put("summaryMoney", summaryMoney);
 		
 		return resultMap;
 	}
	
}
