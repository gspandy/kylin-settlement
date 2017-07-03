package com.rkylin.settle.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.rkylin.common.RedisIdGenerator;
import com.rkylin.crps.pojo.OrderDetail;
import com.rkylin.crps.pojo.OrderDetails;
import com.rkylin.settle.pojo.SettleTransInvoice;
import com.rkylin.settle.pojo.SettleTransInvoiceQuery;
import com.rkylin.settle.service.DsfService;
import com.rkylin.settle.service.SettleTransInvoiceService;
import com.rkylin.settle.settleInterface.SettleWebInterface;
import com.rkylin.settle.util.PagerModel;

/**
 * @Description: 代收付结算表管理
 * @author youyu
 * @Create Time: 
 * @version V1.00
 */
@Controller
@RequestMapping("/invoice")
@Scope("prototype")
public class SettleTransInvoiceController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(SettleTransInvoiceController.class);
	@Autowired
	private SettleTransInvoiceService settleTransInvoiceService;
	@Autowired
	private SettleWebInterface settleWebInterface;
	@Autowired
	private RedisIdGenerator redisIdGenerator;

//	@Autowired
//	private DsfService dsfService;
	/***
	 * 分页查询
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/query_ajax")
	public void queryAjax(SettleTransInvoiceQuery query) throws Exception {
		try {
			//临时的
		/*	OrderDetail o = new OrderDetail();
			o.setOrderNo("OT20161031000001"); 
			o.setRequestNo("DSF14779005003200844");
			o.setStatusId(15);
			o.setErrMsg("测试成功");
			
			List<OrderDetail> tmpList = new ArrayList<OrderDetail>();
			
			tmpList.add(o);
			
			OrderDetails  olist = new OrderDetails();
			olist.setOrderDetails(tmpList);
			try{
//				dsfService.receiveDsfResult(olist);
				
			}catch(Exception e){
				logger.error("=="+e);
			}*/
			
			PagerModel<SettleTransInvoice> pagerModel = settleTransInvoiceService.query(query);
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
	@RequestMapping("/invoice_manager")
	public String openQuery() {
		return "/settlement/trans/invoice/query";
	}
	
	/***
     * 打开详情页面
     * @param id
     * @throws Exception
     */
    @RequestMapping("/invoice_open_view")
    public ModelAndView openView(Long id) {
        ModelAndView modelAndView = new ModelAndView("/settlement/trans/invoice/view");
        modelAndView.addObject("settleTransInvoice", settleTransInvoiceService.findById(id));
        return modelAndView;
    }
    
	/***
	 * 打开修改页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/invoice_open_edit")
	public ModelAndView openEdit(Long id) {
		ModelAndView modelAndView = new ModelAndView("/settlement/trans/invoice/edit");
		modelAndView.addObject("settleTransInvoice", settleTransInvoiceService.findById(id));
		return modelAndView;
	}
	
	/***
	 * 修改
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/edit_ajax")
	public void editAjax(SettleTransInvoice settleTransInvoice) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Integer resultNum = 0;
		resultNum = settleTransInvoiceService.edit(settleTransInvoice);
		Boolean isSuccess = false;
		String msg = "";
		
		if(resultNum > 0) {
			isSuccess = true;
			msg = "修改成功!";
		} else {
			isSuccess = false;
			msg = "修改失败!";
		}
		
		resultMap.put("isSuccess", isSuccess);
		resultMap.put("msg", msg);
		
		try {
			writeJson(resultMap);
		} catch(Exception e) {
			e.printStackTrace();
		    logger.error("返回值JSON序列化失败", e);
		}
	}
	
	/***
	 * 打开导入excel的查询页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/import_excel_query")
	public String importExcelQuery() {
		return "/settlement/trans/invoice/import_excel_query";
	}

	/***
	 * 打开导入excel的查询页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/down_excel_model")
	public void downExcelModel() {
		InputStream inStream = null;
		try {
		   String path = request.getSession().getServletContext().getRealPath(
		            File.separator+"WEB-INF");
           String fileName = "dsf_model.xlsx";
           String filePath = path + File.separator + fileName;
          // 读到流中  
          inStream = new FileInputStream(filePath);// 文件的存放路径  
          // 设置输出的格式  
          response.reset();  
          response.setContentType("bin");
		  response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("dsf_model.xlsx","UTF-8"));
		  
	      // 循环取出流中的数据  
          byte[] b = new byte[100];  
          int len;  

          while ((len = inStream.read(b)) > 0)  
              response.getOutputStream().write(b, 0, len);  
	          
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(inStream !=null){
				try{
					inStream.close();  
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 上传并解析excel
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
		@RequestMapping(value="/importExcel")
		@ResponseBody
		public String icCardImport(MultipartFile uploadFile,HttpServletRequest request){
			InputStream excelFile = null;
			String result=null;
			try {
				excelFile = uploadFile.getInputStream();
				result=settleTransInvoiceService.importInvoice(excelFile);
				logger.info("导入excel给画面返回的信息是:"+result);
			} catch (Exception e) {
				logger.error("上传文件异常！"+e.getMessage());
			}
			return result;
		}
		/***
	     * 挂账
	     */
	    @RequestMapping("/bill")
	    public void doBill(Integer[] ids) {
	    	Map<String, Object> map = new HashMap<String, Object>();
	        try {
	            map = settleTransInvoiceService.doBill(ids);
	            if(!map.containsKey("error")){
	                map.put("result", "success");
	            }
	        } catch (Exception e) {
	        	e.printStackTrace();
	            map.put("result", "error");
	            map.put("msg", "挂账操作异常，请联系管理员！");
	            logger.error("挂账失败",e);
	        }
	        try {
	            writeJson(map);
	        } catch (Exception e) {
	        	e.printStackTrace();
	            logger.error("转换JSON失败",e);
	        }
	    }
	    /***
	     * 取消挂账
	     */
	    @RequestMapping("/cancel_bill")
	    public void doCancelBill(Integer[] ids) {
	        Map<String, Object> map = new HashMap<String, Object>();
	           
	        try {
	            map = settleTransInvoiceService.doCancelBill(ids);
	            if(!map.containsKey("error")){
	                map.put("result", "success");
	            }
	        } catch (Exception e) {
	        	e.printStackTrace();
	            map.put("result", "error");
	            map.put("msg", "取消挂账操作异常，请联系管理员！");
	            logger.error("取消挂账失败",e);
	        }
	        try {
	            writeJson(map);
	        } catch (Exception e) {
	        	e.printStackTrace();
	            logger.error("转换JSON失败",e);
	        }
	    }
	    
		
		/**
		 * 回写结果到汇总表和交易表
		 * @param ids
		 */
		@RequestMapping("/write_result_from_invoice")
		public void writeResult(Integer[] ids){
			String resultMsg = "";
			try {
				settleWebInterface.manualWriteResult(ids);
				resultMsg = "success";
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				writeText(resultMsg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		/**
		 * 发送代收付
		 * @param inRootInstCds 机构号
		 * @param inFlag "0"代付和提现,"1"代收和有T0标记的代付
		 */
		@RequestMapping("/send_dsf")
		public void sendDsf(String dataSource,String inRootInstCds,String inFlag,String ids) {
			String resultMsg = "";
			try {
				String[] rootInstCds = null;
				Integer[] orderTypes = new Integer[10];
				String[] tmpIds = null;
				Integer[] inIds = null;
				if(StringUtils.isNotBlank(inRootInstCds)){
					if(inRootInstCds.contains(",")){
						rootInstCds =  inRootInstCds.split(",");
					}else{
						rootInstCds = new String[1];
						rootInstCds[0] =  inRootInstCds;
					}
				}else{
					rootInstCds = null;
				}
				
				if("".equals(dataSource))  dataSource = null;
			
				if("0".equals(inFlag)){//代付和提现   orderType 2提现，6代付
					orderTypes[0] = 2;
					orderTypes[1] = 6;
				}else if("1".equals(inFlag)){//代收和有T0标记的代付
					orderTypes[0] = 5;
					orderTypes[1] = 6;
				}else{
					orderTypes = null;
				}
				if(StringUtils.isNotBlank(ids)){
					if(ids.contains(",")){
						tmpIds =  ids.split(",");
					}else{
						tmpIds = new String[1];
						tmpIds[0] =  ids;
					}
				}else{
					tmpIds = null;
				}
				
				if(tmpIds !=null && tmpIds.length>0){
					inIds = new Integer[tmpIds.length];
					for(int i=0;i<tmpIds.length;i++){
						inIds[i] = Integer.parseInt(tmpIds[i]);
					}
				}else{
					inIds = null;
				}
				settleWebInterface.sendDsf(dataSource,rootInstCds, orderTypes,inIds);
				resultMsg = "success";
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				writeText(resultMsg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		  /**
	     * 代收付查询总金额
	     * @param ids
	     */
		@RequestMapping("/query_total_amount")
		public void queryTotalAmount(Integer[] ids) {
			String resultMsg = "";
			Long amount = 0L;
			try {
				List<SettleTransInvoice>  settleTransInvoiceList= settleTransInvoiceService.queryByIds(ids);
				for(SettleTransInvoice bean : settleTransInvoiceList){
					amount +=  bean.getAmount();
				}
				resultMsg = String.valueOf(amount);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				writeText(resultMsg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
		/**
		 * 发送代收付之前查询金额和条数，用于js中匹配人工输入的金额和条数是否正确。
		 * @param inRootInstCds 机构号
		 * @param inFlag "0"代付和提现,"1"代收和有T0标记的代付
		 */
		@RequestMapping("/validate_before_send_dsf")
		public void validateBeforeSendDsf(String dataSource,String inRootInstCds,String inFlag,String totalNum,String money) {
			String resultMsg = "";
			try {
				String[] rootInstCds = null;
				Integer[] orderTypes = new Integer[2];
				if(StringUtils.isNotBlank(inRootInstCds)){
					if(inRootInstCds.contains(",")){
						rootInstCds =  inRootInstCds.split(",");
					}else{
						rootInstCds = new String[1];
						rootInstCds[0] =  inRootInstCds;
					}
				}else{
					rootInstCds = null;
				}
				
				if("".equals(dataSource))  dataSource = null;
			
				if("0".equals(inFlag)){//代付和提现   orderType 2提现，6代付
					orderTypes[0] = 2;
					orderTypes[1] = 6;
				}else if("1".equals(inFlag)){//代收和有T0标记的代付
					orderTypes[0] = 5;
					orderTypes[1] = 6;
				}else{
					orderTypes = null;
				}
				
				Map<String,Long> resultMap = settleTransInvoiceService.queryAmountAndTotalNum(dataSource,rootInstCds, orderTypes);
				
				Long count = resultMap.get("count");
				
				Long summaryMoney = resultMap.get("summaryMoney");
				
				if(totalNum.equals(count.toString()) && money.equals(summaryMoney.toString())){
					resultMsg = "success";
				}else{
					resultMsg = "fail";
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				writeText(resultMsg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	/**
	 * 从代收付系统读取代收付结果
	 * @param inRootInstCds 机构号
	 * @param inFlag "0"代付和提现,"1"代收和有T0标记的代付
	 */
	@RequestMapping("/getResultFromDSF")
	public void getResultFromDSF(String rootInstCd,String businessCode,String requestNo,String orderNos,String accountDate) {
		
		String resultMsg = "fail";
		try {
			settleWebInterface.getResultFromDsf(rootInstCd, businessCode, requestNo, orderNos, accountDate);
			resultMsg = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			writeText(resultMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
}
