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
import entity.Student;
import net.sf.json.JSONObject;

/*
 * 修改学生信息
 */
@SuppressWarnings("serial")
@WebServlet("/infoSearchAndFix/alterStuInfo")
public class alterStuInfo extends HttpServlet{
	//数据库驱动，可更改
	private studentDao dao = new studentDao();
	java.sql.Connection con =null;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = null;
		JSONObject json = null;
		java.sql.Connection con = null;
		try {
			response.setCharacterEncoding("UTF-8"); //可以删除(拦截器)
			con = JDBCTools.getConnection(); //捕获异常
			out = response.getWriter(); //流
			json = null;
			
//System.out.println("班级" + request.getParameter("clasz"));	
//System.out.println("学号:" + request.getParameter("stuNumber"));
//System.out.println("姓名："  + request.getParameter("name"));
			//获取参数
			Student student = CommonUtils.toBean(request.getParameterMap(), Student.class);
			String sql = "update student set name = ?  , clasz = ? , tel = ? , college = ? where stuNumber = ?";  //sql可修改
			Object params[] = {student.getName() , student.getClasz() , student.getTel() , student.getCollege() , student.getStuNumber()};
			int flag = dao.update(con , sql, params);
			
//System.out.println(student.toString());
			//第一种直接写入请求参数(待定)....
			//第二种 逻辑判断
			if(flag == 0){ //操作失败
				json = new JSONObject();
				json.put("state", false);
				json.put("message", "修改失败");
				out.write(json.toString());
			}else{  // 打包
				json = new JSONObject();
				json.put("state", true);
				json.put("message", "修改成功");
				out.write(json.toString());
			}
		} catch (Exception e) {
			json = new JSONObject();
			json.put("state", false);
			json.put("message", "修改失败");
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
