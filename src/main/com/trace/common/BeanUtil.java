package com.trace.common;

import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import net.sf.json.JSONObject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class BeanUtil {
	/**
	 * 将json解析为对象
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static Object json2bean(String json,Class<?> clazz,Map<String,Class<?>> map) {
		JSONObject jsonObject = JSONObject.fromObject(json);
		return JSONObject.toBean(jsonObject,clazz,map);
	}
	
	
	/**
	 * 将对象解析为json，无层级格式
	 * @param obj
	 * @return	{"cateId": 1,"cateName": "na"}
	 */
	public static String bean2json(Object bean) {
		XStream xstream = new XStream(new JsonHierarchicalStreamDriver() {
			public HierarchicalStreamWriter createWriter(Writer writer) {
				char[] c = {};
		        return new JsonWriter(writer, c, "", JsonWriter.DROP_ROOT_MODE);   
			}
		});
		xstream.setMode(XStream.NO_REFERENCES);
		return xstream.toXML(bean);
	}
	/**
	 * 将对象解析为json,有层级格式
	 * @param obj
	 * @return	{
				  "cateId": 1,
				  "cateName": "na",
				}
	 */
	public static String bean2json_format(Object bean) {
		XStream xstream = new XStream(new JsonHierarchicalStreamDriver() {
			public HierarchicalStreamWriter createWriter(Writer writer) {
		        return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);   
			}
		});
		xstream.setMode(XStream.NO_REFERENCES);
		return xstream.toXML(bean);
	}
	
	/**
	 * 将Bean转换为XML
	 * 
	 * @param clazzMap
	 *            别名-类名映射Map
	 * @param bean
	 *            要转换为xml的bean对象
	 * @param listMap
	 *            要屏蔽的list中的根标签，如items，使其不显示
	 * @return XML字符串
	 */
	public static String bean2xml(Map<String, Class> clazzMap, Object bean, Map<Class, String> listMap) {
		XStream xstream = new XStream();
		if(clazzMap != null){
			for (Iterator it = clazzMap.entrySet().iterator(); it.hasNext();) {
				Map.Entry<String, Class> m = (Map.Entry<String, Class>) it.next();
				xstream.alias(m.getKey(), m.getValue());
			}
		}
		if(listMap != null){
			for (Iterator it = listMap.entrySet().iterator(); it.hasNext();) {
				Map.Entry<Class, String> m = (Map.Entry<Class, String>) it.next();
				xstream.addImplicitCollection(m.getKey(), m.getValue());
			}
		}
		String xml = xstream.toXML(bean);
		return xml;
	}

	/**
	 * 将XML转换为Bean
	 * 
	 * @param clazzMap
	 *            别名-类名映射Map
	 * @param xml
	 *            要转换为bean对象的xml字符串
	 * @param listMap
	 *            要屏蔽的list中的根标签，如items，使其不显示
	 * @return Java Bean对象
	 */
	public static Object xml2bean(Map<String, Class> clazzMap, String xml, Map<Class, String> listMap) {
		XStream xstream = new XStream(new DomDriver());
		if(clazzMap != null){
			for (Iterator it = clazzMap.entrySet().iterator(); it.hasNext();) {
				Map.Entry<String, Class> m = (Map.Entry<String, Class>) it.next();
				xstream.alias(m.getKey(), m.getValue());
			}
		}
		if(listMap != null){
			for (Iterator it = listMap.entrySet().iterator(); it.hasNext();) {
				Map.Entry<Class, String> m = (Map.Entry<Class, String>) it.next();
				xstream.addImplicitCollection(m.getKey(), m.getValue());
			}
		}
		Object bean = xstream.fromXML(xml);
		return bean;
	}
	public static void main(String[] args) {
		
	}
}
