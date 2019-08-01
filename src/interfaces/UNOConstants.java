package interfaces;

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
	int NUMBERS = 1;
	int ACTION = 2;
	int WILD = 3;

	//ActionCard Characters
	Character charREVERSE = (char) 8634;							//Decimal
	Character charSKIP    = (char) Integer.parseInt("2718",16); 	//Unicode
	
	//ActionCard Functions
	String REVERSE = charREVERSE.toString();
	String SKIP	= charSKIP.toString();
	String DRAW2PLUS = "2+";
	
	//Wild card functions
	String W_COLORPICKER = "W";
	String W_DRAW4PLUS = "4+";	
}