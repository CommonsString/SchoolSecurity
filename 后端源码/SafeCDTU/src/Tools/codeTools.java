package Tools;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import cn.itcast.vcode.servlet.VerifyCodeServlet;
import cn.itcast.vcode.utils.VerifyCode;
import eneityDAO.userDao;
import net.sf.json.JSONObject;

public class codeTools extends VerifyCodeServlet{

	userDao dao=new userDao();
	java.sql.Connection connection=null;
	JSONObject json=null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session=request.getSession();
		
		VerifyCode vc = new VerifyCode();
		BufferedImage image = vc.getImage();//获取一次性验证码图片
		// 该方法必须在getImage()方法之后来调用
		String codeText=vc.getText();
		session.setAttribute("veCode",codeText );
		//PrintWriter out=response.getWriter();
		
		String account = new String(request.getParameter("username").getBytes("iso8859-1"),"UTF-8");
		System.out.println(account);
//		try {
//			connection=JDBCTools.getConnection();
//			String sql="SELECT account FROM users WHERE ?=account";
//			String username=selectAccount(sql,connection,account);
//			if(username.equals("")){
//				json=new JSONObject();
//				json.put("message", "没有该用户");
//				out.write(json.toString());
//			}
//			else{
				try {
					connection=JDBCTools.getConnection();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String sql="UPDATE users SET veCode=? where ? =account";
				setCode(sql, connection,codeText,account );
				VerifyCode.output(image, response.getOutputStream());//把图片写到指定流中
			//}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	private void setCode(String sql,java.sql.Connection connection2,Object... args){
		try {
			dao.update(connection2, sql, args);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String selectAccount(String sql,java.sql.Connection connection2,Object... args){
		try {
			return dao.getForValue(connection2, sql, args);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
