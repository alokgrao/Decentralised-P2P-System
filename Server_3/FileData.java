package com.p2p;

import java.util.ArrayList;

//Class to store the file data at the server

public class FileData {

	int peerid;
	String IP;
	int portnumber;
	String filename;
	public int getPeerid() {
		return peerid;
	}
	public void setPeerid(int peerid) {
		this.peerid = peerid;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	
	
}
