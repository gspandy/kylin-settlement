package com.rkylin.settle.esper;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class MyListener implements UpdateListener {
		//继承UpdateListener，重写update方法
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
	//new/oldEvents表示事件的发生
	//输出语句与main中的EPL语句相关
 	EventBean event = newEvents[0];
 		System.out.println("avg=" +event.get("avg(price)"));
	}	
} 
