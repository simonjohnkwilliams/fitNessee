package com.rmg.TestManager;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;
import com.rmg.credentials.Credentials;


/**
 * This class executes the shell script on the remote server
 * Requires the jSch java library
 * @author gaurav.agarwal
 *
 */
public class ShellExecuter{
	private static int port=22;
 	private static final String HOST_NAME = "fmsHostName";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String KEY_FILE = "keyFile";
	private static final String PASSPHRASE = "passphrase";
	private static final String PORT = "port";
    
 /**
  * This method will execute the script file on the server.
  * This takes file name to be executed as an argument
  * The result will be returned in the form of the list
  * @param scriptFileName
  * @return
  */
 
 
 private ShellExecuter(){}
  
 private static Session getSession(final String username, final String password, final String hostName, final String keyFile, final String passphrase, final int port) throws JSchException, IOException{
	 JSch ssh = new JSch();
	 Session session = null;
	 final byte[] prvkey = FileUtils.readFileToByteArray(new File(keyFile)); 
	 final byte[] emptyPassPhrase = passphrase.getBytes();
	 ssh.addIdentity(username, prvkey,null,emptyPassPhrase);
     System.out.println("identity added ");
     UserInfo ui = new MyUserInfo(); 
     session = ssh.getSession(username,hostName, port);
     session.setUserInfo(ui);
     session.connect();
     return session;
 }
 
 private static Session getSession(String username, String password, String hostname, final int port) throws JSchException{
	 JSch ssh = new JSch();
     Session session = ssh.getSession(username,hostname, port);
     session.setPassword(password);
     session.setConfig("StrictHostKeyChecking","no");
     session.connect();
	 return session;
 }
 
 private static Session getSession(String username, String password, String hostname) throws JSchException{
	 JSch ssh = new JSch();
     Session session = ssh.getSession(username,hostname, port);
     session.setPassword(password);
     session.setConfig("StrictHostKeyChecking","no");
     session.connect();
	 return session;
 }
 
 public static String executeRemoteCommand(final Credentials credentials, final List<String> commandsList) throws Exception{
	 StringBuffer result = new StringBuffer();
	 Session session = null;
	 	if(credentials.getValue(KEY_FILE)!=null&&credentials.getValue(PASSPHRASE)!=null){
	 		session = getSession(credentials.getValue(USERNAME),credentials.getValue(PASSWORD),credentials.getValue(HOST_NAME),
	 				credentials.getValue(KEY_FILE),credentials.getValue(PASSPHRASE),Integer.parseInt(credentials.getValue(PORT)));
	 	}
	 	else{
	 		session = getSession(credentials.getValue(USERNAME),credentials.getValue(PASSWORD),credentials.getValue(HOST_NAME),
	 				Integer.parseInt(credentials.getValue(PORT)));
	 	}
	 	//at this point we have opened a session with the SSH client 
	    Channel channel=session.openChannel("shell");
	    //ChannelShell shell = session.openChannel("shell");
	    
	      
     OutputStream ops = channel.getOutputStream();
     PrintStream ps = new PrintStream(ops, true);

      channel.connect();
      System.out.println("Is channel open: " + channel.isClosed());  
	  System.out.println("Is channel connected: " + channel.isConnected());  
      
      //ps.println("sudo su - "+sudo); 
      for(String command:commandsList){
     	 ps.println(command);
      }
      ps.println("exit");
      ps.println("exit");
      //give commands to be executed inside println.and can have any no of commands sent.
      ps.close();

      InputStream in=channel.getInputStream();
      byte[] bt=new byte[1024];
      String str="";

      while(true)
      {

      while(in.available()>0)
      {
      int i=in.read(bt, 0, 1024);
      if(i<0)
       break;
         str=new String(bt, 0, i);
       //displays the output of the command executed.
         
         result.append(str);


      }
      if(channel.isClosed())
      {

          break;
     }
      //Thread.sleep(5000);
      if(str.contains("logout")){
      channel.disconnect();
      session.disconnect();   
      }
      }
	    return result.toString();
 }
 
 public static String executeRemoteCommand(String username, String password, String hostname, List<String> commands) throws Exception {
	 StringBuffer result = new StringBuffer();
	 	Session session = getSession(username,password, hostname);
	    Channel channel=session.openChannel("shell");
     OutputStream ops = channel.getOutputStream();
     PrintStream ps = new PrintStream(ops, true);

      channel.connect();
      //ps.println("sudo su - "+sudo); 
      for(String command:commands){
     	 ps.println(command);
      }
      ps.println("exit");
      ps.println("exit");
      //give commands to be executed inside println.and can have any no of commands sent.
      ps.close();

      InputStream in=channel.getInputStream();
      byte[] bt=new byte[1024];
      String str="";

      while(true)
      {

      while(in.available()>0)
      {
      int i=in.read(bt, 0, 1024);
      if(i<0)
       break;
         str=new String(bt, 0, i);
       //displays the output of the command executed.
         
         result.append(str);


      }
      if(channel.isClosed())
      {

          break;
     }
      //Thread.sleep(5000);
      if(str.contains("logout")){
      channel.disconnect();
      session.disconnect();   
      }
      }
	    return result.toString(); 
	 
	 
 }
	 	
