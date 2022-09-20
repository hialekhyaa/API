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

public class weather
{
    public static void main(String[] args) throws IOException
    {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter city name: ");
        String cityName = input.nextLine();

        String apiKey = "b186989e53ed00694742de8fa6502638";
        String apiEndpoint = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + apiKey;

        //create URL, connection. and GET
        URL url = new URL(apiEndpoint);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        //inputStream
        int status = con.getResponseCode();

        if (status == 404)
        {
            System.out.println("That city was not found.");
            System.exit(404);
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
       
        //display results
        System.out.printf("The temperature in %s is %.1f F.\n", cityName, tempFahrenheit);

    }

    
}