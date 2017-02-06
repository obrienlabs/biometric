package org.obrienlabs.gps.integration;

import java.util.Observable;

public class EventSource extends Observable implements Runnable {

	@Override
	public void run() {
		System.out.println("_EventSource: run");
		notifyObservers();
	}

}