 public static ArrayList<String> executeFile(String USERNAME, String PASSWORD, String host, String command)
 {
     ArrayList<String> result = new ArrayList<String>();
     try
     {

    	 Session session = getSession(USERNAME,PASSWORD, host);

         //create the excution channel over the session
         ChannelExec channelExec = (ChannelExec)session.openChannel("exec");

         // Gets an InputStream for this channel. All data arriving in as messages from the remote side can be read from this stream.
         InputStream in = channelExec.getInputStream();

         // Set the command that you want to execute
         // In our case its the remote shell script
         //channelExec.setCommand("sh "+scriptFileName);
           channelExec.setCommand(command);
         // Execute the command
           channelExec.connect();

         // Read the output from the input stream we set above
         BufferedReader reader = new BufferedReader(new InputStreamReader(in));
         String line;
         
         //Read each line from the buffered reader and add it to result list
         // You can also simple print the result here 
         while ((line = reader.readLine()) != null)
         {
             result.add(line);
         }

         //retrieve the exit status of the remote command corresponding to this channel
         int exitStatus = channelExec.getExitStatus();

         //Safely disconnect channel and disconnect session. If not done then it may cause resource leak
         channelExec.disconnect();
         session.disconnect();

         if(exitStatus < 0){
            // System.out.println("Done, but exit status not set!");
         }
         else if(exitStatus > 0){
            // System.out.println("Done, but with error!");
         }
         else{
            // System.out.println("Done!");
         }

     }
     catch(Exception e)
     {
         System.err.println("Error: " + e);
     }
     return result;
 }
 public static int authenticateShell(String USERNAME, String PASSWORD, String host, String command)
 {
     ArrayList<String> result = new ArrayList<String>();
     try
     {

    	 Session session = getSession(USERNAME,PASSWORD, host);

         //create the excution channel over the session
         ChannelExec channelExec = (ChannelExec)session.openChannel("exec");

         // Gets an InputStream for this channel. All data arriving in as messages from the remote side can be read from this stream.
         InputStream in = channelExec.getInputStream();

         // Set the command that you want to execute
         // In our case its the remote shell script
         //channelExec.setCommand("sh "+scriptFileName);
           channelExec.setCommand(command);
         // Execute the command
           channelExec.connect();

         // Read the output from the input stream we set above
         BufferedReader reader = new BufferedReader(new InputStreamReader(in));
         String line;
         
         //Read each line from the buffered reader and add it to result list
         // You can also simple print the result here 
         while ((line = reader.readLine()) != null)
         {
             result.add(line);
         }

         //retrieve the exit status of the remote command corresponding to this channel
         int exitStatus = channelExec.getExitStatus();

         //Safely disconnect channel and disconnect session. If not done then it may cause resource leak
         channelExec.disconnect();
         session.disconnect();

         if(exitStatus < 0){
            // System.out.println("Done, but exit status not set!");
         }
         else if(exitStatus > 0){
            // System.out.println("Done, but with error!");
         }
         else{
            // System.out.println("Done!");
         }

     }
     catch(Exception e)
     {
    	 e.printStackTrace();
         return 0;
     }
     return 1;
 }
 public static String downloadFile(String SFTPUSER, String SFTPPASS, String SFTPHOST, String SFTPWORKINGDIR, String SFTPFILE, boolean isText) throws IOException, JSchException, SftpException{
	 
	 Channel     channel     = null;
	 ChannelSftp channelSftp = null;
	 
	 
	 Session session = getSession(SFTPUSER,SFTPPASS, SFTPHOST);
	 channel = session.openChannel("sftp");
	 channel.connect();
	 channelSftp = (ChannelSftp)channel;
	 channelSftp.cd(SFTPWORKINGDIR);
	 byte[] buffer = new byte[1024];
	 BufferedInputStream bis = new BufferedInputStream(channelSftp.get(SFTPFILE));
	 
	 int numRead;
	    
	 ByteArrayOutputStream outString = new ByteArrayOutputStream();
	 try{
	   while ((numRead = bis.read(buffer)) != -1) {
	       outString.write(buffer, 0, numRead);
	   }
	   } finally {
	        bis.close();
	   }
	   
	 
	 String content=new String(outString.toByteArray());
	 if(isText){
		 content = content.replaceAll("\n", "\r\n");
	 }
//	 File newFile = new File(LOCALFILE);
//	 OutputStream os = new FileOutputStream(newFile);
//	 BufferedOutputStream bos = new BufferedOutputStream(os);
//	 int readCount;
//	 //System.out.println("Getting: " + theLine);
//	 while( (readCount = bis.read(buffer)) > 0) {
//	 bos.write(buffer, 0, readCount);
//	 System.out.println("Writing");
//	 }
//	 System.out.println("Finished");
	 bis.close();
//	 bos.close();
	 channel.disconnect();
	 session.disconnect();
	 return content;
	  
 }
 
