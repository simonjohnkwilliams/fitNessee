package com.rmg.Model;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author gaurav.agarwal
 *
 */
public class HTTPHeader {
	
	private HashSet<String> headersKey = new HashSet<String>();
	private HashMap<String, String> headersValue= new HashMap<String, String>();
	
	public void addHeader(String key, String value){
		this.headersKey.add(key);
		this.headersValue.put(key, value);
	}
	
	public void removeHeader(String key){
		this.headersKey.remove(key);
		this.headersValue.remove(key);
	}

	public HashSet<String> getHeadersKey() {
		return headersKey;
	}

	public HashMap<String, String> getHeadersValue() {
		return headersValue;
	}
	
	

}
