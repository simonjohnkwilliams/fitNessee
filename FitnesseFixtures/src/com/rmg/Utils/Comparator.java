package com.rmg.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.rmg.TestManager.QueueManager;

/**
 * @author gaurav.agarwal
 *
 */
public class Comparator extends XMLTestCase {
	
	public boolean parseMessage(String inputLocalFilePath, String inputRFHFilePath, String outputLocalFilePath, QueueManager queueManager) throws XPathExpressionException, InterruptedException, TransformerException, ParserConfigurationException, SAXException{
		return queueManager.runMQDatagram("PARSE.MESSAGE.IN", inputLocalFilePath, inputRFHFilePath, "ISO8859_1" ,"1208", "PARSE.MESSAGE.OUT", outputLocalFilePath, "output/convertRFH.xml", 5000);
	}
	
	public boolean compareBinaryMessages(String file1, String file2){
		FileInputStream is1 = null, is2 = null;
		try {
			return FileUtils.contentEquals(new File(file1), new File(file2));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			
		}finally {
			try {
				if (is1 != null)
					is1.close();
				if (is2 != null)
					is2.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return false;
			}
		}
	}
	
	public boolean compareXMLS(String file1, String file2) {
		FileInputStream is1 = null, is2 = null;
		try {
			File infile = new File(file1);
			is1 = new FileInputStream(infile);
			infile = new File(file2);
			is2 = new FileInputStream(infile);
			XMLUnit.setIgnoreWhitespace(true); // ignore whitespace differences

			// can also compare xml Documents, InputSources, Readers, Diffs
			assertXMLEqual(new InputSource(is1), new InputSource(is2)); // assertXMLEquals
																		// comes
																		// from
																		// XMLTestCase
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			
		} catch(junit.framework.AssertionFailedError e){
			e.printStackTrace();
			return false;
		}finally {
			try {
				if (is1 != null)
					is1.close();
				if (is2 != null)
					is2.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return false;
			}
		}

	}
	
	public String convertFileToString(FileInputStream fis) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		StringBuffer sb = new StringBuffer();
		int ch = br.read();
		while(ch >=0){
			sb.append(ch);
			ch = br.read();
		}
		return sb.toString();
	}
	
	public boolean countNumberOfSiblings(String filePath, String xpathexpr, int expected){
		File infile = new File(filePath);
		try {
			DocumentBuilderFactory domfactory = DocumentBuilderFactory.newInstance();
			domfactory.setNamespaceAware(true);
			DocumentBuilder builder = domfactory.newDocumentBuilder();
			Document doc = builder.parse(infile);
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath.compile(xpathexpr);
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			if(nodes.getLength()==expected){
				return true;
			}else{
				return false;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean checkXPathValue(String filePath, String xpathexpr, String expected){
		File infile = new File(filePath);
		if(xpathexpr.endsWith("/text()")){
		}else{
			xpathexpr = xpathexpr+"/text()";
		}
		try {
			DocumentBuilderFactory domfactory = DocumentBuilderFactory.newInstance();
			domfactory.setNamespaceAware(true);
			DocumentBuilder builder = domfactory.newDocumentBuilder();
			Document doc = builder.parse(infile);
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath.compile(xpathexpr);
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			if((nodes.item(0).getNodeValue()).equals(expected)){
				return true;
			}else{
				return false;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
