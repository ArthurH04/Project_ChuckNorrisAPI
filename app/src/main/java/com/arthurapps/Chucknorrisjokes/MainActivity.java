package com.arthurapps.Chucknorrisjokes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arthurapps.Chucknorrisjokes.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Button btnEnviar;
    TextView txtFrase, txtUrl;

    String frase, url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEnviar = findViewById(R.id.button);
        txtFrase = findViewById(R.id.textFact);
        txtUrl = findViewById(R.id.textUrl);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new QuoteLoader().execute();
            }
        });
    }

    //Carrega a frase
    private class QuoteLoader extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            //Pega o texto JSON no URL
            String jsonString = getJson("https://api.chucknorris.io/jokes/random");

            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                frase = jsonObject.getString("value");
                url = jsonObject.getString("url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            frase = "";
            txtFrase.setText("Carregando a piada");
            txtUrl.setText("Carregando o url");
        }

        @Override
        protected void onPostExecute(Void avoid){
            super.onPostExecute(avoid);

            if (!frase.equals("")){
                txtFrase.setText(frase);
                txtUrl.setText("Url " + url);
                frase = "";
            }
        }
    }
    //Pega o JSON do URL
    private String getJson(String link){
        String stream = "";
        try {
            URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null){
                    sb.append(line);
                    sb.append("\n");

                }
                stream = sb.toString();
                urlConnection.disconnect();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stream;

    }

}
