package com.scada.exception;

public class BadRequestException extends RuntimeException
{

	private static final long serialVersionUID = 58454515451L;
    String message;
    
    public BadRequestException()
    {
    	super();
    }
    
    public BadRequestException(String message)
    {
    	super(message);
    	this.message=message;
    }
	
	public BadRequestException(Exception ex)
	{
		super(ex);
	}
}
