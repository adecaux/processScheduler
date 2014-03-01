package com.logicsat.batch.servlet;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.logicsat.batch.service.TimerAsignadosPendientes;
import com.logicsat.batch.utils.TimerOperation;


/**
 * @author adrian decaux
 *
 */
public class ProcesarAsignadosPendientesServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(ProcesarAsignadosPendientesServlet.class);
	private static final String parameterKey = "operacion";
	@EJB
	private TimerAsignadosPendientes timerAsignadosPendientes;

	public ProcesarAsignadosPendientesServlet()
	{
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String operation = request.getParameter(parameterKey).toUpperCase();
		switch (TimerOperation.valueOf(operation))
		{
			case START:
				timerAsignadosPendientes.startTimer();
				break;
			case STOP:
				timerAsignadosPendientes.stopTimer();
				break;
			default:
				timerAsignadosPendientes.stopTimer();
				break;
		}
	}

	@Override
	public void init() throws ServletException
	{
		super.init();
		timerAsignadosPendientes.stopTimer();
		logger.debug("Starting initial timer: Asignados Pendientes");
		timerAsignadosPendientes.scheduleTimer();
	}
}
