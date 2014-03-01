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
public class TimerAsignadosPendientesImpl implements TimerAsignadosPendientes
{
	@Resource
	private SessionContext ctx;
	private static final Logger logger = LoggerFactory.getLogger(TimerAsignadosPendientesImpl.class);
	private static final String SECONDS = "procesar.asignados.pendientes.time.in.sec";
	private static final String ARGUMENT = "timerProcesarAsignadosPendientes";
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
		callAsignadosPendientesProcess(args);
	}

	private void callAsignadosPendientesProcess(String args[])
	{
		try
		{
			logger.info("Calling aprocesarasignados_pendientes");
			Class<?> cls = Class.forName("aprocesarasignados_pendientes");
			Method meth = cls.getMethod("main", String[].class);
			meth.invoke(null, (Object) args);
			logger.info("aprocesarasignados_pendientes successfully called.");
		}
		catch (ClassNotFoundException e)
		{
			logger.error("Class aprocesarasignados_pendientes not found by reflection.");
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			logger.error("Illegal access for method main in aprocesarasignados_pendientes");
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			logger.error("Illegal argument for method main in aprocesarasignados_pendientes");
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			logger.error("Illegal invocation for method main in aprocesarasignados_pendientes");
			e.printStackTrace();
		}
		catch (NoSuchMethodException e)
		{
			logger.error("problem finding method main in aprocesarasignados_pendientes");
			e.printStackTrace();
		}
		catch (SecurityException e)
		{
			logger.error("problem accessing method main in aprocesarasignados_pendientes");
			e.printStackTrace();
		}
	}

	public void stopTimer()
	{
		@SuppressWarnings("unchecked")
		Collection<Timer> timers = ctx.getTimerService().getTimers();
		for (Timer t : timers)
		{
			System.out.println("---------" + t.getInfo() + "------------");
			if (t.getInfo().equals(ARGUMENT))
			{
				logger.info("Timer Asignados Pendientes Detenido.");
				t.cancel();
			}
		}
	}

	public void startTimer()
	{
		logger.info("Timer Asignados Pendientes Iniciado.");
		ctx.getTimerService().createTimer(new Date(), calculateTime(), ARGUMENT);
	}
}
