// Nur Suhaira Bte Badrul Hisham
// 5841549
// Assignment 2 ReadMe Part 1

Compilation:
============
1. In NurSuhaira_5841549_A2/Part1:
	javac rsa.java 
	// to compile rsa.java to generate rsa.class

Before Execution:
=================
1. Make sure rsa.java is compiled with no error.
2. In command prompt:
	java rsa
	
During Execution:
=================
1. The program will display 5 menu options:
	i. KeyGen: The RSA key generation function
	ii. Sign: The RSA signing function
	iii. Verify: The RSA verification function
	iv. Generate: The RSA message generation function
	v. Quit: Exit program
2. First, choose option 1 where p and q to input 2 to 32 bit length from user 
e.g. 5 bit for p and 5 bit for q
3. Second, choose option 4 to generate message at random from 1 to N-1 value 
(**optional if mssg.txt exists in folder)
(exception handling is done if option 4 is not done first before option 2 and 3)
4. Third, choose option 2 to sign (encrypt) the message
5. Last, choose option 3 to verify (decrypt) the message where if verification succeeds,
message is accepted else message is rejected

After Execution:
=================
4 text files will be generated:
	i. pk.txt
	ii. sk.txt
	iii. mssg.txt
	iv. sig.txt

Execute Environment
===================
Operating system: Ubuntu
Using terminal.
