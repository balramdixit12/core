package com.niit;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.beans.Employee;

/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/myservlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MyServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void requestProcess(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String destinationName = "queue/MyQueue";
		PrintWriter out = response.getWriter();
		Context ic = null;
		ConnectionFactory cf = null;
		Connection connection = null;

		try {
			ic = new InitialContext();

			cf = (ConnectionFactory) ic.lookup("/ConnectionFactory");
			Queue queue = (Queue) ic.lookup(destinationName);

			connection = cf.createConnection();
			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			MessageProducer publisher = session.createProducer(queue);

			connection.start();

			TextMessage message = session.createTextMessage("Hello WildFly !");
			publisher.send(message);

			out.println("Message sento to the JMS Provider");

			// 2. Sending ObjectMessage to the Queue
			ObjectMessage objMsg = session.createObjectMessage();

			Employee employee = new Employee();
			employee.setId(2163);
			employee.setName("Kumar");
			employee.setDesignation("CTO");
			employee.setSalary(100000);
			objMsg.setObject(employee);
			publisher.send(objMsg);
			System.out.println("2. Sent ObjectMessage to the Queue");

		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {

			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		requestProcess(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		requestProcess(request, response);
	}

}
