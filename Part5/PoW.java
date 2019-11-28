// Nur Suhaira Bte Badrul Hisham
// 5841549
// Assignment 2 Part 5

import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

import java.util.ArrayList;

import java.util.Formatter;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.math.BigInteger; 
import java.security.MessageDigest;

public class PoW
{
	static int AES_128 = 128;
	static Formatter outfile;
	static Scanner input = new Scanner (System.in);
	static ArrayList<Block> blockchain = new ArrayList<Block>(); 
	static String data = "";
	static String key = "";
	static String hashValue = "";
	static String [] sentence; // validated ledger
	
	public static void main (String[] args) throws Exception 
	{	
		int size = 0;
		int choice = 0;
		int leadingZero = Integer.parseInt (args [0]);
		String filename = args [1];
		String zero = computeStringZero (leadingZero);
		
		System.out.print("\033[H\033[2J");
		
		System.out.println ("Welcome to Simplified Blockchain: Simulating Proof of Work...");
		
		KeyGenerator keyGenerator = KeyGenerator.getInstance(CryptoMngr.ALGORITHM);
		keyGenerator.init(AES_128);
		
		//Initialization vector
		System.out.println ("\nGenerating IV...");
		SecretKey IV = keyGenerator.generateKey();
		
		while (choice < 3)
		{
			choice = displayMenu (leadingZero, filename);
			switch (choice)
			{
				case 1: System.out.println ("\nGeneration of new ledger");
					System.out.println ("========================");
		
					System.out.println ("\nExtracting data from file...");
					data = extractMsgFromFile ("ledger.txt");
		
					System.out.println ("\nFiltering delimiter from data...");
					data = filterDelimiter (data);
		
					System.out.println ("\nDividing data into blocks of 16 bytes (128 bits)...");
					size = divideIntoBlocks (data);
					sentence = new String [size];
				
					System.out.println ("\nGenerating key...");
					key = generateKey (data, sentence);
					
					System.out.println ("\nInput random nonce into data...");
					int nonce = 0;
					boolean ok = false;
					String temp = "";
					
					System.out.println ("Checking validity of the new block with " + leadingZero + "'0'...");
					while (ok == false)
					{
						data = inputRandomNonce (data, sentence, nonce);
						hashValue = generateFile (filename, sentence, "0", key, IV); // first initial previous hash is "0"
			
						if (isChainValid (hashValue, leadingZero, zero))
						{
							ok = true;
						}
						else
						{
							++nonce;
						}
					}
		
					System.out.println ("\nBlock chain (" + hashValue + ") with a nonce value of " + nonce + " to meet limit parameter...");
					break;
				case 2: System.out.println ("\nValidation of newly record");
					System.out.println ("==========================");
					if (! hashValue.equals (""))
					{
						System.out.println ("\nCurrent ledger: " + hashValue);
						System.out.print ("\nEnter random nonce to be used: ");
						int newnonce = 0;
						newnonce = input.nextInt ();
						String newdata = "";
						input.nextLine ();
						System.out.print ("Enter new data record to be inserted: ");
						newdata = input.nextLine ();
						
						System.out.println ("\nFiltering delimiter from data...");
						newdata = filterDelimiter (newdata);
						
						System.out.println ("\nDividing data into blocks of 16 bytes (128 bits)...");
						int newsize = divideIntoBlocks (newdata);
						sentence = new String [newsize];
				
						System.out.println ("\nRe-using same key...");
						String newkey = generateKey (newdata, sentence); // but not gonna use this key
						
						System.out.println ("\nInput random nonce '" + newnonce + "' into data...");
						newdata = inputRandomNonce (newdata, sentence, newnonce);
						
						System.out.println ("Checking validity of the new block with " + leadingZero + "'0'...");
						String newhashValue = generateFile ("temp.txt", sentence, hashValue, key, IV); // previous hash is hashValue of current ledger
						if (isChainValid (newhashValue, leadingZero, zero))
						{
							System.out.println ("\nBlock chain (" + newhashValue + ") is verified successfully with a nonce value of " + newnonce + "...");
							
							// update current hashValue to newhashValue
							hashValue = newhashValue;
						
							// if new block is valid then copy temp.txt to new_ledger.txt	
							data = copyContentsFromFile (filename);
							newdata = copyContentsFromFile ("temp.txt");
							data += newdata;
							saveToFile (filename);	
							System.out.println ("\nCurrent ledger has been updated in " + filename + "...");	
						}
						else
						{
							System.out.println ("\nBlock chain (" + newhashValue + ") is not valid with a nonce value of " + newnonce + " to meet limit parameter...");
						}
					}
					else
					{
						System.out.println ("\nPlease enter option 1 to retrieve current ledger...");
					}
					break;
				case 3:	System.out.println ("\nThank you for using Simulation of Proof of Work...");
					break;			
			}
		}
	}
	public static int displayMenu (int leadingZero, String filename)
	{
		System.out.println ("\nLimit Parameter: " + leadingZero + " leading zeros");
		System.out.println ("Data Parameter: " + filename);
		input = new Scanner (System.in);
		int choice = 0;
		
		while (choice > 3 || choice < 1)
		{
			System.out.println ("\nPlease choose the following option:-");
			System.out.println ("\t1. Generating a new hashed ledger with random nonce");
			System.out.println ("\t2. Validate newly record with random nonce");
			System.out.println ("\t3. Quit: Exit program\n");
			System.out.print ("Your menu option: ");
			choice = input.nextInt ();
		}
		
		return choice;
	}
	public static String extractMsgFromFile (String filename) 
	{
		try
		{
			input = new Scanner (new File (filename));
		}
		catch (IOException e)
		{
			System.err.println ("Error in IO");
			System.exit (1);
		}
		String temp = "";
		try
		{
			while (input.hasNext ())
			{
				temp += input.nextLine ();
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
		
		return temp;	
	}
	public static String filterDelimiter (String data)
	{
		String temp = "";
		for (int i = 0; i < data.length (); i++)
		{
			if (data.charAt (i) != '\t')
			{
				temp += data.charAt (i);
			}
		}
		
		return temp;
	}
	public static int divideIntoBlocks (String data)
	{	
		int i = 0;
		
		// divide the data into blocks of 16 characters (16 * 8 = 128 bits)	
		// count no. of blocks
		i = data.length () / 16;
		if (data.length () % 16 > 0) // if there is still remainder, that means it would be < 16 bc mod 16
		{
			++i;
		}
		
		System.out.println ("No of blocks in blockchain: " + i);
		return i;
	}
	public static String generateKey (String data, String [] sentence)
	{
		String temp = "";
		
		for (int i = 0; i < sentence.length; i++)
		{
			if (data.length () >= 16)
			{
				temp = data.substring (0, 16);
				data = data.substring (16); // extract the first 16 bits after storing in array
			}
			else // data length < 16 do padding
			{
				temp = data;
				
				while (temp.length () < 16)
				{
					temp = "0" + temp;
				}
			}
			
			sentence [i] = temp;
		}
		
		return sentence [0]; // key is first 128 bits of message
	}
	public static String inputRandomNonce (String data, String [] sentence, int nonce)
	{
		int count = 0;
		String originaldata = data; // retain original data without nonce
		data = nonce + data; // appending nonce at the front
	
		int i = 0;
		while (data.length () > 0)
		{
			// divide the data into blocks of 16 characters (16 * 8 = 128 bits)	
			if (data.length () >= 16)
			{
				data = data.substring (16);
			}
			else
			{
				data = "";
			}
			i++; // count no. of blocks
		}
		sentence = new String [i];
		String tempstr = "";
		data = nonce + originaldata;
		
		for (int j = 0; j < sentence.length; j++)
		{
			if (data.length () >= 16)
			{
				tempstr = data.substring (0, 16);
				data = data.substring (16); // extract the first 16 bits after storing in array
			}
			else
			{
				tempstr = data;
				
			}
			
			sentence [j] = tempstr;
			
		}
		
		return originaldata;
		
	}
	public static String generateFile (String filename, String [] sentence, String previousHash, String key, SecretKey IV) throws Exception
	{
		// create file to store data and hash
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
		
		// adding block into blockchain and updating file with hash values
		for (int i = 0; i < sentence.length; i++)
		{
			if (i == 0)
			{
				blockchain.add (new Block (sentence [i], previousHash, IV, key));
			}
			else
			{
				blockchain.add (new Block (sentence [i], blockchain.get (blockchain.size()-1).hash, IV, key)); 
			}
			
			if (i < sentence.length-1) // outfile for blocks except last block
			{
				outfile.format ("%s%s%s%n", blockchain.get (blockchain.size ()-1).data, "\t", blockchain.get (blockchain.size ()-1).hash);	
			}
		}
		
		MessageDigest md = MessageDigest.getInstance ("MD5"); 
  
            	byte [] messageDigest = md.digest (blockchain.get (blockchain.size ()-1).hash.getBytes ()); 
  
            	BigInteger no = new BigInteger (1, messageDigest); 
  
		String hashtext = no.toString (16); 
	  
		while (hashtext.length () < 32) 
		{ 
			hashtext = "0" + hashtext; 
		} 
		
		outfile.format ("%s%s%s%n", blockchain.get (blockchain.size ()-1).data, "\t", hashtext);
			
		if (outfile != null)
		{
			outfile.close ();
		}
			
		return hashtext;
	}
	public static String computeStringZero (int leadingZero)
	{
		String zero = "";
		for (int i = 0; i < leadingZero; i++)
		{
			zero = zero + "0";
		}
		
		return zero;
	}
	public static boolean isChainValid (String hashValue, int leadingZero, String zero)
	{
		if (hashValue.substring (0, leadingZero).equals (zero))
		{
			return true;
		}
		
		return false;
	}
	public static String copyContentsFromFile (String filename) 
	{
		try
		{
			input = new Scanner (new File (filename));
		}
		catch (IOException e)
		{
			System.err.println ("Error in IO");
			System.exit (1);
		}
		String temp = "";
		try
		{
			while (input.hasNext ())
			{
				temp += input.nextLine () + "\n";
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
		
		return temp;	
	}
	public static void saveToFile (String filename)
	{
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
		outfile.format ("%s", data);
						
		if (outfile != null)
		{
			outfile.close ();
		}
	}
}

class Block 
{
	public static String hash;
	public static String previousHash;
	public static String data; // a simple message
	private static long timeStamp; // as of number of milliseconds since 1/1/1970
	
	// Constructor
	Block (String data, String previousHash, SecretKey IV, String key) throws Exception
	{
		this.data = data;
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.hash = calculateHash (key, IV);
	}
	public static String calculateHash (String key, SecretKey IV) throws Exception
	{
		byte [] cipherText = CryptoMngr.encrypt (key.getBytes (), IV.getEncoded(), 
		(previousHash + Long.toString(timeStamp) + data).getBytes());
		
		String calculatedhash = Base64.getEncoder().encodeToString(cipherText).substring (0, 32);
		
		return calculatedhash;
	}
}
// // Source: https://makeinjava.com/encrypt-decrypt-message-using-aes-128-cbc-java-example/
class CryptoMngr 
{	
	public static String ALGORITHM = "AES";
	private static String AES_CBS_PADDING = "AES/CBC/PKCS5Padding";

	public static byte[] encrypt(final byte[] key, final byte[] IV, final byte[] message) throws Exception 
	{
		return CryptoMngr.encryptDecrypt(Cipher.ENCRYPT_MODE, key, IV, message);
	}

	public static byte[] decrypt(final byte[] key, final byte[] IV, final byte[] message) throws Exception 
	{
		return CryptoMngr.encryptDecrypt(Cipher.DECRYPT_MODE, key, IV, message);
	}

	private static byte[] encryptDecrypt(final int mode, final byte[] key, final byte[] IV, final byte[] message)
			throws Exception 
	{
		final Cipher cipher = Cipher.getInstance(AES_CBS_PADDING);
		final SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
		final IvParameterSpec ivSpec = new IvParameterSpec(IV);
		cipher.init(mode, keySpec, ivSpec);
		
		return cipher.doFinal(message);
	}
}
