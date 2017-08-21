package main.Fixtures.BIG.Unix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.rmg.TestManager.FileManager;
import com.rmg.credentials.Credentials;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class CheckRemoteFileAvailable {

	private String remotefilename;
	private String remotedir;
	private String port;
	private String protocol;
	private String configFile;
	private String error;
		
	private static final String HOST_NAME = "fmsHostName";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String KEY_FILE = "keyFile";
	private static final String PASSPHRASE = "passphrase";

	public String error() {
		return error;
	}
	
	public String getRemotefilename() {
		return remotefilename;
	}

	public void setRemotefilename(String remotefilename) {
		this.remotefilename = remotefilename;
	}

	public String getRemotedir() {
		return remotedir;
	}

	public void setRemotedir(String remotedir) {
		this.remotedir = remotedir;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public boolean isAvailable(){
	try{
		Integer portInt = Integer.parseInt(port);
		Credentials credentials = new Credentials(configFile);
		if (credentials.getError() ==null){
			FileManager fileManager = new FileManager(credentials.getValue(HOST_NAME), credentials.getValue(USERNAME), credentials.getValue(PASSWORD),
					credentials.getValue(KEY_FILE),credentials.getValue(PASSPHRASE));
			if(!fileManager.isFileAvailable(remotefilename,remotedir,portInt, protocol)){
				if(error==null){
					error = "Error the file you are looking for doesn't exist";	
				}
				return false;
			} 
			return true; 	
		}
		else{
			error = credentials.getError();
			return false;
		}

	}
	catch (NumberFormatException e){
		error = e.getMessage();
		return false;
	} catch (SocketException e) {
		error = e.getMessage();
		return false;
	} catch (IOException e) {
		error = e.getMessage();
		return false;
	}
	}
}
