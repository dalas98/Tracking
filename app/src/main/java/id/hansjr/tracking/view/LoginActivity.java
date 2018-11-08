package id.hansjr.tracking.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import id.hansjr.tracking.R;
import id.hansjr.tracking.helper.ApiService;
import id.hansjr.tracking.helper.SharedPrefManager;
import id.hansjr.tracking.helper.UtilsApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText eNIM;
    EditText ePass;
    Button btnLogin;
    Button btnRegist;
    ProgressDialog loading;

    Context mContext;
    ApiService mApiService;
    SharedPrefManager sharedPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPrefManager = new SharedPrefManager(this);

        if (sharedPrefManager.getSpLoginValidate()){
            if (sharedPrefManager.getSpRole().equals("2")){
                startActivity(new Intent(LoginActivity.this, Dashboard.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }else if (sharedPrefManager.getSpRole().equals("1")){
                startActivity(new Intent(LoginActivity.this, DosenDashboard.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        }

        mContext = this;
        mApiService = UtilsApi.getApiService();

        initComponent();
    }

    private void initComponent() {

        eNIM = findViewById(R.id.txtNim);
        ePass = findViewById(R.id.txtPass);

        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(mContext,null,"Please Wait bruh .. ",true,false);
                requestLogin();
            }
        });

        btnRegist = findViewById(R.id.btnRegister);
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext,DaftarActivity.class));
            }
        });
    }

    private void requestLogin() {
        mApiService.loginRequest(eNIM.getText().toString(),ePass.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try{
                                JSONObject jsonResult = new JSONObject(response.body().string());
                                if (jsonResult.getString("error").equals("false")){
                                    Toast.makeText(mContext, "Login Success", Toast.LENGTH_SHORT).show();
                                    String name = jsonResult.getJSONObject("data").getString("fullname");
                                    String email = jsonResult.getJSONObject("data").getString("email");
                                    String username = jsonResult.getJSONObject("data").getString("username");
                                    String role = jsonResult.getJSONObject("data").getString("role");
                                    Integer iduser = jsonResult.getJSONObject("data").getInt("id_user");
                                    FirebaseMessaging.getInstance().subscribeToTopic(username);
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_NAME, name);
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_EMAIL, email);
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_USERNAME, username);
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_ROLE, role);
                                    sharedPrefManager.saveSPInt(SharedPrefManager.SP_ID_USER, iduser);
                                    sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_LOGIN_VALIDATE, true);
                                    if(role.equals("2")){
                                        startActivity(new Intent(mContext, Dashboard.class)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                        finish();
                                    }else if(role.equals("1")){
                                        startActivity(new Intent(mContext, DosenDashboard.class)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                        finish();
                                    }
                                }else {
                                    String error_message = jsonResult.getString("error_msg");
                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else{
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug","onFailure: Error>"+t.toString());
                        loading.dismiss();
                    }
                });
    }
}
