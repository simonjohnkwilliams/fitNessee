package com.rmg.TestManager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.xml.rpc.holders.BooleanHolder;

import org.apache.axis.encoding.Base64;
import org.apache.axis.message.MessageElement;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.capgemini.www.StubbingCommon.CommonServicesProxy;
import com.capgemini.www.StubbingCommon.ParameterType;
import com.capgemini.www.StubbingCommon.RowType;
import com.capgemini.www.StubbingCommon.holders.ResultSetTypeHolder;
import com.rmg.Model.ManageDAOResponse;

import com.rmg.Model.Column;
import com.rmg.Model.Row;
import com.rmg.Utils.Comparator;
import com.rmg.Utils.Utils;

/**
 * @author gaurav.agarwal
 *
 */
public class CSManager {
	
	private String endpoint;
	private String userName;
	private String password;
	
	
	
	public CSManager(String endpoint, String userName, String password) {
		super();
		this.endpoint = endpoint;
		this.userName = userName;
		this.password = password;
	}

	private ManageDAOResponse browseAuditLogs(String interfaceName, String subquery) throws RemoteException{
		if(subquery.contains("WHERE")){
			subquery = subquery + " AND 1=?";
		}else{
			subquery = subquery + " WHERE 1=?";
		}
		subquery = subquery + " ORDER BY TIMESTMP DESC";
		ParameterType[] parameter = new ParameterType[1];
		parameter[0] = new ParameterType(1, "int", "1");
		String query="select * from ( select a.*, ROWNUM rnum from ( "+subquery+" ) a where ROWNUM <= 1 ) where rnum  >= 0";
		ManageDAOResponse response =  manageDAO(endpoint, userName, password, query, parameter);
		String txnuuid = response.getRows().get(0).getColumn("TXNUUID").getValue();
		query="SELECT * FROM AUDIT_LOG WHERE INTERFACE_NAME=? AND TXNUUID=?";
		parameter = new ParameterType[2];
		parameter[0] = new ParameterType(1, "string", interfaceName);
		parameter[1] = new ParameterType(2, "string", txnuuid);
		return manageDAO(endpoint, userName, password, query, parameter);
		
	}
	
	private ManageDAOResponse browseErrorLogs(String interfaceName, String subquery) throws RemoteException{
		if(subquery.contains("WHERE")){
			subquery = subquery + " AND 1=?";
		}else{
			subquery = subquery + " WHERE 1=?";
		}
		subquery = subquery + " ORDER BY TIMESTMP DESC";
		ParameterType[] parameter = new ParameterType[1];
		parameter[0] = new ParameterType(1, "int", "1");
		String query="select * from ( select a.*, ROWNUM rnum from ( "+subquery+" ) a where ROWNUM <= 1 ) where rnum  >= 0";
		ManageDAOResponse response =  manageDAO(endpoint, userName, password, query, parameter);
		String txnuuid = response.getRows().get(0).getColumn("TXNUUID").getValue();
		query="SELECT * FROM ERROR_LOG WHERE INTERFACE_NAME=? AND TXNUUID=?";
		parameter = new ParameterType[2];
		parameter[0] = new ParameterType(1, "string", interfaceName);
		parameter[1] = new ParameterType(2, "string", txnuuid);
		return manageDAO(endpoint, userName, password, query, parameter);
		
	}
	
	private boolean createFileFromByteArray(String filepath, byte[] value){
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filepath);
			fos.write(value);
			fos.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally{
			try {
				if(fos!=null){
					fos.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public boolean browseAuditLogs (String interfaceName, String queryString, String suffix, String outputLocalFolder, String fileextension) {
		try {
			ManageDAOResponse response = browseAuditLogs(interfaceName, queryString);
			for(Row row:response.getRows()){
				String messageName = row.getColumn("MESSAGE_NAME").getValue();
				String message = row.getColumn("MESSAGE").getValue();
				String blob = new Utils().getXPathValue(message, "//BLOB/BLOB");
				if (blob!=null){
					byte[] base64 = Base64.decode(blob);
					createFileFromByteArray(outputLocalFolder+"/"+interfaceName+"_"+messageName+"_"+suffix+"."+fileextension, base64);
				}else{
					createFileFromByteArray(outputLocalFolder+"/"+interfaceName+"_"+messageName+"_"+suffix+"."+fileextension, Hex.decodeHex(message.toCharArray()));
				}
			}
			return true;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (DecoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean browseErrorLogs (String interfaceName, String queryString, String suffix, String outputLocalFolder, String fileextension) {
		try {
			ManageDAOResponse response = browseErrorLogs(interfaceName, queryString);
			for(Row row:response.getRows()){
				String messageName = "Error";
				String message = row.getColumn("MESSAGE").getValue();
				String blob = new Utils().getXPathValue(message, "//BLOB/BLOB");
				if (blob!=null){
					byte[] base64 = Base64.decode(blob);
					createFileFromByteArray(outputLocalFolder+"/"+interfaceName+"_"+messageName+"_"+suffix+"."+fileextension, base64);
				}
				createFileFromByteArray(outputLocalFolder+"/"+interfaceName+"_"+messageName+"_LogEvent_"+suffix+"."+fileextension, Hex.decodeHex(message.toCharArray()));
			}
			return true;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (DecoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public ManageDAOResponse manageDAO(String endpoint, java.lang.String userName, java.lang.String password, java.lang.String sqlString, com.capgemini.www.StubbingCommon.ParameterType[] parameter) throws RemoteException{
		javax.xml.rpc.holders.BooleanHolder isUpdate=new BooleanHolder(true);
		ResultSetTypeHolder resultSet = new ResultSetTypeHolder();
		CommonServicesProxy proxy = new CommonServicesProxy();
		proxy.setEndpoint(endpoint+"/CommonServices/DAOHandler");
		password = encrypt(password,"My reference data");
		proxy.manageDAO(userName, password, sqlString, parameter, isUpdate, resultSet);
		ManageDAOResponse manageDAOResponse = new ManageDAOResponse();
		manageDAOResponse.setUpdate(isUpdate);
		ArrayList<Row> rows = new ArrayList<Row>();
		for(RowType row:resultSet.value){
			Row rowa = new Row();
			MessageElement[] nodes = row.get_any();
			for(MessageElement node:nodes){
				Column column = new Column();
				column.setName(node.getName());
				if(node.getValue()!=null){
					column.setValue(node.getValue());
				}else{
					column.setValue("");
				}
				rowa.addColumn(column);
			}
			rows.add(rowa);
		}
		manageDAOResponse.setRows(rows);
		return manageDAOResponse;
	}
	
	public String encrypt(String Pass, String Key){
		String value="";
		String salt = "`1234567890-=qwertyuiop[]\\asdfghjkl;'zxcvbnm,./~!@#$%^&*()_+QWERTYUIOP{}|ASDFGHJKL:\"ZXCVBNM<>? ";
		int i = 0;
		int j = 0;
		while (i<Pass.length()) {
			int posPass = salt.indexOf(Pass.substring(i,i+1));
			int posKey = salt.indexOf(Key.substring(j,j+1));

			if (j>=(Key.length())){
				j = 1;
			}
			else{
				j = j+1;
			}

			i = i+1;
			int posTotal = posPass + posKey + 2;
			String posChar="";
			if((""+posTotal).length()==1){
				posChar = "00"+posTotal;
			}else if((""+posTotal).length()==2){
				posChar = "0"+posTotal;
			}else if ((""+posTotal).length()==3){
				posChar = ""+posTotal;
			}

			value = value+posChar;
		}
		return value;
	}


}
