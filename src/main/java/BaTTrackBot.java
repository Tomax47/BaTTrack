import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BaTTrackBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText();

            if (command.equals("/trace")) {
                String message = "Enter the IP address : ";
                SendMessage response = new SendMessage();
                response.setChatId(update.getMessage().getChatId().toString());
                response.setText(message);

                try{
                    execute(response);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

        } else if (update.getMessage().getReplyToMessage() != null && update.getMessage().getReplyToMessage().getText().equals("Enter the IP address: ")) {
            try {
                SendMessage dataResponse = new SendMessage();
                dataResponse.setChatId(update.getMessage().getChatId().toString());
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

                String newMessage;
                if (data.getString("status").equals("success")) {
                    newMessage = "STATUS : " + data.getString("status")+"\nCOUNTRY : "+data.getString("country")+"\nCountry-Code : "+data.getString("countryCode")+"\nRegion : "+data.getString("region")+"\nRegion Name : "+data.getString("regionName")+"\nCity : "+data.getString("city")+"\nZIP : "+data.getString("zip");
                    dataResponse.setText(newMessage);

                } else {
                    newMessage = "STATUS : FAIL";
                    dataResponse.setText(newMessage);
                }

                try {
                    execute(dataResponse);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
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
        //Here must be a toekn to return! 
        return null;

    }

}
