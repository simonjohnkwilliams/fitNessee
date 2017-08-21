package main.Fixtures.BIG.Depricated;

import java.io.File;

public class ClearDownFiles {

	private String location;
	private String error; 
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public boolean isClear(){
		File file = new File(System.getProperty("user.dir") + location);
		System.out.println("Attempting to remove file :" + System.getProperty("user.dir") + location);
		if(file.exists()){
			return file.delete();
		}
		else{
			error = "File doesn't exist please check file at " + System.getProperty("user.dir") + location;
			return false;
		}
	}

	public String error() {
		return error;
	}	
	
}
