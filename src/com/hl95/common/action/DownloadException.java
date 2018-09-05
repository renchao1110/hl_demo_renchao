package com.hl95.common.action;

public class DownloadException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DownloadException(){
		
	}
	
	public DownloadException(String message){
		super(message);
	}
}
