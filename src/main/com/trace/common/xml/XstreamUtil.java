package com.trace.common.xml;


import java.util.Iterator;
import java.util.Map;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;

@SuppressWarnings("unchecked")
public final class XstreamUtil {
	/**
	 * 将Bean转换为XML
	 * 
	 * @param clazzMap
	 *            别名-类名映射Map
	 * @param bean
	 *            要转换为xml的bean对象
	 * @param imMap
	 *            要屏蔽的list中的根标签，如items，使其不显示
	 * @return XML字符
	 */
	public static String bean2xml(String encoding, Map<String, Class<?>> clazzMap, Object bean, Map<Class<?>, String> imMap) {
		XStream xstream = null;
		if (encoding == null)
			xstream = new XStream();
		else
			xstream = new XStream(new DomDriver(encoding));
		if(clazzMap != null){
			for (Iterator<?> it = clazzMap.entrySet().iterator(); it.hasNext();) {
				Map.Entry<String, Class<?>> m = (Map.Entry<String, Class<?>>) it.next();
				xstream.alias(m.getKey(), m.getValue());
			}
		}
		if(imMap != null){
			for (Iterator<?> it = imMap.entrySet().iterator(); it.hasNext();) {
				Map.Entry<Class<?>, String> m = (Map.Entry<Class<?>, String>) it.next();
				xstream.addImplicitCollection(m.getKey(), m.getValue());
			}
		}
		String xml = xstream.toXML(bean);
		return xml;
	}

	/**
	 * 将Bean转换为JSON
	 * 
	 * @param clazzMap
	 *            别名-类名映射Map
	 * @param bean
	 *            要转换为xml的bean对象
	 * @param imMap
	 *            要屏蔽的list中的根标签，如items，使其不显示
	 * @return XML字符
	 */
	public static String bean2json(Map<String, Class<?>> clazzMap, Object bean, Map<Class<?>, String> imMap) {
		XStream xstream = new XStream(new JsonHierarchicalStreamDriver());
		if(clazzMap != null){
			for (Iterator<?> it = clazzMap.entrySet().iterator(); it.hasNext();) {
				Map.Entry<String, Class<?>> m = (Map.Entry<String, Class<?>>) it.next();
				xstream.alias(m.getKey(), m.getValue());
			}
		}
		if(imMap != null){
			for (Iterator<?> it = imMap.entrySet().iterator(); it.hasNext();) {
				Map.Entry<Class<?>, String> m = (Map.Entry<Class<?>, String>) it.next();
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
	 *            要转换为bean对象的xml字符
	 * @return Java Bean对象
	 */
	public static Object xml2Bean(Map<String, Class<?>> clazzMap, String xml) {
		XStream xstream = new XStream();
		if(clazzMap != null){
			for (Iterator<?> it = clazzMap.entrySet().iterator(); it.hasNext();) {
				Map.Entry<String, Class<?>> m = (Map.Entry<String, Class<?>>) it.next();
				xstream.alias(m.getKey(), m.getValue());
			}
		}
		Object bean = xstream.fromXML(xml);
		return bean;
	}
	
	public static Object xml2Bean(Map<String, Class<?>> clazzMap, Map<Class<?>, String> imMap, String xml) {
		XStream xstream = new XStream(new DomDriver());
		if(clazzMap != null){
			for (Iterator<?> it = clazzMap.entrySet().iterator(); it.hasNext();) {
				Map.Entry<String, Class<?>> m = (Map.Entry<String, Class<?>>) it.next();
				xstream.alias(m.getKey(), m.getValue());
			}
		}
		if(imMap != null){
			for (Iterator<?> it = imMap.entrySet().iterator(); it.hasNext();) {
				Map.Entry<Class<?>, String> m = (Map.Entry<Class<?>, String>) it.next();
				xstream.addImplicitCollection(m.getKey(), m.getValue());
			}
		}
		Object bean = xstream.fromXML(xml);
		return bean;
	}
}