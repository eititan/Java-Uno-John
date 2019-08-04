package view;

import java.awt.Color;
import java.util.Objects;

import cardModel.ActionCard;
import cardModel.NumberCard;
import cardModel.WildCard;
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

    public static UNOCard convertJson(JSONObject jsonObject) {
		Color color;
		switch (jsonObject.optString("color")) {
			case "Black":
				color = BLACK;
				break;
			case "Blue":
				color = BLUE;
				break;
			case "Green":
				color = GREEN;
				break;
			case "Red":
				color = RED;
				break;
			case "Yellow":
				color = YELLOW;
				break;
			default:
				throw new IllegalArgumentException("Invalid color " + jsonObject.optString("color"));
		}
		String value = jsonObject.optString("type");

		try {
			Integer.parseInt(value);

			return new NumberCard(color, value);
		} catch (NumberFormatException e) {
			// not a number card
		}

		if (Objects.equals(W_COLORPICKER, value) || Objects.equals(W_DRAW4PLUS, value)) {
			return new WildCard(value);
		}

        return new ActionCard(color, value);
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

	@Override
	public boolean equals(Object otherObject) {
		if (this == otherObject) {
			return true;
		}

		if (otherObject == null || getClass() != otherObject.getClass()) {
			return false;
		}

		UNOCard unoCard = (UNOCard) otherObject;
		return type == unoCard.type &&
				cardColor.equals(unoCard.cardColor) &&
				value.equals(unoCard.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cardColor, value, type);
	}
}
