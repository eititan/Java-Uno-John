package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;

import Gui.GameObjects.Player;
import MessageFactory.MessageParser;
import MessageFactory.MessageSender;
import org.json.JSONObject;

public class ClientSocket implements Runnable {

	private PrintWriter out;
	private BufferedReader in;
	private MessageParser parser;
	private Player player;
	public MessageSender messageMan = new MessageSender();
	
	public ClientSocket(String address, int port, Player player) {

		try { 
			Socket socket = new Socket(address, port);
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			this.player = player;
			parser = new MessageParser(this.player);
			
			JSONObject login = messageMan.loginJson(player.username);
			System.out.println(login.toString());
			sendAction(login);

			JSONObject join = messageMan.joinGameJson();
			System.out.println(join.toString());
			sendAction(join);

		} catch (UnknownHostException e) {
			System.out.println("Unknown Host: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			

			//while ((message = in.readLine()) != null) {
			while (true) {
				String message = in.readLine();
				
				System.out.println(message);
				
				if(message.contains("notify")) {
					String drilledJSON = new JSONObject(message).getJSONObject("notify").toString();
					parseNotification(drilledJSON);
				}
			}

		} catch (UnknownHostException e) {
			System.out.println("Unknown Host: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void parseNotification(String drilledJSON){
		Iterator<String> keys = new JSONObject(drilledJSON).keys();
		String key = keys.next();

		System.out.println("key: " + key + " -> drilled JSON: " + drilledJSON);
		switch(key){
			case "draw card":
				parser.drawCard(drilledJSON);
				break;
			case "drew card":
				parser.drewCard(drilledJSON);
				break;
			case "played card":
				parser.playedCard(drilledJSON);
				break;
			case "called uno":
				parser.calledUno(drilledJSON);
				break;
			case "game will started":
				parser.gameWillStart(drilledJSON);
				break;
			case "game started":
				parser.gameStarted(drilledJSON);
				break;
			case "game ended":
				parser.gameEnded(drilledJSON);
				break;
			case "client joined":
				parser.clientJoined(drilledJSON);
				break;
			case "color changed":
				parser.colorChanged(drilledJSON);
				break;
			case "player skipped":
				parser.playerSkipped(drilledJSON);
				break;
			case "reversed":
				parser.reversed(drilledJSON);
				break;
			case "turn changed":
				parser.turnChanged(drilledJSON);
				break;
			case "winner":
				parser.winner(drilledJSON);
				break;
			case "reshuffle": 
				parser.reshuffle(drilledJSON);
				break;
			case "your turn":
				player.setIsTurn();
				parser.yourTurn(drilledJSON);
				break;
			case "card count":
				parser.cardCount(drilledJSON);
			default:
				System.out.println("Error in switch! Beep boop, unhandled notify key: " + key);
		}
	}

	public void sendAction(JSONObject action) {
		out.println(action.toString());
		out.flush();
	}

	public static void main(String[] args) {
		Player player = new Player("testUser");
		new Thread(new ClientSocket("127.0.0.1", 9886, player = new Player("testUser"))).start();
		
	}

}