package hu.bme.mit.asteroid.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A játékban szereplő bónuszokat reprezentáló osztály
 */
public class Powerup extends SpaceObject implements Serializable {
	private static final long serialVersionUID = 5325099147757367725L;

	public static final int RADIUS = 15;

	/**
	 * Az egyes típusok megjelenési valószínűsége %-ban
	 */
	private static final Map<Type, Integer> sProbabilities = new HashMap<>();
	static {
		sProbabilities.put(Type.PLUS_POINTS_SMALL, 10);
		sProbabilities.put(Type.PLUS_POINTS_LARGE, 5);
		sProbabilities.put(Type.UNVULNERABILITY, 5);
		sProbabilities.put(Type.PLUS_LIFE, 1);
	}

	/**
	 * A {@link Powerup} lehetséges típusai
	 */
	public enum Type {
		PLUS_POINTS_SMALL, PLUS_POINTS_LARGE, UNVULNERABILITY, PLUS_LIFE
	}

	private Type mType;

	public Powerup() {
		this(new Vector2D(), Type.PLUS_POINTS_SMALL);
	}

	public Powerup(Vector2D position, Type type) {
		super(position, RADIUS);
		mType = type;
	}

	/**
	 * Adott valószínűségeknek megfelelően visszaadhat valamilyen típusú
	 * {@link Powerup}-ot, az esetek nagyobb részében viszont <code>null</code>
	 * -t ad vissza.
	 * 
	 * @param position
	 *            Az esetleges Powerup pozíciója
	 * @return Valamilyen Powerup, vagy null
	 */
	public static Powerup tryLuck(Vector2D position) {
		final long random = Math.round(Math.random() * 100);
		long sum = 0;
		for (Entry<Type, Integer> entry : sProbabilities.entrySet()) {
			sum += entry.getValue().longValue();
			if (sum > random) {
				return new Powerup(position, entry.getKey());
			}
		}
		return null;
	}

	public void setType(Type type) {
		mType = type;
	}

	public Type getType() {
		return mType;
	}
}
