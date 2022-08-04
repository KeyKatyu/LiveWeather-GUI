package fr.keykatyu.liveweathergui.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum WeatherCodes {

    CLEAR(Arrays.asList(0, 1, 2)),
    RAIN(Arrays.asList(61, 63, 65, 66, 67, 80, 81, 82)),
    THUNDER(Arrays.asList(95, 96, 99)),
    NO_MC_WEATHER_EXIST(Arrays.asList(45, 48, 51, 53, 55, 56, 57, 71, 73, 75, 77, 85, 86));

    private final List<Integer> weatherCodes;
    WeatherCodes(List<Integer> weatherCodes) {
        this.weatherCodes = weatherCodes;
    }

    public List<Integer> getWeatherCodes() {
        return weatherCodes;
    }

    public static WeatherCodes fromInt(int integer) {
        for (WeatherCodes weatherValue : values()) {
            if (weatherValue.weatherCodes.contains(integer)) {
                return weatherValue;
            }
        }
        throw new IllegalArgumentException(String.valueOf(integer));
    }
}
