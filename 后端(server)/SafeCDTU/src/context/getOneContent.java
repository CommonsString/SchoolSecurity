package context;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Tools.JDBCTools;
import eneityDAO.contextDao;
import entity.Context;
import net.sf.json.JSONObject;

//得到单篇文章
@WebServlet("/context/getOneContent")
public class getOneContent extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	contextDao contextDao=new contextDao();
	PrintWriter out=null;
    public getOneContent() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		json=new JSONObject();
		out=response.getWriter();
		String txtId=request.getParameter("txtId");
		//System.out.println("getOneContent");
		//System.out.println("getOneContent   txtId:"+txtId);
		try {
			Context context=new Context();
			connection=JDBCTools.getConnection();
			//得到一篇文章
			String sql="SELECT * FROM context WHERE txtId= ? ";
			context=contextDao.get(connection, sql, txtId);
			
			json.put("context", context);
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
