package com.p2p;

import java.awt.List;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.Random;
import java.util.Scanner;
import java.nio.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import javax.naming.ldap.StartTlsRequest;
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

class Peer {
	
	
	int i=0;
	int j=0;
	String filepath = "Peer2";                                      //Set the path 
	int peerid;
	String IndSrvIP = "127.0.0.1";                                  //Index Server IP  
	String filename;
	String IPaddr;
	int clientport = 4080;                                          //Set Client Port 
	int servport = 4444;                                            //Index Server Port 
	static ArrayList<File> filelist = new ArrayList<File>();
    ServerSocket PeerServSoc;
    Socket indexsocket;
    Socket peerdownloadsoc;
    Servlist indserv;
    ArrayList<Servlist> servlist = new ArrayList<Servlist>();
    
    public Peer(String[] filenames)
    {
    	init(filenames);
    	Thread dthread = new Thread (new PeerReqHandler(clientport));
    	dthread.start();
        cons(); 
    }
    
    public static void main (String[] args)
	{
    	try
    	{
    	String [] filenames = new String[10];
    	int i=0;
    	
    	while(i < 10)
    	{
    	
    	filenames[i] = "File_"+i+"P2.txt";	
    	i++;	
    	
    	}
    	
    	Peer peer = new Peer(filenames);
    	
    	}
    	catch(Exception e)
    	{
    		//e.printStackTrace();
    	}
	    
	}
    
    public void init(String[] filenames)
    {
         this.peerid = 2;                          //Set Peer ID      
         this.IPaddr = "127.0.0.1";                //Set Peer IP Address
         
        try
        
        {
        	System.out.println("||Peer System||");
        	System.out.println("ID : "+this.peerid);
        	FileReader fr = new FileReader("IPPort.txt");         //read the filename in to filereader object    
    		String val=new String();
    		BufferedReader br = new BufferedReader(fr);	
    		val = br.readLine();
    		this.IPaddr = val;
    		System.out.println("IP :"+ this.IPaddr);
    		val = br.readLine();
    		this.IndSrvIP = val;
    		clientport = Integer.parseInt(val);
    		System.out.println("Port :"+clientport);
    		br.close();
    		fr.close(); 	
        	File dir = new File(filepath);
            if(!dir.exists())
            {
           	 dir.mkdir();
            	
         
            if(filenames.length > 0)
    	    {
    		 for(String s : filenames)
    		 {
    			 File f = new File(dir.getPath(),s);
    			 if(!f.exists())
    			 { 
    				 f.createNewFile();
    				 PrintWriter pw = new PrintWriter(f);
    				 pw.println(s+" Content");
    				 pw.close(); 
    			 }
    			 filelist.add(f);
    			 //String s1 = f.getName();
    			 //System.out.println(s1);
    		   }
    		 
    	     }
           }
            else
            {
            	for(File f1 : dir.listFiles())
          	     {
          		 filelist.add(f1);
          	     }
            }
         
         
         serverinit();
         
        }
        catch (Exception e) 
        {
			
		}

    	
    }
    
    
    public void serverinit()
    {
    	try{
    	FileReader fr = new FileReader("config.txt");              //read the filename in to filereader object
    	
		String indip=new String();
		String indport = new String();
		BufferedReader br = new BufferedReader(fr);
		String sname;
		while ((sname = br.readLine()) != null)
		{
			Servlist s = new Servlist();
			String[] tok = sname.split(" ");
			s.servername = tok[0];
			s.IP = tok[1];
			s.portNumber = Integer.parseInt(tok[2]);
			servlist.add(s); 
		}
		br.close();
		fr.close();
		ransrv();
		
    	}
    	catch(Exception e)
    	{
    		
    	}
    }
    
    public void ransrv()
    {
    	Random r = new Random();
		int index = r.nextInt(servlist.size());
		indserv = servlist.get(index);
		IndSrvIP = indserv.IP;
		servport =  indserv.portNumber;
		
		System.out.println("Server Name : "+indserv.servername+" IP : "+IndSrvIP+" Port : "+servport);
    }
    
