package com.heqingbao.microweather.service;

import com.heqingbao.microweather.vo.Weather;
import com.heqingbao.microweather.vo.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherReportServiceImpl implements WeatherReportService {

    @Autowired
    private DataClient dataClient;

    @Override
    public Weather getDataByCityId(String cityId) {
        WeatherResponse resp = dataClient.getDataByCityId(cityId);
        Weather data = null;
        if (resp != null) {
            data = resp.getData();
        }
        return data;
    }
}