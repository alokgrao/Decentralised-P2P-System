package com.p2p;

import java.util.*;
import java.util.function.ToIntFunction;
import java.io.*;
import java.net.*;

public class Server extends Thread {

    
    boolean isStopped;
    
    private ArrayList<FileData> peerlist = new ArrayList<FileData>();
    String Ind_Ip = null;
    int Ind_Port;
    int sid = 1;
    //ArrayList<Servlist> servlist = new ArrayList<Servlist>();
    Servlist nextsrv = new Servlist();
    Socket searsock;
    
	public Server () 
	{
		init();
		start();                          //Start the Server Thread
	
	}
	
    
	
	public static void main (String[] args)
	{
		System.out.println("|| Decentralised Indexing Server "+1+"||");
		
		new Server();                      //Create The server Instance
	}
	
	
	public void register(int peerid, String IP, int port, String filename)
	{
		Iterator<FileData> iterator = peerlist.iterator();
		boolean peerexistflag = false;
		int index=0;
			FileData newpeer = new FileData();
			newpeer.peerid = peerid; 
			newpeer.IP = IP;
			newpeer.portnumber = port;
			newpeer.filename = filename;
			peerlist.add(newpeer);                //Register the file in the list of the server
			System.out.println("File:"+filename+" is registried! from "+"Client:"+newpeer.peerid);
		
	}
	
	
	public ArrayList<FileData> search(String fileName)
	{
		
        Iterator<FileData> iterator = peerlist.iterator();
		
        
		ArrayList<FileData> filepeers = new ArrayList<FileData>();
		
		boolean fileexist = false;
		
		while (iterator.hasNext())
		{
			FileData next = (FileData)iterator.next();
		    
			
		    if(next.filename.equals(fileName))
		    {
		    	fileexist = true;
		    	filepeers.add(next);            // Search the list for the given filename
		    	
		    }
		}
		
		if(!fileexist)
		{
			System.out.println("File Does not exist");
		}
		
		
		return filepeers;
		
		
	}
	
	
	
	public void init()
	{
		try{
		FileReader fr = new FileReader("indip.txt");//read the filename in to filereader object    
		String val=new String();
		BufferedReader br = new BufferedReader(fr);	
		val = br.readLine();
		Ind_Ip = val;
		Ind_Port = Integer.parseInt(br.readLine());
		System.out.println("IP : "+ Ind_Ip+" Port : "+Ind_Port);
		br.close();
		fr.close();
		
		}
		catch(Exception e)
		{
			
		}
	}
	
	public void run()                                       
	{
		
		try{
		
			
		ServerSocket sSocket = new ServerSocket(Ind_Port);
		
		while(true)
		{
		Socket sock = sSocket.accept();
	    System.out.println("Connected");
	    ObjectInputStream in = new ObjectInputStream(sock.getInputStream()); 
		ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
		//String reqsrv = null;
		int hopcount = 0;
		boolean issrv=false;
		
		String arg = (String)in.readObject();
		String[] tok = arg.split(" ");
		String command = tok[0];
		
        
        if(command.equals("Register"))                   //Check Command for registration
        {   
        	int peerid = Integer.parseInt(tok[1]);
        	String IP = tok[2];
			int port = Integer.parseInt(tok[3]);
			String filename = tok[4];
            register(peerid, IP, port, filename);
            out.writeObject(filename + " Successfully Registered on Server "+sid);
        }
        
        else 
        	if(command.equals("Search"))                   //Check Command for Search
        	{
        		String filname = tok[1];
        		
        		ArrayList<FileData> filepeers = search(filname);
        		
        		String res = null;
        		
        		
        		
        		for(FileData f : filepeers)
        		{
        			res = f.peerid + " "+f.IP+" "+f.portnumber+"\n";  
        		}
        		
        		
        			out.writeObject(res);
        		
        		
        		
        	}
        
        
        in.close();                                          //Close socket and stream readers and writers
        out.close(); 
        sock.close();
		}
	   }
		catch(Exception e)
		{
			e.printStackTrace();
		}
					
	}

}
	

	
	

		
		
	
	

