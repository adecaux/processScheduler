package com.logicsat.batch.service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author adrian decaux
 *
 */
@Stateless
public class TimerProcesarEnviadoGPSImpl implements TimerProcesarEnviadoGPS
{
	@Resource
	private SessionContext ctx;
	private static final Logger logger = LoggerFactory.getLogger(TimerProcesarEnviadoGPSImpl.class);
	private static final String SECONDS = "procesar.enviado.gps.time.in.sec";
	private static final String ARGUMENT = "timerProcesarEnviadoGPS";
	private static final long MILLISECONDS_IN_ONE_SECOND = 1000;

	public void scheduleTimer()
	{
		loadProperties();
		ctx.getTimerService().createTimer(new Date(), calculateTime(), ARGUMENT);
	}

	private void loadProperties()
	{
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("default.properties");
		try
		{
			System.getProperties().load(in);
		}
		catch (IOException e)
		{
			logger.error("Error loading default.properties");
			e.printStackTrace();
		}
	}

	private long calculateTime()
	{
		String secondsToProcess = System.getProperty(SECONDS);
		long seconds = (secondsToProcess != null) ? Long.parseLong(secondsToProcess) : 30;
		return seconds * MILLISECONDS_IN_ONE_SECOND;
	}

	@Timeout
	public void timeoutHandler(Timer timer)
	{
		String args[] = null;
		callProcesarEnviadoGPS(args);
	}

	private void callProcesarEnviadoGPS(String[] args)
	{
		try
		{
			logger.info("Calling aprocesarenviadogps");
			Class<?> cls = Class.forName("aprocesarenviadogps");
			Method meth = cls.getMethod("main", String[].class);
			meth.invoke(null, (Object) args);
			logger.info("aprocesarenviadogps successfully called.");
		}
		catch (ClassNotFoundException e)
		{
			logger.error("Class aprocesarenviadogps not found by reflection.");
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			logger.error("Illegal access for method main in aprocesarenviadogps");
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			logger.error("Illegal argument for method main in aprocesarenviadogps");
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			logger.error("Illegal invocation for method main in aprocesarenviadogps");
			e.printStackTrace();
		}
		catch (NoSuchMethodException e)
		{
			logger.error("problem finding method main in aprocesarenviadogps");
			e.printStackTrace();
		}
		catch (SecurityException e)
		{
			logger.error("problem accessing method main in aprocesarenviadogps");
			e.printStackTrace();
		}
	}

	public void stopTimer()
	{
		@SuppressWarnings("unchecked")
		Collection<Timer> timers = ctx.getTimerService().getTimers();
		for (Timer t : timers)
		{
			if (t.getInfo().equals(ARGUMENT))
			{
				logger.info("Timer Enviado GPS Detenido.");
				t.cancel();
			}
		}
	}

	public void startTimer()
	{
		logger.info("Timer Enviado GPS Iniciado");
		ctx.getTimerService().createTimer(new Date(), calculateTime(), ARGUMENT);
	}
}
