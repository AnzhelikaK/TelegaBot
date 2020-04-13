import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Weather {
    private static String CityName;

    public static void setCityName(String cityNameWeather) {
        CityName = cityNameWeather;
        System.out.println(CityName);
    }

    public static String getCityName() {
        return CityName;
    }


    public static String getWeather(Model model) throws IOException {
        System.out.println(CityName + " пришло в метод  getWeather");
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + CityName + "&units=metric&appid=fe982945678dccb35c624850b3029e0f");
        // сканер, который может анализировать строки. ему скармливаем строку
        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";
        while (in.hasNext()) {
            result += in.nextLine();
        }
        // создали json объект из всей строчки
        JSONObject object = new JSONObject(result);
        model.setName(object.getString("name"));
        // берем из всего JSON маленький объект main  {}, так как в нем находятся значения температуры и влажности
        JSONObject main = object.getJSONObject("main");
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));
        // значения main(какая погода сейчас - дождь,солнце)  и значет погоды находятся в МАССИВЕ "weather": [ ]
        JSONArray getArray = object.getJSONArray("weather");
        for (int i = 0; i < getArray.length(); i++) {
            JSONObject obj = getArray.getJSONObject(i);
            model.setIcon((String) obj.get("icon"));
            model.setMain((String) obj.get("main"));
        }
        return "City: " + model.getName() + "\n"
                + "Temperature: " + model.getTemp() + " C" + '\u00B0' + "\n" +
                "Humidity: " + model.getHumidity() + " %" + "\n" +
                "http://openweathermap.org/img/wn/" + model.getIcon() + "@2x.png";
    }


}
