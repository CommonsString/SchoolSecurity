package infoSearchAndFix;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import Tools.JDBCTools;
import eneityDAO.areaDao;
import eneityDAO.departmentDao;
import eneityDAO.userDao;
import net.sf.json.JSONObject;

//修改区域的信息
@WebServlet("/infoSearchAndFix/alterArea")
public class alterArea extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	areaDao areaDao=new areaDao();
	userDao userDao=new userDao();
	departmentDao departmentDao=new departmentDao();
    public alterArea() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("aba");
		json=new JSONObject();
		PrintWriter out=response.getWriter();
		String number=request.getParameter("number");
		String uId=request.getParameter("uId");
		String name=request.getParameter("name");
		String discribe=request.getParameter("discribe");
		String areId=request.getParameter("areId");
		String parAreaId=request.getParameter("parAreaId");
		
		System.out.println("number:"+number+" uId:"+uId+" name:"+name+" discribe:"+discribe+" areId:"+areId);
		
		try {
			connection=JDBCTools.getConnection();
			JDBCTools.beginThing(connection);
			String sql="UPDATE area SET number= ? ,name= ? ,uId= ? , discribe= ?   WHERE areId= ? ";
			areaDao.update(connection, sql,number,name,uId,discribe,areId);
			
			if(parAreaId==null){
				
			}else{
				if(parAreaId.equals("0")){
					String sql1="UPDATE department SET number= ? ,name= ?   WHERE depId= ? ";
					departmentDao.update(connection, sql1,number,name,areId);
				} 
			}
			
			
			if(!uId.equals("")){
				changLevelTo2(connection,uId);
			}
			JDBCTools.commitThing(connection);
			json.put("state", true);
			json.put("message", "修改成功");
			out.write(json.toString());
		} catch (Exception e) {
			JDBCTools.rollbackThing(connection);
			json.put("state", false);
			json.put("message", "系统错误,修改失败");
			out.write(json.toString());
			e.printStackTrace();
		}finally{  
            JDBCTools.release(null, null, connection);
            out.flush();
            out.close();
        }  
	}

	private void changLevelTo2(Connection connection2, String uId) {
			String sql="UPDATE users SET level= '2' WHERE uId= ? ";
			try {
				userDao.update(connection2, sql, uId);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
