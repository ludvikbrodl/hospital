package Server;

import javax.security.cert.X509Certificate;

public class ReplyFactory {
	
	private Database database;
	
	public ReplyFactory() {
		database = new Database();
	}
	
	public String CreateReply(String command, X509Certificate cert) {
		String rights = database.getRights(cert);
		if (rights.equals("")) {
			return "Error, no certificate...";
		}
		
		String result = "";
		// Split the command line
		String[] split = command.split(" ");
		if (split.length > 0) {
			if (rights.charAt(0) == '1' && split[0].equalsIgnoreCase("read") && split.length > 1) {
				String fileName = split[1].replace('-', ' ').trim();
				
				// Reads the content
				result = database.getFile(fileName);
			} else if (rights.charAt(1) == '1' && split[0].equalsIgnoreCase("write") && split.length > 2) {
				String fileName = split[1].replace('-', ' ').trim();
				String data = split[2].replace('-', ' ').trim();
				
				// Appends the data to the file
				result = "Data appended to file: " + database.writeFile(fileName, data);
			} else if (rights.charAt(2) == '1' && split[0].equalsIgnoreCase("create") && split.length > 4) {
				String fileName = split[1].replace('-', ' ').trim();
				String nurseId = split[2].replace('-', ' ').trim();
				String divisionId = split[3].replace('-', ' ').trim();
				String password = split[4].replace('-', ' ').trim();
				
				// Creates an entry in the database
				result = "Created an new entry: " + database.createEntry(fileName, nurseId, divisionId, password);
			} else if (rights.charAt(3) == '1' && split[0].equalsIgnoreCase("remove") && split.length > 1) {
				String fileName = split[1].replace('-', ' ').trim();
				
				// Remove from the database
				result = "Removed from database: " + database.removeFile(fileName);
			} else {
				// Wrong command
				result = "Wrong command or no rights to access.";
			}
		}
		return result;
	}
}
