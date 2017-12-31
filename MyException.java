package lber;

@SuppressWarnings("serial")
class MapFileException extends Exception{
	/**
	 * EFFECTS: constructs a(n) MapFileException object, with message s;
	 */
	public MapFileException(String s) {
		super(s);
	}
}

@SuppressWarnings("serial")
class NoPathException extends Exception{
	/**
	 * EFFECTS: constructs a(n) NoPathException object, with message s;
	 */
	public NoPathException(String s) {
		super(s);
	}
}
