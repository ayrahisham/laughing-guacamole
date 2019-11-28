// Nur Suhaira Bte Badrul Hisham
// 5841549
// Assignment 2 ReadMe Part 4

Compilation:
============
1. In NurSuhaira_5841549_A2/Part4:
	javac dsa.java 
	// to compile dsa.java to generate dsa.class

Before Execution:
=================
1. Make sure dsa.java is compiled with no error.
2. In command prompt:
	java dsa
	
During Execution:
=================
1. The program will display 3 menu options:
	i. Signature: Produce signature on input file
	ii. Verification: Verify input file and signature
	iii. Quit: Exit program
2. First, choose option 1 where an input file (e.g. input.txt file in folder) is used to generate signature
	Note: In input.txt, in sequence follows are the variables stored:
	a. p
	b. q
	c. h
	d. g
	e. x
	f. y
	g. m // using hashcode and random generated msg < q
	Computation is done based on the variables above to produce signature stored in sig.txt
3. Second, choose option 2 to verify signature using an input file and output file produced in option 1
(e.g. input.txt file and sig.txt in folder)
Using the variables stored in these two files, verify the signature that r stored in sig.txt is equal to
v which is computed in option 2.

After Execution:
=================
1 text file will be generated:
	i. sig.txt

Execute Environment
===================
Operating system: Ubuntu
Using terminal.
