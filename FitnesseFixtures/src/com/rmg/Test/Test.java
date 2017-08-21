package com.rmg.Test;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import com.rmg.TestManager.CSManager;
import com.rmg.TestManager.FileManager;
import com.rmg.TestManager.HTTPManager;
import com.rmg.TestManager.QueueManager;
import com.rmg.TestManager.ShellExecuter;
import com.rmg.Utils.Comparator;

/**
 * @author gaurav.agarwal
 *
 */
public class Test {
	private static String fmsHostName, username, password, ftgHostName, esbBatchHostName,mqUsername,gatewayQMName,channel,esbQMName,gatewayQMHostName, csEndpoint,csUserName, csPassword;
	private static int esbBatchPort,gatewayQMPort;
	private static FileManager fmsmanager, ftgmanager;
	private static QueueManager esbqueuemanager, gatewayqueuemanager;
	private static HTTPManager httpmanager;
	private static Comparator comparator;
	private static CSManager csmanager;
	public static void main(String args[]) throws Exception{
		init("ST");
		//we should re-write each of these tests as a fitnesse test. Meaning that we can run these on the server. 
		//this original test puts a file on the input/ dir and 
		//assertEquals("TC1 - Put File on FMS",fmsmanager.putFile("input/shield_psp_id_f_2000-00-00.csv","shield_psp_id_f_2000-00-00.csv", "/var/opt/transfers/I200375/pub/741/in", 22, "SFTP",false),true);
		//
		//this is complete. 
		//assertEquals("TC2 - Execute Move Script",ShellExecuter.executeScript(username, password, fmsHostName, "/opt/big/bin/I200375-741-E000945_move.sh"),true);
		//assertEquals("TC3 - Get File from FMS",fmsmanager.getFile("output/shield_psp_id_f_2000-00-00_move.csv","shield_psp_id_f_2000-00-00.csv", "/var/opt/transfers/E000945/pub/741/out", 22, "SFTP"),true);
		//we need to build this out. -just the execute scripts.
		assertEquals("TC4 - Execute Push Script",ShellExecuter.executeScript(username, password, fmsHostName, "/opt/big/bin/I200375-741-E000945_sftp_push_to_ftg.sh"),true);
		//we can do this. 
		assertEquals("TC5 - Get file from FTG",ftgmanager.getFile("output/shield_psp_id_f_2000-00-00_ftg.csv","shield_psp_id_f_2000-00-00.csv", "/var/opt/transfers/E000945/pub/741/out", 22, "SFTP"),true);
		
		//we have matched off the build of these elements locally and tested them against the Vagrant box. The next step should be how do we test what comes back from the
		//tests and what do the tests hope to achieve. Is there any pattern matching and what is the output. 
		
		//The next step would be to run the MQ tests and make sure that we are happy with these then enter them onto the box in the format that we have. 
		
		assertEquals("TC6 - Run MQ Datagram PSM", esbqueuemanager.runMQDatagram("PSM.SRC.X3.MPER.IN","input/ptp.xml", "ER.TGT.X3.STD.OUT", "output/ptp.xml", 10000), true);
		assertEquals("TC7 - Run MQ Request", esbqueuemanager.runMQRequest("MQREQUEST.IN", "input/ptp.xml", "MQREPLY", "output/ptp_reply.xml", 10000), true);
		assertEquals("TC8 - Run valid HTTPS request", httpmanager.runHTTPSXMLRequest("https://10.106.107.1/CDSAPIService", "input/CDSRequest.xml", "output/CDSResponse.xml", 443, "cert/TESTCA2.jks", "CAPRMGb1g$"), true);
		assertEquals("TC9 - Run invalid HTTPS request", httpmanager.runHTTPSXMLRequest("https://10.106.107.1/CDSAPIService", "input/CDSRequest_schema.xml", "output/CDSResponse_schema.xml", 443, "cert/TESTCA2.jks", "CAPRMGb1g$"), true);
		assertEquals("TC10 - Compare XMLS", comparator.compareXMLS("input/CDSRequest.xml", "input/CDSRequest2.xml"), true);
		assertEquals("TC11 - Check XPath Values", comparator.checkXPathValue("output/CDSResponse.xml", "/*[local-name()='Envelope']/*[local-name()='Body']/*[local-name()='CDSGetRestrictionProhibitionByHSResponse']/*[local-name()='GetRestrictionProhibitionByHSResponse']/*[local-name()='GetRestrictionProhibitionByHSResult']/*[local-name()='RestrictionProhibition']/*[local-name()='ToHSCode']", "999999"), true);
		assertEquals("TC12 - Count Number of Sibings", comparator.countNumberOfSiblings("output/CDSResponse2.xml", "/*[local-name()='Envelope']/*[local-name()='Body']/*[local-name()='CDSGetRestrictionProhibitionByHSResponse']/*[local-name()='GetRestrictionProhibitionByHSResponse']/*[local-name()='GetRestrictionProhibitionByHSResult']/*[local-name()='RestrictionProhibition']/*[local-name()='HSCodeList']", 2), true);
		assertEquals("TC13 - Run MQ Datagram PSM RFH", esbqueuemanager.runMQDatagram("PSM.SRC.X3.MPER.IN","input/ptp.xml", "input/ptp_rfh.xml", "ISO8859_1" ,"1208","ER.TGT.X3.PTY.OUT", "output/ptp.xml", "output/ptp_rfh.xml", 10000), true);
		assertEquals("TC14 - Run MQ Request RFH", esbqueuemanager.runMQRequest("MQREQUEST.IN","input/ptp.xml", "input/ptp_rfh.xml", "ISO8859_1" ,"1208","MQREPLY", "output/ptp_reply.xml", "output/ptp_rfh_reply.xml", 10000), true);
		assertEquals("TC15 - Run HTTPS Request with custom Headers", httpmanager.runHTTPSRequest("input/CDSRequest.xml", "input/Header.txt", "output/CDSResponseCust.xml", "https://10.106.107.1/CDSAPIService", 443, "cert/TESTCA2.jks", "CAPRMGb1g$"), true);
		assertEquals("TC16 - Parse EDIFACT", comparator.parseMessage("input/MEA.txt","input/edifact_parser_props.xml", "output/MEA.xml", esbqueuemanager), true);
		assertEquals("TC17 - Browse Audit Logs",csmanager.browseAuditLogs("W000006-729-I200552", "SELECT * FROM AUDIT_LOG WHERE INTERFACE_NAME='W000006-729-I200552'", "TC17", "output","xml"),true);
		assertEquals("TC18 - Run MQ Datagram in Gateway",gatewayqueuemanager.runMQDatagram("FEP.EPS.GW.PUT", "input/FEB_EPS_Input.txt", "EPS.TGT.INT.GW_L", "output/FEB_EPS_Output.txt", 10000), true);
		assertEquals("TC19 - Run valid HTTPS Get request", httpmanager.runHTTPSGetRequest("http://localhost:8080/CommonServicesCP", "output/CommonServicesCP.html", 443, "cert/TESTCA2.jks", "CAPRMGb1g$"), true);
		assertEquals("TC20 - Run valid HTTPS Get request with custom headers", httpmanager.runHTTPSGetRequest("input/TC1_Header.txt", "output/TC1_Response.json", "http://10.106.52.58:7080/mailpieces/v2/1B10170329001017032AE/events", 443, "cert/TESTCA2.jks", "CAPRMGb1g$"), true);
		assertEquals("TC21 - Put MQ Message", gatewayqueuemanager.putMQMessage("EPS.TGT.INT.GW_L", "input/FEB_EPS_Input.txt"),true);
		assertEquals("TC22 - Get MQ Message", gatewayqueuemanager.getMQMessage("EPS.TGT.INT.GW_L", "output/FEB_EPS_Output.txt", 10000), true);
		assertEquals("TC23 - Browse Error Logs",csmanager.browseErrorLogs("W000006-729-I200552", "SELECT * FROM ERROR_LOG WHERE INTERFACE_NAME='W000006-729-I200552'", "TC23", "output","xml"),true);
	}
	
