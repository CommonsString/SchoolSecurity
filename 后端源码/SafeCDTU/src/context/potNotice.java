package context;

import java.io.IOException;
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
import eneityDAO.contextDao;
import eneityDAO.userDao;
//处理发送定点通知
@WebServlet("/context/potNotice")
public class potNotice extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	contextDao contextDao=new contextDao();
	userDao userDao=new userDao();
    public potNotice() {
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
		String needConfig="a";// 定点通知 a
		String isSee="0";//是否查看
//		String allSee=request.getParameter("allSee");
		
//		System.out.println(" ");
//		System.out.println(" depId:"+depId);
		request.setAttribute("FSEdfsfKfsefseFGfsfNESGfsefNfseFEJS", uId);
		request.setAttribute("FSEdawdaKdwadFHNSdawdEUdawHF", depId);
		
//		request.setAttribute("allSee", allSee);
		
		
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
				contextDao.update(connection, sql,txtId,uId,title,time,depId,content,needConfig,isSee);
		
				////修改定点通知的用户的isNewEveNotice
				updateUserIsNewPotNotice(connection,depId);
				request.setAttribute("msg", "发布成功");
				request.getRequestDispatcher("/uploadArtical.jsp").forward(request,response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				request.setAttribute("msg", "系统错误sql,发送失败");
				request.getRequestDispatcher("/uploadArtical.jsp").forward(request,response);
				e.printStackTrace();
			}catch (Exception e) {
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
	 private void updateUserIsNewPotNotice(Connection connection2, String areId) {
	    	
		 String uId="";
		try {
		//查找出上级用户的id
		String sql="SELECT uId FROM area WHERE areId='"+areId+"';";
		uId=userDao.getForValue(connection2, sql);
		
		//修改定点通知的用户的isNewEveNotice
		String sql2="UPDATE users SET isNewPotNotice='1' WHERE uId= ? ";
		userDao.update(connection2, sql2,uId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
