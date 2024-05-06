package com.example.myapplication;

/*
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class MsgSSLServerSocket {

	private static final String secretKey = "ST1PAI3-secretKey";
	private static final int THREAD_POOL_SIZE = 300;

	// DB operations
	public static Connection createConnection() {
		
		Connection conn = null;
		Statement statement = null;

		try {
			conn = DriverManager.getConnection("jdbc:sqlite:st1-database.db");
			statement = conn.createStatement();
			statement.setQueryTimeout(30);  

			statement.executeUpdate("drop table if exists users");
			statement.executeUpdate("create table users (username string unique, password string)");
			System.out.println("Tabla users creada exitosamente.");

			statement.executeUpdate("drop table if exists transactions");
			statement.executeUpdate("create table transactions (username string, transaction_value string)");
			System.out.println("Tabla transactions creada exitosamente.");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static void insertUser(Connection conn, String username, String password) {

		PreparedStatement preparedStatement = null;

		try {		
			String insertSQL = "INSERT INTO users (username, password) VALUES (?, ?)";
			preparedStatement = conn.prepareStatement(insertSQL);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			preparedStatement.executeUpdate();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void insertTransaction(Connection conn, String username, String transaction) {

		PreparedStatement preparedStatement = null;

		try {		
			String insertSQL = "INSERT INTO transactions (username, transaction_value) VALUES (?, ?)";
			preparedStatement = conn.prepareStatement(insertSQL);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, transaction);
			preparedStatement.executeUpdate();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void populateDatabase(Connection conn) {
		try {
			for (int i = 1; i <= 300; i++) {
				insertUser(conn, "user" + i, "password" + i);
			}
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM users");
			System.out.println("Tabla de usuarios poblada exitosamente con " + rs.getInt(1) + " usuarios.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean verifyUserExists(Connection conn, String username, String password) {
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'");
			return rs.next(); // true if there is a row, false if not
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Integrity msg check
	private static boolean verifyHMAC(String message, String receivedHMAC) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			byte[] secretKeyBytes = secretKey.getBytes();
			SecretKeySpec secretKey = new SecretKeySpec(secretKeyBytes, "HmacSHA256");
			mac.init(secretKey);

			byte[] calculatedHMAC = mac.doFinal(message.getBytes());
			String calculatedHMACBase64 = Base64.getEncoder().encodeToString(calculatedHMAC);
			return calculatedHMACBase64.equals(receivedHMAC);

		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			e.printStackTrace();
			return false;
		}
	}

	private static boolean verifyRepeatedTransaction(Connection conn, String username, String transaction) {
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM transactions WHERE username = '" + username + "' AND transaction_value = '" + transaction + "'");
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void main(String[] args) {
        
		try {
			Connection conn = createConnection();
            populateDatabase(conn);

            SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket serverSocket = (SSLServerSocket) factory.createServerSocket(3343);

            ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

            while (true) {
                System.err.println("Waiting for connection...");
                SSLSocket socket = (SSLSocket) serverSocket.accept();
                executorService.execute(new ClientHandler(socket, conn));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        
		private final SSLSocket socket;
        private final Connection conn;

        public ClientHandler(SSLSocket socket, Connection conn) {
            this.socket = socket;
            this.conn = conn;
        }

		@Override
        public void run() {
            try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                String msg = input.readLine();
                String[] parts = msg.split(",");

                String username = parts[0].trim();
                String password = parts[1].trim();
                String message = parts[2].trim();
                String receivedHMAC = parts[3].trim();

                if (verifyUserExists(conn, username, password) && verifyHMAC(message, receivedHMAC)) {
                    if (!verifyRepeatedTransaction(conn, username, message)) {
                        insertTransaction(conn, username, message);
                        output.println("Transaccion exitosa. El mensaje ha sido almacenado en el servidor");
                    } else {
                        output.println("Transaccion repetida. El mensaje NO ha sido almacenado en el servidor");
                    }
                } else if (!verifyUserExists(conn, username, password)) {
                    output.println("Usuario no reconocido/autenticado. El mensaje NO ha sido almacenado en el servidor");
                } else if (!verifyHMAC(message, receivedHMAC)) {
                    output.println("Mensaje no integro. El mensaje NO ha sido almacenado en el servidor");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
}

 */