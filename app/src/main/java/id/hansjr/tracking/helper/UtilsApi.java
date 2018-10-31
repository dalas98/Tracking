package id.hansjr.tracking.helper;

public class UtilsApi {
    public static final String BASE_URL_API = "https://yusuffarhan.com/Rest_Api/";

    public static ApiService getApiService(){
        return RetrofitClient.getClient(BASE_URL_API).create(ApiService.class);
    }
}
