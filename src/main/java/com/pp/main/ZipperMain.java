package com.pp.main;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.pp.util.DoPrint;

public class ZipperMain {
	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DoPrint.logInfo("Cron Job Executed at: " + sdf.format(new Date()));
	}
}
