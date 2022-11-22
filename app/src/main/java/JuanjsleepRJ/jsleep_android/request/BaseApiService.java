package JuanjsleepRJ.jsleep_android.request;

import JuanjsleepRJ.jsleep_android.model.*;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.Call;
import retrofit2.http.Query;

public interface BaseApiService {
    @GET("account/{id}")
    Call<Account> getAccount(@Path("id") int id);

    @POST("account/login")
    Call<Account> requestLogin(@Query("email") String email, @Query("password") String password);

    @POST("account/register")
    Call<Account> requestRegister(@Query("name") String name, @Query("email") String email, @Query("password") String password);

    @GET("price/{id}")
    Call<Price> getPrice (@Path("id") int id);

    @GET("renter/{id}")
    Call<Renter> getRenter (@Path("id") int id);

    @GET("room/{id}")
    Call<Room> getRoom (@Path("id") int id);
}
