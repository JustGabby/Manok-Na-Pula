package Gamev07;

public class Main {
	
	public static void main(String[] args) {
		Menu menu = new Menu();
		try {
			menu.start();
		} catch (IllegalStateException e) {
			System.out.println("Input is no longer available. Ending the program safely.");
		} catch (RuntimeException e) {
			System.out.println("Unexpected error: " + e.getMessage());
		} finally {
			menu.close();
		}
	}
}