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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet
{
   private static final long serialVersionUID = -4045487181959920758L;
   
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/register.jsp");
      dispatcher.forward(request, response);
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      String newUsername = request.getParameter("newUsername");
      String newPassword = request.getParameter("newPassword");
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
         try (PreparedStatement checkUserStmt = conn.prepareStatement("SELECT COUNT(*) FROM USERS WHERE USERNAME = ?"))
         {
            checkUserStmt.setString(1, newUsername);
            ResultSet rs = checkUserStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0)
            {
               request.setAttribute("mensagemErro", "O nome de usuário já existe");
               RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/register.jsp");
               dispatcher.forward(request, response);
            }
            else
            {
               try (PreparedStatement addUserStmt = conn.prepareStatement("INSERT INTO USERS (USERNAME, PASSWORD) VALUES (?, ?)"))
               {
                  addUserStmt.setString(1, newUsername);
                  addUserStmt.setString(2, newPassword);
                  addUserStmt.executeUpdate();
                  request.getSession().setAttribute("mensagemSucesso", "Usuário Cadastrado com Sucesso!");
                  response.sendRedirect("login");
               }
            }
         }
      }
      catch (SQLException e)
      {
         throw new ServletException(e);
      }
   }
}
