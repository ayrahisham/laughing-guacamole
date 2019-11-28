// Nur Suhaira Bte Badrul Hisham
// 5841549
// Assignment 2 Part 2

import java.util.ArrayList;
import java.util.Collections;

public class knapsack
{
	public static void encrypt (String msg, int [] key, int size)
	{
		int [] decimalform = new int [msg.length()];
		String [] binaryform = new String [msg.length()];
		int [] ciphertext = new int [msg.length()];
	
		convertDecimal (decimalform, msg);
		convertBinary (binaryform, decimalform, msg, size);
		generateCipher (ciphertext, key, binaryform, msg);
		printCipher (ciphertext, binaryform, decimalform, msg);
	}
	public static void convertDecimal (int [] decimalform, String encryptedmsg)
	{
		for (int i = 0; i < encryptedmsg.length (); i++)
		{
			decimalform [i] = (int) (encryptedmsg.charAt (i));
		
		}
	}
	public static void convertBinary (String [] binaryform, int [] decimalform, String encryptedmsg, int size)
	{
		for (int i = 0; i < encryptedmsg.length (); i++)
		{
			binaryform [i] = Integer.toBinaryString (decimalform [i]);
			// padding
			// add preceding 0s to make it size bit 
		    	while (binaryform [i].length() < size) 
		    	{ 
		        	binaryform [i] = "0" + binaryform [i]; 
		    	}
		}
	}
	public static void generateCipher (int [] ciphertext, int [] publickey, String [] binaryform, String encryptedmsg)
	{
		int temp = 0;
		for (int i = 0; i < encryptedmsg.length (); i++)
		{
			for (int j = 0; j < binaryform [i].length (); j++)
			{
				if (binaryform [i].charAt(j) == '1')
				{
					temp += publickey [j];
				}
			}
			ciphertext [i] = temp;
			temp = 0; // reset to 0
		}
	}
	public static void printCipher (int [] ciphertext, String [] binaryform, int [] decimalform, String encryptedmsg)
	{
		for (int i = 0; i < encryptedmsg.length (); i++)
		{
			System.out.print (encryptedmsg.charAt (i) + "\t");
			System.out.print (decimalform [i] + "\t");
			System.out.print (binaryform [i] + "\t");
			System.out.print (ciphertext [i] + "\n");
		}
		System.out.print ("The ciphertext = (");
		for (int i = 0; i < encryptedmsg.length ()-1; i++)
		{
			System.out.print (ciphertext [i] + ", ");
		}
		System.out.print (ciphertext [encryptedmsg.length ()-1] + ") is generated.\n"); 
		// the ciphertext will be generated and shown	
	}
	public static void decrypt (ArrayList msg, int p, int inverse, int [] key, int size)
	{
		int [] plaintext = new int [msg.size ()];
		String [] binaryform = new String [msg.size ()];
		int [] decimalform = new int [msg.size ()];
		char [] plainmsg = new char [msg.size ()];
		
		System.out.println ("\nRecovering plaintext using private key and inverse multiplier...");
		recoverPlaintext (plaintext, inverse, msg, p);
		
        	// convert private key int [] to arraylist
        	ArrayList <Integer> pklist = new ArrayList <> (size);
		for (int i : key) 
		{
			pklist.add (Integer.valueOf(i));
		}
        	Collections.sort (pklist, Collections.reverseOrder ()); // sort in descending order
        	
        	displayKeys (key);
        	
        	int temp = 0;
		for (int i = 0; i < msg.size (); i++)
        	{
			temp = plaintext [i];
			binaryform [i] = "";
			for (int j = 0; j < pklist.size (); j++)
			{
				if (temp >= pklist.get (j))
				{
					binaryform [i] += "1";
					temp -= pklist.get (j);
				}
				else
				{
					binaryform [i] += "0";
				}
			}
			temp = 0;
        	}
        	
        	binaryform = reverseString (binaryform, msg);
        	
		for (int i = 0; i < msg.size (); i++)
        	{
			System.out.print (plaintext [i]);
			for (int j = 0; j < pklist.size (); j++)
			{
				System.out.print ("\t" + binaryform [i].charAt (j));
			}
			System.out.println ();
        	}
        	
        	displayPlaintext (msg, decimalform, binaryform, plainmsg);
        	
	}
	public static void recoverPlaintext (int [] plaintext, int inverse, ArrayList msg, int p)
	{
		for (int i = 0; i < msg.size (); i++)
        	{
			plaintext [i] = inverse * (int) msg.get (i) % p;
        	}
	}
	public static void displayKeys (int [] pk)
	{
		for (int i = 0; i < pk.length; i++)
		{
			System.out.print ("\t" + pk [i]);
		}
		System.out.println ();
	}
	public static String [] reverseString (String [] binaryform, ArrayList msg)
	{
		StringBuilder input; 
  
		for (int i = 0; i < msg.size (); i++)
		{
			// empty builder
			input = new StringBuilder();
			
			 // append a string into StringBuilder input 
			input.append (binaryform [i]); 
		  
			// reverse StringBuilder input 
			input = input.reverse();
			
			// add the new reverse string in string array
			binaryform [i] = input.toString ();
		}
		
		return binaryform;
	}
	public static void displayPlaintext (ArrayList msg, int [] decimalform, String [] binaryform, char [] plainmsg)
	{
		System.out.println ("\nConverting binary into decimal...");
        	System.out.println ("Converting decimal into ascii value...");
        	
        	for (int i = 0; i < msg.size (); i++)
        	{
        		System.out.print (binaryform [i] + " ");
        		decimalform [i] = Integer.parseInt (binaryform [i], 2);
        		System.out.print (decimalform [i] + " ");
        		plainmsg [i] = (char) (decimalform [i]);
        		System.out.print ("\n");
        	}
        	System.out.print ("The plaintext = ("); 
        	for (int i = 0; i < msg.size (); i++)
        	{
        		System.out.print (plainmsg [i]);
        	}
		System.out.print (") is generated.\n");
	}
}
