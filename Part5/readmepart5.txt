// Nur Suhaira Bte Badrul Hisham
// 5841549
// Assignment 2 ReadMe Part 5

Compilation:
============
1. In NurSuhaira_5841549_A2/Part5:
	javac PoW.java 
	// to compile PoW.java to generate PoW.class, block.class, CryptoMngr.class

Before Execution:
=================
1. Make sure PoW.java is compiled with no error.
2. In command prompt:
	java PoW 4 new_ledger.txt
	
During Execution:
=================
1. Generate IV one time
2. Limit and data parameter will be displayed based on user's input
3. The program will display 3 menu options:
	i. Generating a new hashed ledger with random nonce
	ii. Validate newly record with random nonce
	iii. Quit: Exit program
2. For option 1:
	i. Extracting original ledger from "ledger.txt" into data // no hashed value
	ii. Filter delimiter in data (e.g. "\t")
	iii. Divding data into blocks of 16 bytes (128 bits) where no of blocks will be shown (e.g. 18)
	iv. Generating key one time
	vi. Input nonce in an increment order and validate hashValue computed with user input's no. of leading zeros
	v. Once found, a nonce value will be displayed with the new hashed value
	vi. Menu will be displayed once option 1 process is completed
3. For option 2:
	i. Current hash value will be shown
	ii. Input random nonce value (e.g. 123321)
	iii. Input new record to be inserted (e.g. Sionggo receives 50 dollars from Arron)
	iv. New hash value will be displayed and also stored in temp.txt
	vi. If new hash value meets the validity requirement of a hashed block then it will be updated in the new_ledger.txt
	and a successful message be displayed in the program with the nonce value
	v. Else, the new hash value computed will remain in temp.txt and will not be updated in new_ledger.txt and it will be display 		an unsuccessful message will be displayed	

After Execution:
=================
1 text file will be generated:
	i. new_ledger.txt

Execute Environment
===================
Operating system: Ubuntu
Using terminal.
