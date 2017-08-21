package main.Fixtures.BIG.Unix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.rmg.TestManager.ShellExecuter;
import com.rmg.credentials.Credentials;

public class ExecuteRemoteScriptFixture {

	private String remoteScriptLocation;
	private String additionalCommands; 
	private String error;
	private String configFile;
	
	
	public String getRemoteScriptLocation() {
		return remoteScriptLocation;
	}
	public void setRemoteScriptLocation(String remoteScriptLocation) {
		this.remoteScriptLocation = remoteScriptLocation;
	}
	public String error(){
		return error;
	}
	public String getAdditionalCommands() {
		return additionalCommands;
	}

	public void setAdditionalCommands(String additionalCommands) {
		this.additionalCommands = additionalCommands;
	}
	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
	
	public boolean isRun(){
	
		Credentials credentials = new Credentials(configFile);
		List<String> commandsList = additionalCommands!=null?Arrays.asList(additionalCommands.split(";")):new ArrayList<String>();
		if(remoteScriptLocation!=null){
			commandsList.add(remoteScriptLocation);
			try{
				ShellExecuter.executeRemoteCommand(credentials,commandsList);
				return true;
			}
			catch (Exception e) {
				error = e.getMessage();
				return false;
			}
		}
		error = "remote script location null or incorrect.";
		return false;
		
	}
}
