package infoSearchAndFix;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import Tools.JDBCTools;
import eneityDAO.departmentDao;
import eneityDAO.userDao;
import entity.Department;
import entity.User;
import net.sf.json.JSONObject;

//查找自己的信息
@WebServlet("/infoSearchAndFix/getOwnInfo")
public class getOwnInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	java.sql.Connection connection2=null;
	JSONObject json=null; 
	PrintWriter out=null;
	departmentDao departmentDao=new departmentDao();
	userDao userDao=new userDao();
    public getOwnInfo() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String uId=request.getParameter("uId");
			//System.out.println("getOwnINFO userId: "+uId);
			try {
				connection=JDBCTools.getConnection();
				connection2=JDBCTools.getConnection();
				json=new JSONObject();
				out=response.getWriter();
				
				User user=new User();
				if(!"".equals(uId)){
				//查找自己的信息
				String sql="SELECT * FROM users WHERE uId= ? ";
				
				user=userDao.get(connection, sql, uId);
				if(user!=null){
					json.put("user", user);
				}
				else{
					json.put("user", "");
				}
			}
			
			if(!"".equals(user.getDepId())){
				//查找自己的部门
				String sql="SELECT * FROM department WHERE depId= ? ";
				Department department=new Department();
				department=departmentDao.get(connection2, sql, user.getDepId());
				if(department!=null){
					json.put("department", department);
				}
				else{
					json.put("department", "");
				}
			}
			out.write(json.toString());
				
				
//				//查找自己的信息
//				String sql="SELECT * FROM users WHERE uId= ? ";
//				User user=new User();
//				user=userDao.get(connection, sql, uId);
//				
//				//查找自己的部门
//				sql="SELECT * FROM department WHERE depId= ? ";
//				Department department=new Department();
//				department=departmentDao.get(connection2, sql, user.getDepId());
//				
//				json.put("user", user);
//				json.put("department", department);
//				out.write(json.toString());
				
				
				
//			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				JDBCTools.release(null, null, connection);
				JDBCTools.release(null, null, connection2);
				out.flush();
				out.close();
			}
		
	}

}
