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

import Tools.JDBCTools;
import eneityDAO.eventRecordDao;
import eneityDAO.replyDao;
import entity.Reply;
import net.sf.json.JSONObject;

@WebServlet("/workOfEvent/searchReply")
public class searchReply extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	replyDao replyDao=new replyDao();
	PrintWriter out=null;
    public searchReply() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		System.out.println("searchReply");
		
		String eveId=request.getParameter("eveId");
//		System.out.println("searchReply eveId:"+eveId);
		try {
			
			json=new JSONObject();
			out=response.getWriter();
			connection=JDBCTools.getConnection();
			List<Reply> replyList=new ArrayList<Reply>();
			
			//查找出回复记录
			String sql="SELECT * From reply WHERE eveId= ? order by repTime  asc";
			replyList=replyDao.getForList(connection, sql, eveId);
//			for(Reply r:replyList){
//				System.out.println("replyList:"+r.toString());
//			}
			
			json.put("replyList",replyList);
			out.write(json.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{  
            JDBCTools.release(null, null, connection);
            out.flush();
            out.close();
        }  
		
	}

}
