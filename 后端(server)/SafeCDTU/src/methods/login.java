package methods;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.junit.Test;

import com.mysql.jdbc.Connection;
import com.ndktools.javamd5.Mademd5;
import com.ndktools.javamd5.core.MD5;

import Tools.JDBCTools;
import eneityDAO.departmentDao;
import eneityDAO.userDao;
import entity.Department;
import entity.User;
import net.sf.json.JSONObject;
import test.Goods;


//登录
@WebServlet("/methods/login")
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public login() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	
	
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		System.out.println("login");
//		request.setCharacterEncoding("utf-8");
//		HttpSession session=request.getSession();
		java.sql.Connection connection=null; 
		userDao dao =null;
		departmentDao departmentDao=null;
		Department department=null;
		User user=null;
		JSONObject json=null;
		Mademd5 md=new Mademd5();
		PrintWriter out = response.getWriter(); 
		
		
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		String veCode=request.getParameter("veCode");
		String keepLogon=request.getParameter("keepLogon");
		//System.out.println(keepLogon);
		password=md.toMd5(password);
		System.out.println("password:"+password);
		System.out.println("username:"+username);
		boolean oTrue=false;
        try {  
            connection=  JDBCTools.getConnection(); 
            JDBCTools.beginThing(connection);
            dao=new userDao();
            user=new User();
            department=new Department();
            departmentDao=new departmentDao();
            //通过账户查找该用户
            String sql="SELECT * " +  
                    "FROM users " +  
                    "WHERE account = ?";  
            user=dao.get(connection ,sql,username);
            
           
            
            if(user==null){
            	json=new JSONObject();
            	json.put("state", false);
            	json.put("message","没有该用户" );
                out.write(json.toString());  
            }
            else{
            	//得到用户的验证码,并且进行判断与用户输入的验证码是否一致
            	oTrue= user.getVeCode().toUpperCase().equals(veCode.toUpperCase());
            	 System.out.println("oTrue:"+oTrue);
            	 //得到用户的部门
            	String sql2="SELECT * FROM department WHERE depId= ? ";
                String depId=user.getDepId();
                department=departmentDao.get(connection, sql2, depId);
                
            	json=new JSONObject();
            	System.out.println("login:"+"user.getPswd():"+user.getPswd()+"password:"+password);
            	//判断密码
            	if(user.getPswd().equals(password)){
            		if(user.getState()==1){
            			json.put("state", false);
                    	json.put("message","该用户已经登录" );
                    	out.write(json.toString());
            		}else{
            			
            				System.out.println("veCode:"+veCode);
            				if(!oTrue){
                				json.put("state", false);
                            	json.put("message","验证码不正确" );
                            	out.write(json.toString());
                			}else{
//                				session.setAttribute("sessionUser",user );
//                				System.out.println(session+"sss");
                				json.put("state", true);
                            	json.put("message","登录成功" );
                            	json.put("user", user);
                            	json.put("department", department);
                            	out.write(json.toString());
                            	
                           
                            	 
//                                sql="UPDATE users SET state=1 where ? =uId";  
//                                dao.update(connection ,sql,user.getuId());
                			}
            			
            			
            			
            		}
                	 
            	}else{
            		json.put("state", false);
                	json.put("message","密码错误" );
                	out.write(json.toString());
            	}
            }
            JDBCTools.commitThing(connection);
        } catch (Exception e) {  
        	JDBCTools.rollbackThing(connection);
        	json.put("state", false);
        	json.put("message","系统错误,登录失败" );
        	out.write(json.toString());
            e.printStackTrace();  
        }finally{  
            JDBCTools.release(null, null, connection);
            out.flush();
            out.close();
        }  
    
	}

}
