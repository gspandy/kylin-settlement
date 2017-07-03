package com.rkylin.settle.listen;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.crps.pojo.OrderDetail;
import com.rkylin.crps.pojo.OrderDetails;
import com.rkylin.database.BaseDao;
import com.rkylin.settle.service.DsfService;

/**
 * 监听代收付结果的MQ并将结果返回给账户系统
 * @author youyu
 * 
 */
@Component("rkylinCrpsQueueListener")
public class RkylinCrpsQueueListener extends BaseDao implements MessageListener {
	// 日志对象
	protected static Logger logger = LoggerFactory.getLogger(RkylinCrpsQueueListener.class);
	// 代收付业务Bean
	@Autowired
	DsfService dsfService;
	
	public void onMessage(Message msg) {
		logger.info(">>> >>> >>> 开始:监听到rkylinCrpsQueue队列中代收付的结果！");
		if (!(msg instanceof TextMessage)) {
			logger.error(">>> >>> >>> msg 格式异常:" + msg.toString());
			return;
		}
		//批量结果
		OrderDetails orderDetails = null;
		//单笔结果
		OrderDetail orderDetail = null;
		//消息
		TextMessage message = null;
		//消息中的json
		String json = null;
		//json转换OrderDetails的Map
		Map<String, Class<OrderDetails>> orderDetailsClassMap = null;
		JSONObject jsonObject = null;
		message = (TextMessage) msg;
		try {
			json = message.getText();
			logger.info(">>> >>> json : " + json);
		} catch (Exception e) {
			logger.error("message.getText() Exception", e);
			return;
		}
		jsonObject = JSONObject.fromObject(json);
        orderDetailsClassMap = new HashMap<String, Class<OrderDetails>>();
        orderDetailsClassMap.put("orderDetails", OrderDetails.class);
        orderDetails = (OrderDetails) JSONObject.toBean(jsonObject, OrderDetails.class, orderDetailsClassMap);
        if(orderDetails.getOrderDetails() != null && orderDetails.getOrderDetails().size() > 0) {
        	try {
        		dsfService.dubboNotify(orderDetails);
			} catch (Exception e) {
				logger.error(">>> >>> >>> dsfService.dubboNotify(orderDetails) 方法异常", e);
			}
        	
        } else {
        	orderDetail = (OrderDetail) JSONObject.toBean(jsonObject, OrderDetail.class);
        	try {
        		dsfService.dubboNotify(orderDetail);
			} catch (Exception e) {
				logger.error(">>> >>> >>> dsfService.dubboNotify(orderDetails) 方法异常", e);
			}
        }
		logger.info("<<< <<< <<< 结束:监听到rkylinCrpsQueue队列中代收付的结果！");
	}
}