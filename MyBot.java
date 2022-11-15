import org.jibble.pircbot.*;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class MyBot extends PircBot 
{
    
    private static boolean asked = false;

    public MyBot() 
    {
        this.setName("MyBot");
    }
    
    /* (non-Javadoc)
     * @see org.jibble.pircbot.PircBot#onMessage(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (message.equalsIgnoreCase("time")) 
        {
            String time = new java.util.Date().toString();
            sendMessage(channel, sender + ": The time is now " + time);
        }  
        else if (message.equalsIgnoreCase("hello")) 
        {
            sendMessage(channel, sender + ": Hey " + sender + "!");
        }  
        else if (message.equalsIgnoreCase("weather")) 
        {  
            sendMessage(channel, sender + ": Enter location. ");
            asked = true;
        } 
        else
        {
        String location = message;
        try {
            weather(location);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            if (asked == true && weather(location) == 0.0)
            {
                sendMessage(channel, sender + ": Not found.");
                asked = false;
            }
            else
            {
                try {
                    sendMessage(channel, sender + ": Temperature in " + location + " is " + weather(location) + " F");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                asked = false;
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        }
        


            
    }

    public static double weather(String location) throws IOException
    {
        String apiKey = "b186989e53ed00694742de8fa6502638";
        String apiEndpoint = "https://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + apiKey;

        //create URL, connection. and GET
        URL url = new URL(apiEndpoint);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        //inputStream
        int status = con.getResponseCode();

        if (status == 404)
        {
            return 0.0;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) 
        {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();

        //System.out.print(content);

        //turn json into string
        String json = content.toString();
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);

        //get temperature
        JsonElement main = jsonObject.get("main");
        String temp;
        temp = ((JsonObject) main).get("temp").toString();

        //calculate temperature in Fahrenheit
        double tempKelvin = Double.parseDouble(temp); 
        double tempFahrenheit = 1.8 * (tempKelvin - 273) + 32;

        return tempFahrenheit; 
    }

    
}