    public void cons()
    {
    	
    	boolean exit = false;
    	
    	System.out.println("Options \n 1 : Register \n 2 : Lookup \n 3 : Download \n 4 : Display File List \n 5 : Who Am I \n 6 : Exit");	
    	
    	Scanner sc = new Scanner(System.in);
    	try{
    	
    	while(!exit)
    	{	
      		
    	switch(sc.nextInt())
    	{
    	       case 1 : System.out.println("Registering All Files");
                        int peerid = this.peerid;
                        String IP = this.IPaddr;
                        int port = this.clientport;
                        //String fname = sc.next();
                        //long regStartTime = new Date().getTime();
       	    		    register(peerid, IP, port);
       	    		    //long regEndTime = new Date().getTime();                                     /*Evaluation Code*/
       	    		    //long diff = regEndTime - regStartTime;
       	    		    //System.out.println("Registration Time For 10K Files For Peer 8: "+diff); 
       	    		    //System.exit(0);
       	    		    break;
       	    		 
    	       case 2 : System.out.println("Enter Filename to Search");
    	                //long regStartTime1 = new Date().getTime();                           /*Evaluation Code*/
                        /*while(i<10000)
                        {
		                 search("File_"+i+"P7.txt");
		                 i++;
                        }
                        long regEndTime1 = new Date().getTime();
	    		        long diff1 = regEndTime1 - regStartTime1;
	    		        System.out.println("Search Time For 10K Files Peer 7 : "+diff1);*/
       		            search(sc.next());
	    		        //System.exit(0);
       		            break;
       		           
    	       case 3 : System.out.println("Enter Peerid, Peer Ip, Port Number and Filename ");
    	                int pid = Integer.parseInt(sc.next());
    	                String pip = sc.next();
    	                int por = Integer.parseInt(sc.next());
       		            String filname = sc.next();
       		            obtain(pid, pip, por, filname);
    	                /*long regStartTime2 = new Date().getTime();                            /*Evaluation Code
    	                 while(j<10000)
                         {
		                  obtain(8, "127.0.0.1", 4118, "File_"+j+"P8.txt");
		                  j++;
                         }
                        long regEndTime2 = new Date().getTime();
	    		        long diff2 = regEndTime2 - regStartTime2;
	    		        System.out.println("Download time for 10K Files Peer 7: "+diff2);
	    		        */
	    		        //System.exit(0);
       		            break;
       		   
    	       case 4 : for(File f : filelist)
    	                {
    	    	          System.out.println(f.getName());
    	                }
    	                System.out.println("Options \n 1 : Register \n 2 : Lookup \n 3 : Download \n 4 : Display File List \n 5 : Who Am I \n 6 : Exit");
    	                break;
    	                
    	                 
    	       case 5 : System.out.println("\n 	Peer Id : "+ this.peerid +"\n IP Address : "+this.IPaddr+"\n Port Number : "+ this.clientport );   
    	                break; 
    	       case 6 : exit = true;
				        System.exit(0);
				        break;
				       
			   default: break; 	        
    	}
    	
    	
    	}
    	}
    	catch(Exception e)
    	{
    		//e.printStackTrace();
    	}
    	
    	
    }
    
    
    int GetServerKey(String Key)
	{
		int serid = 0;
		int indix = Key.hashCode();                   //Added to Compute Hashcode
		serid = Math.abs(indix % 3);
		return serid;
		//return 0;
	}
    
    public void register(int peerid, String IP, int port)
    {
      
      try{
    		
      	String req = "Register"+" "+peerid+" "+IP+" "+port+" ";
      	String res;
    	
      	
    	
    	System.out.println("Connected to Indexing Server "+indserv.servername);
    	
    	if(filelist!=null)
    	for(File f : filelist)	
    	{
    	
    	if(f != null)	
    	{
    	
        this.indexsocket = new Socket(IndSrvIP, servport);
        ObjectOutputStream out = new ObjectOutputStream(this.indexsocket.getOutputStream());;
        ObjectInputStream in = new ObjectInputStream(this.indexsocket.getInputStream());	
    		
        out.flush();    		
    	
    	
        out.writeObject(req+f.getName());
        	
        res = (String)in.readObject();
    	
    	in.close();
    	out.close();
    	this.indexsocket.close();
    	System.out.println(res);
    	
    	}
    	}
        
    	//out.close();
    	//in.close();
    	
    	
    	}
    	catch(Exception e)
    	{
    		//e.printStackTrace();
    	}
        finally
        {
          System.out.println("Options \n 1 : Register \n 2 : Lookup \n 3 : Download \n 4 : Display File List \n 5 : Who Am I \n 6 : Exit");
        }
    }
    
    
    public void search(String filename)
    {
    	try{
    		
    		ObjectOutputStream out;
    		ObjectInputStream in;  
        	
        	String res;
        	  
        	for(Servlist s : servlist)
        	{
        	
        	String sip = s.IP;
        	int sport = s.portNumber;
        		
        	this.indexsocket = new Socket(sip, sport);
        	System.out.println("Searching on Indexing Server ");
        	
        	out = new ObjectOutputStream(this.indexsocket.getOutputStream());
        	out.flush();
        	out.writeObject("Search"+" "+filename);
        	
        	in = new ObjectInputStream(this.indexsocket.getInputStream());
        	
        		
        	res = (String)in.readObject();
        	
        	in.close();
        	out.close();
        	
        	if(res != null)
        	{
        	System.out.println("File "+filename+" found at\n" + "Peer :" );
        	
        	System.out.println(res);
        	
        	break;
        	}
        	}
             
        	//out.close();
        	//in.close();
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        	}
    	    finally
    	    {
    	       
        	   System.out.println("Options \n 1 : Register \n 2 : Lookup \n 3 : Download \n 4 : Display File List \n 5 : Who Am I \n 6 : Exit");
            }
    }
    
    

	public void obtain(int peerid, String IP, int port, String filename)
	{
		
		try{
	        
	    	ObjectOutputStream out;
	    	ObjectInputStream in;
			Socket downloadsock = new Socket(IP, port);
			
			out = new ObjectOutputStream(downloadsock.getOutputStream());
			out.flush();
			out.writeObject(filename);
			
			in = new ObjectInputStream(downloadsock.getInputStream());
			
			
			String cont = (String)in.readObject();
			
			File f = new File(filepath,filename);
			
			if(!f.exists())
			{
				f.createNewFile();
			}
			
			PrintWriter pw = new PrintWriter(f);
			pw.println(cont); 
			pw.close(); 
			filelist.add(f);
			
			
			System.out.println("File "+ filename + "Downloaded");
			downloadsock.close();
			
			}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    	}
	
        }
	


}
