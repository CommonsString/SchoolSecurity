package infoSearchAndFix;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Tools.JDBCTools;
import eneityDAO.userDao;
import net.sf.json.JSONObject;

//该serlvet未使用
@WebServlet("/infoSearchAndFix/selectSubUser")
public class selectSubUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	userDao uesrDao=new userDao();
	
    public selectSubUser() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json=null;
		java.sql.Connection connection=null;
		PrintWriter out=null;
	
		try {
			json=new JSONObject();
			out=response.getWriter();
			String uId=request.getParameter("uId");
			String subId=request.getParameter("subId");
			connection=JDBCTools.getConnection();
			String sql="UPDATE  users set parId= ? WHERE uId= ? ";
			uesrDao.update(connection, sql, uId,subId);
			json.put("state", true);
			json.put("message", "选择成功");
			out.write(json.toString());
		} catch (Exception e) {
			json.put("state", false);
			json.put("message", "系统错误,选择失败");
			out.write(json.toString());
			e.printStackTrace();
		}finally{  
            JDBCTools.release(null, null, connection);
            out.flush();
            out.close();
        }  
		
	}

}
