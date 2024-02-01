package com.pp.servlet;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pp.main.ZipperMain;
import com.pp.util.DoPrint;
import com.pp.util.Prop;

/**
 * Servlet implementation class Cronner Will Run Cron Jobs
 */
@WebServlet("/")
public class Cronner extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Cronner() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run() {
					ZipperMain.main(null);
				}
			}, 20000, Long.parseLong(Prop.getProperty("timer.period")));
		} catch (Exception e) {
			DoPrint.logException("Cronner Exception", e);
		}

		// response.getWriter().append("Cron Job Scheduled!");
	}

}
