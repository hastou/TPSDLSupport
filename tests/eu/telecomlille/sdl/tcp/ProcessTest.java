package eu.telecomlille.sdl.tcp;

import eu.telecomlille.sdl.Process;

public class ProcessTest extends Process {
	public void doIt() {
		setParent(null, null); // d�marre le thread du Process
		// TODO envoyer des signaux � ce Process : TSDUConnect, TSDUData et TSDUClose
	}

	// TODO red�finir onStart
		// TODO afficher onStart

	// TODO red�finir dispatch
	protected void dispatch() {
		// TODO afficher le signal _sig (h�rit�)
		// TODO afficher sender (h�rit�)
		// TODO appeler stop h�rit� sur r�ception du signal TSDUClose
	}

	public static void main(String[] args) {
		new ProcessTest().doIt();
	}
}