	public static void init(String environment) throws IOException{
		//Initialization
		Properties prop = new Properties();
		InputStream input = new FileInputStream("configs/config_"+environment+".properties");
		prop.load(input);
		fmsHostName = prop.getProperty("fmsHostName");
		username=prop.getProperty("username");
		password=prop.getProperty("password");
		ftgHostName = prop.getProperty("ftgHostName");
		//fmsmanager = new FileManager(fmsHostName, username, password);
		//ftgmanager = new FileManager(ftgHostName, username, password);
		esbBatchHostName = prop.getProperty("esbBatchHostName");
		channel = prop.getProperty("channel");
		gatewayQMHostName = prop.getProperty("gatewayQMHostName");
		gatewayQMPort= Integer.parseInt(prop.getProperty("gatewayQMPort"));
		gatewayQMName = prop.getProperty("gatewayQMName");
		mqUsername = prop.getProperty("mqUsername");
		esbBatchPort = Integer.parseInt(prop.getProperty("esbBatchPort"));
		esbQMName = prop.getProperty("esbQMName");
		esbqueuemanager = new QueueManager(esbBatchHostName, channel, mqUsername, esbBatchPort, esbQMName);
		gatewayqueuemanager = new QueueManager(gatewayQMHostName, channel, mqUsername, gatewayQMPort, gatewayQMName);
		httpmanager = new HTTPManager();
		comparator = new Comparator();
		csEndpoint = prop.getProperty("csEndpoint");
		csUserName = prop.getProperty("csUserName");
		csPassword = prop.getProperty("csPassword");
		csmanager = new CSManager(csEndpoint, csUserName, csPassword);
	}
	
	public static void assertEquals(String testCase, boolean result, boolean assertion){
		if(assertion = result){
			System.out.println(testCase+" executed on "+new Date()+" and Passed");
		}else{
			System.out.println(testCase+" executed on "+new Date()+" and Failed");
		}
	}
	
	public static void assertNotEquals(String testCase, boolean result, boolean assertion){
		if(assertion = result){
			System.out.println(testCase+" executed on "+new Date()+" and Failed");
		}else{
			System.out.println(testCase+" executed on "+new Date()+" and Passed");
		}
	}

}
