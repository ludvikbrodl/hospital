package Server;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import javax.security.cert.X509Certificate;

public class Database {
	private ArrayList<File> files;
	private Map<X509Certificate, String> rights;
	
	public Database() {
		
	}
	
	/**
	 * Get the content of a file in the database.
	 * @param fileName - the name of the file to be read.
	 * @return returns a string containing the data from the file. Null if empty.
	 */
	public String getFile(String fileName) {
		
		return "";
	}
	
	/**
	 * Remove the file from the database.
	 * @param fileName - the name of the file to be removed.
	 * @return returns true if file was removed, else false.
	 */
	public boolean removeFile(String fileName) {
		return true;
	}
	
	/**
	 * Appends some content to a file in the database.
	 * @param fileName - the filename of the file to be written.
	 * @param content - the content that will be appended to the file.
	 * @return returns true if successfully could write to file, else false.
	 */
	public boolean writeFile(String fileName, String content) {
		return true;
	}
	
	/**
	 * Creates a new entry in the database with the specified data.
	 * @param fileName - The name of the new file to be added to the database.
	 * @param nurseId - ID of the nurse for the patient.
	 * @param divisionId - ID of the division in which the nurse is operating in.
	 * @param password - 
	 * @return
	 */
	public boolean createEntry(String fileName, String nurseId, String divisionId, String password) {
		return true;
	}
	
	/**
	 * Get the rights from the certificate
	 * @param cert - certificate.
	 * @return returns the rights for the certificate.
	 */
	public String getRights(X509Certificate cert) {
		return "";
	}
}
