package com.pp.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
	private ScheduledExecutorService scheduler;

	public Cronner() {
		super();
	}

	@Override
	public void init() throws ServletException {
		super.init();
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduleCronJob();
	}

	@Override
	public void destroy() {
		super.destroy();
		if (scheduler != null) {
			scheduler.shutdownNow();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Cron Job Scheduled!");
	}

	private void scheduleCronJob() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		scheduler.scheduleAtFixedRate(() -> {
			try {
				DoPrint.logInfo("Working Directory: " + System.getProperty("user.dir"));
				DoPrint.logInfo("Cron Job Started at: " + sdf.format(new Date()));
				ZipperMain.main(null);
				DoPrint.logInfo("Cron Job Ended at: " + sdf.format(new Date()));
			} catch (Exception e) {
				DoPrint.logException("Exception during cron job execution", e);
			}
		}, 20, Long.parseLong(Prop.getProperty("timer.period")), TimeUnit.SECONDS);
	}
}
