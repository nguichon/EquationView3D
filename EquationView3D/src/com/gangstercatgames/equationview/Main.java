package com.gangstercatgames.equationview;

/**
 * Entry point for the program. Could probably be merged with WindowManager...
 * 
 * @author Nicholas Guichon
 */
public class Main {
	public static String VERSION_STRING = "v0.19.7.29pre";

	/**
	 * Program entry point.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		while (!WindowManager.get().isClosed()) {
			WindowManager.get().Update();
		}
	}

}
