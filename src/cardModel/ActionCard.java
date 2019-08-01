package cardModel;

import java.awt.Color;

import view.UNOCard;

public class ActionCard extends UNOCard{
	
	private int Function = 0;
	
	public ActionCard(Color cardColor, String cardValue){
		super(cardColor,ACTION, cardValue);		
	}	
}
