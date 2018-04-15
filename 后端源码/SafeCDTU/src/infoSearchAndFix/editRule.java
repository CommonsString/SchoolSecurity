package infoSearchAndFix;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Tools.JDBCTools;
import eneityDAO.areRuleDao;
import eneityDAO.areaDao;
import eneityDAO.contextDao;
import eneityDAO.userDao;
import entity.Area;
import entity.User;


//编辑规章制度
@WebServlet("/infoSearchAndFix/editRule")
public class editRule extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	areRuleDao areRuleDao=new areRuleDao();
	areaDao areaDao=new areaDao();
	
    public editRule() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String uId=request.getParameter("FSEdfsfKfsefseFGfsfNESGfsefNfseFEJS");
		String areId=request.getParameter("FSEdawdaKdwadFHNSdawdEUdawHF");
		String content=request.getParameter("HBFJfsefsSfsefEGFBfsefHfseffseJSE");
		Area area=new Area();
//		System.out.println("");
//		System.out.println("editRule:"+uId);
//		System.out.println("editRule:"+areId);
//		System.out.println("editRule:"+content);
		
		request.setAttribute("uId", uId);
		request.setAttribute("depId", areId);
		
		
		try {
			connection=JDBCTools.getConnection();
			String sql="SELECT * FROM area WHERE areId= ? ";
			area=areaDao.get(connection, sql, areId);
//			System.out.println("areId"+areId);
//			System.out.println("area"+area);
			
			//如果区域的负责人和前端传来的负责人是同一个人 则可以修改规章制度
			if(area.getuId().equals(uId)){
				String sql2="UPDATE arerule SET content= ? WHERE areId = ?";
				areRuleDao.update(connection, sql2, content,areId);
				request.setAttribute("msg", "发布成功");
				request.getRequestDispatcher("/editRule.jsp").forward(request,response);
			}else{
				request.setAttribute("msg", "发布失败,你不能管理此区域");
				request.getRequestDispatcher("/editRule.jsp").forward(request,response);
			}
			
			
		} catch (SQLException e) {
			request.setAttribute("msg", "系统错误,发送失败");
			request.getRequestDispatcher("/editRule.jsp").forward(request,response);
			e.printStackTrace();
		} catch (Exception e) {
			request.setAttribute("msg", "系统错误,发送失败");
			request.getRequestDispatcher("/editRule.jsp").forward(request,response);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{  
            JDBCTools.release(null, null, connection);
           
        }  
	}

}
