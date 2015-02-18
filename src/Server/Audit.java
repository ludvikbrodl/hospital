package Server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Audit {
	private static File file = new File("log.txt");
	private static boolean logging = true;
	
	/**
	 * Log the sentence in the audit log.
	 * @param sentence - the text to be logged.
	 */
	public static void log(String sentence) {
		if (logging) {
			BufferedWriter out = null;
			try {
				// Append data with true in filewriter
				FileWriter fstream = new FileWriter(Audit.file, true);
				out = new BufferedWriter(fstream);
				
				// Todays date
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String time = sdf.format(date);
				
				// Write to the audit
				out.write(time + " - " + sentence + "\n");
				
				// Close the file
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Set if the audit class should log or not.
	 * @param val - true and audit will log, false and it will not.
	 */
	public static void setLogging(boolean val) {
		if (val == false) {
			Audit.log("Audit log turned off.");
		}
		// Who turned off the log needs to be implemented.
		Audit.logging = val;
	}
	
	/**
	 * Set the file to store the log information
	 * @param file - the file to save log.
	 */
	public static void setLoggingFile(File file) {
		Audit.log("Changed audit log to file: " + file);
		Audit.file = file;
	}
}
