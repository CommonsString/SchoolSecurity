package infoSearchAndFix;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Tools.JDBCTools;
import Tools.JdbcDAOImpl;
import eneityDAO.areRuleDao;
import eneityDAO.userDao;
import entity.AreRule;
import net.sf.json.JSONObject;


//得到单个的区域的规章制度
@WebServlet("/infoSearchAndFix/getRule")
public class getRule extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	PrintWriter out=null;
	areRuleDao areRuleDao=new areRuleDao();
	
	
    public getRule() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String areId=request.getParameter("areId");
		try {
			out=response.getWriter();
			json=new JSONObject();
			connection=JDBCTools.getConnection();
			AreRule areRule=new AreRule();
			
			//通过区域id得到  单个的规章制度
			String sql="SELECT * FROM arerule WHERE areId= ? ";
			areRule=areRuleDao.get(connection, sql, areId);
			
			json.put("rule", areRule);
			out.write(json.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{  
            JDBCTools.release(null, null, connection);
            out.flush();
            out.close();
        }  
		
	}

}
