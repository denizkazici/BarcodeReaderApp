package com.denizkazici.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button btnScan;
    Button btnProductList;
    Button btnAbout;
    Button btnSearch;
    TextView tvBarcode;
    String barcode="";
    String barcodeID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnScan = findViewById(R.id.btnScan);
        btnSearch = findViewById(R.id.btnSearch);
        btnProductList = findViewById(R.id.btnProductList);
        btnAbout = findViewById(R.id.btnAbout);
        tvBarcode = findViewById(R.id.tvBarcodenum);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, About.class);
                startActivity(intent);
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.barcodelookup.com/v3/products?barcode=4305615621180&formatted=y&key=ies77v425c3fzde2wja1gwwtb1kfqu";
                System.out.println(url);
                new HTTPAsyncTask().execute(url);
            }
        });


    }
    public void onClick(View v){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan");
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!= null){
            if(result.getContents()==null){
                Toast.makeText(getBaseContext(), "cancelled", Toast.LENGTH_LONG).show();
            }else{
                barcode=result.getContents().toString();
                tvBarcode.setText(barcode);



            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
    private class HTTPAsyncTask extends AsyncTask<String, Void, String > {

        protected String doInBackground(String... urls) { return GET(urls[0]);}



        protected void onPostExecute(String result){
            try {
                 System.out.println(result);

                JSONObject jsonObject = new JSONObject(result);
                JSONArray product = jsonObject.getJSONArray("products");
                JSONObject curProduct = (JSONObject) product.get(0);
                String productName = curProduct.getString("title");
                String productCatagory = curProduct.getString("category");
                String productDescription = curProduct.getString("description");
                tvBarcode.setText(productName);
                System.out.println("deneme"+productName);
                System.out.println(productCatagory);
                //JSONObject productImages = curProduct.getJSONObject("images");
            }

            catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }
    public static String GET(String urlString) {
        String json = null;
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(urlString);

            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = urlConnection.getInputStream();

            InputStreamReader isw = new InputStreamReader(in);

            BufferedReader br = new BufferedReader(isw);

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();

            json = sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }
}