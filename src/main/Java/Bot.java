import com.fasterxml.jackson.databind.util.ISO8601Utils;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    public static void main(String[] args) {
        ApiContextInitializer.init(); // инициализация API
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());  // регистрация ботаa
        } catch (TelegramApiRequestException e) {  // commit 2
            e.printStackTrace();
        }
    }


    @Override
    public void onUpdateReceived(Update update) {
        Model model = new Model();
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            System.out.println(message.getText());
            if (message.getText().contains("/W-") || message.getText().contains("/w-")) {
                Weather.setCityName(message.getText().trim().substring(3));
                try {
                    sendMsg(message, Weather.getWeather(model));
                } catch (IOException e) {
                    sendMsg(message, "City is not found");
                }

            } else
                switch (message.getText()) {
                    case "/help":
                        sendMsg(message, "What is the problem, Bro?");
                        break;
                    case "/setting":
                        sendMsg(message, "What will you set up?");
                        break;
                    case "/start":
                        sendMsg(message, "It is main menu. What do you want?");
                        break;
                    case "/weather":
                        sendMsg(message, "Write City as: /W-City (For example: /W-Minsk");
                        break;
                    default:
                        sendMsg(message, "I don't understand you / This city is not found");
                }
        }
    }


    @Override
    public String getBotUsername() {
        return "KryvapustBot";
    }

    @Override
    public String getBotToken() {
        return "1026166360:AAF4b_WcSGo1xZE5yfz2afN7mZ2xcFBuPZA";
    } // убрать последнюю А

    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);   // возможность разметки
        sendMessage.setChatId(message.getChatId().toString()); //определяем какой чат и устанавливаем его в отправляемое сообщение
        sendMessage.setReplyToMessageId(message.getMessageId()); // определяем на какой сообщение должны ответить
        sendMessage.setText(text);
        setButtons(sendMessage);
        // непосредственная отправка сообщения
        try {

            sendMessage(sendMessage);  // зачеркнуто, так как метод устаревший, но он работает
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // метод для выводв клавиатуры
    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(); // создание клавиатуры
        sendMessage.setReplyMarkup(replyKeyboardMarkup); // разметка для клавиатуры - связываем сообщение с клавиатурой
        replyKeyboardMarkup.setSelective(true); // показывать клавиатуру всем полльзователям
        replyKeyboardMarkup.setResizeKeyboard(true); // подгонять размер клавиатуры под количество кнопок
        replyKeyboardMarkup.setOneTimeKeyboard(true); // скрывать клавиатуру после нажития или нет


// Первая строчка клавиатуры
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // создание кнопок для клавиатуры
        keyboardFirstRow.add(new KeyboardButton("/start"));
        keyboardFirstRow.add(new KeyboardButton("/help"));
        keyboardFirstRow.add(new KeyboardButton("/setting"));

// Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton("/weather"));


// Добавление строчек в клавиатуру
        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);

        replyKeyboardMarkup.setKeyboard(keyboardRowList); // устанавливем этот список клавиатуре
    }
}


