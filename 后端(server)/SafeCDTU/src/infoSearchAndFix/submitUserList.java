package infoSearchAndFix;

import java.io.IOException;

import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;

import Tools.JDBCTools;
import eneityDAO.userDao;
import net.sf.json.JSONObject;

//提交自己管理的用户
@WebServlet("/infoSearchAndFix/submitUserList")
public class submitUserList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	userDao userDao=new userDao();
	PrintWriter out=null;
    public submitUserList() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uIdList=request.getParameter("uIdList");
		String uId=request.getParameter("uId");
		
		//将自己需要管理的人员的uId存入数组
		String subId[]=uIdList.split(",");
		
		try {
			json=new JSONObject();
			out=response.getWriter();
			connection=JDBCTools.getConnection();
			JDBCTools.beginThing(connection);
			
			//先将传来的所有用户的 父id清空
			cleaParId(uId,connection);
			
			//再设置自己管理的人员的父id
			setParId(subId,uId,connection);
			
			JDBCTools.commitThing(connection);
			json.put("state", true);
			json.put("message", "提交成功");
			out.write(json.toString());
		} catch (Exception e) {
			json.put("state", false);
			json.put("message", "系统错误,提交失败");
			out.write(json.toString());
			JDBCTools.rollbackThing(connection);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTools.release(null, null, connection);
			out.flush();
			out.close();
		}
	}
	
	private void cleaParId(String uId,java.sql.Connection connection2){
		String sql="UPDATE users SET parId='' WHERE parId= ? ";
		try {
			userDao.update(connection2, sql, uId);
		} catch (SQLException e) {
			json.put("state", false);
			json.put("message", "修改失败");
			out.write(json.toString());
			e.printStackTrace();
		}
	}
	
	private void setParId(String [] subId,String uId,java.sql.Connection connection2){
		String sql="UPDATE users SET parId= ? WHERE uId= ? ";
		
			try {
				for(String s:subId){
				userDao.update(connection2, sql, uId,s);
				}
			} catch (SQLException e) {
				json.put("state", false);
				json.put("message", "修改失败");
				out.write(json.toString());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

}
