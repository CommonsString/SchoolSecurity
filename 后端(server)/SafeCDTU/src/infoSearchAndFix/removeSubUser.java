package infoSearchAndFix;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;

import Tools.JDBCTools;
import eneityDAO.areaDao;
import eneityDAO.userDao;
import entity.Area;
import net.sf.json.JSONObject;


//该servlet未使用
@WebServlet("/infoSearchAndFix/removeSubUser")
public class removeSubUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	userDao uesrDao=new userDao();
	areaDao areaDao=new areaDao();
	
    public removeSubUser() {
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
			if(!searchHasSubArea(connection,subId)){
				String sql="UPDATE  users set parId = NULL WHERE uId= ? ";
				uesrDao.update(connection, sql,subId);
				json.put("state", true);
				json.put("message", "取消成功");
				out.write(json.toString());
			}else{
				json.put("state", false);
				json.put("message", "先删除该用户负责的区域，或者更换该区域的负责人");
				out.write(json.toString());
			}
			
			
			
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

	private boolean searchHasSubArea(java.sql.Connection connection2,String subId){
		
		try {
			List<Area> areaList=new ArrayList<Area>();
			
			String sql="SELECT * FROM area WHERE uId=?";
			areaList=areaDao.getForList(connection2, sql, subId);
			if(areaList.size()==0){
				return false;
			}
			else{
				return true;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
}
