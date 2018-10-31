package id.hansjr.tracking.helper;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("Login")
    Call<ResponseBody> loginRequest(@Field("username")String username,
                                    @Field("password")String password);

    @FormUrlEncoded
    @POST("register")
    Call<ResponseBody> registRequest(@Field("fullname")String name,
                                     @Field("username")String NIM,
                                     @Field("email")String email,
                                     @Field("password")String password);
}
