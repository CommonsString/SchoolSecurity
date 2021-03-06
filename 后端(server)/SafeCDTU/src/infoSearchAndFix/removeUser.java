package infoSearchAndFix;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Tools.CommonUtils;
import Tools.JDBCTools;
import eneityDAO.userDao;
import net.sf.json.JSONObject;

/*
 *删除用户
 */
@SuppressWarnings("serial")
@WebServlet("/infoSearchAndFix/removeUser")
public class removeUser extends HttpServlet{
	//数据库驱动，可更改
	private userDao dao = new userDao();
	java.sql.Connection con=null;
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = null;
		JSONObject json = null;
		java.sql.Connection con = null;
		try {
			response.setCharacterEncoding("UTF-8"); //可以删除(拦截器)
			con = JDBCTools.getConnection(); //捕获异常
			out = response.getWriter(); //流
			//获取参数
			String uId = request.getParameter("uId"); //是否使用 UUID
//System.out.println("参数" + uId);
			String sql = "delete from users where uId = ?";  //sql可修改
//			String sql1="UPDATE eventrecord SET submitUserId = '' WHERE submitUserId = ?";
//			String sql2="UPDATE eventrecord SET executeUserId = '' WHERE executeUserId = ?";
//			JDBCTools.beginThing(con);
			int flag = dao.update(con , sql, uId);
//			int flag1 = dao.update(con , sql1, uId);
//			int flag2 = dao.update(con , sql2, uId);
//System.out.println(flag);
		//	JDBCTools.commitThing(con);
			//第二种 逻辑判断
//			if(flag == 0 || flag1==0 || flag2==0){ //操作失败
			if(flag == 0){
				json = new JSONObject();
				json.put("state", false);
				json.put("message", "删除失败");
				out.write(json.toString());
			}else{  // 打包
				json = new JSONObject();
				json.put("state", true);
				json.put("message", "删除成功");
				out.write(json.toString());
			}
		} catch (Exception e) {
			//JDBCTools.rollbackThing(con);
			json = new JSONObject();
			json.put("state", false);
			json.put("message", "系统错误，删除失败！");
			out.write(json.toString());
//			e.printStackTrace();
			throw new RuntimeException(e);
		}finally{
			JDBCTools.release(null, null, con);
			out.flush();
			CommonUtils.closeConOut(con , out);
//System.out.println("关闭，刷新流");
		}	 		
	}
	
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
}
