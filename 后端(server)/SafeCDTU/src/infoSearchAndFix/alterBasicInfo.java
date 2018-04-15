package infoSearchAndFix;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;


import Tools.JDBCTools;
import eneityDAO.userDao;
import net.sf.json.JSONObject;

//修改自己的基本信息
@WebServlet("/infoSearchAndFix/alterBasicInfo")
public class alterBasicInfo extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	userDao dao=new userDao();
	java.sql.Connection connection=null;
	JSONObject json=null;
       
    public alterBasicInfo() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out=response.getWriter();
		String uId=request.getParameter("uId");
		String name=request.getParameter("name");
		String tel=request.getParameter("tel");
		String email=request.getParameter("email");
		String depId=request.getParameter("depId");
		json=new JSONObject();
		String sql;
//		System.out.println(uId+":"+name+":"+tel+":"+email+":"+depId);
		try {
			connection =JDBCTools.getConnection();
			if (!name.trim().equals(name)) {
				json.put("state", false);
				json.put("message", "姓名中间不能有空格");
				out.write(json.toString());
				
			}else{
				 if(name.equals("")){
					json.put("state", false);
					json.put("message", "姓名不能为空");
					out.write(json.toString());
					
				}else {
					if(tel.equals("")){
						json.put("state", false);
						json.put("message", "电话不能为空");
						out.write(json.toString());
					}
					else {
						if(!checkMobileNumber(tel)){
							json.put("state", false);
							json.put("message", "电话格式错误");
							out.write(json.toString());
						}
						else{
							if(depId.equals("")){
								json.put("state", false);
								json.put("message", "部门不能为空");
								out.write(json.toString());
							}else{

								if(email.equals("")){
									sql="UPDATE users SET name=? , tel=? ,depId= ?  WHERE uId=?";
									dao.update(connection, sql, name,tel,depId,uId);
									json.put("state", true);
									json.put("message", "修改成功");
									out.write(json.toString());
									
								}else{
									if(!checkEmail(email)){
										json.put("state", false);
										json.put("message", "email格式错误");
										out.write(json.toString());
									}else{
//										System.out.println("aa");
										sql="UPDATE users SET name=?  ,  tel=? , email=? ,depId= ? WHERE uId=?";
										dao.update(connection, sql, name,tel,email,depId,uId);
										json.put("state", true);
										json.put("message", "修改成功");
										out.write(json.toString());
									}
									
								}
							
							}
						}
					}
				}
			}
			
		} catch (Exception e) {
			json.put("state", false);
			json.put("message", "系统错误");
			out.write(json.toString());
			e.printStackTrace();
			
		}finally{  
            JDBCTools.release(null, null, connection);
            out.flush();
            out.close();
        }  
		
		
		
		
			
		
	}
	
	public  boolean checkMobileNumber(String mobileNumber){
	    boolean flag = false;
	    try{
	        Pattern regex = Pattern.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
	        Matcher matcher = regex.matcher(mobileNumber);
	        flag = matcher.matches();
	      }catch(Exception e){
	        flag = false;
	      }
	    return flag;
	  }
	
	public  boolean checkEmail(String email){
	    boolean flag = false;
	    try{
	        Pattern regex = Pattern.compile("\\w+@\\w+(\\.\\w+)+");
	        Matcher matcher = regex.matcher(email);
	        flag = matcher.matches();
	      }catch(Exception e){
	        flag = false;
	      }
	    return flag;
	  }

}
