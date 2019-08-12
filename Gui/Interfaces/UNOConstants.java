package Gui.Interfaces;
import java.awt.Color;

public interface UNOConstants {

	//Colors
	Color RED = new Color(192,80,77) {
		@Override
		public String toString() {
			return "Red";
		}
	};
	Color BLUE = new Color(31,73,125) {
		@Override
		public String toString() {
			return "Blue";
		}
	};
	Color GREEN = new Color(0,153,0) {
		@Override
		public String toString() {
			return "Green";
		}
	};
	Color YELLOW = new Color(255,204,0) {
		@Override
		public String toString() {
			return "Yellow";
		}
	};

	Color BLACK = new Color(0,0,0) {
		@Override
		public String toString() {
			return "Black";
		}
	};
	
	//Types
	public static int NUMBERS = 1;
	public static int ACTION = 2;
	public static int WILD = 3;

	//ActionCard Functions
	String REVERSE = "reverse";
	String SKIP	= "skip";
	String DRAW2PLUS = "2+";
	
	//Wild card functions
	String W_COLORPICKER = "W";
	String W_DRAW4PLUS = "4+";	
}
