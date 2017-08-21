package main.Fixtures.BIG.Unix;

import java.io.IOException;
import java.net.SocketException;

import com.jcraft.jsch.JSchException;
import com.rmg.TestManager.FileManager;
import com.rmg.credentials.Credentials;

public class PutFileOnFMS {

	private String localfilepath;
	private String remotefilename;
	private String remotedir;
	private String port;
	private String protocol;
	private String configFile;
		
	private static final String HOST_NAME = "fmsHostName";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String KEY_FILE = "keyFile";
	private static final String PASSPHRASE = "passphrase";
	
	private String error;
	public String getLocalfilepath() {
		return localfilepath;
	}

	public void setLocalfilepath(String localfilepath) {
		this.localfilepath = localfilepath;
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

	public String error(){
		return error;
	}
	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
	
	
	public boolean hasMoved(){
		try{
			Integer portInt = Integer.parseInt(port);
			//need to put in the real location of the credentials file. 
			Credentials credentials = new Credentials(configFile);
			if (credentials.getError() ==null){
				FileManager fileManager = new FileManager(credentials.getValue(HOST_NAME), credentials.getValue(USERNAME), credentials.getValue(PASSWORD), 
					credentials.getValue(KEY_FILE),credentials.getValue(PASSPHRASE));
				return fileManager.putFile(localfilepath,remotefilename,remotedir,portInt, protocol);	
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
		} catch (JSchException e) {
			error = e.getMessage();
			return false;
		} catch (Exception e) {
			error = e.getMessage();
			return false;
		}
	}
}
