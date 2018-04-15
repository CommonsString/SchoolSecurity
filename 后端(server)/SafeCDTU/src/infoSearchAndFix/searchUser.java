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
import eneityDAO.userDao;
import entity.User;
import net.sf.json.JSONObject;


/**
 * 查询用户
 */
@SuppressWarnings("serial")
@WebServlet("/infoSearchAndFix/searchUser")
public class searchUser extends HttpServlet{
	//数据库驱动，可更改
	private userDao dao = new userDao();
	java.sql.Connection con =null;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = null;
		JSONObject json = null;
		java.sql.Connection con = null;
		int total = 0;  //记录数
		int pageSize = 10; //显示条数
		try {
//			System.out.println("进入");
			response.setCharacterEncoding("UTF-8"); //可以删除(拦截器)
			con = JDBCTools.getConnection(); //捕获异常
			out = response.getWriter(); //流
			json = null;
			List<User> userList = null; 
			StringBuilder sqlTrans = new StringBuilder();
			StringBuilder sqlTotal = new StringBuilder();
			
			//获取参数
			String page = request.getParameter("page"); //当前页码
//System.out.println(page);
			int currentPage = Integer.parseInt(page);
//System.out.println("页码" + currentPage);
			
			User user = CommonUtils.toBean(request.getParameterMap() , User.class);
//System.out.println(user.toString());
			sqlTrans.append("SELECT * FROM users");
			String sql = CommonUtils.sqlTransFromUser(sqlTrans , user , true);
//System.out.println("查询：" + sql);
			userList = dao.getForList(con, sql , (currentPage - 1) * pageSize , pageSize);
//System.out.println(user.toString());
			//记录数
			sqlTotal.append("SELECT count(*) FROM users");
			String sqlCount = CommonUtils.sqlTransFromUser(sqlTotal, user, false);
//System.out.println("数量：" + sqlCount);
			long temTotal = dao.getForValue(con, sqlCount);
			total = (int)temTotal;	
//System.out.println(total);
			
			if(userList.isEmpty()){ //封装数据
				//学生组不存在，应该写入错误消息
				json = new JSONObject();
				json.put("state", false);
				json.put("total", total);
				json.put("message", "不存在用户，请重新查询");
				out.write(json.toString());
			}else{  //存在 打包
				json = new JSONObject();
				json.put("state", true);
				json.put("total", total); //总数
				json.put("message", "查询成功！");
				json.put("userList" , userList);
				out.write(json.toString()); //测试,待修改
			}
		} catch (Exception e) {
			//学生组不存在，应该写入错误消息
			json = new JSONObject();
			json.put("state", false);
			json.put("total", total);
			json.put("message", "系统连接失败，请重新查询！");
			out.write(json.toString());			
			e.printStackTrace();
		}finally{
			JDBCTools.release(null, null, con);
			out.flush();
			out.close();
			CommonUtils.closeConOut(con , out);
//System.out.println("关闭，刷新流");
		}	 
	}
	
	
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request , response);
	}
	

}
