package com.example.indecisive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class FoodPickerFragment extends Fragment {
    private FragmentFoodPickerBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager HorizontalLay;

    private JsonObject restaurants;
    private List<Bitmap> bitmapList;

    private FoodPickerAdapter adapter;
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

        // recyclin view junk
        mRecyclerView =(RecyclerView) getActivity().findViewById(R.id.food_picker_recycler);


        bitmapList = new ArrayList<>();
        adapter = new FoodPickerAdapter(bitmapList);
        HorizontalLay = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(HorizontalLay);
        mRecyclerView.setAdapter(adapter);
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(mRecyclerView);

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
            String place_id = chosenRestaurant.get("place_id").toString();
            StringBuilder googlePlacesUrl=new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");;
            googlePlacesUrl.append("place_id="+place_id.replaceAll("\"", ""));
            googlePlacesUrl.append("&fields=photo");
            googlePlacesUrl.append("&key=" + "AIzaSyDln1uHXQm5lIEwR-ElwShFQ0F2WSNyxzM");
            new GoogleGetPicturesTask().execute(googlePlacesUrl.toString());

        }
    }

    public class GoogleGetPicturesTask extends AsyncTask<String, Void, String>{
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
            JsonObject photoArr = (JsonObject) JsonParser.parseString(result);
            List<String> stringList = new ArrayList<>();
            for(int i = 0 ; i<3; i++) {
                try{
                    StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?");
                    googlePlacesUrl.append("&maxwidth=" + (int) width);
                    googlePlacesUrl.append("&photo_reference=" + photoArr.getAsJsonObject("result").getAsJsonArray("photos").get(i).getAsJsonObject().get("photo_reference").toString().replaceAll("\"", ""));
                    googlePlacesUrl.append("&key=" + "AIzaSyDln1uHXQm5lIEwR-ElwShFQ0F2WSNyxzM");
                    stringList.add(googlePlacesUrl.toString());
                }catch(IndexOutOfBoundsException e){
                    continue;
                }
            }

            new GooglePictureAsyncTask().execute(stringList);

        }
    }
    public class GooglePictureAsyncTask extends AsyncTask<List<String>, Void, List<Bitmap> >{

        @Override
        protected List<Bitmap> doInBackground(List<String>... strings) {
            Networker getter = new Networker();
            List<Bitmap> tobeparsed = new ArrayList<>();
            try {
                for(String i : strings[0]) {
                    System.out.println(i);
                    tobeparsed.add(getter.getBitmap(i));
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return tobeparsed;
        }

        @Override
        protected void onPostExecute(List<Bitmap> result) {
            adapter = new FoodPickerAdapter(result);
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            System.out.println(result);
        }
    }
}