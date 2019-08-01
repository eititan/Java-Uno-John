package view;

import java.awt.Color;

import interfaces.UNOConstants;
import org.json.JSONObject;

public abstract class UNOCard implements UNOConstants {
	
	private Color cardColor;
	private String value;
	private int type;
	
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

	public JSONObject toJSON() {
	    JSONObject json = new JSONObject();
	    json.put("type", value);
	    json.put("color", cardColor);

	    return json;
	}
}
