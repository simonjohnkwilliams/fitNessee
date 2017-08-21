package main.Fixtures.BIG.Compare;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ListCsvData {

	String _fileLocation; 
	public ListCsvData(String fileLocation){
		_fileLocation = System.getProperty("user.dir") + fileLocation;
	}
	
	public List<List<List<String>>> query(){
		List<List<List<String>>> returnQuery = new ArrayList(); //table level 
		
		try {
			List<String> fileLines = Files.readAllLines(new File (_fileLocation).toPath());
			for(String column : fileLines){
				List<List<String>> lineValues = new ArrayList<List<String>>(); //row level
				String[] columnValues = column.split(" ");
				int i = 1;
				for(String columnvalue : columnValues){
					List<String> columnList = new ArrayList<String>(); //columnName and value pair
					columnList.add("Col" + i);
					columnList.add(columnvalue);
					lineValues.add(columnList);
					i ++;
				}
				returnQuery.add(lineValues); //add row;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
      	return returnQuery;
	}
}
