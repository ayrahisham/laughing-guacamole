// Nur Suhaira Bte Badrul Hisham
// 5841549
// Assignment 2 Part 2

import java.util.Scanner;
import java.math.BigInteger;
import java.util.ArrayList;

public class task2
{
	static Scanner input = new Scanner (System.in);
	static int size = 0;
	static boolean ok = false;
	static int [] privatekey;
	static int sum = 0;
	static int modulus = 0;
	static int multiplier = 0;
	static int inverseMult = 0;
	static int [] publickey;
	static String msg = "";
	
	public static void main (String [] args)
	{
		System.out.print("\033[H\033[2J");
		
		System.out.println ("Welcome to Trapdoor Knapsack Encryption Scheme...\n");
		
		while (ok == false)
		{
			System.out.print ("Enter the size of the super-increasing knapsack: ");
			size = input.nextInt ();
			if (size < 7)
			{
				System.out.println ("\tSize entered is invalid. Please re-enter...\n");
			}
			else
			{
				ok = true;
			}
		}
		System.out.println ("\nThe size of your knapsack is " + size);
		System. out.println ("\tPlease enter " + size + " value (s):");
		privatekey = new int [size];
		// the user is asked to enter the value of each ai in the private key
		for (int i = 1; i <= size; i++)
		{
			System.out.print ("\tValue " + i + ": ");
			privatekey [i-1] = input.nextInt ();
			sum += privatekey [i-1];
		
		}
		ok = false; // reset flag
		while (ok == false)
		{
			System.out.print ("\nPlease enter a modulus > " + sum + ": ");
			modulus = input.nextInt ();
			if (modulus > sum)
			{
				ok = true;
			}
			else
			{
				System.out.println ("\tModulus entered is < sum. Please re-enter...\n");
			}
		}
		ok = false; // reset flag
		while (ok == false)
		{
			System.out.print ("\nPlease enter a prime multiplier: ");
			multiplier = input.nextInt ();
			// check whether condition of the multipllier is satisfied
			if (multiplier > 1)
			{
				if (checkPrime (BigInteger.valueOf (multiplier)))
				{
					if (modulus > multiplier)
					{
						if (gcd (BigInteger.valueOf (modulus), BigInteger.valueOf (multiplier)).equals (BigInteger.ONE))
						{
							// if gcd = 1, inverse multiplier exists
							inverseMult = getInverse (BigInteger.valueOf (modulus), BigInteger.valueOf (multiplier)).intValue ();
							ok = true;
						}
						else
						{
							System.out.println ("\tMultiplier entered does not have a gcd of 1. Please re-enter...");
						}
					}
					else if (multiplier > modulus)
					{
						if (gcd (BigInteger.valueOf (multiplier), BigInteger.valueOf (modulus)).equals (BigInteger.ONE))
						{
							// if gcd = 1, inverse multiplier exists
							inverseMult = getInverse (BigInteger.valueOf (multiplier), BigInteger.valueOf (modulus)).intValue ();
							ok = true;
						}
						else
						{
							System.out.println ("\tMultiplier entered does not have a gcd of 1. Please re-enter...");
						}
					}
				}
				else
				{
					System.out.println ("\tMultiplier entered is not prime. Please re-enter...");
				}
			}
			else
			{
				System.out.println ("\tMultiplier entered is < 2. Please re-enter...\n");
			}
		}
		
		System.out.println ("\nComputing public key...");
		publickey = new int [size];
		for (int i = 0; i < size; i++)
		{
			publickey [i] = multiplier * privatekey [i] % modulus;
			System.out.println ("\t" + multiplier + " x " + privatekey [i] + " mod " + modulus + " = " + publickey [i]);
		}
		System.out.print ("The public key = (");
		for (int i = 0; i < size-1; i++)
		{
			System.out.print (publickey [i] + ", ");
		}
		System.out.print (publickey [size-1] + ") is obtained.\n"); // the public key will be generated and shown
		
		knapsack ks = new knapsack ();
		
		int choice = 0;
		while (choice < 3)
		{
			choice = displayMenu ();
			switch (choice)
			{
				case 1: System.out.println ("\nEncryption");
					System.out.println ("=================\n");
					System.out.print ("Please enter your set of message: ");
					input.nextLine ();
					msg = input.nextLine ();
					ks.encrypt (msg, publickey, size);
					break;
				case 2: System.out.println ("\nDecryption");
					System.out.println ("================"); 
					ArrayList <Integer> ciphertext = new ArrayList <Integer>();
					int cipher = 0;
					do
					{
						System.out.print ("Please enter your ciphertext (enter -1 to stop): ");
						cipher = input.nextInt ();
						if (cipher != -1)
						{
							ciphertext.add (cipher);
						}
					} while (cipher != -1);
		
					ks.decrypt (ciphertext, modulus, inverseMult, privatekey, size);
					break;
				case 3:	System.out.println ("\nThank you for using Trapdoor Knapsack Encryption Scheme...");
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
			System.out.println ("\t1. Encrypt a set of message");
			System.out.println ("\t2. Decrypt a set of ciphertext");
			System.out.println ("\t3. Quit: Exit program\n");
			System.out.print ("Your menu option: ");
			choice = input.nextInt ();
		}
		
		return choice;
	}
	public static boolean checkPrime (BigInteger n) 
	{ 
		return n.isProbablePrime(1); 
	}
	public static BigInteger gcd (BigInteger n1, BigInteger n2)
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
		
		return n2;
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

}
