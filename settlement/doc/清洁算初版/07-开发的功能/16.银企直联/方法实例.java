	/** 看看看
	 * 40143交易发送到多渠道
	 * Discription:
	 * @return CommonResponse
	 * @author Achilles
	 * @since 2016年7月27日
	 */
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public  CommonResponse submitToGateRouterTransferBatch(String accountDate,Integer sendType,String protocol){
        CommonResponse res = new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (CommUtil.isEmp(protocol)) {
            res.setMsg("协议为空!");
            return res;
        }
        GenerationPaymentQuery query=new GenerationPaymentQuery();
        query.setStatusId(Integer.parseInt(BaseConstants.ACCOUNT_STATUS_OK));
        query.setRootInstCd(protocol);
        if (accountDate==null||"".equals(accountDate)) {
            query.setAccountDate(DateUtils.getDate(getAccountDate(), Constants.DATE_FORMAT_YYYYMMDD));
        }else{
            query.setAccountDate(DateUtils.getDate(accountDate, Constants.DATE_FORMAT_YYYYMMDD));
        }
        List<GenerationPayment> generationPaymentList=null;
        if (sendType!=null) {
            query.setSendType(sendType);
            generationPaymentList=generationPaymentManager.queryList(query); 
        }else{
            generationPaymentList=generationPaymentManager.queryListByOrderType(query); 
        }
	    if (generationPaymentList.size()==0) {
	        res.setMsg("没有查出要发送到多渠道的数据!");
	        return res;
        }
	    BatchPaymentDto dto = new BatchPaymentDto();
	    dto.setSysNo(GatewayConstants.SYS_NO_ACCOUNT_003);
	    dto.setTransCode("16012");
	    dto.setOrgNo(protocol);
        dto.setBusiCode("16011");// 业务编码
        dto.setSignType(1);// 签名类型 固定值1 即MD5
        ParameterInfo parameterInfo = parameterInfoService.getParameterInfoByParaCode(GatewayConstants.GATEROUTER_MD5_KEY);
        String md5Key = parameterInfo.getParameterValue();
        dto.setSignMsg(dto.sign(md5Key));
        
        String batchNo = "U"+CommUtil.getGenerateNum(2);
        if (batchNo.length()>18) {
            batchNo=batchNo.substring(0, 18);
        }
        batchNo+="_GRTRB"; //GateRouterTransferBach
	    dto.setBatchNo(batchNo);
	    dto.setTotalAcount(generationPaymentList.size());
	    dto.setCurrency(BaseConstants.CURRENCY_CNY);
//	    dto.setAccountNo("");
	    dto.setAccountType(GatewayConstants.ACCOUNT_TYPE_PUB);
//	    dto.setAccountName("");
	    dto.setSettleFlag(1);//普通
	    List<PaymentDto> paymentDtoList = new ArrayList<PaymentDto>();
	    long sumAmount = 0l;
	    StringBuffer sb = new StringBuffer("批量转账入参明细字段:");
	    PaymentDto paymentDto = null;
	    for (GenerationPayment generationPayment : generationPaymentList) {
	        paymentDto = new PaymentDto();
	        generationPayment.setProcessResult(batchNo);
	        paymentDto.setSysNo(GatewayConstants.SYS_NO_ACCOUNT_003);
	        paymentDto.setTransCode("16012");
	        paymentDto.setOrgNo(protocol);
	        paymentDto.setBusiCode("16011");// 业务编码
	        paymentDto.setSignType(1);// 签名类型 固定值1 即MD5
	        paymentDto.setSignMsg(paymentDto.sign(md5Key));
	        
	        paymentDto.setTransNo(generationPayment.getOrderNo());
	        paymentDto.setPayAmount(generationPayment.getAmount());
	        paymentDto.setCurrency(BaseConstants.CURRENCY_CNY);
//	        paymentDto.setPayerAccountNo(payerAccountNo);
//	        paymentDto.setPayerAccountName(payerAccountName);
	        paymentDto.setReceiverAccountNo(generationPayment.getAccountNo());
	        paymentDto.setReceiverAccountName(generationPayment.getAccountName());
	        paymentDto.setReceiverAccountType(GatewayConstants.ACCOUNT_TYPE_PRI);
	        if (Constants.ACCOUNT_PROPERTY_PUBLIC.equals(generationPayment.getAccountProperty())) {
	            paymentDto.setReceiverAccountType(GatewayConstants.ACCOUNT_TYPE_PUB);
            }
	        paymentDto.setReceiverBankNo(generationPayment.getPayBankCode());
	        paymentDto.setReceiverBankName(generationPayment.getOpenBankName());
	        paymentDto.setBankFlag(2);//行外
	        if ("305".equals(generationPayment.getBankCode())) {
	            paymentDto.setBankFlag(1);
            }
	        paymentDto.setSettleFlag(1);
	        paymentDto.setPurpose("放款");
	        paymentDtoList.add(paymentDto);
	        sumAmount+=generationPayment.getAmount();
	        sb.append("TransNo="+paymentDto.getTransNo()+",").append("PayAmount="+paymentDto.getPayAmount()+",")
	        .append("Currency="+paymentDto.getCurrency()+",").append("ReceiverAccountNo="+paymentDto.getReceiverAccountNo()+",")
	        .append("ReceiverAccountName="+paymentDto.getReceiverAccountName()+",")
	        .append("ReceiverAccountType="+paymentDto.getReceiverAccountType()+",")
	        .append("ReceiverBankNo="+paymentDto.getReceiverBankNo()+",SettleFlag="+paymentDto.getSettleFlag()+",")
	        .append("ReceiverBankName="+paymentDto.getReceiverBankName()+",")
	        .append("BankFlag="+paymentDto.getBankFlag()+",").append("Purpose="+paymentDto.getPurpose()+"|");
        }
	    logger.info(sb.toString());
//	    BatchAccountBalanceQueryDto queryDto = new BatchAccountBalanceQueryDto();
//	    queryDto.setSysNo(GatewayConstants.SYS_NO_ACCOUNT_001);
//	    queryDto.setTransCode("16001");
//	    queryDto.setOrgNo(protocol);
//	    queryDto.setBusiCode("10001");// 业务编码
//	    queryDto.setChannelNo("160703");
//	    queryDto.setSignType(1);// 签名类型 固定值1 即MD5
//	    queryDto.setSignMsg(queryDto.sign(md5Key));
//	    List<String> accountList = new ArrayList<String>();
//	    parameterInfo = parameterInfoService.getParaInfoByProductId("MINSHENG_PUB_ACCOUNT");
//	    if (parameterInfo==null) {
//            logger.info("参数表没有预制民生测试账户!");
//            res.setMsg("系统异常");
//            return res; 
//        }
//	    accountList.add(parameterInfo.getParameterCode());
//	    queryDto.setAccountList(accountList);
//        logger.info("调用多渠道查余额传入参: SysNo=" + queryDto.getSysNo() + ",TransCode=" + queryDto.getTransCode() + ",OrgNo="
//                + queryDto.getOrgNo() + ",BusiCode=" + queryDto.getBusiCode() + ",SignType="+ queryDto.getSignType() + ",SignMsg=" 
//                + queryDto.getSignMsg() + ",AccountList=" + queryDto.getAccountList());
//	    BatchAccountBalanceQueryRespDto queryRespDto;
//        try {
//            queryRespDto = cmbcAccountService.batchAccountBalanceQuery(queryDto);
//        } catch (Exception e1) {
//            logger.error("调用多渠道余额查询接口异常!", e1);
//            res.setMsg("系统异常!");
//            return res;
//        }
//	    if (queryRespDto==null) {
//            logger.info("调用多渠道查询余额失败,返回对象queryRespDto=null");
//            res.setMsg("系统异常");
//            return res; 
//        }
//        logger.info("调用多渠道查询余额 返回值:  returnCode=" + queryRespDto.getReturnCode() + ",returnMsg=" + queryRespDto.getReturnMsg());
//        if (!"100000".equals(queryRespDto.getReturnCode())) {
//            updateGenPayStatus(generationPaymentList,3,batchNo);
//            res.setMsg(queryRespDto.getReturnMsg());
//            return res;
//        }
//        if (queryRespDto.getAccountBalanceDetailRespDtoList()==null||queryRespDto.getAccountBalanceDetailRespDtoList().size()==0) {
//            logger.info("调用多渠道查询余额集合失败,queryRespDto.getAccountBalanceDetailRespDtoList()="+queryRespDto.getAccountBalanceDetailRespDtoList());
//            res.setMsg("系统异常");
//            return res; 
//        }
//        String usableBalance = queryRespDto.getAccountBalanceDetailRespDtoList().get(0).getUsableBalance();
//        logger.info("调用多渠道查出余额="+usableBalance);
//        if (usableBalance==null||"".equals(usableBalance)||"0".equals(usableBalance)) {
//            logger.info("调用多渠道查询余额失败,金额异常,UsableBalance="+usableBalance);
//            res.setMsg("系统异常");
//            return res; 
//        }
//        Long usableBalancee = null;
//        if (usableBalance.contains(".")) {
//            if (usableBalance.split("\\.")[1].length()==1) {
//                usableBalancee = Long.parseLong((usableBalance.split("\\.")[0]+usableBalance.split("\\.")[1]))*10;
//            }else if(usableBalance.split("\\.")[1].length()==2){
//                usableBalancee = Long.parseLong((usableBalance.split("\\.")[0]+usableBalance.split("\\.")[1]));
//            }
//        }else{
//            usableBalancee = Long.parseLong(usableBalance)*100;
//        }
//        if (sumAmount>usableBalancee) {
//            parameterInfo = parameterInfoService.getParameterInfoByParaCode("sendMobiles");
//            if (parameterInfo==null) {
//                res.setMsg("参数表没有设置手机号,无法发送短信!");  
//                return res;  
//            }
//            SendSMS.sendSMS(parameterInfo.getParameterValue(), "账户系统"+parameterInfo.getRemark()
//            +"，编号YQMS_001存在保理主账户余额不足问题，请及时解决！");
//            res.setMsg("保理主账户余额不足!"); 
//            return res;
//        }
        dto.setSettleFlag(1);
	    dto.setTotalAmount(sumAmount);
        dto.setPaymentDtoList(paymentDtoList);
        logger.info("调用多渠道批量转账入参(批次字段): SysNo=" + dto.getSysNo() + ",TransCode=" + dto.getTransCode() + ",OrgNo="
                + dto.getOrgNo() + ",BusiCode=" + dto.getBusiCode() + ",SignType="+ dto.getSignType() + ",SignMsg=" 
                + dto.getSignMsg() + ",BatchNo=" + dto.getBatchNo()+",TotalAcount="+dto.getTotalAcount()+",Currency="
                +dto.getCurrency()+",AccountType="+dto.getAccountType()+",SettleFlag="+dto.getSettleFlag()+",TotalAmount="
                +dto.getTotalAmount());
        BatchPaymentRespDto resDto = null;
        try {
            resDto = bankPaymentService.batchPayment(dto);
        } catch (Exception e) {
            logger.error("调用多渠道批量转账接口异常!", e);
            res.setMsg("系统异常!");
            return res;
        } 
        logger.info("调用多渠道批量转账返回值:    returnCode=" + resDto.getReturnCode() + ",returnMsg=" + resDto.getReturnMsg());
        if (!"100000".equals(resDto.getReturnCode())) {
            updateGenPayStatus(generationPaymentList,3,batchNo);
            res.setMsg(resDto.getReturnMsg());
            return res;
        }
        logger.info("调用多渠道批量转账返回值:    StatusId=" + resDto.getStatusId());
	    if (resDto.getStatusId()!=10) {
	        updateGenPayStatus(generationPaymentList,3,batchNo);
	        res.setMsg("多渠道受理失败");
	        return res;
        }
	    updateGenPayStatus(generationPaymentList,2,batchNo);
        return new CommonResponse();
	}
