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
 * @author umair.ali
 * @version 1.0
 * 
 *          Servlet implementation class Cronner Will Run Cron Jobs
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
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
			DoPrint.logInfo("Cron Job Scheduled! \nAt:" + sdf.format(new Date()) + " \nWorking Directory is:"
					+ System.getProperty("user.dir")
					+ "\nKindly Keep Your .env File On This Path As Told In README.md\nCome On You Have "
					+ Long.parseLong(Prop.getProperty("timer.delay"))
					+ " Seconds.(If Already Kept Kindly Ignore This 🔥@UMAIR.ALI🔥)");

			response.setContentType("text/html; charset=UTF-8");
			response.getWriter()
					.append("Cron Job Scheduled! <br>At:" + sdf.format(new Date()) + " <br>Working Directory is:"
							+ System.getProperty("user.dir")
							+ "<br>Kindly Keep Your .env File On This Path As Told In README.md<br>Come On You Have "
							+ Long.parseLong(Prop.getProperty("timer.delay"))
							+ " Seconds.(If Already Kept Kindly Ignore This &#128293;@UMAIR.ALI&#128293;)<br>"
							+ "<a href=\"https://github.com/umair-ali-bhutto\" target=\"_blank\">@Github</a><br>"
							+ "<a href=\"mailto:umair2101f@aptechgdn.net\">@Email (umair2101f@aptechgdn.net)</a>");
		} catch (Exception e) {
			response.getWriter().append("Something Went Wrong.");
		}

	}

	private void scheduleCronJob() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");

			scheduler.scheduleAtFixedRate(() -> {
				DoPrint.logInfo("Cron Job Started at: " + sdf.format(new Date()));
				try {
					DoPrint.logInfo("Working Directory: " + System.getProperty("user.dir"));
					ZipperMain.main(null);
				} catch (Exception e) {
					DoPrint.logException("Exception during cron job execution", e);
				}
				DoPrint.logInfo("Cron Job Ended at: " + sdf.format(new Date()));
			}, Long.parseLong(Prop.getProperty("timer.delay")), Long.parseLong(Prop.getProperty("timer.period")),
					TimeUnit.SECONDS);

		} catch (Exception e) {
			DoPrint.logException("Exception CRON JOB", e);
		}
	}
}
