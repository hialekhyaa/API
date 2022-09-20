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

public class fruit
{
    public static void main(String[] args) throws IOException
    {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter fruit: ");
        String fruitName = input.nextLine();

        String apiEndpoint = "https://www.fruityvice.com/api/fruit/" + fruitName;

        URL url = new URL(apiEndpoint);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        //inputStream
        int status = con.getResponseCode();

        if (status == 404)
        {
            System.out.println("The fruit was not found.");
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


        //get fruit name and info
        String name = jsonObject.get("name").toString();

        JsonElement nutritions = jsonObject.get("nutritions");
        String carbs = ((JsonObject) nutritions).get("carbohydrates").toString();
        String protein = ((JsonObject) nutritions).get("protein").toString();
        String fat = ((JsonObject) nutritions).get("fat").toString();
        String calories = ((JsonObject) nutritions).get("calories").toString();
        String sugar = ((JsonObject) nutritions).get("sugar").toString();

        System.out.println("Nutritional information per 100g for " + name);
        System.out.println("Carbohydrates: " + carbs);
        System.out.println("Protein: " + protein);
        System.out.println("Fat: " + fat);
        System.out.println("Calories: " + calories);
        System.out.println("Sugar: " + sugar);

    }
}
