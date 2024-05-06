package com.example.myapplication;

/*
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JOptionPane;

public class MsgSSLClientSocket {

	private static final String secretKey = "ST1PAI3-secretKey";

	private static String generateNonce() {
        return Long.toHexString(ThreadLocalRandom.current().nextLong());
    }

    private static String calculateHMAC(String mensaje) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
			byte[] secretKeyBytes = secretKey.getBytes();
            SecretKeySpec secretKey = new SecretKeySpec(secretKeyBytes, "HmacSHA256");
            mac.init(secretKey);

            byte[] hmacBytes = mac.doFinal(mensaje.getBytes());
			String calculatedHMACBase64 = Base64.getEncoder().encodeToString(hmacBytes);
			return calculatedHMACBase64;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

	// Functionality
	public static void load300Test() throws IOException {
		for (int i = 1; i <= 300; i++) {
			String username = "user" + i;
			String password = "password" + i;
			String msg = "Hello from user" + i + "_" + generateNonce();
			String hmac = calculateHMAC(msg);
	
			new Thread(() -> {
				boolean success = false;
				while (!success) {
					try {
						SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
						SSLSocket socket = (SSLSocket) factory.createSocket("localhost", 3343);
						BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
	
						String clientMsg = username + "," + password + "," + msg + "," + hmac;
						output.println(clientMsg);
						output.flush();
	
						String response = input.readLine();
						System.out.println("Response for " + username + ": " + response);
	
						output.close();
						input.close();
						socket.close();
	
						success = true;
					} catch (IOException e) {
						try {
							Thread.sleep(1000); // 1 second delay before retrying
						} catch (InterruptedException ex) {
							ex.printStackTrace();
						}
					}
				}
			}).start();
		}
	}
	
	public static void functionalityTest() throws IOException {
		
		try {
			SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			SSLSocket socket = (SSLSocket) factory.createSocket("localhost", 3343);
			
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream())); // for reading response from server
			PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream())); // for sending login to server

			String username = JOptionPane.showInputDialog(null, "Introduce tu nombre de usuario:");
			String password = JOptionPane.showInputDialog(null, "Introduce tu clave de acceso:");
			String msg = JOptionPane.showInputDialog(null, "Introduce tu mensaje:");

			msg = msg + "_" + generateNonce();
			String clientMsg = username + "," + password + "," + msg + "," + calculateHMAC(msg);
			output.println(clientMsg);
			output.flush();

			String response = input.readLine(); // read response from server
			JOptionPane.showMessageDialog(null, response); // display response

			output.close();
			input.close();
			socket.close();
		
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			System.exit(0);
		}
	}
	
	public static void main(String[] args) throws IOException {

		String option = JOptionPane.showInputDialog(null, "Introduce 1 para probar la funcionalidad o 2 para probar la carga de 300 conexiones:");

		switch (option) {
			case "1":
				functionalityTest();
				break;
			case "2":
				load300Test();
				break;
			default:
				System.out.println("Valor no válido.");
		}
	}
}

*/