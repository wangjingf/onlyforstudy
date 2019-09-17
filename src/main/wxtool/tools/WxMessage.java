package com.lifesense.tools;

import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dom4j.CDATA;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public class WxMessage {
	String toUserName;
	String fromUserName;
	String msgType;
	String event;
	String eventKey;
	String createTime;
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getEventKey() {
		return eventKey;
	}
	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "WxMessage [toUserName=" + toUserName + ", fromUserName=" + fromUserName + ", msgType=" + msgType
				+ ", event=" + event + ", eventKey=" + eventKey + "]";
	}
	public static void main(String[] args) throws DocumentException{
		String xml = "<xml><ToUserName><![CDATA[gh_ccb71df56bfc]]></ToUserName>"+
		"<FromUserName><![CDATA[o9k1Cwz7Y3H6NPX-_WLd1cl_InVg]]></FromUserName>"+
		"<CreateTime>1506702012</CreateTime>"+
		"<MsgType><![CDATA[event]]></MsgType>"+
		"<Event><![CDATA[CLICK]]></Event>"+
		"<EventKey><![CDATA[signIn]]></EventKey>"+
		"</xml>";
	System.out.println(fromXml(xml).createResponseMsg());	
        
	}
	public static WxMessage fromXml(String xml){
		SAXReader reader = new SAXReader();  
        //读取文件 转换成Document  
        Document document;
		try {
			document = reader.read(new StringReader(xml));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}  
        //获取根节点元素对象  
        Element root = document.getRootElement();  
        Iterator<Element> elms =  root.elementIterator();
        WxMessage message = new WxMessage();
        Map<String,Object> map = new LinkedHashMap<String,Object>();
        while(elms.hasNext()){
        	Element elm = elms.next();
        	map.put(elm.getName(), elm.getText());
        }
        return getInstance(WxMessage.class, map);
	}
	public Element createCDataElement(String name,String text){
		Element elm =DocumentHelper.createElement(name);
		CDATA cdata = DocumentHelper.createCDATA(text);
		elm.add(cdata);
		return elm;
	}
	public Element createElement(String name,String text){
		Element elm =DocumentHelper.createElement(name);
		elm.addText(text);
		return elm;
	}
	public String createResponseMsg(){
		Element elm =  DocumentHelper.createElement("xml");
		elm.add(createCDataElement("ToUserName",getToUserName()));
		elm.add(createCDataElement("FromUserName",getFromUserName()));
		elm.add(createElement("CreateTime",System.currentTimeMillis()/1000+""));
		elm.add(createCDataElement("MsgType","text"));
		elm.add(createCDataElement("Content","success!"));
		elm.add(createElement("MsgId",System.currentTimeMillis()+""));
		return elm.asXML();
	}
	public static <T> T getInstance(Class<T> clazz,Map<String,Object> properties) {
		T obj;
		try {
			obj = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		for(Map.Entry<String, Object> entry : properties.entrySet()){
			String name=entry.getKey();
			String methodName ="set"+name;
			Object value = entry.getValue();
			try {
				Method setMethod = clazz.getMethod(methodName, value.getClass());
				setMethod.invoke(obj, value);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
					
		}
		return obj;
	}
}
