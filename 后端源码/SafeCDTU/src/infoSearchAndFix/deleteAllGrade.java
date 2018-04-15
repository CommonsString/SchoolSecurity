package infoSearchAndFix;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Tools.CommonUtils;
import Tools.JDBCTools;
import eneityDAO.studentDao;
import net.sf.json.JSONObject;

/**
 * 按年级删除，慎用
 */
@SuppressWarnings("serial")
@WebServlet("/infoSearchAndFix/deleteAllGrade")
public class deleteAllGrade extends HttpServlet{
	//数据库驱动，可更改
		private studentDao dao = new studentDao();

		@Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			PrintWriter out = null;
			JSONObject json = null; //json
			java.sql.Connection con = null;
			try {
				response.setCharacterEncoding("UTF-8"); //可以删除(拦截器)
				con = JDBCTools.getConnection(); //捕获异常
				out = response.getWriter(); //流
				JDBCTools.beginThing(con); //开启事务
				String grade = request.getParameter("grade"); //接收数据
//System.out.println("grade : " + grade);
				//条件判断
				if("".equals(grade) || grade == null) {
					json = new JSONObject();
					json.put("state", false);
					json.put("message", "无该年级，删除失败！");
					out.write(json.toString());
					return ;
				}
				String sql = "delete from student where stuNumber like ?";
//System.out.println("sql: + " + sql);
				int flag = dao.update(con, sql, grade + "%");
				JDBCTools.commitThing(con);  //提交事务
				//第二种 逻辑判断
				if(flag == 0){ //操作失败
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
				JDBCTools.rollbackThing(con); //回滚事务
				json = new JSONObject();
				json.put("state", true);
				json.put("message", "系统错误，删除失败！");
				out.write(json.toString());
//				e.printStackTrace();
			}finally{
				out.flush();
				out.close();
				CommonUtils.closeConOut(con , out);
//	System.out.println("关闭，刷新流");
			}	 		
		}
		
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			doPost(request, response);
		}		
	

}
