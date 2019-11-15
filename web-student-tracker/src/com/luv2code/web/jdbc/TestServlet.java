package com.luv2code.web.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	//Define datasource/connection pool for Resource Injection
	@Resource(name="jdbc/university")
	private DataSource dataSource;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//set up the printwriter
		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");
		
		//get a connection
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try{
			myConn = dataSource.getConnection();
			String sql = "Select * from student";
			myStmt = myConn.createStatement();
			
			myRs = myStmt.executeQuery(sql);
			
			while(myRs.next())
			{
				String email = myRs.getString("email");
				out.println(email);
			}
		}
		
		
		catch(Exception exc)
		{
			exc.printStackTrace();
		}
		
		
	}

}
