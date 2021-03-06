package main.Fixtures.BIG.Depricated;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SFTPFileFixture {
	private static final String SERVER_NAME = "ServerName";
	private static final String USER_NAME = "UserName";
	private static final String PASSWORD = "Password";

	/**
	 * The test file from location
    - the test file to location
    - The test file from name
    - the test file to name. 
    - the server name
    - the key pair
	 */
	
	private String fileFromLocation;
	private String fileToLocation;
	private String keyPairFileName;
	
	public String getKeyPairFileName() {
		return keyPairFileName;
	}
	public void setKeyPairFileName(String keyPairFileName) {
		this.keyPairFileName = keyPairFileName;
	}

	private String error;

	public String error(){
		return error;
	}
	
	
	public String getFileFromLocation() {
		return fileFromLocation;
	}
	public void setFileFromLocation(String fileFromLocation) {
		this.fileFromLocation = fileFromLocation;
	}
	public String getFileToLocation() {
		return fileToLocation;
	}
	public void setFileToLocation(String fileToLocation) {
		this.fileToLocation = fileToLocation;
	}
	
	
	public boolean hasMoved(){
		 Map<String,String> map = getServerFromFile();
		 String userName = map.get(USER_NAME);
		 String serverName = map.get(SERVER_NAME);
		 String password = map.get(PASSWORD);
		// String keyPair = map.get(KEY_PAIR);
		 System.out.println("Starting to create the SFTP connection with the details");
		 System.out.println("userName:" + userName);
		 System.out.println("serverName:" + serverName);
		 System.out.println("password:" + "*******");
		 JSch jsch = new JSch();

		 Session session;
			try {
	
				session = jsch.getSession(userName, serverName);
				session.setPassword(password);
				java.util.Properties config = new java.util.Properties(); 
				//we are removing the hostkey to start with for this test. Not ideal for end to end testing but 
				config.put("StrictHostKeyChecking", "no");
				session.setConfig(config);
				session.connect();
	
				 Channel channel = session.openChannel( "sftp" );
				 channel.connect();
				 ChannelSftp channelSftp = (ChannelSftp) channel;
				 File file = new File(System.getProperty("user.dir") + fileFromLocation);
				 System.out.println("putting file = " + file.getPath());
				 System.out.println("to location file = " + System.getProperty("user.dir") +fileToLocation);
				 FileInputStream fis = new FileInputStream(file);
				 channelSftp.put(fis,System.getProperty("user.dir") +fileToLocation);
				 fis.close();
				 channelSftp.exit();
				 session.disconnect();
				 
			} catch (JSchException e) {
				error = e.getMessage();
			} catch (SftpException e) {
				error = e.getMessage();
			} catch (FileNotFoundException e) {
				error = e.getMessage();
			} catch (IOException e) {
				error = e.getMessage();
			} 
			return true;
		}
	
	/*
	 * Parse the file in the location passed in. keys are 
	 * ServerName="ServerNAME",KeyPair="KEYPAIR"
	 */
	private Map<String,String> getServerFromFile(){
		File file = new File(System.getProperty("user.dir") +keyPairFileName);
		List<String> lines= new ArrayList<String>();
		if(file.exists()){
			Scanner in;
			try {
				in = new Scanner(file);
				while(in.hasNextLine()) {
				    lines.add(in.nextLine());
				}
				in.close(); 
			} catch (FileNotFoundException e) {
				error = "No properties for server found";
			}
					
		}
		//we have a list of lines now. Put them into variables 
		Map<String, String> returnMap = new HashMap<String,String>();
		for(String line : lines){
			if(line.startsWith(SERVER_NAME)){
				returnMap.put(SERVER_NAME, line.substring(line.indexOf("=")+1));
			}
			else if(line.startsWith(USER_NAME)){
				returnMap.put(USER_NAME, line.substring(line.indexOf("=")+1));
			}
			else if(line.startsWith(PASSWORD)){
				returnMap.put(PASSWORD, line.substring(line.indexOf("=")+1));
			}
		}
		return returnMap;
	}
}
