package main.Fixtures.BIG.Unix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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

public class PullFileDownLocally {
	private static final String SERVER_NAME = "ServerName";
	private static final String KEY_PAIR = "KeyPair";
	private static final String USER_NAME = "UserName";
	private static final String PASSWORD = "Password";

	/**
	 * Download the file to a local location. 
	 * 
	 */
	
	private String localLocation; //to download to 
	private String remoteLocation; //where to pick up from
	private String localFileName; //file name to call locally
	private String RemoteFileName; //file to pick up remotely. 
	private String keyPairFileName; //details of how to connect to the server
			
	public String getLocalLocation() {
		return localLocation;
	}
	public void setLocalLocation(String fileFromLocation) {
		this.localLocation = fileFromLocation;
	}
	public String getRemoteLocation() {
		return remoteLocation;
	}
	public void setRemoteLocation(String fileToLocation) {
		this.remoteLocation = fileToLocation;
	}
	public String getLocalFileName() {
		return localFileName;
	}
	public void setLocalFileName(String fileFromName) {
		this.localFileName = fileFromName;
	}
	public String getRemoteFileName() {
		return RemoteFileName;
	}
	public void setRemoteFileName(String fileToName) {
		this.RemoteFileName = fileToName;
	}
	public String getKeyPairFileName() {
		return keyPairFileName;
	}
	public void setKeyPairFileName(String keyPairFileName) {
		this.keyPairFileName = keyPairFileName;
	}
	
	public boolean isDownloaded(){
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
				 channelSftp.cd(remoteLocation);
				 System.out.println(channelSftp.ls(remoteLocation));
				 Vector<LsEntry> filesInDir = channelSftp.ls(remoteLocation);
				 for (LsEntry name:filesInDir){
					String value = name.getFilename();
					 if(RemoteFileName.equals(value)){
						 writeOutFile(channelSftp.get(RemoteFileName),localLocation+localFileName);
						 channelSftp.exit();
						 session.disconnect();
						 File file = new File(System.getProperty("user.dir") +localLocation+localFileName);
						 return file.exists();
					 }
				 }
				 channelSftp.exit();
				 session.disconnect();
				 return false;
			} catch (JSchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SftpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			return false;
		}
	
	private void writeOutFile(InputStream inputStream, String fileOutputLocation) {
		OutputStream outputStream = null;

		try {
			// write the inputStream to a FileOutputStream
			File file = new File(System.getProperty("user.dir") +fileOutputLocation);
			//file.createNewFile();
			outputStream =new FileOutputStream(file);

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			outputStream.flush();
			outputStream.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // file = input file
					
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
