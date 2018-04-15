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

import org.apache.commons.lang.ObjectUtils.Null;

import com.mysql.jdbc.Connection;

import Tools.JDBCTools;
import eneityDAO.areaDao;
import eneityDAO.userDao;
import entity.Area;
import entity.User;
import net.sf.json.JSONObject;

//加载下一级区域
@WebServlet("/infoSearchAndFix/loadSubArea")
public class loadSubArea extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	areaDao areaDao=new areaDao();
	userDao userDao=new userDao();
	
	
	public loadSubArea() {
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
			
			List<Area> subAreaList=new ArrayList<Area>();
			Area thisAreaInfo=new Area();
			User user=new User();
			String areId=request.getParameter("areId");
			String uId=request.getParameter("uId");
			connection=JDBCTools.getConnection();
			
			//查找下级区域
			subAreaList=searchSubArea(connection,areId);
			//查找本区域信息
			thisAreaInfo=searchThisArea(connection,areId);
			
			//该区域的负责人不为空
			if((!uId.equals(""))&&(!uId.equals("undefined"))){
				//查找负责人信息
				user=searchThisUser(connection, uId);
				if(user==null){
					User newUser=new User();
					newUser.setuId("");
					newUser.setName("该区域负责人已被删除，需重新选择");
					newUser.setTel("");
					newUser.setEmail("");
					newUser.setuId("");
					json.put("user", newUser);
					
				}
				else{
					
					json.put("user", user);
				}
//				System.out.println("user:"+user.toString());
			}
			else{
				//该区域的负责人为空
				User nullUser=new User();
				nullUser.setName("");
				nullUser.setTel("");
				nullUser.setEmail("");
				nullUser.setuId("");
				json.put("user", nullUser);
//				System.out.println("user:"+nullUser.toString());
			}
			
//			System.out.println("44");
			
			
			
		//	System.out.println("user:"+user.toString());
			
			//下级区域不为0时
			if(subAreaList.size()!=0){
				//返回下级区域的集合
				json.put("subAreaList", subAreaList);
			}
			else{
				json.put("subAreaList", "");
			}
			
//		System.out.println(subAreaList.size());
//			for(Area a:subAreaList){
//				
//				System.out.println(a.getName());
//				System.out.println(a.getDiscribe());
//			}
			
			
			//返回本区域的信息
			json.put("thisAreaInfo", thisAreaInfo);
			out.write(json.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{  
            JDBCTools.release(null, null, connection);
            out.flush();
            out.close();
        }  
	}
	
	private List<Area> searchSubArea(java.sql.Connection connection2,String areId){
		String sql="select * from area where parAreaId = ?";
		try {
			return areaDao.getForList(connection2, sql, areId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	private Area searchThisArea(java.sql.Connection connection2,String areId){
		String sql="select * from area where areId = ?";
		try {
			return areaDao.get(connection2, sql, areId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	private User searchThisUser(java.sql.Connection connection2,String uId){
		String sql="select * from users where uId = ?";
		try {
			if(uId.equals("")){
				return null;
			}
			else{
				return userDao.get(connection2, sql, uId);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

}
