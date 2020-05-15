package Service;

import DAO.WeatherDAO;
import DAO.WeatherModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;

import java.sql.Date;
import java.time.LocalDate;
import java.util.function.Supplier;
import java.util.stream.Stream;


public class WeatherService {
    private String nameOfCity;
    private Supplier<LocalDate> s = LocalDate::now;  // ради использование функционального интерфейса
    private LocalDate date = s.get();
    private Date dateForDB = Date.valueOf(date);
    WeatherModel model;

    public WeatherService(String nameOfCity) {
        this.nameOfCity = nameOfCity;
    }

    public String getWeather() throws IOException {

        if (!checkDB()) {
            System.out.println("зашли получать ответ с сайта для " + nameOfCity);
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + nameOfCity + "&units=metric&appid=fe982945678dccb35c624850b3029e0f");

            // вариант 1 получения json в виде String
            Reader reader = new InputStreamReader((InputStream) url.getContent());
            BufferedReader br = new BufferedReader(reader);
            Stream<String> lines = br.lines();
            StringBuilder result = lines.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append);   // or 1
            //     String result = lines.reduce("", (r,c)->r+c);           // or 2

            // вариант 2 получения json в виде String

/*
        Scanner in = new Scanner((InputStream) url.getContent());  // сканер, который может анализировать строки.
        String result = "";
        while (in.hasNext()) result += in.nextLine();
*/
            workWithJson(result.toString());

        } else System.out.println("Ответ был из БД получен для " + nameOfCity);
        return "City: " + model.getName() + "\n"
                + "Date: " + model.getDate() + "\n"
                + "Temperature: " + model.getTemp() + " C" + '\u00B0' + "\n" +
                "Humidity: " + model.getHumidity() + " %" + "\n" + "Description: " + model.getDescription() + "\n" +
                "http://openweathermap.org/img/wn/" + model.getIcon() + "@2x.png";
    }


    private boolean checkDB() {
        WeatherDAO weatherDAO = new WeatherDAO();    // ? нужно ли создавать тут этот объект или может сделать его методы статичные
        model = weatherDAO.getCityByDate(dateForDB, nameOfCity);
        if (model.getName() != null) {
            return true;
        } else return false;
    }

    private void workWithJson(String result) {
        JSONObject object = new JSONObject(result);               // создали json объект из всей строчки
        model.setName(object.getString("name"));      // уже установили взяб из текстового сообщения
        model.setDate(dateForDB);
        JSONObject main = object.getJSONObject("main");           // берем из всего JSON маленький объект main  {}, так как в нем находятся значения температуры и влажности
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));
        JSONArray getArray = object.getJSONArray("weather"); // значения main(description какая погода сейчас - дождь,солнце)  и значет погоды находятся в МАССИВЕ "weather": [ ]
        for (int i = 0; i < getArray.length(); i++) {
            JSONObject obj = getArray.getJSONObject(i);
            model.setIcon((String) obj.get("icon"));
            model.setDescription((String) obj.get("main"));
        }

        // write weatherModel into DB
        WeatherDAO weatherDAO = new WeatherDAO();
        weatherDAO.add(model);
    }


}
