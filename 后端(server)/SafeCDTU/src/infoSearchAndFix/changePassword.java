package infoSearchAndFix;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ndktools.javamd5.Mademd5;

import Tools.JDBCTools;
import eneityDAO.userDao;
import net.sf.json.JSONObject;

//修改密码
@WebServlet("/infoSearchAndFix/changePassword")
public class changePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
	userDao dao=new userDao();
	java.sql.Connection connection=null;
	JSONObject json=null;
	
    public changePassword() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		System.out.println("changePswd");
		String uId=request.getParameter("uId");
		String password=request.getParameter("password");
//		System.out.println("uId:"+uId+"  password:"+password);
		JSONObject json=null;
		PrintWriter out=null;
		Mademd5 md=new Mademd5();
		try {
			json=new JSONObject();
			out=response.getWriter();
			connection=JDBCTools.getConnection();
			if(uId.equals("")){
				json.put("state", false);
				json.put("message", "用户不能为空");
				out.write(json.toString());
			}else{
				if(password.equals("")){
					json.put("state", false);
					json.put("message", "密码不能为空");
					out.write(json.toString());
				}else{
					password=md.toMd5(password);
					String sql="UPDATE users SET pswd= ? WHERE uId= ? ";
					dao.update(connection, sql, password,uId);
					json.put("state", true);
					json.put("message", "密码修改成功");
					out.write(json.toString());
				}
			}
		} catch (Exception e) {
			json.put("state", false);
			json.put("message", "系统错误,修改失败");
			out.write(json.toString());
			e.printStackTrace();
		}finally{  
            JDBCTools.release(null, null, connection);
            out.flush();
            out.close();
        }  
	}

}
