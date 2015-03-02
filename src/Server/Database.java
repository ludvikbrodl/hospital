package Server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.security.cert.X509Certificate;

public class Database {
	private ArrayList<File> files;
	private Map<X509Certificate, String> rights;
	
	public Database() {
		rights = new HashMap<X509Certificate, String>();
		files = new ArrayList<File>();
		
		// Load all files from the database
		File directory = new File("database/");
		File[] fList = directory.listFiles();
		for (File f : fList) {
			if (f.isFile()) {
				files.add(f);
			}
		}
		
		// Should log who called the method?
	}
	
	/**
	 * Get the content of a file in the database.
	 * @param fileName - the name of the file to be read.
	 * @return returns a string containing the data from the file. Empty string if no file.
	 */
	public String getFile(String fileName) {
		File file = null;
		for (File f : files) {
			if (f.getName().equals(fileName)) {
				file = f;
			}
		}
		String result = "";
		if (file != null) {
			try {
				// Scan through the file
				Scanner scan = new Scanner(file);
				
				while (scan.hasNext()) {
					String line = scan.nextLine();
					if (!line.startsWith("!")) {
						result += line + "\n";
					}
				}
			} catch (FileNotFoundException e) {
				// If it fails just set to null again
				result = null;
			}
		}
		
		return result;
	}
	
	/**
	 * Remove the file from the database.
	 * @param fileName - the name of the file to be removed.
	 * @return returns true if file was removed, else false.
	 */
	public boolean removeFile(String fileName) {
		File file = null;
		for (File f : files) {
			if (f.getName().equals(fileName)) {
				file = f;
			}
		}
		if (file != null) {
			// Remove the file from the database. Log it.
			if (files.remove(file) && file.delete()) {
				Audit.log("The file " + fileName + " was removed from the database");
				return true;
			} else {
				Audit.log("The file " + fileName + " was not removed from the databse.");
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Appends some content to a file in the database.
	 * @param fileName - the filename of the file to be written.
	 * @param content - the content that will be appended to the file.
	 * @return returns true if successfully could write to file, else false.
	 */
	public boolean writeFile(String fileName, String content) {
		File file = null;
		for (File f : files) {
			if (f.getName().equals(fileName)) {
				file = f;
			}
		}
		if (file != null) {
			BufferedWriter out = null;
			try {
				// Append data with true in filewriter
				FileWriter fstream = new FileWriter(fileName, true);
				out = new BufferedWriter(fstream);
				
				// Append content
				out.newLine();
				out.append(content);
				
				// Log the action
				Audit.log("Content was appended to " + fileName);
				
				// Close the file
				out.close();
				
				return true;
			} catch (IOException e) {
				return false;
			}
		}
		return false;
	}
	
	/**
	 * Creates a new entry in the database with the specified data.
	 * @param fileName - The name of the new file to be added to the database.
	 * @param nurseId - ID of the nurse for the patient.
	 * @param divisionId - ID of the division in which the nurse is operating in.
	 * @param password - password for the patient
	 * @return
	 */
	public boolean createEntry(String fileName, String nurseId, String divisionId, String password) {
		BufferedWriter out = null;
		try {
			// Append data with true in filewriter
			File f = new File(fileName);
			FileWriter fstream = new FileWriter(f, true);
			out = new BufferedWriter(fstream);
			
			out.write("!" + fileName);
			out.newLine();
			out.write("!" + nurseId);
			out.newLine();
			out.write("!" + divisionId);
			out.newLine();
			out.write("!" + password); 		// Hash the passwords with salt??????
			
															// Should log who created the entry??????
			// Audit - created a new entry in the database
			String log = "A new entry was created in the database: "
					+ fileName + " " + nurseId + " " + divisionId;
			Audit.log(log);
			
			// Close the file
			out.close();
			
			// Save to the database
			files.add(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * Get the rights from the certificate
	 * @param cert - certificate.
	 * @return returns the rights for the certificate. Empty string if none.
	 */
	public String getRights(X509Certificate cert) {
		return "";
	}
}
