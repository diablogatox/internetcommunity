package com.orfid.internetcommunity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class GameJSONParser {

	List<GameItem> listArray;
	
	public List<GameItem> parse(JSONObject jObject) {
		
		JSONArray jGames = null;
		try {			
			jGames = jObject.getJSONArray("data");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return getGames(jGames);
	}
	
	
	private List<GameItem> getGames(JSONArray jGames) {
		int gameCount = jGames.length();
		List<GameItem> gameList = new ArrayList<GameItem>();
		GameItem game = null;	

		for(int i=0; i<gameCount;i++) {
			try {
				game = getFriend((JSONObject)jGames.get(i));
				gameList.add(game);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return gameList;
	}
	
	private GameItem getFriend(JSONObject jGame){

		GameItem game = new GameItem();
		String id = "";
		String name = "";
		
		try {
			id = jGame.getString("id");
			name = jGame.getString("name");

			game.setId(id);
			game.setName(name);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return game;
	}
}
