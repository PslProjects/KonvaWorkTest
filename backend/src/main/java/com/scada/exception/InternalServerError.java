package com.scada.exception;

public class InternalServerError extends Exception
{

	
	private static final long serialVersionUID = 144949656L;
	String message;
	
	public InternalServerError()
	{
		super();
	}
	
	public InternalServerError(String message)
	{
		super(message);
		this.message=message;	
	}
}
