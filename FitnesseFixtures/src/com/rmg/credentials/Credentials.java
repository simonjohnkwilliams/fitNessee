package com.rmg.credentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @author siwillia
 *This credentials class will deal with all of the properties files that are coming in for fitnesse. These should also contain some encryption later on so that the 
 *items are not held in clear text on the servers. 
 */
public class Credentials {

	private Properties prop; 
	private String error;
	
	public Credentials (final String credentialsFileName){
		prop = new Properties();
		try {
			InputStream input = new FileInputStream(credentialsFileName);
			prop.load(input);
		} catch (IOException e) {
			error = e.getMessage();
		}
	}
	
	public String getValue(String valueName){
		return prop.getProperty(valueName);
	}
	
	public String getError(){
		return error;
	}
	
}
