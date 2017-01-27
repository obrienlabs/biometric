package com.obrienlabs.gps.integration;

import java.util.Observable;
import java.util.Observer;

public class ObservableApp {

	public static void main(String[] args) {
		EventSource eventSource = new EventSource();
		Observer observer = new ObserverImpl();
		eventSource.addObserver(observer);
		/*eventSource.addObserver(new ObserverImpl() {
			public void update(Observable observable, Object arg) {
				System.out.println("_observable.update()");
			}
		});*/
		new Thread(eventSource).start();
	}

}

