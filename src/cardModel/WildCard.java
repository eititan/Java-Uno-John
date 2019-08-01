package cardModel;

import java.awt.Color;

import view.UNOCard;

public class WildCard extends UNOCard {
	
	private int Function = 0;
	private Color chosenColor;
	
	public WildCard(String cardValue){
		super(BLACK, WILD, cardValue);		
	}
	
	public void useWildColor(Color wildColor){
		chosenColor = wildColor;
	}
	
	public Color getWildColor(){
		return chosenColor;
	}

}