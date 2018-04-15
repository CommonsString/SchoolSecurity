package infoSearchAndFix;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Tools.CommonUtils;
import Tools.JDBCTools;
import eneityDAO.departmentDao;
import eneityDAO.studentDao;
import entity.Student;
import net.sf.json.JSONObject;


/**
 * 查询学生信息
 */
@SuppressWarnings("serial")
@WebServlet("/infoSearchAndFix/searchStudent")
public class searchStudent extends HttpServlet{
	//数据库驱动，可更改
	private studentDao dao = new studentDao();
	private departmentDao daoDepar = new departmentDao();
	java.sql.Connection con =null;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
System.out.println("进入");
		PrintWriter out = null;
		java.sql.Connection con = null;
		JSONObject json = null;
		List<Student> studentList = null;
		int total = 0;  //记录数
		int pageSize = 10; //显示条数
		try {
			
//System.out.println("年级：" + request.getParameter("grade"));
			response.setCharacterEncoding("UTF-8"); //可以删除(拦截器)
			con = JDBCTools.getConnection(); //捕获异常
			out = response.getWriter(); //流
			StringBuilder sqlTrans = new StringBuilder(); //查询
			StringBuilder sqlTotal = new StringBuilder(); //记录数
			String deptID = request.getParameter("depId");
//System.out.println("部门ID：" + deptID);
			//获取参数
			String page = request.getParameter("page"); //当前页码
			int currentPage = Integer.parseInt(page);
//System.out.println("当前页码" + currentPage);
			//返回列表数

			if("0".equals(deptID)){
				Student student = CommonUtils.toBean(request.getParameterMap(), Student.class);
				sqlTrans.append("SELECT * FROM student");
				String sql = CommonUtils.sqlTransFromStuMan(sqlTrans , student , true);
//System.out.println("sql ： 0" + sql);
				studentList = dao.getForList(con, sql , (currentPage - 1) * pageSize , pageSize);
				sqlTotal.append("SELECT count(*) FROM student");
				String sqlTotalT = CommonUtils.sqlTransFromStuMan(sqlTotal, student, false);
				long temTotal = dao.getForValue(con, sqlTotalT);
				total = (int)temTotal;
//System.out.println("total:" + total);
			}else{
//System.out.println("进入2。。。。");
				String sqlDept = "select name from department where depId = ?";
				String deptName = daoDepar.getForValue(con, sqlDept, deptID);
//	System.out.println("部门id: " + deptName);
				Student student = CommonUtils.toBean(request.getParameterMap(), Student.class);
				sqlTrans.append("SELECT * FROM student");
				String sql = CommonUtils.sqlTransFromStu(sqlTrans , student , true , deptName); //返回数据
	System.out.println("sql:" + sql);
				studentList = dao.getForList(con, sql , (currentPage - 1) * pageSize , pageSize);
				//记录数
				sqlTotal.append("SELECT count(*) FROM student");
				String sqlCount = CommonUtils.sqlTransFromStu(sqlTotal, student, false , deptName);
				long temTotal = dao.getForValue(con, sqlCount);
				total = (int)temTotal;
			}
			
			if(studentList.isEmpty()){ //封装数据
				//学生组不存在，应该写入错误消息
				json = new JSONObject();
				json.put("state", false);
				json.put("message", "不存在学生信息！");
				json.put("total", total); //总记录数
				out.write(json.toString());
			}else{  //存在 打包
				json = new JSONObject();
				json.put("state", true);
				json.put("message", "查询成功");
				json.put("studentList" , studentList);
				json.put("total", total); //总记录数
				out.write(json.toString()); //测试,待修改
			}
		} catch (Exception e) {
			json = new JSONObject();
			json.put("state", false);
			json.put("message", "系统错误,查询失败!");
			json.put("total", total); //总记录数
			out.write(json.toString()); //测试,待修改			
//			e.printStackTrace();
		} finally{
			JDBCTools.release(null, null, con);
			out.flush();
			CommonUtils.closeConOut(con , out);
		}
	}
	
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request , response);
	}
	
}
