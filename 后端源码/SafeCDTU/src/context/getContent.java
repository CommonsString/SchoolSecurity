package context;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import com.mysql.jdbc.Connection;

import Tools.JDBCTools;
import eneityDAO.contextDao;
import entity.Context;
import entity.EventRecord;
import net.sf.json.JSONObject;


/*
 * 超管得到发出的所有部门通知
 * 
 * 用户得到部门通知
 * 
 * 
 * */
@WebServlet("/context/getContent")
public class getContent extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	contextDao contextDao=new contextDao();
   
    public getContent() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out=null;
		
		
		try {
			json=new JSONObject();
			out=response.getWriter();
			String depId=request.getParameter("depId");
			String page=request.getParameter("page");
//			System.out.println("depId:"+depId+" page:"+page);
		
			//分页
			int startPage=Integer.valueOf(page).intValue();
			startPage=(startPage-1)*10;
			
			List<Context> contextList=new ArrayList<Context>();
			connection=JDBCTools.getConnection();
			
			//得到文章
			if(depId.equals("0")){
				//超管
//				System.out.println("bb");
				String sql="SELECT * FROM context  Where needConfig= 'b' order by time desc  limit ?,10";
				contextList=contextDao.getForList(connection, sql,startPage);
			}else{
				//一般用户
//				System.out.println("bbb");
				String sql="SELECT * FROM context WHERE depId like ?  AND needConfig= 'b'  order by time desc  limit ?,10";
				depId="%"+depId+"%";
				contextList=contextDao.getForList(connection, sql, depId,startPage);
			}
			//总条数
			String total;
			total=getTotal(connection, depId);
//			for(Context c:contextList){
//				System.out.println(c.toString());
//			}
//			System.out.println("total:"+total);
			
			json.put("total", total);
			json.put("contextList", contextList);
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
	
	private String getTotal(java.sql.Connection connection2,String depId){
		Object object=new Object();
		try {
			if(depId.equals("0")){
				String sql="SELECT count(*) FROM context Where needConfig= 'b' ";
//					System.out.println("aa");
					object=contextDao.getForValue(connection2, sql);
				
			}else{
//				System.out.println("aaa");
				String sql="SELECT count(*) FROM context WHERE depId like ?  AND needConfig= 'b' ";
				depId="%"+depId+"%";
				object=contextDao.getForValue(connection2, sql, depId);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return object.toString();
		
	}

}
