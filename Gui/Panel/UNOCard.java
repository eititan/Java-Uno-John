package Gui.Panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import Gui.Interfaces.CardInterface;
import Gui.Interfaces.UNOConstants;

public class UNOCard extends JPanel implements CardInterface, UNOConstants {
	
	private Color cardColor = null;
	private String type = null;
	private Border defaultBorder = BorderFactory.createEtchedBorder(WHEN_FOCUSED, Color.white, Color.gray);
	private Border focusedBorder = BorderFactory.createEtchedBorder(WHEN_FOCUSED, Color.black, Color.gray);

	public UNOCard(){
	}
	
	public UNOCard(Color cardColor, String cardType){
		this.cardColor = cardColor;
		this.type = cardType;
		this.setPreferredSize(CARDSIZE);
		this.setBorder(defaultBorder);
		this.addMouseListener(new MouseHandler());
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		int cardWidth = CARDSIZE.width;
		int cardHeight = CARDSIZE.height;
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, cardWidth, cardHeight);
		int margin = 5;
		g2.setColor(cardColor);
		g2.fillRect(margin, margin, cardWidth-2*margin, cardHeight-2*margin);
		g2.setColor(Color.white);
		AffineTransform org = g2.getTransform();
		g2.rotate(45,cardWidth*3/4,cardHeight*3/4);
		g2.fillOval(0,cardHeight*1/4,cardWidth*3/5, cardHeight);
		g2.setTransform(org);		
		
		Font defaultFont = new Font("Helvetica", Font.BOLD, 20);		
		FontMetrics fm = this.getFontMetrics(defaultFont);
		int StringWidth = fm.stringWidth(type)/2;
		int FontHeight = defaultFont.getSize()*1/3;
		g2.setColor(cardColor);
		g2.setFont(defaultFont);
		g2.drawString(type, cardWidth/2-StringWidth, cardHeight/2+FontHeight);
		
		defaultFont = new Font("Helvetica", Font.ITALIC, cardWidth/5);		
		fm = this.getFontMetrics(defaultFont);
		StringWidth = fm.stringWidth(type)/2;
		FontHeight = defaultFont.getSize()*1/3;
		g2.setColor(Color.white);
		g2.setFont(defaultFont);
		g2.drawString(type, 2*margin,5*margin);
	}	

	class MouseHandler extends MouseAdapter {
		
		public void mouseEntered(MouseEvent e){
			setBorder(focusedBorder);
		}
		
		public void mouseExited(MouseEvent e){
			setBorder(defaultBorder);
		}
	}
	
	public void setCardSize(Dimension newSize){
		this.setPreferredSize(newSize);
	}
	
	public void setColor(Color newColor) {
		this.cardColor = newColor;
	}

	public Color getColor() {
		return cardColor;
	}

	@Override
	public void setType(String newType) {
		this.type = newType;
	}

	@Override
	public String getType() {
		return type;
	}

	public String getColorString() {
		return cardColor.toString();
	}

	@Override
	public String toString(){
		return cardColor + " " + getType();
	}
}
