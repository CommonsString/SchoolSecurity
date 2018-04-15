package infoSearchAndFix;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.print.DocFlavor.STRING;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Out;
import org.omg.Messaging.SyncScopeHelper;

import com.mysql.jdbc.Connection;

import Tools.JDBCTools;
import eneityDAO.areaDao;
import eneityDAO.userDao;
import entity.Area;
import entity.User;
import net.sf.json.JSONObject;

//查找下级用户(未使用)
@WebServlet("/infoSearchAndFix/findSubUser")
public class findSubUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	userDao userDao=new userDao();
       
    public findSubUser() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		json=new JSONObject();
		PrintWriter out=response.getWriter();
		List<User> subUserList=new ArrayList<User>();
		String parAreaId=request.getParameter("areId");
		
//		System.out.println("parAreaId1:"+parAreaId);
		try {
			if(parAreaId.equals("00")){
				//不存在区域id为00的区域  只是用00作为父区域id  表示顶级区域
			}
			else{
				connection=JDBCTools.getConnection();
				//查找下级用户
				subUserList=findSubUserForAreId(connection,parAreaId);
			}
			
			
			
			
//		for(User a:subUserList){
//				
//				System.out.println("2:"+a.getName());
////				System.out.println(a.());
//			}	
			json.put("subUserList", subUserList);
			
			out.write(json.toString());
		} catch (Exception e) {

			e.printStackTrace();
		}finally{  
            JDBCTools.release(null, null, connection);
            out.flush();
            out.close();
        }  
	}
	private List<User> findSubUserForAreId(java.sql.Connection connection2, String parAreaId) {
//		System.out.println("findSubUserForAreId");
		try {
			List<User> list=new ArrayList<User>();
			//先查找出当前区域的负责人
			String sql="SELECT uId FROM area Where areId= ? ";
			String uId="";
			uId=userDao.getForValue(connection2, sql, parAreaId);
			String sql2;
			System.out.println("parAreaId:"+parAreaId);
			
			if(uId.equals("")){
					
			}else{
				
				if(parAreaId.equals("0")){
					 sql2="SELECT * FROM users WHERE (depId is not null and depId<>'')";
					 list=userDao.getForList(connection2, sql2 );
				}
				else{
					//通过当前区域的负责人  查找出他的下级
					//String sql2="SELECT * FROM users WHERE parId= ? ";//修改(旧)
					//list=userDao.getForList(connection2, sql2,uId );//修改(旧)
					 sql2="SELECT * FROM users WHERE depId IN(SELECT depId From users WHERE uId= ? )";	//修改(新)
					 list=userDao.getForList(connection2, sql2,uId );//修改(新)
				}
				
			}
			
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

//	private List<User> findSubUserForId(java.sql.Connection connection2,String uId){
//		
//		try {
//			String sql="SELECT * FROM users WHERE parId= ?";
//			List<User> list=new ArrayList<User>();
//			list=userDao.getForList(connection2, sql, uId);
//			return list;
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
}
