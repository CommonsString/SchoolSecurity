package test;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class picdeltest
 */
@WebServlet("/picdeltest")
public class picdeltest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public picdeltest() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("test1"); 
		System.out.println(request.getSession().getServletContext().getRealPath(""));
	//D:\eclipse\project\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\SafeCDTU\
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("test"); 
		File directory = new File("");//设定为当前文件夹 
		 try{ 
		     System.out.println(directory.getCanonicalPath());//获取标准的路径 
		     System.out.println(directory.getAbsolutePath());//获取绝对路径 
		 }catch(Exception e){
			 
		 } 
	}

}
