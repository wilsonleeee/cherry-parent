package com.cherry.cm.util;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.cherry.cm.cmbeans.UserInfo;
import com.googlecode.jsonplugin.JSONUtil;

public class DataUtil {

	private static final String POINT = ".";
	private static final String JSON = ".json";
	private static final String DATA_FORM = "form";
	private static final String DATA_USERINFO = "userinfo";
	private static final String DATA_OTHERFORMDATA = "otherFormData";
	private static final String DATA_SETSESSION = "setSession";
	private static final String DATA_GETMODEL = "getModel";
	private static final String DATA_DATALIST = "dataList";
	/**
	 * 取得为指定测试类中正在运行的测试方法设定的测试数据，返回的是与测试方法相对应的所有的数据，该方法只能在标注了@Test注解的方法下运行。
	 * @param clzz 指定的测试类
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getDataMap(Class clzz)
			throws Exception {
		String methodName = getRunningMethod(clzz);
		// JSON数据文件
		File file = getJsonFile(clzz);
		return getMapInFile(file, methodName);

	}
	/**
	 * 取得为指定测试类中指定的测试方法设定的测试数据，这里返回的是与测试方法相对应的所有的数据，该方法可以在任何地方调用。
	 * @param clzz 指定的测试类
	 * @param caseName 指定的测试case名称（测试case方法名）
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getDataMap(Class clzz,
			String caseName) throws Exception {
		File file = getJsonFile(clzz);
		return getMapInFile(file, caseName);

	}
	/**
	 * 在clzz所在的包下与clzz同名的JSON文件
	 * 
	 * @param clzz
	 * @return
	 * @throws Exception
	 */
	private static File getJsonFile(Class clzz) throws Exception {
		String path = clzz.getResource("").getPath();
		String name = clzz.getName();
		name = name.substring(name.lastIndexOf(POINT)+1);
		path +=  name + JSON;
		return new File(path);
	}
	/**
	 * 在clzz所在的包下取得名为name的文件
	 * 
	 * @param clzz
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static File getFile(Class clzz,String name) throws Exception {
		String path = clzz.getResource("").getPath();
		path += name;
		return new File(path);
	}

	/**
	 * 在file文件中查找关键字为key的属性值
	 * @param file
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static Map<String, Object> getMapInFile(File file, String key)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if (file.exists()) {
			String json = FileUtils.readFileToString(file,"UTF-8");
			Map<String, Object> mainMap = (Map<String, Object>) JSONUtil
					.deserialize(json);
			if (null != mainMap) {
				map = (Map<String, Object>) mainMap.get(key);
			}
		}
		return map;
	}

	/**
	 * 取得运行时clzz类正在执行的方法名
	 * 
	 * @param clzz
	 * @return
	 * @throws Exception
	 */
	private static String getRunningMethod(Class clzz)
			throws Exception {
		String result = null;
		Thread th = Thread.currentThread();
		StackTraceElement[] elements = th.getStackTrace();
		for (StackTraceElement element : elements) {
			if (element.getClassName().equals(clzz.getName())) {
				result = element.getMethodName();
				break;
			}
		}
		return result;
	}
	
