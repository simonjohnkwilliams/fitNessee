package main.Fixtures.BIG.Depricated;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class FilePollerAndMover implements Runnable{
	
	private String locationToPoll;
	private String locationToMove;
	
	public FilePollerAndMover(String locationToPoll, String locationToMove){
			this.locationToPoll = locationToPoll;
			this.locationToMove = locationToMove;
	}
	
	public String getLocationToPoll() {
		return locationToPoll;
	}
	public void setLocationToPoll(String locationToPoll) {
		this.locationToPoll = locationToPoll;
	}
	public String getLocationToMove() {
		return locationToMove;
	}
	public void setLocationToMove(String locationToMove) {
		this.locationToMove = locationToMove;
	}
	public boolean pollFile() {
		Thread runner = new Thread(new FilePollerAndMover(this.locationToPoll,this.locationToMove));
		runner.run();
		return true;
	}
	private void runWatcher() throws InterruptedException, IOException {
		Path faxFolder = Paths.get(System.getProperty("user.dir") + locationToPoll);
		WatchService watchService = FileSystems.getDefault().newWatchService(); 
		faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,StandardWatchEventKinds.ENTRY_DELETE,StandardWatchEventKinds.ENTRY_MODIFY);
		boolean valid = true;
		do {
			Thread t = new Thread();
			WatchKey watchKey = watchService.take();
			for (WatchEvent event : watchKey.pollEvents()) {	
				WatchEvent.Kind kind = event.kind(); 
				if(StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) { 
					String fileName = event.context().toString();	
					System.out.println("File Created:" + fileName);
					moveFile(System.getProperty("user.dir") + locationToPoll + "/" + fileName, System.getProperty("user.dir") + locationToMove);
					valid = false;
					break ;
				}
				valid = watchKey.reset();
			}
		} while (valid);
		
	}

	private boolean moveFile(String fromLocation, String toLocation) {
		File file = new File(fromLocation);
		return file.renameTo(new File(toLocation + "/" + file.getName()));
	}

	@Override
	public void run() {
		try {
			runWatcher();
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
