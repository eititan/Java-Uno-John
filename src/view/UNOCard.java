package view;

import java.awt.Color;

import interfaces.UNOConstants;

public abstract class UNOCard implements UNOConstants {
	
	private Color cardColor = null;
	private String value = null;
	private int type = 0;
	
	public UNOCard(){
		
	}
	
	public UNOCard(Color cardColor, int cardType, String cardValue){
		this.cardColor = cardColor;
		this.type = cardType;
		this.value = cardValue;
	}
	
	public void setColor(Color newColor) {
		this.cardColor = newColor;
	}

	public Color getColor() {
		return cardColor;
	}

	public void setValue(String newValue) {
		this.value = newValue;		
	}
	
	public String getValue() {
		return value;
	}
	
	public void setType(int newType) {
		this.type = newType;
	}

	public int getType() {
		return type;
	}
}
