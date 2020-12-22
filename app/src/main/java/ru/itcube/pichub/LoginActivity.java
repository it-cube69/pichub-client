package ru.itcube.pichub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ru.itcube.pichub.model.JWTToken;
import ru.itcube.pichub.model.LoginModel;

public class LoginActivity extends AppCompatActivity {

    private EditText loginText;
    private EditText passwordText;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginText = findViewById(R.id.loginText);
        passwordText = findViewById(R.id.passwordText);
        signInButton = findViewById(R.id.signInButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpTask().execute();
            }
        });

    }

    private final class HttpTask extends AsyncTask<Void, Void, JWTToken> {

        @Override
        protected void onPostExecute(JWTToken jwtToken) {
            if (jwtToken != null && jwtToken.getTokenId() != null && !jwtToken.getTokenId().equals("")) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("token", jwtToken.getTokenId());
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Wrong credentials", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected JWTToken doInBackground(Void... voids) {

            OkHttpClient okHttpClient = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

            LoginModel loginModel = new LoginModel();
            loginModel.setUsername(loginText.getText().toString());
            loginModel.setPassword(passwordText.getText().toString());

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            // {"username":"dfdsfsdf", "password":"fdsfsdfsd"}
            String json = gson.toJson(loginModel);

            RequestBody body = RequestBody.create(json, mediaType);

            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8080/api/auth")
                    .post(body)
                    .build();

            try (Response response = okHttpClient.newCall(request).execute()){
                JWTToken token = gson.fromJson(response.body().string(), JWTToken.class);
                return token;
            } catch (IOException e) {
                Log.e("LOGIN CONNECTION", e.getMessage());
                return new JWTToken();
            }
        }
    }
}