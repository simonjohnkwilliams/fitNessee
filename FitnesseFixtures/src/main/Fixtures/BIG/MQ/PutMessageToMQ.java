package main.Fixtures.BIG.MQ;

import com.ibm.mq.MQException;
import com.rmg.TestManager.QueueManager;
import com.rmg.credentials.Credentials;
/**
 * 
 * @author siwillia
 * This class puts something onto an MQ and tells it the message queue it should be sent to. 
 * 
 */
public class PutMessageToMQ {

	private String configFile;
	private String error; 
	private byte [] messageId;
	private String queueName;
	private String localFilePath;
	
	private static final String HOST_NAME = "esbBatchHostName";
	private static final String CHANNEL = "channel";
	private static final String USER_NAME = "mqUsername";
	private static final String PORT = "mqPort";
	private static final String QM_NAME = "qmName";
	
	public String messageId(){
		return messageId.toString();
	}
	public byte[] getMessageId() {
		return messageId;
	}
	public void setMessageId(byte[] messageId) {
		this.messageId = messageId;
	}
	public String error(){
		return this.error;
	}
	public String getQueueName(){
		return queueName;
	}
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	public String getLocalFilePath() {
		return localFilePath;
	}
	public void setLocalFilePath(String localFilePath) {
		this.localFilePath = localFilePath;
	}
	public String getConfigFile() {
		return configFile;
	}
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
		
	
	public boolean isSuccess(){
		Credentials credentials = new Credentials(configFile);
		final QueueManager esbqueuemanager = new QueueManager(credentials.getValue(HOST_NAME), credentials.getValue(CHANNEL),
				credentials.getValue(USER_NAME), Integer.parseInt(credentials.getValue(PORT)), credentials.getValue(QM_NAME));
		try {
			messageId = esbqueuemanager.putMessageToMQ(queueName, localFilePath);
		} catch (MQException e) {
			error = e.getMessage();
		}
		if(messageId==null){
			return false;
		}
		return true;
	}
}