	/**
	 * 给指定的对象的属性注入值，参数map中存在并且也属于该对象的属性才能被正确注入。
	 * @param t 要注入的对象
	 * @param map 存放属性对应的值
	 * 
	 * @return t 注入属性值后的对象
	 * 
	 * */
	public static <T> T injectObject(T t,Map<String,Object> map) throws Exception{
		PropertyDescriptor[] ps = Introspector.getBeanInfo(t.getClass()).getPropertyDescriptors();
		for(PropertyDescriptor properdesc : ps){
			String properName = properdesc.getName();
			if(map.containsKey(properName)){
				//取得属性的setter方法
				Method setter = properdesc.getWriteMethod();
				setter.setAccessible(true);
				Class[] setProType = setter.getParameterTypes();
				if(setter != null){
					Object value = map.get(properName);
					//判断是否为参数类型是否为数组类型
					if(setProType[0].isArray() && value instanceof java.util.List ){
						Class paramtype = setProType[0].getComponentType();
						String paramTypeName = paramtype.getSimpleName();
						int length = ((List)value).size();
						Object[] parameter=new Object[1];
						if(paramTypeName.equals("String")){
							String[] strArr = new String[length];
							for(int index = length-1 ; index >= 0 ; index--){
								strArr[index] = (String)((List)value).get(index);
							}
							parameter[0] = strArr;
						}else if(paramTypeName.equals("int")||paramTypeName.equals("Integer")){
							int[] intArr = new int[length];
							for(int index = length-1 ; index >= 0 ; index--){
								intArr[index] = Integer.parseInt((String.valueOf(((List)value).get(index))));
							}
							parameter[0] = intArr;
						}else if(paramTypeName.equals("double")||paramTypeName.equals("Double")){
							double[] douArr = new double[length];
							for(int index = length-1 ; index >= 0 ; index--){
								douArr[index] = Double.parseDouble((String.valueOf(((List)value).get(index))));
							}
							parameter[0] = douArr;
						}else if(paramTypeName.equals("float")||paramTypeName.equals("Float")){
							float[] floArr = new float[length];
							for(int index = length-1 ; index >= 0 ; index--){
								floArr[index] = Float.parseFloat(((String.valueOf(((List)value).get(index)))));
							}
							parameter[0] = floArr;
						}
						setter.invoke(t,parameter);
					}else if(setProType[0].toString().equals("int")){
						int newValue = Integer.parseInt(String.valueOf(value));
						setter.invoke(t, newValue);
					}else{
						setter.invoke(t, value);
					}
				}
			}
		}
		return t;
	}

	/**
	 * 取得指定的测试类对象中正在运行的测试数据，并将form（otherFromData）和userinfo中的数据设定到指定action中，返回测试数据，该方法只能在标注了@Test注解的方法下调用，调用该方法后不需要再手动将userinfo和form加入到action，建议使用。
	 * @param clzz 测试代码所属类
	 * @param t 要测试的action对象
	 * 
	 * @return dataMap 返回测试数据
	 * 
	 * 
	 * */
	public static <T1,T2> Map setTestData(T1 t,T2 t1) throws Exception {
		
		Map dataMap = getDataMap(t.getClass());
		
		Map<String,Object> realData = new HashMap<String,Object>();
		
		if (dataMap.containsKey(DATA_FORM)) {

			Map<String,Object> formData = (Map) dataMap.get(DATA_FORM);
			
			for(Map.Entry<String, Object> en : formData.entrySet()){
				realData.put(en.getKey(), ((Map)en.getValue()).get("value"));
			}
			
		}
		
		if(dataMap.containsKey(DATA_OTHERFORMDATA)){
			Map<String,Object> otherFormData = (Map) dataMap.get(DATA_OTHERFORMDATA);
			realData.putAll(otherFormData);
		}
		
		if(!realData.isEmpty()){
			// 利用反射机制取得getModel方法,执行该方法取得返回action中的form
			Method method = t1.getClass().getMethod(DATA_GETMODEL, null);
			Object form = method.invoke(t1, null);
			DataUtil.injectObject(form, realData);
		}

		if (dataMap.containsKey(DATA_USERINFO)) {

			Map userData = (Map) dataMap.get(DATA_USERINFO);

			if(!userData.isEmpty()){
				// 取得UserInfo对象
				UserInfo userInfo = new UserInfo();
				// 往userInfo中注入值
				DataUtil.injectObject(userInfo, userData);
				//取得测试类中的setSession方法
				Class[] parameterType = new Class[2];
				parameterType[0] = String.class;
				parameterType[1] = Object.class;
				Method setSession = t.getClass().getSuperclass().getMethod(DATA_SETSESSION,parameterType);
				//设定为可见
				setSession.invoke(t, DATA_USERINFO,userInfo);
				
				//取得要测试的action中的setSession方法
				Method setSession1 = t1.getClass().getMethod(DATA_SETSESSION,Map.class);
				Map<String,Object> session = new HashMap<String,Object>();
				session.put(DATA_USERINFO, userInfo);
				setSession1.invoke(t1, session);
			}
		}

		return dataMap;
	}
	
