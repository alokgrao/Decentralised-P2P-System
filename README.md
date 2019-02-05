# Decentralised-P2P-System
This is a Decentralised P2P system where servers store the details of the peers and the peers will hold the files. 
these files can be requested and downloaded by other peers.
A Peer needs to register the File with one of the decentralised servers in order for the file to bevisible to others.

Checking Peer Data 

1.	On starting a peer the Peer application will provide options to  
Register, Lookup,  Download,  List files,  Who Am I, Exit 
2.	Select option 5 which will display the peer id the IP and the port number which is used to register with the server 

Registering A file 

1.	 On starting a peer the Peer application will provide options to 
                Register, Lookup,  Download,  List files,  Who Am I, Exit
2.	type “1” in the terminal and press enter 
3.	All the files in the shared folder are registered with the indexing server that is chosen randomly when the peer starts
4.	The Server will Return a Messages saying the Files for the Peer are successfully registered with it. 

Searching a File 

1.	On starting a peer the Peer application will provide options to 
Register, Lookup,  Download,  List files,  Who Am I, Exit
2.	type “2” in the terminal and press enter 
3.	The Application will ask you to enter the Filename, enter the filename and press enter 
4.	The Application will first search for the file in the registered indexing server 
5.	if the file is not found then other indexing servers are searched and the result is displayed


Downloading a File 

1.	On starting a peer the Peer application will provide options to 
    Register, Lookup,  Download,  List files,  Who Am I, Exit
2.	type “3” in the terminal and press enter 
3.	The Application will ask you to enter The PeerID IP address Port Number Filename 
enter the values with a space between them eg: 1 127.0.0.1 4001 File_1.txt and press Enter 
4.	The Application will download the requested file into the shared folder and a message is shown saying that the file has been downloaded. 
5.	type “4” in the terminal and press enter 
6.	The newly downloaded file can be seen in the display  The newly downloaded file is listed in the shared folder. 

Running The application on a multi machine environment 
Initially the application is set to run on local machine using different port numbers however the application can run on multiple machines. 
Navigate to the application folder and find the folders for all servers and peers, navigate to the peer or server folder which you want to run on the machine, open the file IPPort for the Peers and change The IP address in the file, similarly for indexing servers navigate to the server folder and find the indip.txt file and change the IP in the file to  the IP of the machine. Running the application remains  the same.


