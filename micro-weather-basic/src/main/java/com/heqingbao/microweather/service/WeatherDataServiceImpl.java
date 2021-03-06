package com.heqingbao.microweather.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heqingbao.microweather.vo.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Service
public class WeatherDataServiceImpl implements WeatherDataService {

    private static final String WEATHER_URI = "http://wthrcdn.etouch.cn/weather_mini?";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public WeatherResponse getDataByCityId(String cityId) {
        String uri = WEATHER_URI + "citykey=" + cityId;
        return doGetWeather(uri);
    }

    @Override
    public WeatherResponse getDataByCityName(String cityName) {
        String uri = WEATHER_URI + "city=" + cityName;
        return doGetWeather(uri);
    }

    @Override
    public void syncDataByCityId(String cityId) {
        String uri = WEATHER_URI + "citykey=" + cityId;
        saveWeatherData(uri);
    }

    private void saveWeatherData(String uri) {
        ResponseEntity<String> respString = restTemplate.getForEntity(uri, String.class);
        if (respString.getStatusCode().value() != 200) {
            return;
        }

        String strBody = respString.getBody();

        // 数据写入缓存
        stringRedisTemplate.opsForValue().set(uri, strBody, 3, TimeUnit.HOURS);
    }

    private WeatherResponse doGetWeather(String uri) {
        WeatherResponse response = null;
        String strBody = null;

        // 先检查缓存
        if (stringRedisTemplate.hasKey(uri)) {
            strBody = stringRedisTemplate.opsForValue().get(uri);
        } else {
            ResponseEntity<String> respString = restTemplate.getForEntity(uri, String.class);
            if (respString.getStatusCode().value() != 200) {
                return null;
            }
            strBody = respString.getBody();

            // 数据写入缓存
            stringRedisTemplate.opsForValue().set(uri, strBody, 3, TimeUnit.HOURS);
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            response = mapper.readValue(strBody, WeatherResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}