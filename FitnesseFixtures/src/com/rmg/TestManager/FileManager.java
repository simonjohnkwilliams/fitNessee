package com.rmg.TestManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;

/**
 * @author gaurav.agarwal
 *
 */
public class FileManager {
	private String hostName;
	private String username;
	private String password;
	private String keyfile;
	private String passphrase;
	public FileManager(final String hostName, final String username, final String password, final String keyfile, final String passphrase) {
		super();
		this.hostName = hostName;
		this.username = username;
		this.password = password;
		this.keyfile = keyfile;
		this.passphrase = passphrase;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getKeyfile() {
		return keyfile;
	}
	public void setKeyfile(String keyfile) {
		this.keyfile = keyfile;
	}
	public String getPassphrase() {
		return passphrase;
	}
	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}
	
	public boolean putFile(String localfilepath, String remotefilename, String remotedir, int port, String protocol) throws SocketException, IOException, JSchException, SftpException{
		if(protocol.equals("SFTP")){
			return putSFTPFile(localfilepath, remotefilename, port, remotedir);
		}else if(protocol.equals("FTP")){
			return putFTPFile(localfilepath, remotefilename, remotedir);
		}else if(protocol.equals("FTPS")){
			return putFTPSFile(localfilepath, remotefilename, remotedir);
		}else{
			return false;
		}
	}
			    
	private boolean putSFTPFile(String localfilepath, String remotefilename, int port, String remotedir) throws JSchException, IOException, SftpException{
		Session session = null;
	    Channel channel = null;
	    try {
	        JSch ssh = new JSch();
	        if(StringUtils.isNotEmpty(keyfile)&&StringUtils.isNotEmpty(passphrase)){
	        	final byte[] prvkey = FileUtils.readFileToByteArray(new File(keyfile)); 
		        final byte[] emptyPassPhrase = passphrase.getBytes();
		        ssh.addIdentity(username, prvkey,null,emptyPassPhrase);
	            System.out.println("identity added ");
	            UserInfo ui = new MyUserInfo(); // MyUserInfo implements UserInfo
	            session = ssh.getSession(username,hostName, port);
	            session.setUserInfo(ui);
	        }
	        else{
	        	session = ssh.getSession(username,hostName, port);
	        	session.setPassword(this.password);
		        session.setConfig("StrictHostKeyChecking","no");
	        }
	        session.connect();
	        channel = session.openChannel("sftp");
	        channel.connect();
	        //System.out.println("Is channel open: " + channel.isClosed());  
	  	  	//System.out.println("Is channel connected: " + channel.isConnected());  
	        ChannelSftp sftp = (ChannelSftp) channel;
	        //System.out.println("In directory: " + sftp.pwd());
	        //System.out.println("In directory: " + sftp.lpwd());
	        	sftp.put(localfilepath, remotedir+"/"+remotefilename);
	 	        sftp.chmod(509, remotedir+"/"+remotefilename);
	 	        return true;
	        
	    } finally {
	        if (channel != null) {
	            channel.disconnect();
	        }
	        if (session != null) {
	            session.disconnect();
	        }
	    }
	}
		
