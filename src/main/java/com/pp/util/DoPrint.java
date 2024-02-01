package com.pp.util;

import org.apache.log4j.Logger;

public class DoPrint {

	private static final Logger log = Logger.getLogger(DoPrint.class);

	public static void logInfo(String message) {
		if (log.isInfoEnabled()) {
			log.info(message);
		}
	}

	public static void logException(String message, Exception e) {
		if (log.isInfoEnabled()) {
			e.printStackTrace();
			log.error(message, e);
			log.info(message + "|" + e.getMessage());
		}
	}

}
