package methods;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ndktools.javamd5.Mademd5;

import Tools.JDBCTools;
import eneityDAO.userDao;
import entity.User;
import net.sf.json.JSONObject;

//注册
@WebServlet("/methods/regist")
public class regist extends HttpServlet {
	private static final long serialVersionUID = 1L;
	JSONObject json=null;
	PrintWriter out =null;
	java.sql.Connection connection=null;
	userDao userDao=new userDao();
    public regist() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			boolean b;
			boolean a=false;
			String username=request.getParameter("username");
			String password=request.getParameter("password");
			//判断输入的密码是否有空格
			 int aa=password.indexOf(" ");
			 if(aa!=-1){
				 a=true;
			 }
			userDao dao =new userDao();
			json=new JSONObject();
			Mademd5 md=new Mademd5();
			out = response.getWriter(); 
			
			
			String uId=UUID.randomUUID().toString().replaceAll("\\-", "");
			String depId="";
			int level=3;
			String na="空";
			 String tel="";
			 String sex="";
			 String email="";
			 String parId="";
			 int isNewReply=0;
			 int isNewPotNotice=0;
			 int isNewDepNotice=0;
			 int isNewEveNotice=0;
			connection=JDBCTools.getConnection();
			
			String sql="SELECT account FROM users WHERE account= ?";
			String acc=dao.getForValue(connection, sql, username);
			if(acc==null){
				b=false;
			}
			else{
				b=true;
			}
			if(b){
				json.put("state", false);
	         	json.put("message","注册失败,账号已经存在" );
	            out.write(json.toString()); 
			}else{
				if(password.equals("")){
					 json.put("state", false);
		         	 json.put("message","请输入密码" );
		             out.write(json.toString());  
				}else if(a){
					 json.put("state", false);
		         	 json.put("message","密码不能有空格" );
		             out.write(json.toString()); 
				}
				else{
					 String sql2="insert into users (uId,account,pswd,name,tel,sex,depId,email,parId,level,isNewReply,isNewPotNotice,isNewDepNotice,isNewEveNotice) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					 password=md.toMd5(password);
					 
			         dao.update(connection, sql2, uId,username,password,na,tel,sex,depId,email,parId,level,isNewReply,isNewPotNotice,isNewDepNotice,isNewEveNotice);
			         
			         User user=new User();
			         String sql3="SELECT * FROM users WHERE uId= ? ";
			         user=userDao.get(connection, sql3, uId);
			         json.put("user",user );
			         json.put("state", true);
		         	 json.put("message","注册成功" );
		             out.write(json.toString());  
				}
				
			}
			
	        
		} catch (Exception e) {
			json.put("state", false);
        	json.put("message","系统错误,注册失败" );
            out.write(json.toString()); 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{  
            JDBCTools.release(null, null, connection);
            out.flush();
            out.close();
        }  
	}
}
