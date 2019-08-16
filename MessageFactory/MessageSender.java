package MessageFactory;

import org.json.JSONObject;

public class MessageSender {

    public MessageSender(){
 
    }
  
    public JSONObject playCardJson(String type, String color) {
        JSONObject topObject = new JSONObject();
        topObject.put("type", "application");

        JSONObject messageObject = new JSONObject();
        messageObject.put("module", "JavaUno");

        JSONObject actionObject = new JSONObject();
        JSONObject playCardObject = new JSONObject();

        JSONObject cardObject = new JSONObject();
        cardObject.put("type", type);
        cardObject.put("color", color);

        playCardObject.put("card", cardObject);
        actionObject.put("play card", playCardObject);

        messageObject.put("action", actionObject);
        topObject.put("message", messageObject);

        return topObject;
    }

    public JSONObject loginJson(String username) {
        JSONObject topObject = new JSONObject();
        topObject.put("type", "login");

        JSONObject loginObj = new JSONObject();
        loginObj.put("password", "Password1");
        loginObj.put("username", username);
        topObject.put("message", loginObj);

        return topObject;
    }

    public JSONObject joinGameJson() {
        JSONObject topObject = new JSONObject();
        topObject.put("type", "application");

        JSONObject messageObject = new JSONObject();
        messageObject.put("module", "JavaUno");
        messageObject.put("action", "join");

        topObject.put("message", messageObject);

        return topObject;
    }

    public JSONObject callUnoJson(String user) {
        JSONObject topObject = new JSONObject();
        topObject.put("type", "application");

        JSONObject messageObject = new JSONObject();
        messageObject.put("module", "JavaUno");

        JSONObject actionObject = new JSONObject();
        actionObject.put("call uno", user);

        messageObject.put("action", actionObject);
        topObject.put("message", messageObject);

        return topObject;
    }

    public JSONObject drawCardJson() {
        JSONObject topObject = new JSONObject();
        topObject.put("type", "application");

        JSONObject messageObject = new JSONObject();
        messageObject.put("module", "JavaUno");
        messageObject.put("action", "draw card");

        topObject.put("message", messageObject);

        return topObject;
    }

    public JSONObject changeColorJson(String color) {
        JSONObject topObject = new JSONObject();
        topObject.put("type", "application");

        JSONObject messageObject = new JSONObject();
        messageObject.put("module", "JavaUno");
        
        	JSONObject changecolorObject = new JSONObject();        
        	changecolorObject.put("color change", color);
        	
        messageObject.put("action", changecolorObject);
        topObject.put("message", messageObject);

        return topObject;
    }
}