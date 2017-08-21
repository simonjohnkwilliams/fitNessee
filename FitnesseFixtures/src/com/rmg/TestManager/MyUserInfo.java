package com.rmg.TestManager;

import javax.swing.JOptionPane;

import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

public class MyUserInfo implements UserInfo, UIKeyboardInteractive{
	public String getPassword(){ return null; }
	public boolean promptYesNo(String str){
		return true;		
	}

	String passphrase = "password";
	public String getPassphrase(){ 
		return passphrase; 
	}
	@Override
	public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt,
			boolean[] echo) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean promptPassword(String message) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean promptPassphrase(String message) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void showMessage(String message) {
		// TODO Auto-generated method stub
		
	}
}
