package Client;

import java.net.*;
import java.io.*;

import javax.net.ssl.*;
import javax.security.cert.X509Certificate;
import javax.swing.text.html.HTMLDocument.HTMLReader.SpecialAction;

import java.security.KeyStore;
import java.security.cert.*;
import java.util.Scanner;

/*
 * This example shows how to set up a key manager to perform client
 * authentication.
 *
 * This program assumes that the client is not inside a firewall.
 * The application can be modified to connect to a server outside
 * the firewall by following SSLSocketClientWithTunneling.java.
 */
public class Client {

    public static void main(String[] args) throws Exception {
        String host = null;
        int port = -1;
        for (int i = 0; i < args.length; i++) {
            System.out.println("args[" + i + "] = " + args[i]);
        }
        if (args.length < 2) {
            System.out.println("USAGE: java client host port");
           // System.exit(-1);
        }
        try { /* get input parameters */
            host = "localhost"; //args[0]
            port =  6000; //Integer.parseInt(args[1]);
        } catch (IllegalArgumentException e) {
            System.out.println("USAGE: java client host port");
            System.exit(-1);
        }

        try { /* set up a key manager for client authentication */
            SSLSocketFactory factory = null;
            try {
                char[] password = "password".toCharArray();
                KeyStore ks = KeyStore.getInstance("JKS");
                KeyStore ts = KeyStore.getInstance("JKS");
                KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
                
                TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
                SSLContext ctx = SSLContext.getInstance("TLS");
				ts.load(new FileInputStream("clienttruststore"), password); // truststore password (storepass);
				
				String userName = "";
				String userPass = "";
				
					@SuppressWarnings("resource")
					Scanner scan = new Scanner(System.in);
					System.out.println("Usernames are the names of the keystores with the keys, ie: clientkeystore_nurse4");
					System.out.println("All passwords are the the string after the '_' sign");
					System.out.println("except for gov which password is passgov");
					while (true) {
						System.out.println("Input Username: ");
						userName = scan.nextLine();
						System.out.println("Input password");
						userPass = scan.nextLine();			
						try {
							ks.load(new FileInputStream(userName), userPass.toCharArray());  // keystore password (storepass)
							break;
						} catch (FileNotFoundException fileE) {
							System.out.println("Username does not exist");
						} catch (IOException ioe) {
							System.out.println("Incorrect Password or username is not a keystore");
						}
					}
				kmf.init(ks, userPass.toCharArray()); // certificate password (keypass)
				tmf.init(ts);
				ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
                factory = ctx.getSocketFactory();
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
            SSLSocket socket = (SSLSocket)factory.createSocket(host, port);
            System.out.println("\nsocket before handshake:\n" + socket + "\n");

            /*
             * send http request
             *
             * See SSLSocketClient.java for more information about why
             * there is a forced handshake here when using PrintWriters.
             */
            socket.startHandshake();

            SSLSession session = socket.getSession();
            X509Certificate cert = (X509Certificate)session.getPeerCertificateChain()[0];
            String subject = cert.getSubjectDN().getName();
            System.out.println("certificate name (subject DN field) on certificate received from server:\n" + subject + "\n");
            System.out.println("socket after handshake:\n" + socket + "\n");
            System.out.println("secure connection established\n\n");

            BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg;
			for (;;) {
                System.out.print(">");
                msg = read.readLine();
                if (msg.equalsIgnoreCase("quit")) {
				    break;
				}
                System.out.print("sending '" + msg + "' to server...");
                out.println(msg);
                out.flush();
                System.out.println("done");
                System.out.println("Received!");
                String res = in.readLine();
                
               System.out.println("received '" + res.replaceAll("@£", "\n") + "' from server\n");
            }
            in.close();
			out.close();
			read.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Creates a cert for the client with script
    private boolean createCert() {
    	return true;
    }
    
    // Send a string to the server?
    private String sendString(String send) {
    	return "";
    }
    
}