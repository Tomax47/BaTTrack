import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class BaTTrackBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {

        String command = update.getMessage().getText();

        if (command.equals("/trace")) {
            String message = "Enter the IP address : ";
            SendMessage response = new SendMessage();
            response.setChatId(update.getMessage().getChatId().toString());
            response.setText(message);

            try{
                execute(response);
                try {
                    String ip = update.getMessage().getText();
                    URL url = new URL("http://ip-api.com/json/" + ip);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer content = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();
                    JSONObject data = new JSONObject(content.toString());
                    SendMessage dataResponse = new SendMessage();
                    dataResponse.setChatId(update.getMessage().getChatId().toString());
                    String newMessage;
                    if (ip.length() == 0) {
                        newMessage = "STATUS : FAIL";
                        dataResponse.setText(newMessage);
                        execute(dataResponse);
                    } else if (data.getString("status").equals("success")) {
                        newMessage = "STATUS : " + data.getString("status")+"\nCOUNTRY : "+data.getString("country")+"\nCountry-Code : "+data.getString("countryCode")+"\nRegion : "+data.getString("region")+"\nRegion Name : "+data.getString("regionName")+"\nCity : "+data.getString("city")+"\nZIP : "+data.getString("zip");
                        dataResponse.setText(newMessage);
                        execute(dataResponse);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public String getBotUsername() {
        return "BaTTrackBot";
    }

    @Override
    public String getBotToken() {
        return "6003971779:AAHVVxTw7p6kfs367v2XncCeetS5yeRW8N8";

    }
}
