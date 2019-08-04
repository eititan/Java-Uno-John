package cardModel;

import java.awt.Color;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;

import interfaces.GameConstants;
import view.UNOCard;

/**
 * This Class contains standard 108-Card stack
 */
public class CardDeck implements GameConstants {

	private LinkedList<UNOCard> unoCards;
	
	public CardDeck(){

		//Initialize Cards

		unoCards = new LinkedList<>();
		
		addCards();
		Collections.shuffle(unoCards);
	}
	
	
	//Create 108 cards for this CardDeck
	private void addCards() {
		for(Color color:UNO_COLORS){
			
			//Create 76 NumberCards --> doubles except 0s.
			for(int num : UNO_NUMBERS){
				int i=0;
				do{
					unoCards.add(new NumberCard(color, Integer.toString(num)));
					i++;
				}while(num!=0 && i<2);
			}
			
			//Create 24 ActionCards --> everything twice
			for(String type : ActionTypes){
				for(int i=0;i<2;i++)
					unoCards.add(new ActionCard(color, type));
			}					
		}		
		
		for(String type : WildTypes){
			for(int i=0;i<4;i++){
				unoCards.add(new WildCard(type));
			}
		}
	}
	
	public UNOCard drawCard() {
		if(unoCards.getFirst() == null) {
			//handle better
			return null;
		}
		return unoCards.removeFirst();
	}
	
	public boolean isEmpty() {
        return 0 == unoCards.size();
    }

    public void shuffleIn(Deque<UNOCard> discard) {
        unoCards.addAll(discard);
        Collections.shuffle(unoCards);
    }
}
