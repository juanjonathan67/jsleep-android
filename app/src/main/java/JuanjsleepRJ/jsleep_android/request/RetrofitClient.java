package JuanjsleepRJ.jsleep_android.request;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit client to call requests
 */
public class RetrofitClient {
    /**
     * Retrofit client
     */
    private static Retrofit retrofit = null;

    /**
     * Get currently active retrofit client
     * @param baseUrl URL of client
     * @return Returns found retrofit client
     */
    public static Retrofit getClient(String baseUrl){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

