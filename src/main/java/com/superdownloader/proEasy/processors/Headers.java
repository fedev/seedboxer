package com.superdownloader.proEasy.processors;

public class Headers {

	public static final String SEPARATOR = ".";

	public static final String PREFIX = "proEasy" + SEPARATOR;

	public static final String USERNAME = PREFIX + "username";

	public static final String FILES = PREFIX + "files";

	public static final String FTP_PREFIX = PREFIX + "ftp" + SEPARATOR;
	public static final String FTP_USERNAME = FTP_PREFIX + "username";
	public static final String FTP_PASSWORD = FTP_PREFIX +"password";
	public static final String FTP_URL = FTP_PREFIX +"url";
	public static final String FTP_REMOTE_DIR = FTP_PREFIX + "remoteDir";
	public static final String FTP_SENT = FTP_PREFIX + "sent";

	public static final String NOTIFICATION_PREFIX = PREFIX + "notification" + SEPARATOR;
	public static final String NOTIFICATION_EMAIL = NOTIFICATION_PREFIX + "email";

}
