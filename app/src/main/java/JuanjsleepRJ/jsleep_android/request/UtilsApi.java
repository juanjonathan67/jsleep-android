package JuanjsleepRJ.jsleep_android.request;

/**
 * API class
 */
public class UtilsApi {
    /**
     * URL of API with server's IP and port
     */
    public static final String BASE_URL_API = "http://192.168.0.123:8080/";

    /**
     * Method to get API service
     * @return Returns the retrofit client
     */
    public static BaseApiService getApiService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}

