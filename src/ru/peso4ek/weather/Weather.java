package ru.peso4ek.weather;

import java.util.ArrayList;
import java.util.List;

public class Weather {
    private List<Day> days = new ArrayList<>();

    /**
     * Внесение дня в массив days
     */
    public void insertDay(Day day) {
        days.add(day);
    }

    /**
     * Возвращает день по индексу из массива days
     */
    public Day getDay(int index) {
        return days.get(index);
    }

    /**
     * Вовзращает размер массива с днями
     */
    public int getDaysListSize(){
        return days.size();
    }
}
