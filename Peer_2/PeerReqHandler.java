package com.p2p;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

class PeerReqHandler implements Runnable{

	int portnumber;
	ServerSocket peerserversock;
	Socket peerhandlersock;
	String filename;
	
	  
	public PeerReqHandler(int port)
	{
		this.portnumber = port;               //Assign the port number
		
	}
	
	public void run()
	{
		BufferedReader clientrec;
		File sendfile = null;
		
		
		try{
            
			peerserversock = new ServerSocket(this.portnumber);        //Open the peer server socket
			
			System.out.println("Peer Server Started");
			
			while(true)
			{
				peerhandlersock = peerserversock.accept();
				
				System.out.println("Connection recieved from :" + peerhandlersock.getInetAddress().getHostName());
				
				ObjectInputStream in = new ObjectInputStream(peerhandlersock.getInputStream());
				
				filename = (String)in.readObject();
				
				System.out.println("Donwload Request for File : " + filename );
				
				ObjectOutputStream out = new ObjectOutputStream(peerhandlersock.getOutputStream());
				
				for (File f : Peer.filelist) 
				{
					if(f.getName().equals(filename))        //Search the file list for the searched file name
					{
						sendfile = f;
					    break;    
					 
					}   
						
				}
				
				out.flush();
				
				 BufferedReader br = new BufferedReader(new FileReader(sendfile));
				    
                 StringBuilder sb = new StringBuilder();
				 String line = br.readLine();

				 while (line != null) {                     //Write file to output
				      sb.append(line);
				      sb.append("\n");
				       line = br.readLine();
				        }
				
				out. writeObject(sb.toString());
				
	            br.close();
	            out.close();
	            in.close();
	            peerhandlersock.close();
			}
		    
		}
		catch (Exception e)
		{
			//e.printStackTrace();
		}
		
		
	}
	
}