	private boolean putFTPFile(String localfilepath, String remotefilename, String remotedir){
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(this.hostName);
			ftpClient.login(this.username, this.password);
			ftpClient.changeWorkingDirectory(remotedir);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			File file= new File(localfilepath);
			boolean result = ftpClient.storeFile(remotefilename, new FileInputStream(file));
			if(result){
				return(true);
			}else{
				return(false);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return(false);
		} finally{
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	private boolean putFTPSFile(String localfilepath, String remotefilename, String remotedir) throws IOException{
		FTPSClient ftpClient = new FTPSClient(false);
		try {
			ftpClient.connect(this.hostName);
			ftpClient.login(this.username, this.password);
			ftpClient.changeWorkingDirectory(remotedir);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			File file= new File(localfilepath);
			boolean result = ftpClient.storeFile(remotefilename, new FileInputStream(file));
			if(result){
				return(true);
			}else{
				return(false);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return(false);
		} finally{
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public boolean getFile(String localfilePath,  String remoteFile, String remotedir, int port, String protocol) throws IOException{
		if(protocol.equals("SFTP")){
			return getSFTPFile(localfilePath, remoteFile, port, remotedir);
		}else if(protocol.equals("FTP")){
			return getFTPFile(localfilePath,remoteFile, remotedir);
		}else if(protocol.equals("FTPS")){
			return getFTPSFile(localfilePath,remoteFile, remotedir);
		}else{
			return false;
		}
	}
	
	private boolean getSFTPFile(String localFilePath, String remotefilename, int port, String remotedir){
		Session session = null;
	    Channel channel = null;
	    try {
	        JSch ssh = new JSch();
	        session = ssh.getSession(username,hostName, port);
	        session.setPassword(this.password);
	        session.setConfig("StrictHostKeyChecking","no");
	        session.connect();
	        channel = session.openChannel("sftp");
	        channel.connect();
	        ChannelSftp sftp = (ChannelSftp) channel;
	        sftp.get(remotedir+"/"+remotefilename,localFilePath);
	        return true;
	        
	    } catch (JSchException  e) {
	        return false;
	    } catch (SftpException e) {
	    	return false;
	    } finally {
	        if (channel != null) {
	            channel.disconnect();
	        }
	        if (session != null) {
	            session.disconnect();
	        }
	    }
	}
	
	private boolean getFTPFile(String localFilePath,String remotefilename, String remotedir){
		FTPClient ftpClient = new FTPClient();
		File file= null;
		FileOutputStream os = null;
		try {
			ftpClient.connect(this.hostName);
			ftpClient.login(this.username, this.password);
			ftpClient.changeWorkingDirectory(remotedir);
			file= new File(localFilePath);
			os = new FileOutputStream(file);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.retrieveFile(remotefilename, os);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		} finally{
			if(os!=null)
				try {
					os.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			if(ftpClient!=null)
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
			
	}

	private boolean getFTPSFile(String localFilePath, String remotefilename, String remotedir){
		FTPSClient ftpClient = new FTPSClient(false);
		File file= null;
		FileOutputStream os = null;
		try {
			ftpClient.connect(this.hostName);
			ftpClient.login(this.username, this.password);
			ftpClient.changeWorkingDirectory(remotedir);
			file= new File(localFilePath);
			os = new FileOutputStream(file);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.retrieveFile(remotefilename, os);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		} finally{
			if(os!=null)
				try {
					os.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			if(ftpClient!=null)
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public boolean isFileAvailable(final String remoteFile, final String remotedir, final int port, final String protocol) throws IOException{
		if(protocol.equals("SFTP")){
			return touchFileOrDirSFTP(remoteFile, port, remotedir);
		}else if(protocol.equals("FTP")){
			//return getFTPFile(localfilePath,remoteFile, remotedir);
		}else if(protocol.equals("FTPS")){
			//return getFTPSFile(localfilePath,remoteFile, remotedir);
		}else{
			return false;
		}
		return false;
	}
	
	public boolean touchFileOrDirSFTP(final String remotefilename, final int port, final String remotedir){
		Session session = null;
	    Channel channel = null;
	    try {
	        JSch ssh = new JSch();
	        session = ssh.getSession(username,hostName, port);
	        session.setPassword(this.password);
	        session.setConfig("StrictHostKeyChecking","no");
	        session.connect();
	        channel = session.openChannel("sftp");
	        channel.connect();
	        ChannelSftp sftp = (ChannelSftp) channel;
	        //connected now to touch the file. 
	        SftpATTRS att = sftp.stat(remotedir+"/"+remotefilename);
	        if(att!=null){
	        	return true;	
	        }
	        return false;
	        
	    } catch (JSchException  e) {
	        e.printStackTrace();
	    	return false;
	    } catch (SftpException e) {
	    	e.printStackTrace();
	    	return false;
	    } finally {
	        if (channel != null) {
	            channel.disconnect();
	        }
	        if (session != null) {
	            session.disconnect();
	        }
	    }
	}
}