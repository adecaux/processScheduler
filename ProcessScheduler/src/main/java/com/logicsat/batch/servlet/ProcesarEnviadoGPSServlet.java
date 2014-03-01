package com.logicsat.batch.servlet;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.logicsat.batch.service.TimerProcesarEnviadoGPS;
import com.logicsat.batch.utils.TimerOperation;

/**
 * @author adrian decaux
 *
 */
public class ProcesarEnviadoGPSServlet extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(ProcesarEnviadoGPSServlet.class);
	private static final String parameterKey = "operacion";
	@EJB
	private TimerProcesarEnviadoGPS timerProcesarEnviadoGPS;

	public ProcesarEnviadoGPSServlet()
	{
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String operation = request.getParameter(parameterKey).toUpperCase();
		switch (TimerOperation.valueOf(operation))
		{
			case START:
				timerProcesarEnviadoGPS.startTimer();
				break;
			case STOP:
				timerProcesarEnviadoGPS.stopTimer();
				break;
			default:
				timerProcesarEnviadoGPS.stopTimer();
				break;
		}
	}

	@Override
	public void init() throws ServletException
	{
		super.init();
		timerProcesarEnviadoGPS.stopTimer();
		logger.info("Starting initial timer: Enviado GPS");
		timerProcesarEnviadoGPS.scheduleTimer();
	}
}
