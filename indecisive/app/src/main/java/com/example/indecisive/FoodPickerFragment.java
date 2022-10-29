package com.example.indecisive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.indecisive.databinding.FragmentFoodPickerBinding;
import com.example.indecisive.databinding.FragmentFoodPickerInputBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.Random;


public class FoodPickerFragment extends Fragment {
    private FragmentFoodPickerBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private JsonObject restaurants;

    private double width = Resources.getSystem().getDisplayMetrics().widthPixels;
    private double height;
    Random randGenerator;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFoodPickerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Integer seed = getArguments().getInt("magicNumber");

        if(seed==518200){
            randGenerator = new Random();
        }else {
            randGenerator = new Random(seed);
        }
        try {
            searchForFood();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void searchForFood() throws IOException {
        /* Get current location*/
        try {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        //Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("&location=" + "35.84897592819507" + "%2C" + "-86.36885554204993");

        //googlePlacesUrl.append("&location=-33.8670522%2C151.1957362");
        googlePlacesUrl.append("&radius=" + "30000");
        googlePlacesUrl.append("&types=" + "restaurant");
        googlePlacesUrl.append("&name="+getArguments().getString("keyword"));
        googlePlacesUrl.append("&key=" + "AIzaSyDln1uHXQm5lIEwR-ElwShFQ0F2WSNyxzM");

/*
        URL url = new URL(googlePlacesUrl.toString());
        HttpURLConnection urlconn = (HttpURLConnection) url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
*/
        new GoogleAsyncTask().execute(googlePlacesUrl.toString());

        //JsonObject arr = JsonParser.parseReader(in).getAsJsonObject();

    }

    private JsonElement getRestaurant(){

        JsonArray restArr = restaurants.getAsJsonArray("results");
        JsonElement temp= restArr.get(randGenerator.nextInt(restArr.size()));
        return temp;
    }

    public class GoogleAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            Networker getter = new Networker();
            String tobeparsed;
            try {
                tobeparsed = getter.get(strings[0].toString());

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return tobeparsed;
        }


        @Override
        protected void onPostExecute(String result) {
            restaurants = (JsonObject) JsonParser.parseString(result);
            System.out.println(restaurants.toString());
            JsonObject chosenRestaurant = getRestaurant().getAsJsonObject();
            binding.restaurantName.setText(chosenRestaurant.get("name").toString());
            StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?");
            googlePlacesUrl.append("&maxwidth=" + (int) width);
            googlePlacesUrl.append("&photo_reference=" +chosenRestaurant.getAsJsonArray("photos").get(0).getAsJsonObject().get("photo_reference").toString().replaceAll("\"",""));
            googlePlacesUrl.append("&key=" + "AIzaSyDln1uHXQm5lIEwR-ElwShFQ0F2WSNyxzM");

            new GooglePictureAsyncTask().execute(googlePlacesUrl.toString());
        }
    }

    public class GooglePictureAsyncTask extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            Networker getter = new Networker();
            Bitmap tobeparsed;
            try {
                System.out.println(strings[0].toString());
                tobeparsed = getter.getBitmap(strings[0].toString());

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return tobeparsed;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            binding.photo.setImageBitmap(result);
        }
    }
}