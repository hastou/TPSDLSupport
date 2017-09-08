package eu.telecomlille.sdl;

/**
 * Classe de base pour un process SDL ; une instanciation de cette classe Java
 * suivie d'un appel � {@link #setParent(IProcess, Stepper)} implante une
 * instanciation de process SDL.
 * 
 * @author Christophe TOMBELLE
 */
public class Process implements IProcess {
	// Expressions pr�d�finies de type PId.
	protected IProcess offspring;
	protected IProcess parent;
	protected IProcess self;
	protected IProcess sender;
	/** Fifo SDL. */
	protected Fifo _fifo;
	/** signal pr�lev� en t�te de fifo. */
	protected ISignal _sig;
	/** Contr�le la terminaison de cette instance de process. */
	protected volatile boolean _bRun;
	/** Contr�le le d�marrage de cette instance de process. */
	protected Thread thread;
	/** Stepper (pour debug) du thread de cette instance de process. */
	protected Stepper stepper;
	/** Stepper (pour debug) du dernier fils cr��. */ 
	protected Stepper stpOffspring;

	/**
	 * Cr�er une instance de ce process (� compl�ter par un appel � {@link #setParent(IProcess, Stepper)}).
	 */
	public Process() {
		self = this;
		_fifo = new Fifo();
	}

	/**
	 * D�l�gue � {@link Stepper#block()} du Stepper du thread de ce Process.
	 */
	protected void block() {
		if (stepper != null)
			stepper.block();
	}

	/**
	 * Ajouter le signal sp�cifi� � la fifo de l'instance de process.
	 */
	@Override
	public void add(ISignal sig) {
		synchronized (_fifo) {
			_fifo.add(sig);
			if (_fifo.getSize() == 1)
				_fifo.notify();
		}
	}

	/**
	 * Fournir le {@link Stepper} du dernier fils cr�� (par un CREATE) ou null
	 * si aucun fils cr��.
	 * 
	 * @return Le Stepper du fils.
	 */
	public Stepper getOffspringStepper() {
		return stpOffspring;
	}

	/**
	 * Support pour CREATE �tablissant les relations p�re / fils et d�marrant le
	 * thread du fils; � utiliser dans le thread du p�re (contr�l� par
	 * stpCaller).
	 * 
	 * @param stpCaller
	 *            Le stepper pour contr�ler le thread du process p�re ou null si
	 *            pas de debug souhait�.
	 * @param pidOffspring
	 *            Le process fils.
	 */
	public void create(Stepper stpCaller, IProcess pidOffspring) {
		offspring = pidOffspring;
		if (stpCaller != null) {
			stpOffspring = new Stepper();
			/*
			 * Permettre au test d'acqu�rir stpOffspring avant que le fils
			 * d�marre.
			 */
			stpCaller.block();
		}
		offspring.setParent(this, stpOffspring);
	}

	/**
	 * Support pour CREATE �tablissant les relations p�re / fils et d�marrant le
	 * thread du fils.
	 * 
	 * @param pidOffspring
	 *            Le process fils.
	 */
	public void create(IProcess pidOffspring) {
		create(stepper, pidOffspring);
	}

	/**
	 * D�finir le parent de ce Process et d�marrer cette instance de process.
	 */
	@Override
	public void setParent(IProcess procParent, Stepper s) {
		parent = procParent;
		stepper = s;
		thread = new Thread(new Runnable() {
			public void run() {
				_bRun = true;
				onStart();
				block();
				while (_bRun)
					try {
						_sig = null;
						synchronized (_fifo) {
							while (_fifo.isEmpty()) {
//								System.out.println("blocking on fifo");
								_fifo.wait();
							}
							_sig = (Signal) _fifo.get();
						}
						sender = _sig.getSender();
						// dispatch following current state
						dispatch();
						block();
					} catch (InterruptedException e) {
					}
			}
		});
//		thread.setDaemon(true);
		thread.start();
	}

	/**
	 * Support pour le STOP SDL ; appeler cette m�thode pour terminer cette
	 * instance de process.
	 */
	protected void stop() {
		_bRun = false;
	}

	/**
	 * Cette m�thode est appel�e quand le thread de ce process d�marre, avant
	 * que la pr�sence d'un signal soit recherch� en t�te de fifo ; � red�finir
	 * dans une sous-classe pour implanter la pseudo-transition d'initialisation
	 * (support pour START).
	 */
	protected void onStart() {
	}

	/**
	 * Cette m�thode est appel�e quand un signal disponible dans _sig a �t�
	 * pr�lev� en t�te de Fifo ; � red�finir dans une sous-classe pour implanter
	 * la r�action au signal _sig selon l'�tat courant.
	 */
	protected void dispatch() {
	}

	/**
	 * Indiquer si cette instance de process est termin�e.
	 * 
	 * @return true si instance de process termin�e.
	 */
	public boolean isStopped() {
		return !_bRun;
	}
}
