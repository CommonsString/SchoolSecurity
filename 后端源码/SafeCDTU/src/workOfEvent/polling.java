package workOfEvent;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils.Null;

import Tools.JDBCTools;
import eneityDAO.replyDao;
import eneityDAO.userDao;
import entity.Reply;
import entity.User;
import net.sf.json.JSONObject;
//轮询方法  查看是否有各种新消息
@WebServlet("/workOfEvent/polling")
public class polling extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	replyDao replyDao=new replyDao();
	PrintWriter out=null;
	userDao userDao=new userDao();
    public polling() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("polling");
		String uId=request.getParameter("uId");
		json=new JSONObject();
		out=response.getWriter();
		try {
			//id不为空时才进行轮询查询是否有新信息
			if(!"undefined".equals(uId.trim())||uId!=null){
			
					connection=JDBCTools.getConnection();
					User  user;
					String sql="SELECT * From users WHERE uId= ? ";
					user=userDao.get(connection, sql,uId);
					
					if(user==null){
						
					}
					else{
						//查找出是否有回复记录
						if(user.getIsNewReply()!=1){
							json.put("newReply",0);
							
						}else{
							String sql2="UPDATE users SET isNewReply='0' WHERE uId= ? ";
							userDao.update(connection, sql2, uId);
							json.put("newReply",1);
						}
						
						//是否有新的定点通知
						if(user.getIsNewPotNotice()!=1){
							json.put("newPotNotice",0);
							
						}else{
							String sql2="UPDATE users SET isNewPotNotice='0' WHERE uId= ? ";
							userDao.update(connection, sql2, uId);
							json.put("newPotNotice",1);
						}
						
						//是否有新的部门通知
						if(user.getIsNewDepNotice()!=1){
							json.put("newDepNotice",0);
							
						}else{
							String sql2="UPDATE users SET isNewDepNotice='0' WHERE uId= ? ";
							userDao.update(connection, sql2, uId);
							json.put("newDepNotice",1);
						}
						
						//是否有新的事件
						if(user.getIsNewEveNotice()!=1){
							json.put("newEveNotice",0);
							
						}else{
							String sql2="UPDATE users SET isNewEveNotice='0' WHERE uId= ? ";
							userDao.update(connection, sql2, uId);
							json.put("newEveNotice",1);
						}
						
						out.write(json.toString());
					}
					
				
				
				
			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{  
            JDBCTools.release(null, null, connection);
            json.put("no", "aaa");
            out.write(json.toString());
            out.flush();
            out.close();
        }  
		
	}

}
