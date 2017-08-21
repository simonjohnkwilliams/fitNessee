package main.Fixtures.BIG.Depricated;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

public class LoadFileFixture {
	private static final String SERVER_NAME = "ServerName";
	private static final String KEY_PAIR = "KeyPair";
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
	private String fileFromName;
	private String fileToName;
	private String keyPairFileName;
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
	public String getFileFromName() {
		return fileFromName;
	}
	public void setFileFromName(String fileFromName) {
		this.fileFromName = fileFromName;
	}
	public String getFileToName() {
		return fileToName;
	}
	public void setFileToName(String fileToName) {
		this.fileToName = fileToName;
	}
	public String getKeyPairFileName() {
		return keyPairFileName;
	}
	public void setKeyPairFileName(String keyPairFileName) {
		this.keyPairFileName = keyPairFileName;
	}
	
	
	public boolean hasMoved(){
		 Map<String,String> map = getServerFromFile();
		 String userName = map.get(USER_NAME);
		 String serverName = map.get(SERVER_NAME);
		 String password = map.get(PASSWORD);
		// String keyPair = map.get(KEY_PAIR);
		
		 JSch jsch = new JSch();

		 Session session;
			try {
	
				session = jsch.getSession(userName, serverName);
				session.setPassword(password);
				java.util.Properties config = new java.util.Properties(); 
				//we are removing the hostkey to start with for this test. Not ideal for end to end testing but 
				config.put("StrictHostKeyChecking", "no");
				session.setConfig(config);
				/*String keyString = "....";
				 
				 parse the key
				byte [] key = Base64.getDecoder().decode ( keyString ); // Java 8 Base64 - or any other
				 
				HostKey hostKey = new HostKey ( info.getHostname (), key );
				 
				// add the host key
				jsch.getHostKeyRepository ().add ( hostKey );
				*/
				session.connect();
	
				 Channel channel = session.openChannel( "sftp" );
				 channel.connect();
				 ChannelSftp channelSftp = (ChannelSftp) channel;
				 channelSftp.cd(fileToLocation);
				 System.out.println(channelSftp.ls(fileToLocation));
				 Vector<LsEntry> filesInDir = channelSftp.ls(fileToLocation);
				 for (LsEntry name:filesInDir){
					String value = name.getFilename();
					 if(fileToName.equals(value)){
						 channelSftp.exit();
						 session.disconnect();
						 return true;
					 }
				 }
				 File file = new File(System.getProperty("user.dir") +fileFromLocation+fileFromName);
				 channelSftp.put(new FileInputStream(file),fileToName);
				 filesInDir = channelSftp.ls(fileToLocation);
				 for (LsEntry name:filesInDir){
					String value = name.getFilename();
					 if(fileToName.equals(value)){
						 channelSftp.exit();
						 session.disconnect();
						 return true;
					 }
				 }
				 channelSftp.exit();
				 session.disconnect();
				 
			} catch (JSchException e) {
				error = e.getMessage();
			} catch (SftpException e) {
				error = e.getMessage();
			} catch (FileNotFoundException e) {
				error = e.getMessage();
			} 
			return false;
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
			else if(line.startsWith(KEY_PAIR)){
				returnMap.put(KEY_PAIR, line.substring(line.indexOf("=")+1));
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
