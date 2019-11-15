package com.luv2code.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private StudentDbUtil studentDbUtil;
	
	@Resource(name="jdbc/university")
	private DataSource dataSource;
	
	
	@Override
	public void init() throws ServletException {
		super.init();
		
		//Create student db util ... and pass in the conn pool / datasource
		try{
			studentDbUtil = new StudentDbUtil(dataSource);
		}
		catch(Exception exc)
		{
			throw new ServletException(exc);
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			
			//read the command parameter
			String theCommand = request.getParameter("command");
			
			//if the command is missing then default to listing students
			if(theCommand==null)
			{
				theCommand="LIST";
			}
			
			// route to the approriate method
			
			switch(theCommand)
			{
			case "LIST":
				listStudents(request,response);
				break;
				
			case "ADD":
				addStudent(request,response);
				break;
				
			case "LOAD":
				loadStudent(request,response);	
				break;
				
			case "UPDATE":
				updateStudent(request,response);
				break;
				
			case "DELETE":
				deleteStudent(request,response);
				break;
			
			default:
				listStudents(request,response);
			}
			
			
			//list the students ... in MVC fashion
			
			
			listStudents(request,response);
		}
		catch(Exception exc)
		{
			throw new ServletException(exc);
		}
	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		//read student id from form data
		String theStudentId = request.getParameter("studentId");
		
		//delete student from database
		studentDbUtil.deleteStudent(theStudentId);
		
		//send back to list of student page
		listStudents(request,response);
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		int id=Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		Student theStudent = new Student(id,firstName,lastName,email);
		studentDbUtil.updateStudent(theStudent);
		listStudents(request,response);
		
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		//read student id from data
		String theStudentId = request.getParameter("studentId");
		Student theStudent = studentDbUtil.getStudent(theStudentId);
		
		request.setAttribute("THE_STUDENT", theStudent);
		
		RequestDispatcher dispatcher= request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
		
		
		//send to jsp
		
		
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		//read student info from the form data
		String firstName=request.getParameter("firstName");
		String lastName=request.getParameter("lastName");
		String email=request.getParameter("email");
		
		
		//create a new student object
		Student theStudent=new Student(firstName, lastName, email);
		
		//add the student to the database
		studentDbUtil.addStudent(theStudent);
		
		//send back to the main page
		listStudents(request,response);
		
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) 
		throws Exception{
		
		//get student from db util
		List<Student> students = studentDbUtil.getStudents();
		
		//add students to the request
		request.setAttribute("STUDENT_LIST",students);
		
		//send to JSP (view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
		
	}

}