	/**
	 * 根据指定的测试case中的测试数据设定form对象,并返回设定属性值的form对象。
	 * @param clzz 当前测试类
	 * @param caseName 测试方法名(测试case名称)
	 * @param t 需要设定属性值的form对象
	 * 
	 * @return form 返回设定属性值后的form对象
	 * 
	 * */
	public static <T> T getForm(Class clzz,String caseName,T t)throws Exception{
		
		//取得某个测试case中的所有的测试数据
		Map<String, Object> dataMap = getDataMap(clzz,caseName);
		
		if(null == t) return null;
		
		Map<String,Object> realData = new HashMap<String,Object>();
		
		if(dataMap.containsKey(DATA_FORM)){
			Map<String,Object> formData = (Map) dataMap.get(DATA_FORM);
			
			for(Map.Entry<String, Object> en : formData.entrySet()){
				realData.put(en.getKey(), ((Map)en.getValue()).get("value"));
			}
			
		}
		
		if(dataMap.containsKey(DATA_OTHERFORMDATA)){
			Map<String,Object> otherFormData = (Map) dataMap.get(DATA_OTHERFORMDATA);
			realData.putAll(otherFormData);
		}
		
		if(!realData.isEmpty()){
			DataUtil.injectObject(t, realData);
		}
		
		return t;
	}
	
	/**
	 * 根据正在运行的测试case给form设定属性值，并返回设定好属性值的form对象。
	 * @param clzz 指定的测试类
	 * @param t 需要设定属性值的form对象
	 * 
	 * @return t 返回设定属性值后的form对象
	 * 
	 * */
	public static <T> T getForm(Class clzz,T t)throws Exception{
		
		//取得正在运行的方法的方法名
		String methodName = getRunningMethod(clzz);
		
		//取得某个测试case中的所有的测试数据
		Map<String, Object> dataMap = getDataMap(clzz,methodName);
		
		if(null == t) return null;
		
		Map<String,Object> realData = new HashMap<String,Object>();
		if(dataMap.containsKey(DATA_FORM)){
			Map<String,Object> formData = (Map) dataMap.get(DATA_FORM);
			for(Map.Entry<String, Object> en : formData.entrySet()){
				realData.put(en.getKey(), ((Map)en.getValue()).get("value"));
			}
		}
		if(dataMap.containsKey(DATA_OTHERFORMDATA)){
			Map<String,Object> otherFormData = (Map) dataMap.get(DATA_OTHERFORMDATA);
			realData.putAll(otherFormData);
		}
		if(!realData.isEmpty()){
			DataUtil.injectObject(t, realData);
		}
		
		return t;
	}
	
	/**
	 * 取得正在运行的测试case中的form数据，并返回Map数据，供Selenium+Junit4测试使用
	 * @param clzz 指定的测试类
	 * 
	 * @return formData 返回from数据
	 * 
	 * */
	public static Map getFormData(Class clzz)throws Exception{
		
		//取得正在运行的方法的方法名
		String methodName = getRunningMethod(clzz);
		
		//取得某个测试case中的所有的测试数据
		Map<String, Object> dataMap = getDataMap(clzz,methodName);
		
		Map formData = null;
		
		if(dataMap.containsKey(DATA_FORM)){
			formData = (Map) dataMap.get(DATA_FORM);
		}
		
		return formData;
	}
	
