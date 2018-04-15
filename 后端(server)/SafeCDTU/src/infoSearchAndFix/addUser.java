package infoSearchAndFix;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Tools.CommonUtils;
import Tools.JDBCTools;
import eneityDAO.userDao;
import entity.User;
import net.sf.json.JSONObject;

/**
 * 添加用户
 */
@SuppressWarnings("serial")
@WebServlet("/infoSearchAndFix/addUser")
public class addUser extends HttpServlet{
     //数据库驱动，可更改
     private userDao dao = new userDao();
     @Override
     protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          PrintWriter out = null;
          JSONObject json = null;
          java.sql.Connection con=null;
          try {
              response.setCharacterEncoding("UTF-8"); //可以删除(拦截器)
              con = JDBCTools.getConnection(); //捕获异常
              out = response.getWriter(); //流

              //获取参数
              User user = CommonUtils.toBean(request.getParameterMap() , User.class);
              //设置属性
               user.setPswd("4297F44B13955235245B2497399D7A93"); //MD5加密
               user.setuId(UUID.randomUUID().toString().replace("-", "")); // 设置id
              user.setLevel(3); //权限
              user.setIsNewReply(0);
              String sql = "insert into users (uId,account,pswd,name,sex,tel,email,position,level,isNewReply,isNewPotNotice,isNewDepNotice,isNewEveNotice) values ( ?,?,?,?,?,?,?,?,?,?,?,?,?)";  //sql可修改
              Object params[] = {user.getuId() , user.getAccount() , user.getPswd() , user.getName()
                        , user.getSex() , user.getTel() , user.getEmail() , user.getPosition() , user.getLevel()
                        , user.getIsNewReply() , user.getIsNewPotNotice() , user.getIsNewDepNotice() , user.getIsNewEveNotice()};
              int flag = dao.update(con , sql, params);

              //第二种 逻辑判断
              if(flag == 0){ //操作失败
                   json = new JSONObject();
                   json.put("state", false);
                   json.put("message", "添加用户信息失败！");
                   out.write(json.toString());
              }else{  // 打包
                   json = new JSONObject();
                   json.put("state", true);
                   json.put("message", "添加成功");
                   out.write(json.toString());
              }
          } catch (Exception e) {
              json = new JSONObject();
              json.put("state", false);
              json.put("message", "系统错误，添加用户信息失败！");
              out.write(json.toString());
//            e.printStackTrace();  //调试时打开
          }finally{
              JDBCTools.release(null, null, con);
              out.flush();
              out.close();

          }
     }

     @Override
     protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          doPost(request, response);
     }

}



