package ui.controller;


import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.db.StudentDB;
import domain.model.Student;

@WebServlet("/StudentInfo")
public class StudentInformatie extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String destination;
	
	StudentDB klas = new StudentDB();
       
    public StudentInformatie() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		destination = "index.html";
		String command = request.getParameter("command");

		switch (command){
			case "overview":
				destination = overview(request, response);
				break;
			case "find":
				destination = find(request, response);
				break;
			case "add":
				destination = add(request, response);
				break;
			case "delete":
				destination = delete(request, response);
				break;
			default:
				destination = "index.html";
				break;
		}

		request.getRequestDispatcher(destination).forward(request, response);
	}

	private String delete(HttpServletRequest request, HttpServletResponse response) {
    	String verwijderString = request.getParameter("verwijder");

    	if (verwijderString.equals("Zeker")){
			String voornaam = request.getParameter("voornaam");
			String naam = request.getParameter("naam");
			klas.verwijder(naam, voornaam);
			destination = "studentOverview.jsp";
			return destination;
		}else{
			return "index.html";
		}



	}

	private String add(HttpServletRequest request, HttpServletResponse response){
		String naam = request.getParameter("naam");
		String voornaam = request.getParameter("voornaam");
		String leeftijd = request.getParameter("leeftijd");
		String studierichting = request.getParameter("studierichting");

		String destination = "index.html";

		if (naam.isEmpty() || voornaam.isEmpty() || leeftijd.isEmpty() || studierichting.isEmpty()) {
			destination = "studentForm.jsp";
		}
		else {
			Student student = new Student(naam, voornaam, Integer.parseInt(leeftijd), studierichting);
			klas.voegToe(student);
			request.setAttribute("studenten", klas.getKlas());
			destination = "studentOverview.jsp";
		}

		return destination;
	}

	private String find(HttpServletRequest request, HttpServletResponse response){
		String naam=request.getParameter("naam");
		String voornaam=request.getParameter("voornaam");
		String destination="";

		if (naam==null || voornaam== null) {
			destination="nietGevonden.jsp";
		}
		else {
			Student student=klas.vind(naam, voornaam);
			if (student==null) {
				destination="nietGevonden.jsp";
			}
			else {
				destination="gevonden.jsp";
				request.setAttribute("student", student);
			}
		}

		return destination;
	}

	private String overview(HttpServletRequest request, HttpServletResponse response){
		request.setAttribute("studenten", klas.getKlas());
		destination = "studentOverview.jsp";
		return destination;
	}

}
