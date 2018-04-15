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
import entity.Student;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/*
 * 批量删除学生信息
 */
@SuppressWarnings("serial")
@WebServlet("/infoSearchAndFix/batchDeleteStuInfo")
public class batchDeleteStuInfo extends HttpServlet{
	//数据库驱动，可更改
	private studentDao dao = new studentDao();

	@SuppressWarnings({ "unused",  "static-access" })
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = null;
		JSONObject json = null; //json
		JSONArray tempJson = null; //临时对象
		int[] flag = null;
		
		java.sql.Connection con = null;
		Object[][] params = null; //query参数
		ArrayList<Student> studentList = new ArrayList<Student>(); //数组
		ArrayList<String> list = new ArrayList<String>();
		try {
			response.setCharacterEncoding("UTF-8"); //可以删除(拦截器)
			con = JDBCTools.getConnection(); //捕获异常
			out = response.getWriter(); //流
			JDBCTools.beginThing(con); //开启事务
			
			String jsonArrParam = request.getParameter("studentList"); //接收数据
			tempJson = JSONArray.fromObject(jsonArrParam);
			for(int j = 0 ; j < tempJson.length() ; j++){
				json = tempJson.getJSONObject(j); //获取json
				studentList.add((Student) json.toBean(json , Student.class));
			}
			params = CommonUtils.getParamsStu(studentList);
			
			if(params != null && params.length != 0){ //盘空
				//执行查询
				String sql = "delete from student where stuNumber = ?";
				flag = dao.batch(con, sql , params);
			}
			
			JDBCTools.commitThing(con);  //提交事务
			//第二种 逻辑判断
			if(flag.length == 0){ //操作失败
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
		}finally{
			out.flush();
			out.close();
			CommonUtils.closeConOut(con , out);
//System.out.println("关闭，刷新流");
		}	 		
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}	
	

}
