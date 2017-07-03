package com.rkylin.settle.util;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.rkylin.settle.esper.MyListener;
import com.rkylin.settle.esper.OrderEvent;

public class Exampleesper {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//声明
		Configuration config = new Configuration();
		//添加包所在路径，可以单个添加，也可以自动获取
		config.addEventTypeAutoName("org.myapp.event");

		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(config);

		//通过获取OrderEvent的price，求出在30秒里面的平均值，具体语句后面会说
		String epl = 
		"select avg(price) from OrderEvent.win:time(30 sec)";

		//直接创建EPStatement，获取EPL，进行查询操作
		EPStatement statement = epService.getEPAdministrator().createEPL(epl);
		//创建监听
		MyListener listener = new MyListener();
		//添加监听，监听里面的EPL语句与main方法里的EPL相呼应，监听里面EPL所需要的数据，main必须要有，main有的，监听里面的EPL并不需要一定要有
		statement.addListener(listener);
		
		//加载事件方法，输入所需处理的数据
		OrderEvent event = new OrderEvent("shirt", 74.50);
		//运行时，发送数据，进行处理，返回数值
		epService.getEPRuntime().sendEvent(event);
	}

}
