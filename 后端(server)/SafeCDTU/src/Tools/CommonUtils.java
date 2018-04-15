package Tools;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.ndktools.javamd5.Mademd5;

import entity.Student;
import entity.User;

public class CommonUtils {
	
	private CommonUtils(){}
	
	/**
	 * 1. 每次用户修改密码、添加用户默认密码都必须md转换后放入数据库
	 * 2. 登陆时 要先把用户密码转md5后在去数据库中比较
	 */
	public static String md5TransForm(String value){
		Mademd5 md = new Mademd5();
		String temp = md.toMd5(value);
		if("".equals(temp)) return null;
		return temp;
	}	
	
	
	/**
	 * 填充参数
	 * 批量删除
	 */
	
	public static Object[][] getParams(ArrayList<String> params){
		
		Object[][] temParams = new Object[params.size()][];
		if(temParams != null && temParams.length != 0){
			for(int i = 0 ; i < temParams.length ; i++){
				temParams[i] = new Object[]{params.get(i)};
			}
		}
		return temParams;
	}
	
	public static Object[][] getParamsStu(ArrayList<Student> params){
		
		Object[][] temParams = new Object[params.size()][];
		if(temParams != null && temParams.length != 0){
			for(int i = 0 ; i < temParams.length ; i++){
				temParams[i] = new Object[]{params.get(i).getStuNumber()};
			}
		}
		return temParams;
	}
	
	
	/*
	 * 把map转换成对象    
	 * 反射创建Bean对象
	 */
	@SuppressWarnings("rawtypes")
	public static <T> T toBean(Map map , Class<T> clazz) {
		try{
		T bean = null; 
		bean = clazz.newInstance();
		BeanUtils.populate(bean, map);
		return bean;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}	
	
	/*
	 * 关闭流
	 */
	public static void closeConOut(Object...object){
		//遍历
		for(Object obj : object){
			if(obj instanceof Connection) close((Connection) obj);
			if(obj instanceof PrintWriter) close((PrintWriter) obj);
		}
	}
	private static void close(PrintWriter out){
		if(out != null){
			out.close();
System.out.println("关闭流");
		}
	}
	private static void close(Connection con){
		if(con != null){
			try {
				con.close();
System.out.println("关闭连接");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 条件判断
	 */
	private static boolean isNull(String field){
		if(field == null || "".equals(field))  return true;
		return false;
	}	
	
	
	/**
	 * SQL拼接
	 */
	public static String sqlTransFromStuMan(StringBuilder sql , Student student , boolean turn){
		
		boolean flag = false;
		if(!isNull(student.getStuNumber())){ 
//			sql.append("stuNumber = ? ");
			sql.append(" WHERE stuNumber like '" + student.getStuNumber() + "%' ");
			flag = true;				
		}
		if(!isNull(student.getName())){
			if(flag){
//				sql.append("AND name = ? ");
				sql.append("AND name like '%" + student.getName() + "%' ");
			}else{
				sql.append(" WHERE name like '%" + student.getName() + "%' ");
				flag = true;
			}
		}
		if(!isNull(student.getClasz())){
			if(flag){
//				sql.append("AND clasz = ? ");
				sql.append("AND clasz like '" + student.getClasz() + "%' ");
			}else{
				sql.append(" WHERE clasz like '" + student.getClasz() + "%' ");
				flag = true;
			}
		}			
		if(!isNull(student.getTel())){
			if(flag){
//				sql.append("AND tel = ? ");
				sql.append("AND tel like '" + student.getTel() + "%' ");
			}else{
				sql.append(" WHERE tel like '" + student.getTel() + "%' ");
				flag = true;
			}				
		}			
		if(!isNull(student.getCollege())){
			if(flag){
				sql.append("AND college like '" + student.getCollege() + "%' ");
			}else{
				sql.append(" WHERE college like '" + student.getCollege() + "%' ");
			}				
		}
		if(turn == true ){
			sql.append("ORDER BY clasz limit ? , ?");
		}
		return sql.toString();  
	}
	
	/**
	 * SQL拼接
	 */
	public static String sqlTransFromStu(StringBuilder sql , Student student , boolean turn , String params){
		
		boolean flag = false;
		if(!isNull(student.getStuNumber())){ 
			sql.append(" WHERE stuNumber like '" + student.getStuNumber() + "%' "
					+ "AND college = '" + params + "' ");
			flag = true;				
		}
		if(!isNull(student.getName())){
			if(flag){
				sql.append("AND name like '%" + student.getName() + "%' ");
			}else{
				sql.append(" WHERE name like '%" + student.getName() + "%' "
						+ "AND college = '" + params + "' ");
				flag = true;
			}
		}
		if(!isNull(student.getClasz())){
			if(flag){
				sql.append("AND clasz like '%" + student.getClasz() + "%' ");
			}else{
				sql.append(" WHERE clasz like '%" + student.getClasz() + "%' "
						+ "AND college = '" + params + "' ");
				flag = true;
			}
		}			
		if(!isNull(student.getTel())){
			if(flag){
				sql.append("AND tel like '%" + student.getTel() + "%' ");
			}else{
				sql.append(" WHERE tel like '%" + student.getTel() + "%' "
						+ "AND college = '" + params + "' ");
				flag = true;
			}				
		}			
		if(!isNull(student.getCollege())){
			if(flag){
				sql.append("AND college = '" + student.getCollege() + "%' ");
			}else{
				sql.append(" WHERE college like '%" + student.getCollege() + "%' "
						+ "AND college = '" + params + "' ");
			}				
		}
		
		if(turn == true ){
			sql.append("ORDER BY clasz limit ? , ?");
		}
		return sql.toString();  
	}
	
	
	/**
	 * SQL拼接 
	 */
	public static String sqlTransFromUser(StringBuilder sql , User user , boolean turn){
		
		boolean flag = false;
		if(!isNull(user.getAccount())){ 
			sql.append(" WHERE account like '%" + user.getAccount() + "%' ");
			flag = true;				
		}
		if(!isNull(user.getName())){
			if(flag){
				sql.append("AND name like '%" + user.getName() + "%' ");
			}else{
				sql.append(" WHERE name like '%" + user.getName() + "%' ");
				flag = true;
			}
		}
		if(!isNull(user.getTel())){
			if(flag){
				sql.append("AND tel like '%" + user.getTel() + "%' ");
			}else{
				sql.append(" WHERE tel like '%" + user.getTel() + "%' ");
			}
		}	
		
		if(turn == true){
			sql.append("limit ? , ?");
		}
		return sql.toString();
	}

}
