package workOfEvent;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import Tools.JDBCTools;
import eneityDAO.areaDao;
import eneityDAO.eventRecordDao;
import eneityDAO.replyDao;
import eneityDAO.userDao;
import net.sf.json.JSONObject;
//给下级回复
@WebServlet("/workOfEvent/repToSubUser")
public class repToSubUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	areaDao areaDao=new areaDao();
	PrintWriter out=null;
	replyDao replyDao=new replyDao();
	userDao userDao=new userDao();
	eventRecordDao eventRecordDao=new eventRecordDao();
	Date nowTime;
    public repToSubUser() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String eveId=request.getParameter("eveId");
		String repContext=request.getParameter("repContext");
		String name=request.getParameter("name");//上级自己的名字
		String submitUserId=request.getParameter("submitUserId");//修改下级 用户表中是否有新的回复的字段
		String repId=UUID.randomUUID().toString().replaceAll("\\-", "");
		
//		System.out.println("repToTopUser eveId:"+eveId);
//		System.out.println("repToTopUser repContext:"+repContext);
//		System.out.println("repToTopUser name:"+name);
//		System.out.println("repToTopUser submitUserId:"+submitUserId);
		
		try {
			json=new JSONObject();
			out=response.getWriter();
			connection=JDBCTools.getConnection();
			JDBCTools.beginThing(connection);

			//将接受消息的用户中的是否有新的回复的字段改成1
			//updateUserIsNewReply(connection,submitUserId);
			updateUserIsNewReply(connection,eveId);

			//将事件的是否有上级回复的字段改为1，下级就可以得到有上级回复的信息
			changeIsNewTopReply(connection,eveId);

			//将回复添加到回复表
			nowTime=new Date();
			String sql="INSERT INTO reply (repId,eveId,repUserName,repContext,repTime) VALUES (?,?,?,?,?)";
			replyDao.update(connection, sql, repId,eveId,name,repContext,nowTime);

			//将事件表中的最新回复时间改为当前时间
			String sql2="UPDATE eventrecord SET replyTime= ? WHERE eveId= ? ";
			eventRecordDao.update(connection, sql2, nowTime,eveId);
			
			JDBCTools.commitThing(connection);

			json.put("state",true);
			json.put("message","回复成功" );
			out.write(json.toString());
			
		} catch (Exception e) {
			json.put("state",false);
			json.put("message","回复失败,系统错误" );
			out.write(json.toString());
			
			JDBCTools.rollbackThing(connection);
			e.printStackTrace();
		}finally{  
            JDBCTools.release(null, null, connection);
            out.flush();
            out.close();
        }  
		
	}

	private void changeIsNewTopReply(Connection connection22, String eveId) {
		String sql="UPDATE eventrecord SET isNewTopReply='1' WHERE eveId= ? ";
		try {
			eventRecordDao.update(connection22, sql, eveId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void updateUserIsNewReply(Connection connection2, String eveId) {
		try {
		//String sql="UPDATE users SET isNewReply='1' WHERE uId= ? ";
		String sql="SELECT uId FROM area WHERE areId IN(SELECT areId FROM eventrecord WHERE eveId = ? ) ";
		
		String uId=userDao.getForValue(connection2, sql, eveId);
		
		String  sql2="UPDATE users SET isNewReply='1' WHERE uId = ?";
		userDao.update(connection2, sql2,uId);
		
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

}
