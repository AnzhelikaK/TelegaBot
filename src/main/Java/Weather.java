import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;

import java.sql.Date;
import java.time.LocalDate;
import java.util.function.Supplier;
import java.util.stream.Stream;


public class Weather {
    private Model model;
    private Supplier<LocalDate> s = LocalDate::now;  // ради использование функционального интерфейса
    private LocalDate date = s.get();
    private Date dateForDB = Date.valueOf(date);


    public Weather(String nameOfCity) {
        this.model = new Model();
        model.setNameOfCity(nameOfCity.toLowerCase());
    }

    public String getWeather() throws IOException {

        if (!checkDateInDB(model, dateForDB)) {
            System.out.println("зашли получать ответ с сайта для " + model.getNameOfCity());
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + model.getNameOfCity() + "&units=metric&appid=fe982945678dccb35c624850b3029e0f");

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
            writeDateInDB();
        } else System.out.println("Ответ был из БД получен для " + model.getNameOfCity());
        return "City: " + model.getNameOfCity() + "\n"
                + "Temperature: " + model.getTemp() + " C" + '\u00B0' + "\n" +
                "Humidity: " + model.getHumidity() + " %" + "\n" + "Description: " + model.getDescription() + "\n" +
                "http://openweathermap.org/img/wn/" + model.getIcon() + "@2x.png";
    }


    private boolean checkDateInDB(Model model, Date dateForDB) {
        DataBase db = new DataBase();
        db.getConnectDB();
        return db.answerOfDB(model, dateForDB);
    }

    private void workWithJson(String result) {
        JSONObject object = new JSONObject(result);               // создали json объект из всей строчки
        //    model.setNameOfCity(object.getString("name"));      // уже установили взяб из текстового сообщения
        JSONObject main = object.getJSONObject("main");           // берем из всего JSON маленький объект main  {}, так как в нем находятся значения температуры и влажности
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));
        JSONArray getArray = object.getJSONArray("weather"); // значения main(description какая погода сейчас - дождь,солнце)  и значет погоды находятся в МАССИВЕ "weather": [ ]
        for (int i = 0; i < getArray.length(); i++) {
            JSONObject obj = getArray.getJSONObject(i);
            model.setIcon((String) obj.get("icon"));
            model.setDescription((String) obj.get("main"));
        }
    }

    private void writeDateInDB() {
        DataBase db = new DataBase();
        db.getConnectDB();
        db.writeInDB(model, dateForDB);
        db.closeDB();
    }

}
