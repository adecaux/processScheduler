package com.logicsat.batch.service;

import javax.ejb.Local;


/**
 * @author adrian decaux
 *
 */
@Local
public interface TimerAsignadosPendientes
{
	void scheduleTimer();

	void startTimer();

	void stopTimer();
}
