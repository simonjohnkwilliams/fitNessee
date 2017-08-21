package com.rmg.Utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author gaurav.agarwal
 *
 */
public class Utils {
	public String getXPathValue(String str, String xpathexpr){
		if(xpathexpr.endsWith("/text()")){
		}else{
			xpathexpr = xpathexpr+"/text()";
		}
		try {
			DocumentBuilderFactory domfactory = DocumentBuilderFactory.newInstance();
			domfactory.setNamespaceAware(true);
			DocumentBuilder builder = domfactory.newDocumentBuilder();
			byte[] b = new BigInteger(str,16).toByteArray();
			ByteArrayInputStream bis = new ByteArrayInputStream(b);
			Document doc = builder.parse(bis);
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath.compile(xpathexpr);
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			if(nodes.getLength()!=0){
				return nodes.item(0).getNodeValue();
			}else{
				return null;
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
		return null;
	}
}
