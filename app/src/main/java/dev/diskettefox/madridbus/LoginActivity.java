package dev.diskettefox.madridbus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
        TextView register = findViewById(R.id.lg_register);
        TextInputLayout username = findViewById(R.id.lg_TxtUser);
        TextInputLayout password = findViewById(R.id.login_Password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        login.setOnClickListener(v -> {
            String user = username.getEditText().getText().toString();
            String pass = password.getEditText().getText().toString();
            boolean valid = true;
            if (user.isEmpty()){
                username.setErrorEnabled(true);
                username.setError(getResources().getText(R.string.message_empty_username));
                valid = false;
            } else {
                if (!user.contains("@") || !user.contains(".")) {
                    username.setErrorEnabled(true);
                    username.setError(getResources().getText(R.string.message_invalid_username));
                    valid = false;
                } else {
                    username.setErrorEnabled(false);
                }
            }
            if (pass.isEmpty()){
                password.setErrorEnabled(true);
                password.setError(getResources().getText(R.string.message_empty_password));
                valid = false;
            } else {
                if (pass.length() < 8) {
                    password.setErrorEnabled(true);
                    password.setError(getResources().getText(R.string.message_short_password));
                    valid = false;
                } else {
                    password.setErrorEnabled(false);
                }
            }
            if (valid) {
                username.setErrorEnabled(false);
                password.setErrorEnabled(false);
                getToken(user, pass);
            }
        });
        register.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://datos.emtmadrid.es/user/register"));
            startActivity(browserIntent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        }
        return true;
    }

    public void getToken(String email, String password) {
        ApiInterface apiInterface = ApiCall.callApi().create(ApiInterface.class);

        Call<TokenModel> call = apiInterface.getTokenByUser(email, password);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TokenModel> call, @NonNull Response<TokenModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TokenModel token = response.body();
                    if (token.getCode() == 1) {
                        TokenModel.Data data = token.getData().get(0);
                        Log.d("Token", data.getAccessToken());
                        ApiCall.setToken(data.getAccessToken());
                        Toast.makeText(LoginActivity.this, R.string.message_successful_login, Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (token.getCode() == 89) {
                        Toast.makeText(LoginActivity.this, R.string.message_invalid_credentials, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("API Error", "Unexpected error");
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