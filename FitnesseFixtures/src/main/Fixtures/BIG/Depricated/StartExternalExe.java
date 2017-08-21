package main.Fixtures.BIG.Depricated;

import java.io.IOException;

public class StartExternalExe {

	private String location;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	} 
	
	public boolean isStarted(){
		
		try {
			ProcessBuilder process = new ProcessBuilder(System.getProperty("user.dir") + location);
			Process p;
			p = process.start();
			return p.isAlive();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false; 
	}
}
