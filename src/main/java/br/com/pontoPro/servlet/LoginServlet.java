package br.com.pontoPro.servlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet
{
   private static final long serialVersionUID = -7497744503325113306L;
   
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/login.jsp");
      dispatcher.forward(request, response);
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      String username = request.getParameter("username");
      String password = request.getParameter("password");
      try
      {
         Class.forName("org.hsqldb.jdbc.JDBCDriver");
      }
      catch (ClassNotFoundException e)
      {
         e.printStackTrace();
      }
      try (Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:testdb", "sa", ""))
      {
         try (Statement stmt = conn.createStatement())
         {
            stmt.execute("CREATE MEMORY TABLE IF NOT EXISTS USERS (USERNAME VARCHAR(255) PRIMARY KEY, PASSWORD VARCHAR(255) NOT NULL)");
         }
         try (PreparedStatement checkUserStmt = conn.prepareStatement("SELECT COUNT(*) FROM USERS WHERE USERNAME = ? AND PASSWORD = ?"))
         {
            checkUserStmt.setString(1, username);
            checkUserStmt.setString(2, password);
            ResultSet rs = checkUserStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0)
            {
               response.sendRedirect("plataforma");
            }
            else
            {
               request.setAttribute("mensagemErro", "Usuário ou senha incorretos");
               RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/login.jsp");
               dispatcher.forward(request, response);
            }
         }
      }
      catch (SQLException e)
      {
         throw new ServletException(e);
      }
   }
}
