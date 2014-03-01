package com.logicsat.batch.utils;

/**
 * @author adrian decaux
 *
 */
public enum TimerOperation
{
	START("start"), STOP("stop");

	private String operation;

	TimerOperation(String operation)
	{
		this.operation = operation;
	}

	public String getOperation()
	{
		return this.operation;
	}
}
