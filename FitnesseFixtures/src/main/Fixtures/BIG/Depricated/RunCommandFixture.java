package main.Fixtures.BIG.Depricated;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RunCommandFixture {

	private String command;
	private String error;

	public String error(){
		return error;
	}
	public void setCommand(String command) {
		this.command = command;
	} 
	
	public boolean isStarted(){
		try {
			ProcessBuilder process = new ProcessBuilder(command);
			System.out.println("Running command : " + command);
			Process p;
			p = process.start();
			int errCode = p.waitFor();
	        System.out.println("Echo command executed, any errors? " + (errCode == 0 ? "No" : "Yes"));
	        if(errCode != 0){
	        	error = output(p.getInputStream());
	        }  
	    	return p.isAlive();
		} catch (IOException e) {
			error = e.getMessage();
			return false;
		} catch (InterruptedException e) {
			error = e.getMessage();
			return false;
		} 
	}
	
    private static String output(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + System.getProperty("line.separator"));
            }
        } finally {
            br.close();
        }
        return sb.toString();
    }
}
