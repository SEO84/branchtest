package game;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;

public class Test2 {
	
	private static String userID = "keka";
	private static String passwd = "8e0d594dde4bbcf43518a19a7ae60748521bfa88e0538a0c111eec587e8b648b";

    public static String hashPassword(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(s.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		Object user = null;
		while (null == user) {
			System.out.println("<Login>");
			System.out.print("id: ");
			final String userIdInput = scanner.nextLine();
			
			System.out.print("password: ");
			final String passwordInput = scanner.nextLine();
			
			if (userIdInput.equals(userID) && hashPassword(userID+'+'+passwordInput).equals(passwd)) {
				System.out.println("Welcome back!");
				user = new Object();
			} else {
				System.out.println("Invalid user.");
			}
		}
		

		
		scanner.close();
	}

}
