package com.rmg.TestManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ibm.mq.MQC;
import com.ibm.mq.MQConnectionSecurityParameters;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.headers.CMQC;
import com.ibm.mq.headers.MQHeaderList;
import com.ibm.mq.headers.MQRFH2;

/**
 * @author gaurav.agarwal
 *
 */
public class QueueManager {
	private String hostName;
	private String channel;
	private String username;
	private int port;
	private String qmName;

	public QueueManager(String hostName, String channel, String username, int port, String qmName) {
		super();
		this.hostName = hostName;
		this.channel = channel;
		this.username = username;
		this.port = port;
		this.qmName = qmName;
		
		
	}
	public QueueManager(String hostName, String channel,  int port, String qmName) {
		super();
		this.hostName = hostName;
		this.channel = channel;
		this.port = port;
		this.qmName = qmName;
		
		
	}

	public byte[] putMessageToMQ(String queueName, String localFilePath)throws MQException{
		return this.putMessage(queueName, localFilePath);
	}
	
	private byte[] putMessage(String queueName, String localFilePath) throws MQException{
		// Set up MQ environment
		MQEnvironment.hostname = this.hostName;
		MQEnvironment.channel = this.channel;
		MQEnvironment.port = this.port;
		MQEnvironment.userID = this.username;
		FileInputStream is = null;
		MQQueueManager qMgr = null;
		MQQueue queue = null;
		try{
			qMgr = new MQQueueManager(qmName);
			int openOptions = MQC.MQOO_OUTPUT + MQC.MQOO_FAIL_IF_QUIESCING;
	
			queue = qMgr.accessQueue(queueName,openOptions); 
			MQMessage msg = new MQMessage();
			
			File file = new File(localFilePath);
			is = new FileInputStream(file);
			msg.write(IOUtils.toByteArray(is));
			
			MQPutMessageOptions pmo = new MQPutMessageOptions();
			queue.put(msg, pmo);
			return msg.messageId;
		}catch (MQException me){
			throw me;
			//return null;
		}catch (IOException io){
			return null;
		}finally{
			try {
				if(is!=null)
					is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(queue!=null)
					queue.close();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(qMgr!=null)
					qMgr.disconnect();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public boolean putMQMessage(String queueName, String localFilePath){
		// Set up MQ environment
		MQEnvironment.hostname = this.hostName;
		MQEnvironment.channel = this.channel;
		MQEnvironment.port = this.port;
		MQEnvironment.userID = this.username;
		FileInputStream is = null;
		MQQueueManager qMgr = null;
		MQQueue queue = null;
		try{
			qMgr = new MQQueueManager(qmName);
			int openOptions = MQC.MQOO_OUTPUT + MQC.MQOO_FAIL_IF_QUIESCING;
	
			queue = qMgr.accessQueue(queueName,openOptions); 
			MQMessage msg = new MQMessage();
			
			File file = new File(localFilePath);
			is = new FileInputStream(file);
			msg.write(IOUtils.toByteArray(is));
			
			MQPutMessageOptions pmo = new MQPutMessageOptions();
			queue.put(msg, pmo);
			return true;
		}catch (MQException me){
			return false;
		}catch (IOException io){
			return false;
		}finally{
			try {
				if(is!=null)
					is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(queue!=null)
					queue.close();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(qMgr!=null)
					qMgr.disconnect();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private byte[] putRFHMessage(String queueName, String localFilePath, String localRFHFilePath, String encoding, String ccsid) throws XPathExpressionException, TransformerException, ParserConfigurationException, SAXException{
		// Set up MQ environment
		MQEnvironment.hostname = this.hostName;
		MQEnvironment.channel = this.channel;
		MQEnvironment.port = this.port;
		MQEnvironment.userID = this.username;		
		FileInputStream is = null;
		MQQueueManager qMgr = null;
		MQQueue queue = null;
		try{
			qMgr = new MQQueueManager(qmName);
			int openOptions = MQC.MQOO_OUTPUT + MQC.MQOO_FAIL_IF_QUIESCING;
	
			queue = qMgr.accessQueue("RFH.GENERATE.IN",openOptions); 
			MQMessage msg = new MQMessage();
			msg.format = MQC.MQFMT_STRING;
			msg.feedback = MQC.MQFB_NONE;
			msg.messageType = MQC.MQMT_DATAGRAM;
			
			
			File file = new File(localFilePath);
			is = new FileInputStream(file);
			//Convert the input stream to string
			String msgString = IOUtils.toString(is, encoding);
			File fileRFH = new File(localRFHFilePath);
			is = new FileInputStream(fileRFH);
			//Convert the input stream to string
			String rfhString = IOUtils.toString(is, encoding);
			
			
			//Create the string message'
			String putMsg = "<Root>"+rfhString+"<InputQueue>"+queueName+"</InputQueue><CCSID>"+ccsid+"</CCSID><Message><![CDATA["+msgString+"]]></Message></Root>";
			
			msg.write(IOUtils.toByteArray(putMsg));
			
			MQPutMessageOptions pmo = new MQPutMessageOptions();
			queue.put(msg, pmo);
			return msg.messageId;
		}catch (MQException me){
			return null;
		}catch (IOException io){
			return null;
		}finally{
			try {
				if(is!=null)
					is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(queue!=null)
					queue.close();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(qMgr!=null)
					qMgr.disconnect();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public boolean putMQRFHMessage(String queueName, String localFilePath, String localRFHFilePath, String encoding, String ccsid) throws XPathExpressionException, TransformerException, ParserConfigurationException, SAXException{
		// Set up MQ environment
		MQEnvironment.hostname = this.hostName;
		MQEnvironment.channel = this.channel;
		MQEnvironment.port = this.port;
		MQEnvironment.userID = this.username;
		FileInputStream is = null;
		MQQueueManager qMgr = null;
		MQQueue queue = null;
		try{
			qMgr = new MQQueueManager(qmName);
			int openOptions = MQC.MQOO_OUTPUT + MQC.MQOO_FAIL_IF_QUIESCING;
	
			queue = qMgr.accessQueue("RFH.GENERATE.IN",openOptions); 
			MQMessage msg = new MQMessage();
			msg.format = MQC.MQFMT_STRING;
			msg.feedback = MQC.MQFB_NONE;
			msg.messageType = MQC.MQMT_DATAGRAM;
			
			
			File file = new File(localFilePath);
			is = new FileInputStream(file);
			//Convert the input stream to string
			String msgString = IOUtils.toString(is, encoding);
			File fileRFH = new File(localRFHFilePath);
			is = new FileInputStream(fileRFH);
			//Convert the input stream to string
			String rfhString = IOUtils.toString(is, encoding);
			
			
			//Create the string message'
			String putMsg = "<Root>"+rfhString+"<InputQueue>"+queueName+"</InputQueue><CCSID>"+ccsid+"</CCSID><Message><![CDATA["+msgString+"]]></Message></Root>";
			
			msg.write(IOUtils.toByteArray(putMsg));
			
			MQPutMessageOptions pmo = new MQPutMessageOptions();
			queue.put(msg, pmo);
			return true;
		}catch (MQException me){
			return false;
		}catch (IOException io){
			return false;
		}finally{
			try {
				if(is!=null)
					is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(queue!=null)
					queue.close();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(qMgr!=null)
					qMgr.disconnect();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private byte[] putMessage(String queueName, String replyToQ, String localFilePath){
		// Set up MQ environment
		MQEnvironment.hostname = this.hostName;
		MQEnvironment.channel = this.channel;
		MQEnvironment.port = this.port;
		MQEnvironment.userID = this.username;
		FileInputStream is = null;
		MQQueueManager qMgr = null;
		MQQueue queue = null;
		try{
			qMgr = new MQQueueManager(qmName);
			int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT | MQC.MQOO_FAIL_IF_QUIESCING;
	
			queue = qMgr.accessQueue(queueName,openOptions); 
			MQMessage msg = new MQMessage();
			msg.format = MQC.MQFMT_STRING;
			msg.feedback = MQC.MQFB_NONE;
			msg.messageType = MQC.MQMT_REQUEST;
			msg.replyToQueueName =replyToQ;
			msg.replyToQueueManagerName = qmName;
			File file = new File(localFilePath);
			is = new FileInputStream(file);
			msg.write(IOUtils.toByteArray(is));
			
			MQPutMessageOptions pmo = new MQPutMessageOptions();
			pmo.options = MQC.MQPMO_FAIL_IF_QUIESCING | MQC.MQGMO_NO_SYNCPOINT;
			queue.put(msg, pmo);
			is.close();
			queue.close();
			qMgr.disconnect();
			return msg.messageId;
		}catch (MQException me){
			return null;
		}catch (IOException io){
			return null;
		}finally{
			try {
				if(is!=null)
					is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(queue!=null)
					queue.close();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(qMgr!=null)
					qMgr.disconnect();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private byte[] putRFHMessage(String queueName, String replyToQ, String localFilePath, String localRFHFilePath, String encoding, String ccsid) throws XPathExpressionException, TransformerException, ParserConfigurationException, SAXException{
		// Set up MQ environment
		MQEnvironment.hostname = this.hostName;
		MQEnvironment.channel = this.channel;
		MQEnvironment.port = this.port;
		MQEnvironment.userID = this.username;
		FileInputStream is = null;
		MQQueueManager qMgr = null;
		MQQueue queue = null;
		try{
			qMgr = new MQQueueManager(qmName);
			int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT | MQC.MQOO_FAIL_IF_QUIESCING;
	
			queue = qMgr.accessQueue("RFH.GENERATE.IN",openOptions); 
			MQMessage msg = new MQMessage();
			msg.format = MQC.MQFMT_STRING;
			msg.feedback = MQC.MQFB_NONE;
			msg.messageType = MQC.MQMT_REQUEST;
			msg.replyToQueueName =replyToQ;
			msg.replyToQueueManagerName = qmName;
			File file = new File(localFilePath);
			is = new FileInputStream(file);
			//Convert the input stream to string
			String msgString = IOUtils.toString(is, encoding);
			File fileRFH = new File(localRFHFilePath);
			is = new FileInputStream(fileRFH);
			//Convert the input stream to string
			String rfhString = IOUtils.toString(is, encoding);
			
			
			//Create the string message'
			String putMsg = "<Root>"+rfhString+"<InputQueue>"+queueName+"</InputQueue><CCSID>"+ccsid+"</CCSID><Message><![CDATA["+msgString+"]]></Message></Root>";
			msg.write(IOUtils.toByteArray(putMsg));
			
			MQPutMessageOptions pmo = new MQPutMessageOptions();
			pmo.options = MQC.MQPMO_FAIL_IF_QUIESCING | MQC.MQGMO_NO_SYNCPOINT;
			queue.put(msg, pmo);
			is.close();
			queue.close();
			qMgr.disconnect();
			return msg.messageId;
		}catch (MQException me){
			return null;
		}catch (IOException io){
			return null;
		}finally{
			try {
				if(is!=null)
					is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(queue!=null)
					queue.close();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(qMgr!=null)
					qMgr.disconnect();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	@SuppressWarnings("deprecation")
	public boolean getMessageUsingMsgId(String queueName, byte[] messageId, String localFilePath){
		// Set up MQ environment
		MQEnvironment.hostname = this.hostName;
		MQEnvironment.channel = this.channel;
		MQEnvironment.port = this.port;
		MQEnvironment.userID = this.username;
		FileOutputStream os =null;
		MQQueue queue = null;
		MQQueueManager qMgr =null;
		try{
			qMgr = new MQQueueManager(qmName);
			int openOptions = MQC.MQOO_INQUIRE + MQC.MQOO_FAIL_IF_QUIESCING + MQC.MQOO_INPUT_SHARED;;
	
			queue = qMgr.accessQueue(queueName,openOptions); 
			
			MQMessage retrievedMessage = new MQMessage();
			retrievedMessage.messageId = messageId;
	
			MQGetMessageOptions gmo = new MQGetMessageOptions();
			gmo.options = MQC.MQGMO_WAIT;
			queue.get(retrievedMessage, gmo);
			File file = new File(localFilePath);
			os = new FileOutputStream(file);
			os.write(IOUtils.toByteArray(retrievedMessage.readString(retrievedMessage.getMessageLength())));
			os.close();
			queue.close();
			qMgr.disconnect();
			return true;
		}catch (MQException me){
			return false;
		}catch (IOException io){
			return false;
		}finally{
			
			try {
				if(os!=null)
					os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(queue!=null)
					queue.close();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(qMgr!=null)
					qMgr.disconnect();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static String getStringBetween(String str, String startDelim, String endDelim){
		if(startDelim.equals("<![CDATA[")){
			String result = str.substring(str.indexOf(startDelim) + 9, str.indexOf(endDelim));
			return result;
		}else if(startDelim.equals("<RFH>")){
			String result = str.substring(str.indexOf(startDelim), str.indexOf(endDelim))+"</RFH>";
			return result;
		}else{
			String result = str.substring(str.indexOf(startDelim)+ startDelim.length(), str.indexOf(endDelim));
			return result;
		}
	}
	
	@SuppressWarnings("deprecation")
	public boolean getRFHMessageUsingMsgId(String queueName, byte[] messageId, String localFilePath, String localRFHFilePath) throws InterruptedException{
		// Set up MQ environment
		MQEnvironment.hostname = this.hostName;
		MQEnvironment.channel = this.channel;
		MQEnvironment.port = this.port;
		MQEnvironment.userID = this.username;
		FileOutputStream os =null;
		MQQueue queue = null;
		MQQueueManager qMgr =null;
		try{
			qMgr = new MQQueueManager(qmName);
			int openOptions = MQC.MQOO_INQUIRE + MQC.MQOO_FAIL_IF_QUIESCING + MQC.MQOO_INPUT_SHARED;;
	
			queue = qMgr.accessQueue(queueName,openOptions); 
			
			MQMessage retrievedMessage = new MQMessage();
			retrievedMessage.messageId = messageId;
	
			MQGetMessageOptions gmo = new MQGetMessageOptions();
			gmo.options = MQC.MQGMO_WAIT;
			queue.get(retrievedMessage, gmo);
			queue.close();
			//Put the message into another queue
			
			openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT | MQC.MQOO_FAIL_IF_QUIESCING;
			queue = qMgr.accessQueue("STR.GENERATE.IN",openOptions); 
			
			MQPutMessageOptions pmo = new MQPutMessageOptions();
			pmo.options = MQC.MQPMO_FAIL_IF_QUIESCING | MQC.MQGMO_NO_SYNCPOINT;
			queue.put(retrievedMessage, pmo);
			queue.close();
			Thread.sleep(5000);
			//Get the formatted message out
			openOptions = MQC.MQOO_INQUIRE + MQC.MQOO_FAIL_IF_QUIESCING + MQC.MQOO_INPUT_SHARED;;
			
			queue = qMgr.accessQueue("STR.GENERATE.OUT",openOptions); 
			
			retrievedMessage = new MQMessage();
			retrievedMessage.messageId = messageId;
	
			gmo = new MQGetMessageOptions();
			gmo.options = MQC.MQGMO_WAIT;
			queue.get(retrievedMessage, gmo);
			
			String retrievedString = retrievedMessage.readString(retrievedMessage.getMessageLength());
			String body = getStringBetween(retrievedString, "<![CDATA[", "]]>");
			String rfh = getStringBetween(retrievedString, "<RFH>", "</RFH>");
			
			File file = new File(localFilePath);
			os = new FileOutputStream(file);
			os.write(IOUtils.toByteArray(body));
			os.close();
			
			file = new File(localRFHFilePath);
			os = new FileOutputStream(file);
			os.write(IOUtils.toByteArray(rfh));
			os.close();
			
			queue.close();
			qMgr.disconnect();
			return true;
		}catch (MQException me){
			return false;
		}catch (IOException io){
			return false;
		}finally{
			
			try {
				if(os!=null)
					os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(queue!=null)
					queue.close();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(qMgr!=null)
					qMgr.disconnect();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public boolean getMessageUsingCorrelId(String queueName, byte[] correlationId, String localFilePath){
		// Set up MQ environment
		MQEnvironment.hostname = this.hostName;
		MQEnvironment.channel = this.channel;
		MQEnvironment.port = this.port;
		MQEnvironment.userID = this.username;
		FileOutputStream os =null;
		MQQueue queue = null;
		MQQueueManager qMgr =null;
		try{
			qMgr = new MQQueueManager(qmName);
			int openOptions = MQC.MQOO_INQUIRE + MQC.MQOO_FAIL_IF_QUIESCING + MQC.MQOO_INPUT_SHARED;;
	
			queue = qMgr.accessQueue(queueName,openOptions); 
			
			MQMessage retrievedMessage = new MQMessage();
			retrievedMessage.correlationId = correlationId;
	
			MQGetMessageOptions gmo = new MQGetMessageOptions();
			gmo.options = MQC.MQGMO_WAIT;
			queue.get(retrievedMessage, gmo);
			File file = new File(localFilePath);
			os = new FileOutputStream(file);
			os.write(IOUtils.toByteArray(retrievedMessage.readString(retrievedMessage.getMessageLength())));
			os.close();
			queue.close();
			qMgr.disconnect();
			return true;
		}catch (MQException me){
			return false;
		}catch (IOException io){
			return false;
		}finally{
			
			try {
				if(os!=null)
					os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(queue!=null)
					queue.close();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(qMgr!=null)
					qMgr.disconnect();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("deprecation")
	public boolean getRFHMessageUsingCorrelId(String queueName, byte[] correlationId, String localFilePath, String localRFHFilePath) throws InterruptedException{
		// Set up MQ environment
		MQEnvironment.hostname = this.hostName;
		MQEnvironment.channel = this.channel;
		MQEnvironment.port = this.port;
		MQEnvironment.userID = this.username;
		FileOutputStream os =null;
		MQQueue queue = null;
		MQQueueManager qMgr =null;
		try{
			qMgr = new MQQueueManager(qmName);
			int openOptions = MQC.MQOO_INQUIRE + MQC.MQOO_FAIL_IF_QUIESCING + MQC.MQOO_INPUT_SHARED;;
	
			queue = qMgr.accessQueue(queueName,openOptions); 
			
			MQMessage retrievedMessage = new MQMessage();
			retrievedMessage.correlationId = correlationId;
	
			MQGetMessageOptions gmo = new MQGetMessageOptions();
			gmo.options = MQC.MQGMO_WAIT;
			queue.get(retrievedMessage, gmo);
			
			queue.close();
			//Put the message into another queue
			
			openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT | MQC.MQOO_FAIL_IF_QUIESCING;
			queue = qMgr.accessQueue("STR.GENERATE.IN",openOptions); 
			
			MQPutMessageOptions pmo = new MQPutMessageOptions();
			pmo.options = MQC.MQPMO_FAIL_IF_QUIESCING | MQC.MQGMO_NO_SYNCPOINT;
			queue.put(retrievedMessage, pmo);
			queue.close();
			
			Thread.sleep(5000);
			
			//Get the formatted message out
			openOptions = MQC.MQOO_INQUIRE + MQC.MQOO_FAIL_IF_QUIESCING + MQC.MQOO_INPUT_SHARED;
			
			queue = qMgr.accessQueue("STR.GENERATE.OUT",openOptions);
			
			retrievedMessage = new MQMessage();
			retrievedMessage.correlationId = correlationId;
	
			gmo = new MQGetMessageOptions();
			gmo.options = MQC.MQGMO_WAIT;
			queue.get(retrievedMessage, gmo);
			String retrievedString = retrievedMessage.readString(retrievedMessage.getMessageLength());
			String body = getStringBetween(retrievedString, "<![CDATA[", "]]>");
			String rfh = getStringBetween(retrievedString, "<RFH>", "</RFH>");
			
			File file = new File(localFilePath);
			os = new FileOutputStream(file);
			os.write(IOUtils.toByteArray(body));
			os.close();
			
			file = new File(localRFHFilePath);
			os = new FileOutputStream(file);
			os.write(IOUtils.toByteArray(rfh));
			os.close();
			
			queue.close();
			qMgr.disconnect();
			return true;
		}catch (MQException me){
			return false;
		}catch (IOException io){
			return false;
		}finally{
			
			try {
				if(os!=null)
					os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(queue!=null)
					queue.close();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(qMgr!=null)
					qMgr.disconnect();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("deprecation")
	public boolean getMQMessage(String queueName, String localFilePath, int waitInterval){
		// Set up MQ environment
		MQEnvironment.hostname = this.hostName;
		MQEnvironment.channel = this.channel;
		MQEnvironment.port = this.port;
		MQEnvironment.userID = this.username;
		FileOutputStream os =null;
		MQQueue queue = null;
		MQQueueManager qMgr =null;
		try{
			qMgr = new MQQueueManager(qmName);
			int openOptions = MQC.MQOO_FAIL_IF_QUIESCING | MQC.MQOO_INPUT_SHARED | MQC.MQOO_BROWSE;  

			queue = qMgr.accessQueue(queueName, openOptions);

			MQMessage retrievedMessage    = new MQMessage();
			MQGetMessageOptions gmo = new MQGetMessageOptions();
			gmo.options = MQC.MQGMO_WAIT | MQC.MQGMO_BROWSE_FIRST;
			gmo.matchOptions=MQC.MQMO_NONE;
			gmo.waitInterval=waitInterval;

			//read the message          
			queue.get(retrievedMessage,gmo); 
			gmo.options=CMQC.MQGMO_MSG_UNDER_CURSOR;
			queue.get(retrievedMessage, gmo);
			File file = new File(localFilePath);
			os = new FileOutputStream(file);
			os.write(IOUtils.toByteArray(retrievedMessage.readString(retrievedMessage.getMessageLength())));
			os.close();
			
			queue.close();
			qMgr.disconnect();
			return true;
		}catch (MQException me){
			return false;
		}catch (IOException io){
			return false;
		}finally{
			
			try {
				if(os!=null)
					os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(queue!=null)
					queue.close();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(qMgr!=null)
					qMgr.disconnect();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("deprecation")
	public boolean getMQRFHMessage(String queueName, byte[] correlationId, String localFilePath, String localRFHFilePath, int waitInterval) throws InterruptedException{
		// Set up MQ environment
		MQEnvironment.hostname = this.hostName;
		MQEnvironment.channel = this.channel;
		MQEnvironment.port = this.port;
		MQEnvironment.userID = this.username;
		FileOutputStream os =null;
		MQQueue queue = null;
		MQQueueManager qMgr =null;
		try{
			qMgr = new MQQueueManager(qmName);
			int openOptions = MQC.MQOO_FAIL_IF_QUIESCING | MQC.MQOO_INPUT_SHARED | MQC.MQOO_BROWSE;  

			queue = qMgr.accessQueue(queueName, openOptions);

			MQMessage retrievedMessage    = new MQMessage();
			MQGetMessageOptions gmo = new MQGetMessageOptions();
			gmo.options = MQC.MQGMO_WAIT | MQC.MQGMO_BROWSE_FIRST;
			gmo.matchOptions=MQC.MQMO_NONE;
			gmo.waitInterval=waitInterval;

			//read the message          
			queue.get(retrievedMessage,gmo);
			gmo.options=CMQC.MQGMO_MSG_UNDER_CURSOR;
			queue.get(retrievedMessage, gmo);
			queue.close();
			//Put the message into another queue
			
			openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT | MQC.MQOO_FAIL_IF_QUIESCING;
			queue = qMgr.accessQueue("STR.GENERATE.IN",openOptions); 
			
			MQPutMessageOptions pmo = new MQPutMessageOptions();
			pmo.options = MQC.MQPMO_FAIL_IF_QUIESCING | MQC.MQGMO_NO_SYNCPOINT;
			queue.put(retrievedMessage, pmo);
			queue.close();
			
			Thread.sleep(5000);
			
			//Get the formatted message out
			openOptions = MQC.MQOO_INQUIRE + MQC.MQOO_FAIL_IF_QUIESCING + MQC.MQOO_INPUT_SHARED;
			
			queue = qMgr.accessQueue("STR.GENERATE.OUT",openOptions);
			
			retrievedMessage = new MQMessage();
			retrievedMessage.correlationId = correlationId;
	
			gmo = new MQGetMessageOptions();
			gmo.options = MQC.MQGMO_WAIT;
			queue.get(retrievedMessage, gmo);
			String retrievedString = retrievedMessage.readString(retrievedMessage.getMessageLength());
			String body = getStringBetween(retrievedString, "<![CDATA[", "]]>");
			String rfh = getStringBetween(retrievedString, "<RFH>", "</RFH>");
			
			File file = new File(localFilePath);
			os = new FileOutputStream(file);
			os.write(IOUtils.toByteArray(body));
			os.close();
			
			file = new File(localRFHFilePath);
			os = new FileOutputStream(file);
			os.write(IOUtils.toByteArray(rfh));
			os.close();
			
			queue.close();
			qMgr.disconnect();
			return true;
		}catch (MQException me){
			return false;
		}catch (IOException io){
			return false;
		}finally{
			
			try {
				if(os!=null)
					os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(queue!=null)
					queue.close();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(qMgr!=null)
					qMgr.disconnect();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	//really we should split this up so that we have a put and a get seperately. 
	public boolean runMQDatagram(String putQueueName, String inputLocalFilePath, String getQueueName, String outputLocalFilePath, long waitTime) throws InterruptedException{
		byte[] messageId;
		try {
			messageId = this.putMessage(putQueueName, inputLocalFilePath);
			if(messageId==null){
				return false;
			}
			//System.out.println(convertByteToHexString(messageId));
			Thread.sleep(waitTime);
			return this.getMessageUsingMsgId(getQueueName, messageId, outputLocalFilePath);
		} catch (MQException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	

	
	public boolean runMQDatagram(String putQueueName, String inputLocalFilePath, String[] getQueueNames, String[] outputLocalFilePath, long waitTime) throws InterruptedException{
		byte[] messageId;
		try {
			messageId = this.putMessage(putQueueName, inputLocalFilePath);
			if(messageId==null){
				return false;
			}
			//System.out.println(convertByteToHexString(messageId));
			Thread.sleep(waitTime);
			boolean returnFlag = true;
			int i = 0;
			for(String getQueueName:getQueueNames){
				returnFlag =  this.getMessageUsingMsgId(getQueueName, messageId, outputLocalFilePath[i]);
				if(returnFlag == false){
					break;
				}
				i++;
			}
			return returnFlag;
		} catch (MQException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private String convertByteToHexString(byte[] input){
		StringBuffer result = new StringBuffer();
		for(byte b : input){
			result.append(String.format("%02X ", b));
		}
		return result.toString();
	}
	
	public boolean runMQDatagram(String putQueueName, String inputLocalFilePath, String inputLocalRFHFilePath, String encoding, String ccsid, String getQueueNames[], String[] outputLocalFilePath, String[] outputLocalRFHFilePath, long waitTime) throws InterruptedException, XPathExpressionException, TransformerException, ParserConfigurationException, SAXException{
		byte[] messageId = this.putRFHMessage(putQueueName, inputLocalFilePath, inputLocalRFHFilePath, encoding, ccsid);
		if(messageId==null){
			return false;
		}
		Thread.sleep(waitTime);
		boolean returnFlag = true;
		int i = 0;
		for(String getQueueName:getQueueNames){
			returnFlag = this.getRFHMessageUsingMsgId(getQueueName, messageId, outputLocalFilePath[i], outputLocalRFHFilePath[i]);
			if(returnFlag == false){
				break;
			}
			i++;
		}
		return returnFlag;	
	}
	
	public boolean runMQDatagram(String putQueueName, String inputLocalFilePath, String inputLocalRFHFilePath, String encoding, String ccsid, String getQueueName, String outputLocalFilePath, String outputLocalRFHFilePath, long waitTime) throws InterruptedException, XPathExpressionException, TransformerException, ParserConfigurationException, SAXException{
		byte[] messageId = this.putRFHMessage(putQueueName, inputLocalFilePath, inputLocalRFHFilePath, encoding, ccsid);
		if(messageId==null){
			return false;
		}
		Thread.sleep(waitTime);
		return this.getRFHMessageUsingMsgId(getQueueName, messageId, outputLocalFilePath, outputLocalRFHFilePath);
	}
	
	public boolean runMQRequest(String putQueueName, String inputLocalFilePath, String replyToQName, String outputLocalFilePath, long waitTime) throws InterruptedException{
		byte[] messageId = this.putMessage(putQueueName, replyToQName, inputLocalFilePath);
		if(messageId==null){
			return false;
		}
		Thread.sleep(waitTime);
		return this.getMessageUsingCorrelId(replyToQName, messageId, outputLocalFilePath);
	}
	
	public boolean runMQRequest(String putQueueName, String inputLocalFilePath, String inputLocalRFHFilePath, String encoding, String ccsid, String replyToQName, String outputLocalFilePath, String outputLocalRFHFilePath, long waitTime) throws InterruptedException, XPathExpressionException, TransformerException, ParserConfigurationException, SAXException{
		byte[] messageId = this.putRFHMessage(putQueueName, replyToQName, inputLocalFilePath, inputLocalRFHFilePath, encoding, ccsid);
		if(messageId==null){
			return false;
		}
		Thread.sleep(waitTime);
		return this.getRFHMessageUsingCorrelId(replyToQName, messageId, outputLocalFilePath, outputLocalRFHFilePath);
	}
	
	public ArrayList<String> getAllProperties(String filepath) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{
		ArrayList<String> properties = new ArrayList<String>();
		File file = new File(filepath);
		XPath xPath =  XPathFactory.newInstance().newXPath();
		String expression = "//*[not(*)]";
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document document = builder.parse(file);
		document.getDocumentElement().normalize();
		NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
		for(int i = 0 ; i < nodeList.getLength(); i++) {
		        String xpathStr = (getXPath(nodeList.item(i)));
		        xpathStr.replace("#document/RFH/", "");
		        properties.add(xpathStr);
		}
		return properties;
		
	}
	
	private Document getChildDoc(Node node) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document newDocument = builder.newDocument();
		Node importedNode = newDocument.importNode(node, true);
		newDocument.appendChild(importedNode);
		return newDocument;
		
	}
	
	private String getXPath(Node node) {
	    Node parent = node.getParentNode();
	    if (parent == null) {
	    	return node.getNodeName();
	    }
	    if(node.getChildNodes().getLength()==1){
	    	return getXPath(parent) + "/" + node.getNodeName() + "=" + (node.getTextContent());
	    }else{
	    	return getXPath(parent) + "/" + node.getNodeName();
	    }
	}
	
	
	
	private String convertDocumentToString(Document doc) throws TransformerException{
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(doc), new StreamResult(writer));
		String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
		return output;
	}
	
	public String[] getPropertiesArray(String filePath) throws XPathExpressionException, TransformerException, ParserConfigurationException, SAXException, IOException{
		File file = new File(filePath);
		ArrayList<String> propList = new ArrayList<String>();
		XPath xPath =  XPathFactory.newInstance().newXPath();
		String expression = "/RFH/*";
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document document = builder.parse(file);
		document.getDocumentElement().normalize();
		NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
		for(int i=0; i<nodeList.getLength();i++){
			propList.add(convertDocumentToString(getChildDoc(nodeList.item(i))));
		}
		String[] propArr = new String[propList.size()];
		propArr = propList.toArray(propArr);
		return propArr;
	}

}
