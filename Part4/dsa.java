// Nur Suhaira Bte Badrul Hisham
// 5841549
// Assignment 2 Part 4

import java.util.Scanner;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.Formatter;
import java.io.FileNotFoundException;
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException; 

public class dsa
{
	static Scanner input;
	static final int psize = 1024;
	static final int qsize = 160;
	static Formatter outfile;
	
	public static void main (String [] args)
	{
		int choice = 0;
		System.out.print("\033[H\033[2J");
		
		System.out.println ("Welcome to Implementation of Digital Signature Algorithm (DSA)...");
		
		while (choice < 3)
		{
			choice = displayMenu ();
			switch (choice)
			{
				case 1: System.out.println ("\nProduce signature");
					System.out.println ("=================\n");
					KeyGen ("input.txt");
					generateSign ("input.txt");
					break;
				case 2: System.out.println ("\nVerify signature");
					System.out.println ("================\n"); 
					verifySign ("input.txt", "sig.txt");
					break;
				case 3:	System.out.println ("\nThank you for using Digital Signature Algorithm (DSA)...");
					break;
					
			}
		}
	}
	public static int displayMenu ()
	{
		input = new Scanner (System.in);
		int choice = 0;
		
		while (choice > 3 || choice < 1)
		{
			System.out.println ("\nPlease choose the following option:-");
			System.out.println ("\t1. Signature: Produce signature on input file");
			System.out.println ("\t2. Verification: Verify input file and signature");
			System.out.println ("\t3. Quit: Exit program\n");
			System.out.print ("Your menu option: ");
			choice = input.nextInt ();
		}
		
		return choice;
	}
	// create parameters of key components
	public static void KeyGen (String filename)
	{
		BigInteger p = BigInteger.ZERO; // prime
		BigInteger q = BigInteger.ZERO; // prime (q | p-1)
		BigInteger h = BigInteger.ZERO; // less than p-1
		BigInteger g = BigInteger.ZERO; // generator
		BigInteger x = BigInteger.ZERO; // private signing key (< q)
		BigInteger y = BigInteger.ZERO; // public verification key
		boolean ok = false;
	
		while (ok == false)
		{
			q = getRandomPrime (qsize); // 160 bit (q = q | p-1)
			p = q.multiply (new BigInteger ("6")).add (BigInteger.ONE); // 512 to 1024 bit 
			if (checkPrime (p))
			{
				if (checkDivisible (p, q))
				{
					ok = true;
				}
			}
		}
		h = getRandomH (p, q); // h < p -1
		g = h.modPow ((p.subtract (BigInteger.ONE)).divide (q), p);
		x = getRandom (q);
		y = g.modPow (x, p);
		
		// create files to store key info
		try
		{
			outfile = new Formatter (filename);
		}
		catch (FileNotFoundException f)
		{
			System.err.println ("File could not be opened for creation");
			System.exit (1);
		}
		catch (SecurityException s)
		{
			System.err.println ("Write permission denied");
			System.exit (1);
		}
		
		BigInteger m = BigInteger.ZERO;
		String msg = "Hello World!";
		msg = hash (msg);
		m = new BigInteger (msg, 16);
		outfile.format ("%d%n%d%n%d%n%d%n%d%n%d%n%d", p, q, h, g, x, y, m);
		
		if (outfile != null)
		{
			outfile.close ();
			System.out.println ("DSA Parameters for \"" + filename + "\" have been created.");
		}
	}
	public static BigInteger getRandomPrime (int n)
	{
		// A Random class has only 48 bits where as SecureRandom can have up to 128 bits. 			
		// So the chances of repeating in SecureRandom are smaller.
		SecureRandom random = new SecureRandom();
		BigInteger result = BigInteger.ZERO;
		
		do
		{
			result = BigInteger.probablePrime (n+1, random);  
        		// between 0 and (2^n – 1) where n is the bit length for result
        	}
        	while (result.bitLength () < n); // regenerate if bit size is smaller than n bit
        	
        	return result;
	}
	public static BigInteger getRandomH (BigInteger p, BigInteger q)
	{
		SecureRandom random = new SecureRandom();
		BigInteger result = BigInteger.ZERO;
		boolean ok = false;
		
		while (ok == false)
		{
			result = new BigInteger (p.bitLength()-1, random);  // h < p-1 
        		// between 0 and (2^n – 1) where n is the bit length for result
        		
        		if (result.modPow (((p.subtract (BigInteger.ONE)).divide (q)),p).compareTo (BigInteger.ONE) > 0)
        		{
        			ok = true;
        		}
        	}
        	
        	return result;
		
	}
	public static BigInteger getRandom (BigInteger b)
	{
		SecureRandom random = new SecureRandom();
		BigInteger result = BigInteger.ZERO;
	
		result = new BigInteger (b.bitLength(), random);  // x < q 
        	// between 0 and (2^n – 1) where n is the bit length for result	
        	
        	return result;
		
	}
	public static String hash (String input) 
	{ 
		try 
		{ 
		    // getInstance() method is called with algorithm SHA-1 
		    MessageDigest md = MessageDigest.getInstance("SHA-1"); 
	  
		    // digest() method is called 
		    // to calculate message digest of the input string 
		    // returned as array of byte 
		    byte[] messageDigest = md.digest (input.getBytes ()); 
	  
		    // Convert byte array into signum representation 
		    BigInteger no = new BigInteger(1, messageDigest); 
	  
		    // Convert message digest into hex value 
		    String hashtext = no.toString(16); 
	  
		    // Add preceding 0s to make it 32 bit 
		    while (hashtext.length() < 32) { 
		        hashtext = "0" + hashtext; 
		    } 
	  
		    // return the HashText 
		    return hashtext; 
		} 
	  
		// For specifying wrong message digest algorithms 
		catch (NoSuchAlgorithmException e) 
		{ 
			System.out.println("Exception thrown"
                               + " for incorrect algorithm: " + e); 
                        return null; 
		} 
	 } 
	// check parameters of key components
	public static void generateSign (String filename)
	{
		BigInteger p = BigInteger.ZERO; // prime
		BigInteger q = BigInteger.ZERO; // prime (q | p-1)
		BigInteger h = BigInteger.ZERO; // less than p-1
		BigInteger g = BigInteger.ZERO; // generator
		BigInteger x = BigInteger.ZERO; // private signing key (< q)
		BigInteger y = BigInteger.ZERO; // public verification key
		BigInteger m = BigInteger.ZERO; // message
		BigInteger k = BigInteger.ZERO; // signing message
		BigInteger r = BigInteger.ZERO; // signature
		BigInteger s = BigInteger.ZERO; // signature
	
		try
		{
			input = new Scanner (new File (filename));
		}
		catch (IOException e)
		{
			System.err.println ("Error in IO");
			System.exit (1);
		}
		
		try
		{
			while (input.hasNext ())
			{
				p = input.nextBigInteger (); 
				q = input.nextBigInteger ();
				h = input.nextBigInteger ();
				g = input.nextBigInteger ();
				x = input.nextBigInteger ();
				y = input.nextBigInteger ();
				m = input.nextBigInteger ();
			}
		}
		catch (NoSuchElementException e)
		{
			System.err.println ("No such element exception caught");
			System.exit (1);
		}  
		
		if (input != null)
		{
			input.close ();
		}
		
		System.out.println ("Public/private keys setup...");
		if (checkPrime (p))
		{
			System.out.println ("\tP check OK");
		}
		else
		{
			System.out.println ("\tP check NOT OK");
		}
		if (checkPrime (q))
		{
			if (checkDivisible (p, q))
			{
				System.out.println ("\tQ check OK");
			}
			else
			{
				System.out.println ("\tQ check NOT OK");
			}
		}
		else
		{
			System.out.println ("\tQ check NOTOK");
		}
		if (checkH (p, h))
		{
			System.out.println ("\tH check OK");
		}
		else
		{
			System.out.println ("\tH check NOTOK");
		}
		if (checkG (p, q, h, g))
		{
			System.out.println ("\tG check OK");
		}
		else
		{
			System.out.println ("\tG check NOTOK");
		}
		if (checkX (q, x))
		{
			System.out.println ("\tX check OK");
		}
		else
		{
			System.out.println ("\tX check NOTOK");
		}
		if (checkY (y, g, x, p))
		{
			System.out.println ("\tY check OK");
		}
		else
		{
			System.out.println ("\tY check NOTOK");
		}
		
		k = new BigInteger ("7"); // generateK (q);
		r = generateR (g, k, p, q); // first part of signature
		k = getInverse (q, k); // update to inverse k
		s = k.multiply (m.add (x.multiply (r))).mod (q); // second part of signature
		outfileSign ("sig.txt", r, s); // write signature in output file
	}
	public static boolean checkPrime (BigInteger n) 
	{ 
		return n.isProbablePrime(1); 
	}
	public static boolean checkDivisible (BigInteger p, BigInteger q)
	{	
		if ((p.subtract (BigInteger.ONE)).mod (q).equals (BigInteger.ZERO))
		{
			return true;
		}
		
		return false;
	} 
	public static boolean checkH (BigInteger p, BigInteger h)
	{
		// check that h < p-1
		if (h.compareTo (p.subtract (BigInteger.ONE)) < 0)
		{
			return true;
		}
		return false;
	}
	public static boolean checkG (BigInteger p, BigInteger q, BigInteger h, BigInteger g)
	{
		// g = h^((p-1)/q) mod p
		BigInteger result = h.modPow ((p.subtract (BigInteger.ONE)).divide (q) ,p);
		if (g.equals (result))
		{
			return true;
		}
		
		return false;
	}
	public static boolean checkX (BigInteger q, BigInteger x)
	{
		// check that x < q
		if (x.compareTo (q) < 0)
		{
			return true;
		}
		return false;
	}
	public static boolean checkY (BigInteger y, BigInteger g, BigInteger x, BigInteger p)
	{
		// y = g^x mod p
		BigInteger result = g.modPow (x, p);
		if (y.equals (result))
		{
			return true;
		}
		return false;
	}
	public static BigInteger generateK (BigInteger q)
	{
		SecureRandom random = new SecureRandom ();
		BigInteger k = BigInteger.ZERO;
		
		// k < q
		do
		{
			k = new BigInteger (q.bitLength (), random);
		} while (k.compareTo (q) >= 0 || k.compareTo (BigInteger.ONE) <= 0); // check k < q
		
		return k;
	}
	public static BigInteger generateR (BigInteger g, BigInteger k, BigInteger p, BigInteger q)
	{
		BigInteger r = BigInteger.ZERO;
		
		r = (g.modPow (k, p)).mod (q);
		
		return r;
	}
	public static BigInteger getInverse (BigInteger n1, BigInteger n2)
	{
		BigInteger tempN1 = n1;
		BigInteger a1 = BigInteger.ONE;
		BigInteger b1 = BigInteger.ZERO;
		BigInteger a2 = BigInteger.ZERO;
		BigInteger b2 = BigInteger.ONE;
		
		BigInteger q = n1.divide (n2);
		BigInteger r = n1.mod (n2);
		BigInteger tempQ = BigInteger.ZERO; // temp for q
		BigInteger temp = BigInteger.ZERO; // temp for a1 and b1
		
		while (r.compareTo (BigInteger.ZERO) > 0)
		{
			n1 = n2;
			n2 = r;
			tempQ = q; // store q temp
			q = n1.divide (n2);
			r = n1.mod (n2);
			temp = a1;
			a1 = a2;
			a2 = temp.subtract (tempQ.multiply (a2));
			temp = b1;
			b1 = b2;
			b2 = temp.subtract (tempQ.multiply (b2));
		}
		
		if (b2.compareTo (BigInteger.ZERO) < 0)
		{
			return b2.add (tempN1);
		}
		
		return b2;
	}
	public static void outfileSign (String filename, BigInteger r, BigInteger s)
	{
		// create file to store signature
		try
		{
			outfile = new Formatter (filename);
		}
		catch (FileNotFoundException f)
		{
			System.err.println ("File could not be opened for creation");
			System.exit (1);
		}
		catch (SecurityException e)
		{
			System.err.println ("Write permission denied");
			System.exit (1);
		}
		
		
		outfile.format ("%d%n%d", r, s);
		
		if (outfile != null)
		{
			outfile.close ();
			System.out.println ("\tSignature for \"" + filename + "\" has been created.");
		}
	}
	public static void verifySign (String filename, String filename2)
	{
		BigInteger p = BigInteger.ZERO; // prime
		BigInteger q = BigInteger.ZERO; // prime (q | p-1)
		BigInteger h = BigInteger.ZERO; // less than p-1
		BigInteger g = BigInteger.ZERO; // generator
		BigInteger x = BigInteger.ZERO; // private signing key (< q)
		BigInteger y = BigInteger.ZERO; // public verification key
		BigInteger m = BigInteger.ZERO; // message
		BigInteger k = BigInteger.ZERO; // signing message
		BigInteger r = BigInteger.ZERO; // signature
		BigInteger s = BigInteger.ZERO; // signature
	
		try
		{
			input = new Scanner (new File (filename)); // input.txt
		}
		catch (IOException e)
		{
			System.err.println ("Error in IO");
			System.exit (1);
		}
		
		try
		{
			while (input.hasNext ())
			{
				p = input.nextBigInteger (); 
				q = input.nextBigInteger ();
				h = input.nextBigInteger ();
				g = input.nextBigInteger ();
				x = input.nextBigInteger ();
				y = input.nextBigInteger ();
				m = input.nextBigInteger ();
			}
		}
		catch (NoSuchElementException e)
		{
			System.err.println ("No such element exception caught");
			System.exit (1);
		}  
		
		if (input != null)
		{
			input.close ();
		}
		
		try
		{
			input = new Scanner (new File (filename2)); // sig.txt
		}
		catch (IOException e)
		{
			System.err.println ("Error in IO");
			System.exit (1);
		}
		
		try
		{
			while (input.hasNext ())
			{
				r = input.nextBigInteger (); 
				s = input.nextBigInteger ();
			}
		}
		catch (NoSuchElementException e)
		{
			System.err.println ("No such element exception caught");
			System.exit (1);
		}  
		
		if (input != null)
		{
			input.close ();
		}
		
		System.out.println ("Verifying...");
		System.out.println ("\tComputing w...");
		BigInteger w = getInverse (q, s);
		System.out.println ("\tComputing u1...");
		BigInteger u1 = (m.multiply (w)).mod (q);
		System.out.println ("\tComputing u2...");
		BigInteger u2 = (r.multiply (w)).mod (q);
		System.out.println ("\tComputing v...");
		BigInteger temp = g.modPow (u1, p);
		temp = temp.multiply (y.modPow (u2, p));
		BigInteger v = (temp.mod (p)).mod (q);
		
		if (v.equals (r))
		{
			System.out.println ("Signature is verified.");
		}
		else
		{
			System.out.println ("Signature is not verified.");
		}
	}
}
