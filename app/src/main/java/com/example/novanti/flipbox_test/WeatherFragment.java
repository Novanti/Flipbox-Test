package com.example.novanti.flipbox_test;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.Locale;

import API.RetrofitAPIEndpointInterface;
import butterknife.ButterKnife;
import model.City;
import model.Weather;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WeatherFragment extends Fragment {
    TextView name;
    TextView coord;
    TextView population;
    TextView country;
    TextView sys;
    TextView description;

    Handler handler;

    private Retrofit retrofit;
    private ProgressDialog dialog;
    public static final String BASE_API_URL = "http://api.openweathermap.org/data/2.5/forecast/city?id=1642911&APPID=18462b55353f6635d44b16387d6616cc/";

    public WeatherFragment() {
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_weather_fragment, container, false);
        name = (TextView) rootView.findViewById(R.id.city_field);
        coord = (TextView) rootView.findViewById(R.id.updated_field);
        population = (TextView) rootView.findViewById(R.id.details_field);
        country = (TextView) rootView.findViewById(R.id.current_temperature_field);
        sys = (TextView) rootView.findViewById(R.id.sys);
        description = (TextView) rootView.findViewById(R.id.description);
        updateWeatherData();
        initializeRetrofit();
        return rootView;
    }
    private void initializeRetrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void updateWeatherData() {
        new Thread() {
            public void run() {
                RetrofitAPIEndpointInterface apiService = retrofit.create(RetrofitAPIEndpointInterface.class);
                Call<City> result = apiService.getCityInfo();
                result.enqueue(new Callback<City>() {
                    @Override
                    public void onResponse(Call<City> call, Response<City> response) {
                        try {
                            name.setText(response.body().getName());
                            coord.setText(response.body().getCoord().toString());
                            population.setText(response.body().getPopulation());
                            country.setText(response.body().getCountry());
                            sys.setText(response.body().getSys().toString());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<City> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
                Call<Weather> weather = apiService.getWeather();
                weather.enqueue(new Callback<Weather>() {
                    @Override
                    public void onResponse(Call<Weather> call, Response<Weather> response) {
                        try {
                            description.setText(response.body().getDescription());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<Weather> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        };
    }
}
