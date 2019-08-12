package Gui.Interfaces;

import java.awt.Color;

import Gui.Panel.InfoPanel;


public interface GameConstants extends UNOConstants {
	
	Color[] UNO_COLORS = {RED, BLUE, GREEN, YELLOW};
	Color WILD_CARDCOLOR = BLACK;
	
	int[] UNO_NUMBERS =  {0,1,2,3,4,5,6,7,8,9};	
	String[] ActionTypes = {REVERSE,SKIP,DRAW2PLUS};	
	String[] WildTypes = {W_COLORPICKER, W_DRAW4PLUS};

	int MANUAL = 2;
	
	InfoPanel infoPanel = new InfoPanel();
}
