package com.example.weatherradar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Attributes
    private RelativeLayout HomeRL;
    private ProgressBar LoadingPL;
    private TextView CityTV, TemperatureTv, ConditionTV;
    private RecyclerView WeatherRv;
    private TextInputEditText CityTIET;
    private ImageView BackIV, IconIV, searchIV;

    private ArrayList<WeatherModel> weatherModelArrayList;
    private WeatherAdapter weatherAdapter;

    private LocationManager locationManager;
    private final int Permissions = 1;
    private String CityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        // Accessing components through attributes
        HomeRL = findViewById(R.id.idRLHome);
        LoadingPL = findViewById(R.id.idPBLoading);
        CityTV = findViewById(R.id.idTVCityName);
        TemperatureTv = findViewById(R.id.idTVTemperature);
        ConditionTV = findViewById(R.id.idTVCondition);
        WeatherRv = findViewById(R.id.idRVWeather);
        CityTIET = findViewById(R.id.idCityName);
        BackIV = findViewById(R.id.idIVBack);
        IconIV = findViewById(R.id.idIVIcon);
        searchIV = findViewById(R.id.idIVSearchIcon);

        weatherModelArrayList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(this,weatherModelArrayList);

        WeatherRv.setAdapter(weatherAdapter);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//        && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Permissions);
//        }

       // Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        //    CityName = GetCityName(location.getLongitude(), location.getLatitude());
        GetWeatherInfo(CityName);

        searchIV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String city = CityTIET.getText().toString();

                if (city.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please Enter Your City!", Toast.LENGTH_SHORT).show();
                } else {
                    CityTV.setText(CityName);
                    GetWeatherInfo(city);
                }


            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

//        if (requestCode == Permissions) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//
//            }
//            else {
//                Toast.makeText(this, "Please Provide Location Permissions", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        }
    }

    private String GetCityName(double longitude, double latitude) throws IOException {

        String CityName = "Not found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());

        List<Address> addresses = gcd.getFromLocation(latitude, longitude, 10);

        for (Address adr : addresses) {
            if (adr != null) {
                String city =   adr.getLocality();
                if (city != null && !city.equals("")) {
                    CityName = city;

                } else {
                    Log.d("TAG", "City Not Found!");
                    Toast.makeText(this, "User City Not Found!", Toast.LENGTH_SHORT).show();

                }
            }
        }
        return CityName;
    }

    private void GetWeatherInfo(String city) {
        String URL = "http://api.weatherapi.com/v1/forecast.json?key=0cd7ffdf2bac4fc6919141743222905&q =" + city + "&days=1&aqi=no&alerts=no";
        CityTV.setText(CityName);
        RequestQueue request = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LoadingPL.setVisibility(View.GONE);
                HomeRL.setVisibility(View.VISIBLE);
                weatherModelArrayList.clear();

                try {
                    String Temperature = response.getJSONObject("current").getString("temp_c");
                    TemperatureTv.setText(Temperature + "°c");
                    int isDay = response.getJSONObject("current").getInt("is_day");
                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("http:".concat(conditionIcon)).into(IconIV);
                    ConditionTV.setText(condition);

                    if (isDay == 1) {
                        // https://unsplash.com/photos/sy9k7HBOgqI
                            Picasso.get().load("https://unsplash.com/photos/sy9k7HBOgqI").into(BackIV);
                    } else {
                        // https://unsplash.com/photos/_ahYGI9nExQ
                        Picasso.get().load("https://unsplash.com/photos/_ahYGI9nExQ").into(BackIV);
                    }

                    JSONObject forecast = response.getJSONObject("forecast");
                    JSONObject forecastDay = forecast.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray = forecastDay.getJSONArray("hour");

                    for (int i = 0; i < hourArray.length(); i++) {
                        JSONObject hourObj = hourArray.getJSONObject(i);
                        String time = hourObj.getString("time");
                        String temp = hourObj.getString("temp_c");
                        String img = hourObj.getJSONObject("condition").getString("icon");
                        String wind = hourObj.getString("wind_kph");

                        weatherModelArrayList.add(new WeatherModel(time,temp, img, wind));
                    }
                    weatherAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Please Enter a Valid City", Toast.LENGTH_SHORT).show();

            }
        });
        request.add(jsonObjectRequest);


    }

}