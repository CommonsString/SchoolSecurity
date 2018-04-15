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
import eneityDAO.studentDao;
import net.sf.json.JSONObject;
/*
 * 删除学生信息
 */
@SuppressWarnings("serial")
@WebServlet("/infoSearchAndFix/deleteStuInfo")
public class deleteStuInfo extends HttpServlet{
	//数据库驱动，可更改
	private studentDao dao = new studentDao();

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
			String stuNumber = request.getParameter("stuNumber");
//System.out.println(stuNumber);
			String sql = "delete from student where stuNumber = ?";  //sql可修改
			int flag = dao.update(con , sql, stuNumber);
			
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
			//待修改
			json = new JSONObject();
			json.put("state", false);
			json.put("message", "删除失败");
			out.write(json.toString());
//			e.printStackTrace(); //调试时打开
		} finally{
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
