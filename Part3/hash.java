// Nur Suhaira Bte Badrul Hisham
// 5841549
// Assignment 2 Part 3

public class hash
{
	static final String FIRSTNAME = "SUHAIRA ";
	static String MSG = "The Cat-In-The-Hat owes ";
	static int x = 1; // initial x
	static int x2 = 2; // initial x'
	
	public static void main (String [] args)
	{
		System.out.print("\033[H\033[2J");
		
		System.out.println ("Collision Finding of Hash Functions...\n");
		
		String m = "";
		String m2 = "";
		String h = "";
		String h2 = "";
		boolean ok = false;
		int trial = 0;
		while (ok == false)
		{
			m += MSG + FIRSTNAME + x + " dollars"; // first message (m)
			m2 += MSG + FIRSTNAME + x2 + " dollars"; // second message (m')
			
			h = ssha1.encrypt (m);
			h2 = ssha1.encrypt (m2);
			
			if (h.equals (h2))
			{
				ok = true;
			}
			else
			{
				++x; // finding next x
				++x2; // finding next x'
				m = "";
				m2 = "";
				++trial; // increment by 1 after completing 1 round
			}
			
		}
		
		// if there is a collision, then print
		System.out.println ("\tM value      : " + m);
		System.out.println ("\tM' value     : " + m2);
		System.out.println ("\n\tH (M) value  : " + h);
		System.out.println ("\tH (M') value : " + h2);
		System.out.println ("\nNo. of trials before collision : " + trial);
		
	}
}
