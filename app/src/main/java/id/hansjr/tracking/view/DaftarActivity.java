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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import id.hansjr.tracking.R;
import id.hansjr.tracking.helper.ApiService;
import id.hansjr.tracking.helper.UtilsApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarActivity extends AppCompatActivity {
    EditText txtNama, txtNIM, txEmail, txtPassword;
    Button btnRegist;
    ProgressDialog loading;
    Context mContext;
    ApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        mContext = this;
        mApiService = UtilsApi.getApiService();

        initComponent();
    }

    private void initComponent() {
        txtNama = findViewById(R.id.etNama);
        txtNIM = findViewById(R.id.etNIM);
        txEmail = findViewById(R.id.etEmail);
        txtPassword = findViewById(R.id.etPassword);

        btnRegist = findViewById(R.id.btnRegister);
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(mContext, null, "Please Wait ...", true, false);
                requestRegist();
            }
        });

    }

    private void requestRegist() {
        mApiService.registRequest(txtNama.getText().toString(),
                txtNIM.getText().toString(),
                txEmail.getText().toString(),
                txtPassword.getText().toString()).
                enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            try {
                                JSONObject jsonResult = new JSONObject(response.body().string());
                                if (jsonResult.getString("error").equals("false")) {
                                    String alert = jsonResult.getString("message");
                                    Toast.makeText(mContext, alert, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(mContext, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    String alert = jsonResult.getString("message");
                                    Toast.makeText(mContext, alert, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: Error>" + t.toString());
                        loading.dismiss();
                    }
                });
    }
}
