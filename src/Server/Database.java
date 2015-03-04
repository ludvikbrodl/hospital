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
		
	}
	
	/**
	 * Get the content of a file in the database.
	 * @param fileName - the name of the file to be read.
	 * @param marker - if special markers for header should be showed or not.
	 * @return returns a string containing the data from the file. Empty string if no file.
	 */
	public String getFile(String fileName, boolean marker) {
		File file = null;
		for (File f : files) {
			if (f.getName().equals(fileName + ".txt")) {
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
					if (line.startsWith("!") && marker) {
						result += line + "\n";
					}
					if (!line.startsWith("!")) {
						result += line + "\n";
					}
				}
			} catch (FileNotFoundException e) {
				// If it fails just set to empty again
				result = "";
			}
		}
		
		return result;
	}
	
	/**
	 * Remove the file from the database.
	 * @param fileName - the name of the file to be removed.
	 * @param actor - the user who did the action.
	 * @return returns true if file was removed, else false.
	 */
	public boolean removeFile(String fileName, String actor) {
		File file = null;
		for (File f : files) {
			if (f.getName().equals(fileName + ".txt")) {
				file = f;
			}
		}
		if (file != null) {
			// Remove the file from the database. Log it.
			if (files.remove(file) && file.delete()) {
				Audit.log(actor + " removed the file " + fileName + ".txt from the database.");
				return true;
			} else {
				Audit.log(actor + " tried to remove the file " + fileName + ".txt from the database.");
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
	 * @param actor - the user who did the action.
	 * @return returns true if successfully could write to file, else false.
	 */
	public boolean writeFile(String fileName, String content, String actor) {
		File file = null;
		for (File f : files) {
			if (f.getName().equals(fileName + ".txt")) {
				file = f;
			}
		}
		if (file != null) {
			BufferedWriter out = null;
			try {
				// Append data with true in filewriter
				FileWriter fstream = new FileWriter("database/" + fileName + ".txt", true);
				out = new BufferedWriter(fstream);
				
				// Append content
				out.newLine();
				out.append(content);
				
				// Log the action
				Audit.log(actor + " appended content to " + fileName + ".txt");
				
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
	 * @param doctorId - ID of the doctor for the patient.
	 * @param nurseId - ID of the nurse for the patient.
	 * @param divisionId - ID of the division in which the nurse is operating in.
	 * @param actor - the user who did the action.
	 * @return
	 */
	public boolean createEntry(String fileName, String doctorId, String nurseId, String divisionId, String actor) {
		BufferedWriter out = null;
		try {
			// Append data with true in filewriter
			File f = new File("database/" + fileName + ".txt");
			FileWriter fstream = new FileWriter(f, true);
			out = new BufferedWriter(fstream);
			
			out.write("!" + fileName);
			out.newLine();
			out.write("!" + doctorId);
			out.newLine();
			out.write("!" + nurseId);
			out.newLine();
			out.write("!" + divisionId);
			
			// Audit - created a new entry in the database
			String log = actor + " created a new entry in the database: "
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
}
