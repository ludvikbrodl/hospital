package Server;

import javax.security.cert.X509Certificate;

public class ReplyFactory {

	private Database database;

	public ReplyFactory() {
		database = new Database();
	}

	public String CreateReply(String command, X509Certificate cert) {
		String result = "";
		// Split the command line
		String[] split = command.split(" ");
		if (split.length > 0) {

			// Get the file and check for patientID, nurseID and divisionID
			String fileName = split[1].replace('-', ' ').trim();
			String file_content = database.getFile(fileName, true);
			
			String patientID = "";
			String nurseID = "";
			String divisionID = "";
			if (file_content != "") {

				// Get the three fields and remove the ! marker
				String lines[] = file_content.split("\\r?\\n");
				patientID = lines[0].substring(1).trim();
				nurseID = lines[2].substring(1).trim();
				divisionID = lines[3].substring(1).trim();
			}
			
			String[] ra = getRights(cert, patientID, nurseID, divisionID);
			if (ra[0].equals("")) {
				return "Error, no certificate...";
			}
			
			// get the rights
			String rights = ra[0];

			// Get the user identity
			String actor = ra[1];

			if (rights.charAt(0) == '1' && split[0].equalsIgnoreCase("read")
					&& split.length > 1) {

				// Reads the content
				result = database.getFile(fileName, false);
			} else if (rights.charAt(1) == '1'
					&& split[0].equalsIgnoreCase("write") && split.length > 2) {
				String data = split[2].replace('-', ' ');
				for (int i = 3; i < split.length; i++) {
					data += (" " + split[i]);
				}

				// Appends the data to the file
				result = "Data appended to file: "
						+ database.writeFile(fileName, data, actor);
			} else if (rights.charAt(2) == '1'
					&& split[0].equalsIgnoreCase("create") && split.length > 3) {
				String nurseId = split[2].replace('-', ' ').trim();
				String divisionId = split[3].replace('-', ' ').trim();
				String doctorId = actor;

				// Creates an entry in the database
				result = "Created an new entry: "
						+ database.createEntry(fileName, doctorId, nurseId, divisionId, actor);
			} else if (rights.charAt(3) == '1'
					&& split[0].equalsIgnoreCase("remove") && split.length > 1) {

				// Remove from the database
				result = "Removed from database: "
						+ database.removeFile(fileName, actor);
			} else {
				// Wrong command
				result = "Wrong command or no rights to access.";
			}
		}
		return result;
	}

	// Returns both the right and the actor
	private String[] getRights(X509Certificate cert, String patientID, String nurseID, String divisionID) {
		if (cert == null) {
			 return new String[] {"", ""};
		}

		String name = cert.getSubjectDN().getName();

		// Split for ",". First field is CN. Second OU.
		String[] fields = name.split(",");

		// See if it's a government organisation and return rights
		if (fields[1].contains("Government")) {
			return new String[] { "1001", "gov" };
		} else {
			// Nurse, Doctor, patient...
			// Split the CN field
			String[] CN_field = fields[0].split("=");

			// Split the CN value with "/" to obtain user type and number
			String[] CN_value = CN_field[1].split("/");

			// Get the user type
			String type = CN_value[0];

			// Get the actor (unique username)
			String actor = CN_value[1];

			// Get the division
			String division = fields[1].substring(3);

			// Check doctor
			if (type.equalsIgnoreCase("doctor")) {
				if (divisionID.equals("") || division.equalsIgnoreCase(divisionID) ) {
					return new String[] { "1110", actor };
				} else {
					return new String[] { "0000", actor };
				}
			}

			// Check nurse
			if (type.equalsIgnoreCase("nurse")) {
				if (actor.equalsIgnoreCase(nurseID)) {
					return new String[] { "1100", actor };
				} else {
					return new String[] { "0000", actor };
				}
			}

			// Check patient
			if (type.equalsIgnoreCase("patient")) {
				if (actor.equalsIgnoreCase(patientID)) {
					return new String[] { "1000", actor };
				} else {
					return new String[] { "0000", actor };
				}
			}
			return new String[] {"1111", actor};
		}
	}
}
