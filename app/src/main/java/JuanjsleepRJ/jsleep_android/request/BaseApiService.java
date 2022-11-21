package JuanjsleepRJ.jsleep_android.request;

import JuanjsleepRJ.jsleep_android.model.*;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.Call;

public interface BaseApiService {
    @GET("account/{id}")
    Call<Account> getAccount(@Path("id") int id);

    @GET("price/{id}")
    Call<Price> getPrice (@Path("id") int id);

    @GET("renter/{id}")
    Call<Renter> getRenter (@Path("id") int id);

    @GET("room/{id}")
    Call<Room> getRoom (@Path("id") int id);
}
