package ru.peso4ek.weather.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.peso4ek.weather.Day;
import ru.peso4ek.weather.Weather;

import java.io.IOException;
import java.util.HashMap;

public class WebParser {

    /**
     * Возвращает список городов
     */
    public HashMap<String, String> getCities() throws IOException {
        //Получение документа HTML
        Document htmlPage = Jsoup.connect("https://pogoda.mail.ru/country/russia/")
                .userAgent("Chrome/4.0.249.0 Safari/532.5")
                .referrer("http://www.yandex.com")
                .get();

        Elements htmlCities = htmlPage.select(".city-list__val-text [href]");

        HashMap<String, String> cities = new HashMap<>();
        for (Element element : htmlCities) {
            cities.put(element.text(), element.attributes().get("href"));
        }

        return cities;
    }

    /**
     * Возвращает экземпляр погоды на конкретный парс (замер)
     */
    public Weather ParseWeather(String city) throws IOException {
        //Получение документа HTML
        Document htmlPage = Jsoup.connect("https://pogoda.mail.ru/" + city)
                .userAgent("Chrome/4.0.249.0 Safari/532.5")
                .referrer("http://www.yandex.com")
                .get();

        Elements htmlWeatherPane = htmlPage.select(".day_index");
        Elements htmlCurrentDayWeather = htmlPage.select(".information__content__wrapper");

        //Создаем погоду
        Weather currentWeather = new Weather();

        //Добавление текущего дня (первым в листе идет текущий день)
        String[] currentDayData = htmlCurrentDayWeather.text().split(" ");
        Day currentDay = new Day("Сегодня", currentDayData[0],
                currentDayData[4] + " " + currentDayData[5],
                currentDayData[6] + " " + currentDayData[7], currentDayData[11],
                currentDayData[14] + " " + currentDayData[15], currentDayData[18], currentDayData[27]);
        currentWeather.insertDay(currentDay);

        //Заполнение дней начиная с "Завтра"
        for (Element element : htmlWeatherPane) {
            String[] dayData = element.text().split(" ");

            if (dayData[0].equals("Завтра")) {
                Day day = new Day(dayData[0], dayData[1] + " " + dayData[2],
                        dayData[3], dayData[4] + " " + dayData[5],
                        dayData[6], dayData[7] + " " + dayData[8], dayData[9], dayData[10]);
                currentWeather.insertDay(day);
                continue;
            }

            Day day = new Day(dayData[0] + " " + dayData[1], dayData[2] + " " + dayData[3],
                    dayData[4], dayData[5] + " " + dayData[6],
                    dayData[7], dayData[8] + " " + dayData[9], dayData[10], dayData[11]);
            currentWeather.insertDay(day);
        }

        return currentWeather;
    }
}
