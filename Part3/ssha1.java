// Java program to calculate SHA-1 hash value 
// Source: https://www.geeksforgeeks.org/sha-1-hash-in-java/

import java.math.BigInteger; 
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException; 

public class ssha1
{ 
	public static String encrypt (String input) 
	{ 
		try 
		{ 
			// getInstance() method is called with algorithm SHA-1 
			MessageDigest md = MessageDigest.getInstance("SHA-1"); 

			// digest() method is called 
			// to calculate message digest of the input string 
			// returned as array of byte 
			byte[] messageDigest = md.digest(input.getBytes()); 
			
			// Convert byte array into signum representation 
			BigInteger no = new BigInteger(1, messageDigest); 

			// Convert message digest into hex value 
			String hashtext = no.toString(16); // each digit is 4 bits so 40 digits is 160 bits
			
			// Add preceding 0s to make it 32 bit (padding)
			while (hashtext.length() < 32) { 
				hashtext = "0" + hashtext; 
			}
			
			// each hexa value is 8 bits (character)
			// so 36 bits / 8 bits = 4.5 = 5 (characters)
			hashtext = hashtext.substring (0, 5);

			// return the HashText 
			return hashtext; 
		} 

		// For specifying wrong message digest algorithms 
		catch (NoSuchAlgorithmException e) 
		{ 
			throw new RuntimeException(e); 
		} 
	} 
	
} 

