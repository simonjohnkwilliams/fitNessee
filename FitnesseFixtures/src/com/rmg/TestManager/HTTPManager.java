package com.rmg.TestManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.rmg.Model.HTTPHeader;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;


/**
 * @author gaurav.agarwal
 *
 */
@SuppressWarnings("deprecation")
public class HTTPManager {
	
	private String convertFileToString(String filename) throws IOException{
		File file = new File(filename);
		return FileUtils.readFileToString(file);
	}
	
	public boolean runHTTPSRequest(String inFilePath, String headerFile, String outFilePath, String url, int httpsPort, String storeFileName, String storePassword){
		FileOutputStream os = null;
		FileInputStream is = null;
		try {
			File infile = new File(inFilePath);
			is = new FileInputStream(infile);
			String requestBody = IOUtils.toString(is);
			HTTPHeader header = new HTTPHeader();
			String lines[] = convertFileToString(headerFile).split("\\r?\\n");
			for(String line:lines){
				String keyVal[] = line.split(":");
				header.addHeader(keyVal[0], keyVal[1]);
			}
			String response = this.httpsRequest(requestBody, header, url, httpsPort, storeFileName, storePassword);
			File outfile = new File(outFilePath);
			os = new FileOutputStream(outfile);
			os.write(response.getBytes());
			os.close();
			is.close();
			return true;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally{
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public boolean runHTTPSGetRequest(String headerFile, String outFilePath, String url, int httpsPort, String storeFileName, String storePassword){
		FileOutputStream os = null;
		try {
			HTTPHeader header = new HTTPHeader();
			String lines[] = convertFileToString(headerFile).split("\\r?\\n");
			for(String line:lines){
				String keyVal[] = line.split(":");
				header.addHeader(keyVal[0], keyVal[1]);
			}
			String response = this.httpsGetRequest(header, url, httpsPort, storeFileName, storePassword);
			File outfile = new File(outFilePath);
			os = new FileOutputStream(outfile);
			os.write(response.getBytes());
			os.close();
			return true;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally{
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	private String httpsRequest(String request, HTTPHeader header, String url, int httpsPort, String storeFileName, String storePassword) throws ClientProtocolException, IOException{
				
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(url);
		HttpEntity entity = new ByteArrayEntity(request.getBytes("UTF-8"));
		HashSet<String> headersKey = header.getHeadersKey();
		HashMap<String, String> headersValue = header.getHeadersValue();
	    postRequest.setEntity(entity);
	    for(String headerKey:headersKey){
	    	postRequest.addHeader(headerKey, headersValue.get(headerKey));
	    }
	    
	    Scheme sch = this.setupSSLScheme(storeFileName, storePassword, httpsPort);
	    httpClient.getConnectionManager().getSchemeRegistry().register(sch);
	    
		HttpResponse response = httpClient.execute(postRequest);

		HttpEntity entityResponse = response.getEntity();
		String body = IOUtils.toString(entityResponse.getContent(), "UTF-8");
		httpClient.close();
		return body;
	}
	
	private String httpsGetRequest(HTTPHeader header, String url, int httpsPort, String storeFileName, String storePassword) throws ClientProtocolException, IOException{
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(url);
		HashSet<String> headersKey = header.getHeadersKey();
		HashMap<String, String> headersValue = header.getHeadersValue();
	    for(String headerKey:headersKey){
	    	getRequest.addHeader(headerKey, headersValue.get(headerKey));
	    }
	    
	    Scheme sch = this.setupSSLScheme(storeFileName, storePassword, httpsPort);
	    httpClient.getConnectionManager().getSchemeRegistry().register(sch);
	    
		HttpResponse response = httpClient.execute(getRequest);

		HttpEntity entityResponse = response.getEntity();
		String body = IOUtils.toString(entityResponse.getContent(), "UTF-8");
		httpClient.close();
		return body;
	}
	
	private String httpRequest(String request, HTTPHeader header, String url) throws ClientProtocolException, IOException{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(url);
		HttpEntity entity = new ByteArrayEntity(request.getBytes("UTF-8"));
		HashSet<String> headersKey = header.getHeadersKey();
		HashMap<String, String> headersValue = header.getHeadersValue();
	    postRequest.setEntity(entity);
	    for(String headerKey:headersKey){
	    	postRequest.addHeader(headerKey, headersValue.get(headerKey));
	    }
	    
	    HttpResponse response = httpClient.execute(postRequest);

		HttpEntity entityResponse = response.getEntity();
		String body = IOUtils.toString(entityResponse.getContent(), "UTF-8");
		httpClient.close();
		return body;
	}
	
	private String httpXMLRequest(String requestBody, String url) throws ClientProtocolException, IOException{
		HTTPHeader headers = new HTTPHeader();
		headers.addHeader("Content-Type", "application/xml");
		return httpRequest(requestBody, headers, url);
	}
	
	private String httpJSONRequest(String requestBody, String url) throws ClientProtocolException, IOException{
		HTTPHeader headers = new HTTPHeader();
		headers.addHeader("Content-Type", "application/json");
		return httpRequest(requestBody, headers, url);
	}
	public boolean runHTTPXMLRequest(String url, String inFilePath, String outFilePath){
		FileInputStream is = null;
		FileOutputStream os = null;
		try {
			File infile = new File(inFilePath);
			is = new FileInputStream(infile);
			String requestBody = (IOUtils.toByteArray(is)).toString();
			String response = this.httpXMLRequest(requestBody, url);
			File outfile = new File(outFilePath);
			os = new FileOutputStream(outfile);
			os.write(response.getBytes());
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally{
			try {
				if(is!=null)
					is.close();
				if(os!=null)
					os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return false;
			}
		}
		
	}
	
	public boolean runHTTPJSONRequest(String url, String inFilePath, String outFilePath){
		FileInputStream is = null;
		FileOutputStream os = null;
		try {
			File infile = new File(inFilePath);
			is = new FileInputStream(infile);
			String requestBody = (IOUtils.toByteArray(is)).toString();
			String response = this.httpJSONRequest(requestBody, url);
			File outfile = new File(outFilePath);
			os = new FileOutputStream(outfile);
			os.write(response.getBytes());
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		}finally{
			try {
				if(is!=null)
					is.close();
				if(os!=null)
					os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return false;
			}
		}
		
	}
	
	private String httpsXMLRequest(String requestBody, String url, int httpsPort, String storeFileName, String storePassword) throws ClientProtocolException, IOException{
		HTTPHeader headers = new HTTPHeader();
		headers.addHeader("Content-Type", "application/xml");
		return httpsRequest(requestBody, headers, url, httpsPort, storeFileName, storePassword);
	}
	
	private String httpsJSONRequest(String requestBody, String url, int httpsPort, String storeFileName, String storePassword) throws ClientProtocolException, IOException{
		HTTPHeader headers = new HTTPHeader();
		headers.addHeader("Content-Type", "application/json");
		return httpsRequest(requestBody, headers, url, httpsPort, storeFileName, storePassword);
	}
	
	private String httpsGETRequest(String url, int httpsPort, String storeFileName, String storePassword) throws ClientProtocolException, IOException{
		HTTPHeader headers = new HTTPHeader();
		headers.addHeader("Content-Type", "application/json");
		return httpsGetRequest(headers, url, httpsPort, storeFileName, storePassword);
	}
	
	public boolean runHTTPSXMLRequest(String url, String inFilePath, String outFilePath, int httpsPort, String storeFileName, String storePassword){
		FileInputStream is = null;
		FileOutputStream os = null;
		try {
			File infile = new File(inFilePath);
			is = new FileInputStream(infile);
			String requestBody = IOUtils.toString(is);
			String response = this.httpsXMLRequest(requestBody, url, httpsPort, storeFileName, storePassword);
			File outfile = new File(outFilePath);
			os = new FileOutputStream(outfile);
			os.write(response.getBytes());
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally{
			try {
				if(is!=null)
					is.close();
				if(os!=null)
					os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return false;
			}
		}
		
	}
	
	public boolean runHTTPSJSONRequest(String url, String inFilePath, String outFilePath, int httpsPort, String storeFileName, String storePassword){
		FileInputStream is = null;
		FileOutputStream os = null;
		try {
			File infile = new File(inFilePath);
			is = new FileInputStream(infile);
			String requestBody = (IOUtils.toByteArray(is)).toString();
			String response = this.httpsJSONRequest(requestBody, url, httpsPort, storeFileName, storePassword);
			File outfile = new File(outFilePath);
			os = new FileOutputStream(outfile);
			os.write(response.getBytes());
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		}finally{
			try {
				if(is!=null)
					is.close();
				if(os!=null)
					os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return false;
			}
		}
		
	}
	public boolean runHTTPSGetRequest(String url, String outFilePath, int httpsPort, String storeFileName, String storePassword){
		FileInputStream is = null;
		FileOutputStream os = null;
		try {
			String response = this.httpsGETRequest(url, httpsPort, storeFileName, storePassword);
			File outfile = new File(outFilePath);
			os = new FileOutputStream(outfile);
			os.write(response.getBytes());
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		}finally{
			try {
				if(is!=null)
					is.close();
				if(os!=null)
					os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return false;
			}
		}
		
	}
	
	public Scheme setupSSLScheme(String storeFileName, String storePassword, int port){
		File truststoreFile = new File(storeFileName);
		if (!truststoreFile.exists() || !truststoreFile.isFile()) {
		    System.err.println("Not found or not a file: "
				       + truststoreFile.getPath());
		    return null;
		}
		String truststorePassword = storePassword;
		if (truststorePassword.length() == 0) {
		    System.err.println("Empty truststore password, giving up");
		    return null;
		}
		File keystoreFile = new File(storeFileName);
		if (!keystoreFile.exists() || !keystoreFile.isFile()) {
		    System.err.println("Not found or not a file: "
				       + keystoreFile.getPath());
		    return null;
		}
		String keystorePassword = storePassword;
		if (keystorePassword.length() == 0) {
		    System.err.println("Empty keystore password, giving up");
		    return null;
		}
		String privateKeyPassword = storePassword;
		if (privateKeyPassword.length() == 0) {
		    System.err.println("Empty private key password, giving up");
		    return null;
		}
		try {
		    // The expected server certificate must be in a *trust* store.
		    // keytool must report "trustedCertEntry" when listing the contents.
		    KeyStore trustStore = KeyStore.getInstance(KeyStore
							       .getDefaultType());
		    FileInputStream trustStream = new FileInputStream(truststoreFile);
		    try {
			trustStore.load(trustStream, truststorePassword.toCharArray());
			} catch (Exception ex) {
			System.err.println("Failed to load truststore: "
					   + ex.toString());
			return null;
		    } finally {
			try {
			    trustStream.close();
			} catch (Exception ignore) {
			}
		    }

		    // The required user key must be in a *key* store.
		    // keytool must report "PrivateKeyEntry" when listing the contents.
		    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		    FileInputStream keyStream = new FileInputStream(keystoreFile);
		    try {
			keyStore.load(keyStream, keystorePassword.toCharArray());
			} catch (Exception ex) {
			System.err.println("Failed to load keystore: " + ex.toString());
			return null;
		    } finally {
			try {
			    keyStream.close();
			} catch (Exception ignore) {
			}
		    }
		    
		 // Do not do this in production!!!
			HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
			
			// Set verifier     
			HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
		    
		    // Create and register a socket factory for all HTTPS connections
		    SSLSocketFactory socketFactory = null;
		    try {
			// http://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/index.html
			// This constructor has zero words of documentation in the
			// version 4.1.2 javadoc; I only figured it out by googling.
			socketFactory = new SSLSocketFactory(keyStore,
							     privateKeyPassword, trustStore);
			socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
		    } catch (UnrecoverableKeyException ke) {
			System.err
			    .println("Failed to create SSLSocketFactory, possible wrong password on client private key");
			return null;
		    } catch (KeyManagementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (KeyStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    // This is the default port number only; others are allowed
		    Scheme sch = new Scheme("https", port, socketFactory);
		    return sch;
		} catch (KeyStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally{
			//Do nothing
		}
		return null;
	}
	
}
