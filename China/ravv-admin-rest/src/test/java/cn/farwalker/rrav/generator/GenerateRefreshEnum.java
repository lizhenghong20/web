package cn.farwalker.rrav.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import cn.farwalker.ravv.service.web.slider.constants.PageNameEnum;
import cn.farwalker.waka.orm.EnumManager;
import cn.farwalker.waka.orm.EnumManager.IEnumJson;
import cn.farwalker.waka.util.Tools;
import cn.farwalker.waka.util.YMException;

/**
 * 刷新枚举
 * @author Administrator
  */
public class GenerateRefreshEnum {
	private static final String CRLF="\r\n";
	public static void main(String[] args){
		String clazzName =  readLine();
		if(clazzName.length()>0){
			List<? extends IEnumJson>  enums = getEnumValues(clazzName);
			updateEnumValues(enums);
		}
		else{ //
			Class[] clazz ={PageNameEnum.class
			};
			List alls = new ArrayList<>();
			for(Class c : clazz){
				List<? extends IEnumJson>  enums = getEnumValues(c.getName());
				alls.addAll(enums);
			}
			updateEnumValues(alls);
		}
	}
	private static String readLine(){
		System.out.println("输入枚举全类名:cn.farwalker.ravv.common.constants.ApplyStatusEnum,(空就全部)");
		try(Scanner scan = new Scanner(System.in)){
			String read = scan.nextLine();
			return read;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	private static List<? extends IEnumJson> getEnumValues(String clazzName) {
        try { 
            Class<?> clazz = Class.forName(clazzName);
            if(clazz.isEnum() && IEnumJson.class.isAssignableFrom(clazz)){
                return EnumManager.getEnumValues(clazz);
                /*Method method = clazz.getMethod("values");
                Object[] args = null;
                IEnumJson[] values = (IEnumJson[])method.invoke(null, args);
                return Arrays.asList(values);
                */
            }
            else{
                throw new YMException(clazzName + "没有实现IEnumJson的枚举接口");
            }
        }
        catch(RuntimeException e){
            throw e;
        }
        catch (ClassNotFoundException e) {
            throw new YMException("没有找到类:" + clazzName);
        } 
        catch (Exception e) {
            throw new YMException("没有找到类:" + clazzName);
        }
    }
	private static int updateEnumValues(List<? extends IEnumJson> values) {
		if(values == null || values.isEmpty()){
			return 0;
		}
		StringBuilder sb = new StringBuilder();
		String paths = "XXXX";
		String INSERT="INSERT INTO cw_enums (paths, project, ID, code, label, sequence) VALUES ('%s', 'RAVV', '%s', '%s', '%s', %s);";
		
        //重新写入
        int i =0;
        for(IEnumJson e : values){
    		String es = e.getClass().getName();
    		if(!es.equalsIgnoreCase(paths)){//删除旧记录
    			paths = es;//新的类
    			String delete = "delete from cw_enums where paths='" +  paths + "';"  + CRLF;
    	        sb.append(delete);
    		}
    		
        	String id =Tools.timerKey.getTimeId();
        	String code =e.getValue().toString();
        	String label = e.getLabel();
        	i+=10;
            String s = String.format(INSERT, paths, id,code,label, String.valueOf(i));
            sb.append(s).append(CRLF);
        }
        System.out.println("=============枚举数据 JSON===============");
        String json = showEnum(values);
        System.out.println(json);
        
        System.out.println("=============枚举数据 SQL===============");
        System.out.println(sb);
        return Integer.valueOf(values.size());
	}
	/**按枚举名输出json*/
	private static String showEnum(List<? extends IEnumJson> values) {
		// StringBuilder enums = new StringBuilder();
		Map<String, List<Map<String, Object>>> enums = new HashMap<>();
		for (IEnumJson e : values) {
			String clazz = e.getClass().getSimpleName();
			String paths = String.valueOf(clazz.charAt(0)).toLowerCase()
					+ clazz.substring(1);
			List<Map<String, Object>> enumx = enums.get(paths);
			if (enumx == null) {
				enumx = new ArrayList<>();
				enums.put(paths, enumx);
			}
			Map<String, Object> val = new HashMap<>();
			val.put("key", e.getValue());
			val.put("label", e.getLabel());
			enumx.add(val);
		}

		String json = Tools.json.toJson(enums);
		return json;
	}
}
