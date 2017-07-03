package com.rkylin.settle.util;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class PagerModel<T> implements Serializable  {
	private static final long serialVersionUID = 1L;
	private int total;//数据数量
	private List<T> list;//返回结果list
	private List<Map> mapList;//返回结果list
	private Object obj;//返回对象
	private boolean retBln;
	private String result;//返回结果
	private String msg;//返回描述
	private Map dataMap;//返回描述
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	
	public List<Map> getMapList() {
		return mapList;
	}
	public void setMapList(List<Map> mapList) {
		this.mapList = mapList;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public boolean isRetBln() {
		return retBln;
	}
	public void setRetBln(boolean retBln) {
		this.retBln = retBln;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Map getDataMap() {
		return dataMap;
	}
	public void setDataMap(Map dataMap) {
		this.dataMap = dataMap;
	}
	@Override
	public String toString() {
		return "PagerModel [total=" + total + ", list=" + list + ", mapList="
				+ mapList + ", obj=" + obj + ", retBln=" + retBln + ", result="
				+ result + ", msg=" + msg + ", dataMap=" + dataMap + "]";
	}
}
