package dev.diskettefox.madridbus;

import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import dev.diskettefox.madridbus.api.ApiCall;
import dev.diskettefox.madridbus.api.ApiInterface;
import dev.diskettefox.madridbus.models.TokenModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        Button login = findViewById(R.id.lg_button);
        TextInputLayout username = findViewById(R.id.lg_TxtUser);
        TextInputLayout password = findViewById(R.id.login_Password);

        login.setOnClickListener(v -> {
            String user = "";
            String pass = "";
            if (username.getEditText() != null){
                user = username.getEditText().getText().toString();
            }

            if (password.getEditText() != null){
                pass = password.getEditText().getText().toString();
            }

            getToken(user, pass);
            finish();
        });
    }

    public void getToken(String email, String password) {
        ApiInterface apiInterface = ApiCall.callApi().create(ApiInterface.class);

        Call<TokenModel> call = apiInterface.getTokenByUser(email, password);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TokenModel> call, @NonNull Response<TokenModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TokenModel token = response.body();
                    if (token.getData() != null) {
                        if (token.getData().isEmpty()) {
                            TokenModel.Data data = token.getData().get(0);
                            Log.d("Token", data.getAccessToken());
                            ApiCall.setToken(data.getAccessToken());
                            Toast.makeText(LoginActivity.this, R.string.succesful_login, Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Log.e("API Error", "Unable to connect to database");
                    Toast.makeText(LoginActivity.this, R.string.no_connection, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TokenModel> call, @NonNull Throwable t) {
                Log.e("Call Error", "Error retrieving lines",t);
            }
        });
    }
}