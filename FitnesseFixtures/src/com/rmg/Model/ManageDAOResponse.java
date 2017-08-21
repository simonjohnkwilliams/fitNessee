package com.rmg.Model;

import java.util.ArrayList;

import javax.xml.rpc.holders.BooleanHolder;

/**
 * @author gaurav.agarwal
 *
 */
public class ManageDAOResponse {
	private ArrayList<Row> rows;
	private boolean isUpdate;
	public ArrayList<Row> getRows() {
		return rows;
	}
	public void setRows(ArrayList<Row> rows) {
		this.rows = rows;
	}
	public boolean isUpdate() {
		return isUpdate;
	}
	public void setUpdate(BooleanHolder isUpdate) {
		this.isUpdate = isUpdate.value;
	}
	
	
}
