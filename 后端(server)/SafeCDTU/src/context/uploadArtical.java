package context;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import Tools.JDBCTools;
import eneityDAO.contextDao;
import eneityDAO.userDao;
import net.sf.json.JSONObject;

//上传文章
@WebServlet("/context/uploadArtical")
public class uploadArtical extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	contextDao contextDao=new contextDao();
	userDao userDao=new userDao();
	
    public uploadArtical() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String content=request.getParameter("HBFJfsefsSfsefEGFBfsefHfseffseJSE");
		String title=request.getParameter("JHNdawdFdawdSEHFdawdawfsFCSFESNU");
		String uId=request.getParameter("FSEdfsfKfsefseFGfsfNESGfsefNfseFEJS");
		String depId=request.getParameter("FSEdawdaKdwadFHNSdawdEUdawHF");
		String needConfig="b";// 部门通知 b
		String isSee="0";//是否查看
		
		
//		System.out.println(" ");
//		System.out.println(" depId:"+depId);
		request.setAttribute("FSEdfsfKfsefseFGfsfNESGfsefNfseFEJS", uId);
		request.setAttribute("FSEdawdaKdwadFHNSdawdEUdawHF", depId);
		
//		request.setAttribute("allSee", allSee);
		
		String[] depIdList=depId.split(",");
		
		String txtId=UUID.randomUUID().toString().replaceAll("\\-", "");
		Date time=new Date();
//		System.out.println(content);
		if(title.equals("")){
			request.setAttribute("msg", "标题不能为空");
			request.getRequestDispatcher("/uploadArtical.jsp").forward(request,response);
		}
		else if(content.equals("")){
			request.setAttribute("msg", "文章内容不能为空");
			request.getRequestDispatcher("/uploadArtical.jsp").forward(request,response);
		}
		else if(depId.equals("")){
			request.setAttribute("msg", "没有选择部门");
			request.getRequestDispatcher("/uploadArtical.jsp").forward(request,response);
		}
		else if(uId.equals("")){
			request.setAttribute("msg", "用户没有登录");
			request.getRequestDispatcher("/uploadArtical.jsp").forward(request,response);
		}
		else{
			String sql="INSERT INTO context (txtId,uId,title,time,depId,content,needConfig,isSee) values (?,?,?,?,?,?,?,?)";
			try {
				connection=JDBCTools.getConnection();
				JDBCTools.beginThing(connection);
				contextDao.update(connection, sql,txtId,uId,title,time,depId,content,needConfig,isSee);
				
				////修改用户的isNewDepNotice，通知该部门成员有新的部门通知
				for(int i=0;i<depIdList.length;i++){
//					System.out.println("depIdList:"+depIdList[i]);
					updateUserIsNewDepNotice(connection,depIdList[i]);
				}
				
				
				JDBCTools.commitThing(connection);
				request.setAttribute("msg", "发布成功");
				request.getRequestDispatcher("/uploadArtical.jsp").forward(request,response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				request.setAttribute("msg", "系统错误sql,发送失败");
				request.getRequestDispatcher("/uploadArtical.jsp").forward(request,response);
				e.printStackTrace();
			}catch (Exception e) {
				JDBCTools.rollbackThing(connection);
				request.setAttribute("msg", "系统错误,发送失败");
				request.getRequestDispatcher("/uploadArtical.jsp").forward(request,response);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				JDBCTools.release(null, null, connection);
			}
			
		}
	//	System.out.println(content);
//		request.setAttribute("msg", "跳转");
//		request.getRequestDispatcher("/uploadArtical.jsp").forward(request,response);
		
	}
	 private void updateUserIsNewDepNotice(Connection connection2, String depId) {
	    	
		try {
		//修改用户的isNewDepNotice，通知该部门成员有新的部门通知
		String sql2="UPDATE users SET isNewDepNotice='1' WHERE depId= ? ";
		userDao.update(connection2, sql2,depId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
