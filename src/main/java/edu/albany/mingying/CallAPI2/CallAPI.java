package edu.albany.mingying.CallAPI2;

import java.io.IOException;
import java.util.Iterator;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CallAPI {
	public static void main(String[] args) {
		System.out.println("Calling AlphaVantage API...");
		Client client= ClientBuilder.newClient();

		// Core settings are here, put what ever API parameter you want to use
		WebTarget target= client.target("https://www.alphavantage.co/query")
		   .queryParam("function", "TIME_SERIES_WEEKLY")
		   .queryParam("symbol", "AAPL")
		   .queryParam("apikey", "8LY5VJQYBZTYS262");
		// Actually calling API here, Use HTTP GET method
		// data is the response JSON string
		String data = target.request(MediaType.APPLICATION_JSON).get(String.class);
		
		try {
			// Use Jackson to read the JSON into a tree like structure
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(data);
			
			// Make sure the JSON is an object, as said in their documents
			assert root.isObject();
			// Read the "Meta Data" property of JSON object
			JsonNode metadata = root.get("Meta Data");
			assert metadata.isObject();
			// Read "2. Symbol" property of "Meta Data" property
			if (metadata.get("2. Symbol").isValueNode()) {
				System.out.println(metadata.get("2. Symbol").asText());
			}
			// Print "4. Time Zone" property of "Meta Data" property of root JSON object
			System.out.println(root.at("/Meta Data/4. Time Zone").asText());
			// Read "Weekly Time Series" property of root JSON object
			Iterator<String> dates = root.get("Weekly Time Series").fieldNames();
			while(dates.hasNext()) {
				// Read the first date's open price
				String n = root.at("/Weekly Time Series/" + dates.next() + "/1. open").asText();
				System.out.println(Double.parseDouble(n));
				// remove break if you wan't to print all the open prices.
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
