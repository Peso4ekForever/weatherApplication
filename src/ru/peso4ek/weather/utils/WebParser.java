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
    private static HashMap<String, String> cities = new HashMap<>();

    /**
     * Возвращает список городов
     */
    public static HashMap<String, String> getCities() throws IOException {
        //Получение документа HTML
        Document htmlPage = Jsoup.connect("https://pogoda.mail.ru/country/russia/")
                .userAgent("Chrome/4.0.249.0 Safari/532.5")
                .referrer("http://www.yandex.com")
                .get();

        Elements htmlCities = htmlPage.select(".city-list__val-text [href]");

        for (Element element : htmlCities) {
            cities.put(element.text(), element.attributes().get("href"));
        }

        return cities;
    }

    /**
     * Возвращает экземпляр погоды на конкретный парс (замер)
     */
    public Weather ParseWeather(String city) throws IOException {
        //Создаем погоду
        Weather currentWeather = new Weather();

        //Получение документа HTML
        Document htmlPage = Jsoup.connect("https://pogoda.mail.ru/" + city)
                .userAgent("Chrome/4.0.249.0 Safari/532.5")
                .referrer("http://www.yandex.com")
                .get();

        Elements htmlWeatherPane = htmlPage.select(".day_index");
//        Elements htmlCurrentDayWeather = htmlPage.select(".information__content__wrapper");

        //Получение документа HTML
        Document htmlPage24hours = Jsoup.connect("https://pogoda.mail.ru" + city + "24hours/")
                .userAgent("Chrome/4.0.249.0 Safari/532.5")
                .referrer("http://www.yandex.com")
                .get();

        Elements htmlWeather24Hours = htmlPage24hours.select(".p-forecast__current");
        String curDayTemperatureMax = htmlWeather24Hours.select(".p-forecast__temperature-value").text();
        Elements curDayData = htmlWeather24Hours.select(".p-forecast__data");
        String curDayTemperatureDescription = htmlWeather24Hours.select(".p-forecast__description").text();

        String curDayTemperatureMin = curDayData.get(0).text();
        String curDayPressure = curDayData.get(1).text();
        String curDayWind = curDayData.get(2).text();
        String curDayWet = curDayData.get(3).text();

        String curDayUV = "0";

        try {
            curDayUV = curDayData.get(4).text();
        } catch (Exception ex) {
            System.out.println("UV data does not exist!");
        }

        //Добавление текущего дня (первым в листе идет текущий день)
        Day currentDay = new Day("Сегодня", curDayTemperatureMax + " " + curDayTemperatureMin,
                curDayTemperatureDescription,
                curDayPressure, curDayWet,
                curDayWind, curDayUV, " ");
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

    public static String getCityUrlByName(String name) {
        return cities.get(name);
    }
}
