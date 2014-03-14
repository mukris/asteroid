package hu.bme.mit.asteroid.control;

/**
 * A játék irányítási felületének interfésze. Absztrakt osztály. Ezen az
 * interfészen keresztül valósul meg az irányítás a tényleges fizikai beviteli
 * lehetőségektől függetlenül. A leszármazottak a {@link Callback} interfész
 * eseménykezelő függvényeit hívják a megfelelő fizikai vezérlőutasítások
 * esetén.
 */
public abstract class ControlInterface {

	/**
	 * A fizikai irányítási felületről érkező logikai vezérlő utasítások
	 * interfésze. Innentől kezdve a vezérlőjelek függetlenek a fizikai beviteli
	 * lehetőségektől.
	 */
	public interface Callback {
		/**
		 * Gyorsítás esemény bekövetkezésekor hívódó eseménykezelő függvény
		 */
		public void onAccelerate();

		/**
		 * Balra fordulás esemény bekövetkezésekor hívódó eseménykezelő függvény
		 */
		public void onTurnLeft();

		/**
		 * Jobbra fordulás esemény bekövetkezésekor hívódó eseménykezelő
		 * függvény
		 */
		public void onTurnRight();

		/**
		 * Tüzelés esemény bekövetkezésekor hívódó eseménykezelő függvény
		 */
		public void onFire();
	}

	/**
	 * Referencia az interfészre
	 */
	protected Callback mCallback;

	/**
	 * Feliratkozás az interfész által kibocsátott eseményekre
	 * 
	 * @param callback
	 *            Az interfészt implementáló osztály
	 */
	public void setCallback(Callback callback) {
		mCallback = callback;
	}
}
