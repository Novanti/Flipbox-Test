package API;

import model.City;
import model.Weather;
import retrofit2.Call;
import retrofit2.http.GET;
/**
 * Created by Novanti on 17/09/2016.
 */
public interface RetrofitAPIEndpointInterface {
    @GET("api")
    Call<City> getCityInfo();

    @GET("api")
    Call<Weather> getWeather();
}
