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
 * 重置密码  E10ADC3949BA59ABBE56E057F20F883E
 * 	
 */
@SuppressWarnings("serial")
@WebServlet("/infoSearchAndFix/resetPassword")
public class resetPassword extends HttpServlet{
	//数据库驱动，可更改
	private userDao dao = new userDao();
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
			//获取参数
			String uId = request.getParameter("uId");
			
			//强制修改
			String sql = "update users set pswd = ? where uId = ?";
			
			String pswdDefault = "4297F44B13955235245B2497399D7A93";
			
			int flag = dao.update(con , sql,  pswdDefault , uId);
			
			//第二种 逻辑判断
			if(flag == 0){ //操作失败
				json = new JSONObject();
				json.put("state", false);
				json.put("message", "重置失败");
				out.write(json.toString());
			}else{  // 打包
				json = new JSONObject();
				json.put("state", true);
				json.put("message", "重置成功,默认密码为123123");
				out.write(json.toString());
			}
		} catch (Exception e) {
			json = new JSONObject();
			json.put("state", true);
			json.put("message", "系统错误，重置失败");
			out.write(json.toString());			
//			e.printStackTrace();
			throw new RuntimeException(e);
		} finally{
			JDBCTools.release(null, null, con);
			out.flush();
			
			CommonUtils.closeConOut(con , out);
//			System.out.println("关闭，刷新流");
		}	
	}
	
	
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request , response);
	}
}
