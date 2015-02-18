package Server;

import java.io.File;

public class Audit {
	private static final File file = new File("log.txt");
	private static boolean logging = true;
	
	public Audit() {
		
	}
	
	/**
	 * Log the sentence in the audit log.
	 * @param sentence - the text to be logged.
	 */
	public static void log(String sentence) {
		
	}
	
	/**
	 * Set if the audit class should log or not.
	 * @param val - true and audit will log, false and it will not.
	 */
	public static void setLogging(boolean val) {
		Audit.logging = val;
	}
	
	/**
	 * Set the file to store the log information
	 * @param file - the file to save log.
	 */
	public static void setLoggingFile(File file) {
		Audit.file = file;
	}
}
