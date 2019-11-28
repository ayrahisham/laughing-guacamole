// Nur Suhaira Bte Badrul Hisham
// 5841549
// Assignment 2 Part 1

import java.util.Scanner;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Formatter;
import java.util.Scanner;
import java.io.File;
import java.util.NoSuchElementException;

public class rsa
{
	static int pbit = 2; // default smallest bit length
	static int qbit = 2; // default smallest bit length
	static Formatter outfile;
	static Scanner input;
	
	public static void main (String [] args)
	{
		int choice = 0;
		
		System.out.print("\033[H\033[2J");
		
		System.out.println ("Welcome to Implementation of RSA Signature Algorithm...");
		
		while (choice < 5)
		{
			choice = displayMenu ();
			switch (choice)
			{
				case 1: System.out.println ("\nKey generation");
					System.out.println ("==============\n");
					input = new Scanner (System.in);
					boolean ok = false;
					
					while (ok == false)
					{
						System.out.print ("Enter the bit-length of p (2 - 32): ");
						pbit = input.nextInt ();
						if ((pbit >= 2) && (pbit <= 32))
						{
							ok = true;
						}
					}
					
					ok = false;
					while (ok == false)
					{
						System.out.print ("Enter the bit-length of q (2 - 32): ");
						qbit = input.nextInt ();
						if ((qbit >= 2) && (qbit <= 32))
						{
							ok = true;
						}
					}
					KeyGen (pbit, qbit);
					break;
				case 2: System.out.println ("\nSigning");
					System.out.println ("=======\n"); 
					try
					{
						input = new Scanner (new File ("mssg.txt"));
					}
					catch (FileNotFoundException f)
					{
						System.err.println ("Please enter option 4 to generate message...");
						break;
					}
					Sign ("sk.txt", "mssg.txt", "sig.txt");
					break;
				case 3: System.out.println ("\nVerify");
					System.out.println ("======\n");
					try
					{
						input = new Scanner (new File ("mssg.txt"));
					}
					catch (FileNotFoundException f)
					{
						System.err.println ("Please enter option 4 to generate message...");
						break;
					}
					if (Verify ("pk.txt", "sig.txt", "mssg.txt"))
					{
						System.out.println ("Message is accepted.");
					}
					else
					{
						System.out.println ("Message is rejected.");
					}
					break;
				case 4: System.out.println ("\nGenerate Message");
					System.out.println ("================\n");
					try
					{
						input = new Scanner (new File ("sk.txt"));
					}
					catch (FileNotFoundException f)
					{
						System.err.println ("Please enter option 1 to generate keys...");
						break;
					}
					generateMsg ("sk.txt", "mssg.txt");
					break;
				case 5: System.out.println ("\nThank you for using RSA Signature...");
					break;
			}
		}
	}
	public static int displayMenu ()
	{
		input = new Scanner (System.in);
		int choice = 0;
		
		while (choice > 5 || choice < 1)
		{
			System.out.println ("\nPlease choose the following option:-");
			System.out.println ("\t1. KeyGen: The RSA key generation function");
			System.out.println ("\t2. Sign: The RSA signing function");
			System.out.println ("\t3. Verify: The RSA verification function");
			System.out.println ("\t4. Generate: The RSA message generation function");
			System.out.println ("\t5. Quit: Exit program\n");
			System.out.print ("Your menu option: ");
			choice = input.nextInt ();
		}
		
		return choice;
	}
	
	// create parameters of key components
	public static void KeyGen (int psize, int qsize)
	{
		BigInteger p;
		BigInteger q;
		BigInteger N; // p * q
		BigInteger e; // exponent
		BigInteger d; // inverse of e
		boolean ok;
	
		p = getRandomPrime (psize);
		ok = false;
		BigInteger x = BigInteger.ZERO;
		while (ok == false)
		{
			x = getRandomPrime (qsize);
			if (x.compareTo (p) != 0) // make sure p and q are distinct
			{
				ok = true;
			}
		}
		q = x;
		N = p.multiply (q);
		BigInteger min = new BigInteger ("2");
		BigInteger phi = (p.subtract (BigInteger.ONE)).multiply (q.subtract (BigInteger.ONE));
		e = getRandomExp (min, phi.subtract (BigInteger.ONE)); // range from 2 to phi-1
		d = getInverseE (phi, e);		
		
		createPKeys ("pk.txt", N, e);
		createSKeys ("sk.txt", N, p, q, d);
	}
	
	public static BigInteger getRandomPrime (int n)
	{
		// A Random class has only 48 bits where as SecureRandom can have up to 128 bits. 			
		// So the chances of repeating in SecureRandom are smaller.
		SecureRandom random = new SecureRandom();
		
		BigInteger result = BigInteger.probablePrime (n+1, random);  
        	// between 0 and (2^n – 1) where n is the bit length for result
        	// and then ignore values whenever it is bigger than result
        	
        	return result;
	}
	
