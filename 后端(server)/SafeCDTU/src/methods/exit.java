package methods;

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

//退出方法
@WebServlet("/methods/exit")
public class exit extends HttpServlet {
	userDao dao=new userDao();
	java.sql.Connection connection=null;
	private static final long serialVersionUID = 1L;
       
    public exit() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String userId=request.getParameter("uId");
		JSONObject json=null;
		PrintWriter out=response.getWriter();
		try {
			connection=JDBCTools.getConnection();
			//将状态改为0  代表未登录
			String sql="UPDATE users SET state=0 where uId= ? ";
			setUserState(sql,connection,userId);
			json=new JSONObject();
			json.put("state", true);
        	json.put("message","退出成功" );
        	out.write(json.toString());
			
		} catch (Exception e) {
			json.put("state", false);
        	json.put("message","系统错误,退出失败" );
        	out.write(json.toString());
			e.printStackTrace();
			
		}finally{  
            JDBCTools.release(null, null, connection);
            out.flush();
            out.close();
        }  
		
	}
	private void setUserState(String sql,java.sql.Connection connection2,Object... args){
		try {
			dao.update(connection2, sql, args);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getUserState(String sql,java.sql.Connection connection2,Object... args){
		try {
			return dao.getForValue(connection2, sql, args);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


}
