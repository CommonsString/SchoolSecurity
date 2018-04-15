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
import eneityDAO.areRuleDao;
import eneityDAO.areaDao;
import eneityDAO.departmentDao;
import eneityDAO.userDao;
import net.sf.json.JSONObject;

//添加子区域
@WebServlet("/infoSearchAndFix/addSubArea")
public class addSubArea extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	areaDao areaDao=new areaDao();
	userDao userDao=new userDao();
	areRuleDao areRuleDao=new areRuleDao();
	departmentDao departmentDao=new departmentDao();
    public addSubArea() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		System.out.println("addSubArea");
		json=new JSONObject();
		PrintWriter out=response.getWriter();
		String number=request.getParameter("number");
		String uId=request.getParameter("uId");
		String name=request.getParameter("name");
		String discribe=request.getParameter("discribe");
		String parAreaId=request.getParameter("parAreaId");
//		System.out.println("number:"+number+" uId:"+uId+" name:"+name+" discribe:"+discribe+" parAreaId:"+parAreaId);
		String areId=UUID.randomUUID().toString().replaceAll("\\-", "");
		String ruleId=UUID.randomUUID().toString().replaceAll("\\-", "");
		String content="";
		try {
			
			connection=JDBCTools.getConnection();
			JDBCTools.beginThing(connection);
			
			//添加子区域信息
			String sql="insert into area (areId,number,name,uId,discribe,parAreaId) values(?,?,?,?,?,?)";
			areaDao.update(connection, sql,areId,number,name,uId,discribe,parAreaId);
			
			//添加管理详情(规章制度)表
			String sql2="insert into arerule(ruleId,areId,content) values(?,?,?)";
			areRuleDao.update(connection, sql2, ruleId,areId,content);
			
			if(parAreaId.equals("0")){
				//添加一级区域,需要添加相应的部门
				//String depId=UUID.randomUUID().toString().replaceAll("\\-", "");;
				String sql3="insert into department(depId,number,name) values(?,?,?)"; 
				departmentDao.update(connection, sql3, areId,number,name);
			}
			if(!uId.equals("")){
				changLevelTo2(connection,uId);
			}
			JDBCTools.commitThing(connection);
			json.put("state", true);
			json.put("message", "添加成功");
			out.write(json.toString());
		} catch (Exception e) {
			JDBCTools.rollbackThing(connection);
			json.put("state", false);
			json.put("message", "系统错误,添加失败");
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