	public static BigInteger getRandomExp (BigInteger min, BigInteger max)
	{	
		if (min.compareTo (max) >= 0) 
		{
			throw new IllegalArgumentException ("max must be greater than min");
		}

		SecureRandom random = new SecureRandom();
		
		BigInteger exp = BigInteger.probablePrime (max.bitLength(), random);
		
		while (exp.compareTo (max) > 0 || exp.compareTo (min) < 0)
		{
			exp = BigInteger.probablePrime (max.bitLength(), random);	
		}
			
		return exp; 
	
	}
	
	public static BigInteger getInverseE (BigInteger n1, BigInteger n2)
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

	public static void createPKeys (String filename, BigInteger N, BigInteger e)
	{
		// create files to store public key info
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
		
		
		outfile.format ("%d%n%d", N, e);
		
		if (outfile != null)
		{
			outfile.close ();
			System.out.println ("Parameters for \"" + filename + "\" have been created.");
		}
	}
	
	public static void createSKeys (String filename, BigInteger N, BigInteger p, BigInteger q, BigInteger d)
	{
		// create files to store secret key info
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
		
		
		outfile.format ("%d%n%d%n%d%n%d", N, p, q, d);
		
		if (outfile != null)
		{
			outfile.close ();
			System.out.println ("Parameters for \"" + filename + "\" have been created.");
		}
	}
	
	public static void generateMsg (String filename, String outputfile)
	{
		BigInteger N = BigInteger.ZERO;
		BigInteger p = BigInteger.ZERO;
		BigInteger q = BigInteger.ZERO;
		BigInteger d = BigInteger.ZERO;
		BigInteger msg = BigInteger.ZERO;
		
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
				N = input.nextBigInteger (); // secret key
				p = input.nextBigInteger ();
				q = input.nextBigInteger ();
				d = input.nextBigInteger (); // secret key
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
		
		SecureRandom random = new SecureRandom();
		
		while (msg.compareTo (BigInteger.ONE) < 0 || msg.compareTo (N) >= 0) // N must be positive (1 to N-1)
		{
			msg = new BigInteger (N.bitLength(), random); // generate at random for doc   	
		}
		
		try
		{
			outfile = new Formatter (outputfile);
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
		
		outfile.format ("%d", msg);
		
		if (outfile != null)
		{
			outfile.close ();
			System.out.println ("Message has been created " + outputfile + ".");
		}
	}
	public static void Sign (String filename, String filename2, String outputfile)
	{
		BigInteger N = BigInteger.ZERO;
		BigInteger p = BigInteger.ZERO;
		BigInteger q = BigInteger.ZERO;
		BigInteger d = BigInteger.ZERO;
		BigInteger M = BigInteger.ZERO;
		BigInteger S = BigInteger.ZERO;
		
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
			// retrieve N and d
			while (input.hasNext ())
			{
				N = input.nextBigInteger (); // secret key
				p = input.nextBigInteger ();
				q = input.nextBigInteger ();
				d = input.nextBigInteger (); // secret key
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
			input = new Scanner (new File (filename2));
		}
		catch (IOException e)
		{
			System.err.println ("Error in IO");
			System.exit (1);
		}
		
		try
		{
			// retrieve M
			while (input.hasNext ())
			{
				M = input.nextBigInteger ();
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
		
		S = M.modPow (d, N);
		
		try
		{
			outfile = new Formatter (outputfile);
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
		
		outfile.format ("%d", S);
		
		if (outfile != null)
		{
			outfile.close ();
			System.out.println ("Signature has been created in " + outputfile + ".");
		}
	}
	public static boolean Verify (String publicfile, String sigfile, String msgfile)
	{
		BigInteger N = BigInteger.ZERO;
		BigInteger E = BigInteger.ZERO;
		BigInteger M = BigInteger.ZERO;
		BigInteger S = BigInteger.ZERO;
		
		try
		{
			input = new Scanner (new File (publicfile));
		}
		catch (IOException e)
		{
			System.err.println ("Error in IO");
			System.exit (1);
		}
		
		try
		{
			// retrieve N and e
			while (input.hasNext ())
			{
				N = input.nextBigInteger (); // public key
				E = input.nextBigInteger (); // public key
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
			input = new Scanner (new File (sigfile));
		}
		catch (IOException e)
		{
			System.err.println ("Error in IO");
			System.exit (1);
		}
		
		try
		{
			// retrieve N and d
			while (input.hasNext ())
			{
				S = input.nextBigInteger (); // signature
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
			input = new Scanner (new File (msgfile));
		}
		catch (IOException e)
		{
			System.err.println ("Error in IO");
			System.exit (1);
		}
		
		try
		{
			// retrieve N and d
			while (input.hasNext ())
			{
				M = input.nextBigInteger (); // message
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
		
		// comparing the true message M with the verification components (S, e, N)
		if (M.equals (S.modPow (E, N)))
		{
			return true;
		}
		
		return false;
	}
}


