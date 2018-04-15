package workOfEvent;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Tools.GetProperty;
import Tools.JDBCTools;
import eneityDAO.evePicDao;
import eneityDAO.eventRecordDao;
import entity.EvePic;
import net.sf.json.JSONObject;

//删除单个事件
@WebServlet("/workOfEvent/removeEvent")
public class removeEvent extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	eventRecordDao eventRecordDao=new eventRecordDao();
	evePicDao evePicDao = new evePicDao();
    public removeEvent() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		json=new JSONObject();
		PrintWriter out=response.getWriter();
		String eveId=request.getParameter("eventId");
		String everUrl=GetProperty.getPropertyByName("proper","everUrl");
//		System.out.println("eveId:"+eveId);
		try {
			connection=JDBCTools.getConnection();
			JDBCTools.beginThing(connection);
			//通过eveid获取evepic的图片对象数组
			List<EvePic> evepicList =new ArrayList<EvePic>();
			String sql2="SELECT * FROM evepic WHERE eveId = ? ";
			evepicList=evePicDao.getForList(connection, sql2, eveId);
			String[] srcList=new String[evepicList.size()];
			//获取图片对象中的src: http://118.89.53.234:8099/SafeCDTU/         eventPic/18619b24d3be4c3bb224fbd41eeaee82.png
			int i=0;
			for (EvePic evePic : evepicList) {
				srcList[i]=evePic.getSrc();
				i++;
			}
			//获取当前项目的绝对路径
			String url=request.getSession().getServletContext().getRealPath("");
			//转换成绝对路径F:\\java\\package9\\T903FileTestDemo.java
			//D:\eclipse\project\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\SafeCDTU\
			//创建文件  删除图片
			for(int k=0;k<srcList.length;k++){
				srcList[k]=srcList[k].replace(everUrl, "");
				srcList[k]=srcList[k].replace("/", "\\");
				srcList[k]=url+srcList[k];
				File file=new File(srcList[k]);
				file.delete();
			}
			String sql3="DELETE FROM evepic  WHERE  eveId= ?";
			evePicDao.update(connection, sql3, eveId);
			
			String sql="DELETE FROM eventrecord  WHERE  eveId= ?";
			
			eventRecordDao.update(connection, sql,eveId);
			JDBCTools.commitThing(connection);
			json.put("state", true);
			json.put("message", "删除成功");
			out.write(json.toString());
		} catch (Exception e) {
			JDBCTools.rollbackThing(connection);
			json.put("state", false);
			json.put("message", "系统错误,删除失败");
			out.write(json.toString());
			e.printStackTrace();
		}finally{  
            JDBCTools.release(null, null, connection);
            out.flush();
            out.close();
        }  
		
	}

}
