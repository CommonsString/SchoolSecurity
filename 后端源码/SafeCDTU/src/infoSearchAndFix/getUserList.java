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

import Tools.JDBCTools;
import eneityDAO.userDao;
import entity.User;
import net.sf.json.JSONObject;

@WebServlet("/infoSearchAndFix/getUserList")
public class getUserList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	userDao uesrDao=new userDao();
    public getUserList() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json=null;
		java.sql.Connection connection=null;
		PrintWriter out=null;
		List<User> userList=new ArrayList<User>();
System.out.println("aaa");
		try {
			json=new JSONObject();
			out=response.getWriter();
			String uId=request.getParameter("uId");
			String depId=request.getParameter("depId");
			System.out.println("getUserList   uId:"+uId+" depId:"+depId);
			connection=JDBCTools.getConnection();
			
			//查找自己可以管理和  已经管理的人员
			userList=searchIsSubUser(connection,uId,depId);

//			for(User u:userList){
//				System.out.println(u.getName());
//			}
			json.put("userList", userList);

			out.write(json.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{  
            JDBCTools.release(null, null, connection);
            out.flush();
            out.close();
        }  
	}

	private List<User> searchIsSubUser(java.sql.Connection connection2,String uId,String depId){
		
		try {
			
			List<User> list=new ArrayList<User>();
			
			
			if(depId.equals("0")){
				//超管  得到  所有部门  父id为空的用户  和 自己所管理的人员
				String sql="SELECT * FROM users WHERE parId= ? or parId is NULL or parId =''";
				list=uesrDao.getForList(connection2, sql, uId);
				return list;
			}else{
				//一般用户  得到  自己部门  父id为空的用户  和 自己所管理的人员
				String sql="SELECT * FROM users WHERE (depId= ? AND (parId is NULL or parId ='')) or parId= ?  ";
				list=uesrDao.getForList(connection2, sql, depId,uId);
				return list;
			}
			
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;
		
	}
	

}
