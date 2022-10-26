package ru.peso4ek.weather;

public class Day {
    private final String date;
    private final String temperature;
    private final String description;
    private final String pressure;
    private final String wet;
    private final String wind;
    private final String uvIndex;
    private final String downfallChance;

    /**
     * Конструктор дня
     */
    public Day(String date, String temperature, String description, String pressure, String wet, String wind, String uvIndex, String downfallChance) {
        this.date = date;
        this.temperature = temperature;
        this.description = description;
        this.pressure = pressure;
        this.wet = wet;
        this.wind = wind;
        this.uvIndex = uvIndex;
        this.downfallChance = downfallChance;
    }

    /**
     * Возвращает календарную дату дня
     */
    public String getDate() {
        return date;
    }

    /**
     * Возвращает температуру дня
     */
    public String getTemperature() {
        return temperature;
    }

    /**
     * Возвращает краткое описание погоды
     */
    public String getDescription() {
        return description;
    }

    /**
     * Возвращает давление в мм. рт. столба.
     */
    public String getPressure() {
        return pressure;
    }

    /**
     * Возвращает влажность в процентах
     */
    public String getWet() {
        return wet;
    }

    /**
     * Возвращает силу ветра
     */
    public String getWind() {
        return wind;
    }

    /**
     * Возвращает индекс ультрафиолета
     */
    public String getUvIndex() {
        return uvIndex;
    }

    /**
     * Возвращает шанс выпадения осадков
     */
    public String getDownfallChance() {
        return downfallChance;
    }
}