 public static byte[] downloadBinaryFile(String SFTPUSER, String SFTPPASS, String SFTPHOST, String SFTPWORKINGDIR, String SFTPFILE) throws IOException, JSchException, SftpException{
	 
	 Channel     channel     = null;
	 ChannelSftp channelSftp = null;
	 Session session = getSession(SFTPUSER,SFTPPASS, SFTPHOST);
	 channel = session.openChannel("sftp");
	 channel.connect();
	 channelSftp = (ChannelSftp)channel;
	 channelSftp.cd(SFTPWORKINGDIR);
	 byte[] buffer = new byte[1024];
	 BufferedInputStream bis = new BufferedInputStream(channelSftp.get(SFTPFILE));
	 
	 int numRead;
	    
	 ByteArrayOutputStream outString = new ByteArrayOutputStream();
	 try{
	   while ((numRead = bis.read(buffer)) != -1) {
	       outString.write(buffer, 0, numRead);
	   }
	   } finally {
	        bis.close();
	   }
	   
	 
	 byte[] content= outString.toByteArray();
	 
//	 File newFile = new File(LOCALFILE);
//	 OutputStream os = new FileOutputStream(newFile);
//	 BufferedOutputStream bos = new BufferedOutputStream(os);
//	 int readCount;
//	 //System.out.println("Getting: " + theLine);
//	 while( (readCount = bis.read(buffer)) > 0) {
//	 bos.write(buffer, 0, readCount);
//	 System.out.println("Writing");
//	 }
//	 System.out.println("Finished");
	 bis.close();
//	 bos.close();
	 channel.disconnect();
	 session.disconnect();
	 return content;
	  
 }
 public static void uploadFile(String SFTPUSER, String SFTPHOST, String SFTPPASS, String SFTPWORKINGDIR, String FILETOTRANSFER, String FILENAME, boolean isText){
	 
	 Channel     channel     = null;
	 ChannelSftp channelSftp = null;
	 
	  
	 try{
		 		 Session session = getSession(SFTPUSER,SFTPPASS, SFTPHOST);
	             channel = session.openChannel("sftp");
	             channel.connect();
	             channelSftp = (ChannelSftp)channel;
	             channelSftp.cd(SFTPWORKINGDIR);
	             if(isText){
	            	 channelSftp.put(new ByteArrayInputStream((FILETOTRANSFER.replaceAll("\r\n", "\n")).getBytes()), FILENAME);
	             }else{
	            	 channelSftp.put(new ByteArrayInputStream(FILETOTRANSFER.getBytes()), FILENAME);
	             }
	             channel.disconnect();
	        	 session.disconnect();
	 }catch(Exception ex){
	 ex.printStackTrace();
	 }
 }
 
 public static void uploadBinaryFile(String SFTPUSER, String SFTPHOST, String SFTPPASS, String SFTPWORKINGDIR, byte[] FILETOTRANSFER, String FILENAME){
	 
	 Channel     channel     = null;
	 ChannelSftp channelSftp = null;
	 
	  
	 try{
		 		 Session session = getSession(SFTPUSER,SFTPPASS, SFTPHOST);
	             channel = session.openChannel("sftp");
	             channel.connect();
	             channelSftp = (ChannelSftp)channel;
	             channelSftp.cd(SFTPWORKINGDIR);
	             
	             channelSftp.put(new ByteArrayInputStream(FILETOTRANSFER), FILENAME);
	             channel.disconnect();
	        	 session.disconnect();
	 }catch(Exception ex){
	 ex.printStackTrace();
	 }
 }
 
 public static boolean executeScript(String username, String password, String hostName, String scriptName){
	ArrayList<String> commands = new ArrayList<String>();
	//commands.add("sudo su - bigadm");
	commands.add(scriptName);
	try {
		ShellExecuter.executeRemoteCommand(username, password, hostName, commands);
		return true;
	} catch (Exception e) {
		e.printStackTrace();
		return false;
	}
 }
 
}