package main.Fixtures.BIG.Unix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class MoveFileFixture {

	private String origin;
	private String destination;
	private String error;
	
	public String error(){
		return error;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	} 
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	public boolean isMoved(){
		//get workig dir
		String workingDir = System.getProperty("user.dir");
		File originFile = new File(workingDir + origin);
	    if(!originFile.exists()){
	    	error = "Error origin file doesn't exist: " + workingDir + origin;
	    	return false;
	    }
	     
		File destinationFile = new File(workingDir +destination);
		if(!destinationFile.exists()){
			try {
				destinationFile.createNewFile();
			} catch (IOException e) {
				error = e.getMessage() + workingDir +destination + " doesn't exist";
				return false;
			}
		}
	    OutputStream destinationStream;
		try {
			destinationStream = new FileOutputStream(destinationFile);
			Files.copy(originFile.toPath(), destinationStream);
			destinationStream.flush();
			destinationStream.close();
		} catch (FileNotFoundException e) {
			error = e.getMessage() + workingDir +destination + " doesn't exist";
			return false;
		} catch (IOException e) {
			error = e.getMessage()+ workingDir +destination + " doesn't exist";
			return false;
		}		
		return true;
	}
	
    
}
