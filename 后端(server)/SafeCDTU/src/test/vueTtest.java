package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import entity.User;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class vueTtest
 */
@WebServlet("/vueTtest")
public class vueTtest extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    public vueTtest() {
        super();
      
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("进来了1");
//		response.setContentType("text/html;charset=UTF-8;pageEncoding=UTF-8");
//        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Access-Control-Allow-Origin", "*");

        Goods goods1 = new Goods();
        goods1.setId("1");
        goods1.setBarcode("101");
        goods1.setName("中华面粉");
        goods1.setShortName("面粉");

        Goods goods2 = new Goods();
        goods2.setId("2");
        goods2.setBarcode("102");
        goods2.setName("中华面包");
        goods2.setShortName("面包");

        Goods goods3 = new Goods();
        goods3.setId("3");
        goods3.setBarcode("103");
        goods3.setName("中华面条");
        goods3.setShortName("面条");

//        List<Goods> goodList = new ArrayList<Goods>();
//        goodList.add(goods1);
//        goodList.add(goods2);
//        goodList.add(goods3);
        
        PrintWriter out = response.getWriter();  

        try {
            //String json = JSONObject.toJSONString(goodList);
        	//String json=goodList.toString();
        	JSONObject json=new JSONObject();
        	json.put("goods1", goods1);
        	json.put("goods2", goods2);
            System.out.println("json:" + json);
            out.write(json.toString());  
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Goods goods1 = new Goods();
        goods1.setId("1");
        goods1.setBarcode("101");
        goods1.setName("中华面粉");
        goods1.setShortName("面粉");

        Goods goods2 = new Goods();
        goods2.setId("2");
        goods2.setBarcode("102");
        goods2.setName("中华面包");
        goods2.setShortName("面包");

        Goods goods3 = new Goods();
        goods3.setId("3");
        goods3.setBarcode("103");
        goods3.setName("中华面条");
        goods3.setShortName("面条");

        List<Goods> goodList = new ArrayList<Goods>();
        goodList.add(goods1);
        goodList.add(goods2);
        goodList.add(goods3);
        
        PrintWriter out = response.getWriter();  

        try {
//            String json = JSONObject.toJSONString(goodList);
//        	String json=goodList.toString();
        	JSONObject json=new JSONObject();
        	json.put("goodList", goodList);
//        	json.put("goods1", goods1);
//        	json.put("goods2", goods2);
            System.out.println("json:" + json);
            out.write(json.toString());  
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
	}
	
	
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
////		String name=acceptJSON(request);
////		System.out.println("name:"+name);
//		user user=new user();
//		user.setId("123");
//		String name=request.getParameter("name");
//		user.setName(name);
//		String cat=request.getParameter("cat");
//		Object param=request.getParameter("param");
//		System.out.println("name:"+name);
//		System.out.println("cat:"+cat);
//		System.out.println("param:"+param);
//		Goods goods3 = new Goods();
//        goods3.setId("3");
//        goods3.setBarcode("103");
//        goods3.setName("中华面条");
//        goods3.setShortName("面条");
//       // PrintWriter out = response.getWriter(); 
//     
//        HttpSession sessoin=request.getSession();
//        sessoin.setAttribute("sessionUser", user);
//       
//        try {
//        	JSONObject json=new JSONObject();
//        	json.put("goods3", goods3);
//        	
//            System.out.println("json:" + json);
//            
//            response.setContentType("application/json");
//            response.getWriter().write(json.toString());
//            //out.write(json.toString());  
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
////            out.flush();
////            out.close();
//        }
//		
//	}
	
	

}
