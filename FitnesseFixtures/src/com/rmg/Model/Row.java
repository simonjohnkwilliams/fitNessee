package com.rmg.Model;

import java.util.ArrayList;

/**
 * @author gaurav.agarwal
 *
 */
public class Row {
	
	private ArrayList<Column> columns = new ArrayList<Column>();
	
	public void addColumn(Column column){
		this.columns.add(column);
	}

	public ArrayList<Column> getColumns() {
		return columns;
	}
	public Column getColumn(int index){
		return this.columns.get(index);
	}
	public Column getColumn(String columnName){
		for(Column column:columns){
			if(column.getName().equals(columnName)){
				return column;
			}
		}
		return null;
	}
}