	/**
	 * 取得指定的测试case中的form数据,并返回Map数据,供Selenium+Junit4测试使用
	 * @param clzz 指定的测试类
	 * @param caseName 测试case名称(测试方法名)
	 * 
	 * @return formData 返回from数据
	 * 
	 * */
	public static Map getFormData(Class clzz,String caseName)throws Exception{
		
		//取得某个测试case中的所有的测试数据
		Map<String, Object> dataMap = getDataMap(clzz,caseName);
		
		Map formData = null;
		
		if(dataMap.containsKey(DATA_FORM)){
			formData = (Map) dataMap.get(DATA_FORM);
		}
		
		return formData;
	}
	
	/**
	 * 根据指定测试case中测试数据设定的userinfo值，并返回userinfo对象。
	 * @param clzz 指定的测试类
	 * @param caseName 测试方法名 (测试case名称)
	 * 
	 * @return userinfo 返回userinfo对象
	 * 
	 * */
	public static UserInfo getUserInfo(Class clzz,String caseName) throws Exception{
		
		//取得某个测试case中的所有的测试数据
		Map<String, Object> dataMap = getDataMap(clzz,caseName);
		
		UserInfo userInfo = new UserInfo();
		
		if(dataMap.containsKey(DATA_USERINFO)){
			Map userInfoMap = (Map) dataMap.get(DATA_USERINFO);
			if(!userInfoMap.isEmpty()){
				// 往userInfo中注入值
				DataUtil.injectObject(userInfo, userInfoMap);
			}
		}
		//打出userinfo信息
        Set<Entry<String, Object>> set = dataMap.entrySet();
        for (Iterator<Map.Entry<String, Object>> it = set.iterator(); it.hasNext();) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
            System.out.println(entry.getKey() + "============》" + entry.getValue());
        }
		
		return userInfo;
	}
	
	/**
	 * 根据正在运行的测试case中测试数据设定的userinfo值,并返回userinfo对象。
	 * @param clzz 指定的测试类
	 * 
	 * @return userinfo 返回userinfo对象
	 * 
	 * */
	public static UserInfo getUserInfo(Class clzz) throws Exception{
		
		//取得正在运行的方法的方法名
		String methodName = getRunningMethod(clzz);
		
		//取得某个测试case中的所有的测试数据
		Map<String, Object> dataMap = getDataMap(clzz,methodName);
		
		UserInfo userInfo = new UserInfo();
		
		if(dataMap.containsKey(DATA_USERINFO)){
			Map userInfoMap = (Map) dataMap.get(DATA_USERINFO);
			if(!userInfoMap.isEmpty()){
				// 往userInfo中注入值
				DataUtil.injectObject(userInfo, userInfoMap);
			}
		}
		
		return userInfo;
	}
	
	/**
	 * 取得指定测试case中设定的dataList，该方法可以在任何地方被调用。
	 * @param clzz 指定的测试类
	 * @param caseName 测试方法名(测试case名称)
	 * 
	 * @return dataList 返回的数据
	 * 
	 * */
	public static List getDataList(Class clzz,String caseName) throws Exception{
		
		//取得某个测试case中的所有的测试数据
		Map<String, Object> dataMap = getDataMap(clzz,caseName);
		
		List list = null;
		if(dataMap.containsKey(DATA_DATALIST)){
			list = (List) dataMap.get(DATA_DATALIST);
		}
		return list;
	}
	
	/**
	 * 取得正在运行的测试case中设定的dataList,该方法只能在标注了@Test注解的方法下调用。
	 * @param clzz 指定的测试类
	 * 
	 * @return dataList 返回的数据
	 * 
	 * */
	public static List getDataList(Class clzz) throws Exception{
		
		//取得正在运行的方法的方法名
		String methodName = getRunningMethod(clzz);
		
		//取得某个测试case中的所有的测试数据
		Map<String, Object> dataMap = getDataMap(clzz,methodName);
		
		List dataList = null;
		if(dataMap.containsKey(DATA_DATALIST)){
			dataList = (List) dataMap.get(DATA_DATALIST);
		}
		return dataList;
	}
	

}
