package com.example.indecisive;
/*
    George Gannon
    FoodPickerFragment.java

    Responsible for displaying results of the food search as well as making associated API calls
    developed from the previous input fragment,
 */

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import android.widget.Button;


import com.example.indecisive.databinding.FragmentFoodPickerBinding;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private RecyclerView mRecyclerView;
    private LinearLayoutManager HorizontalLay;
    private Bundle bundlecopy;
    private JsonObject restaurants;
    private List<Bitmap> bitmapList;
    private LocationRequest locationRequest;

    private FoodPickerAdapter adapter;
    private double width = Resources.getSystem().getDisplayMetrics().widthPixels;
    private double height;
    private Integer radius;
    private double lat;
    private double lon;

    private double restlat;
    private double restlon;
    private String restadd;
    Random randGenerator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFoodPickerBinding.inflate(inflater, container, false);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        return binding.getRoot();
    }


    @Override
    /*
        After the view is created, we need to do a few things:
            create an adapter for our recycler view
            prepare data for API calls
            get location
     */
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bundlecopy=savedInstanceState;
        int seed = getArguments().getInt("magicNumber");
        radius = getArguments().getInt("radiusNumber")*1000;

        // recyclin view junk.
        // Mostly stuff required to get the pictures in a recycler view.
        mRecyclerView =(RecyclerView) getActivity().findViewById(R.id.food_picker_recycler);
        bitmapList = new ArrayList<>(); // arary list of bitmaps
        adapter = new FoodPickerAdapter(bitmapList);
        HorizontalLay = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false); // horizontal instead of vertical
        mRecyclerView.setLayoutManager(HorizontalLay);
        mRecyclerView.setAdapter(adapter);
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(mRecyclerView);

        binding.retry.setVisibility(View.GONE);
        binding.letsgo.setVisibility(View.GONE);
        if(seed == 5182000){
            randGenerator = new Random();
        }else {
            randGenerator = new Random(seed);
        }
        getLocation();

        binding.retry.setOnClickListener(new View.OnClickListener(){

             @Override
             public void onClick(View view) {
                 binding.restaurantName.setText("");
                 binding.restaurantAddress.setText("");
                 List<Bitmap> empty = new ArrayList<>();
                 adapter = new FoodPickerAdapter(empty);
                 mRecyclerView.setAdapter(adapter);
                 adapter.notifyDataSetChanged();
                 getLocation();
             }
                                         }
        );

        binding.letsgo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Creates an Intent that will load a map where the app went to
                restadd.replace(' ','+');
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+restadd);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });


    }

    /*
    This is the heavy lifting function. First in the chain of API calls.
        The chain required:
            Parse Input and do a nearby search -> from nearby search randomly pick one restaurant and search for more information using places search->
            finally, get the pictures from the places search and access them through the API to get their bitmaps
     */
    public void searchForFood() throws IOException {
        /* Get current location*/
        binding.progressBar1.setVisibility(View.VISIBLE);

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("&location=" + lat + "%2C" + lon);

        //googlePlacesUrl.append("&location=-33.8670522%2C151.1957362");
        String rad = radius.toString();
        googlePlacesUrl.append("&radius=" + rad);
        googlePlacesUrl.append("&types=" + "restaurant");
        googlePlacesUrl.append("&name="+getArguments().getString("keyword"));
        googlePlacesUrl.append("&key=" + "AIzaSyDln1uHXQm5lIEwR-ElwShFQ0F2WSNyxzM");

        new GoogleAsyncTask().execute(googlePlacesUrl.toString()); // first api call using built string above.


    }
    /*
        Helper function to randomly pick a restaurant from a list of restaurants.
     */
    private JsonElement getRestaurant(){

        JsonArray restArr = restaurants.getAsJsonArray("results");
        JsonElement temp= restArr.get(randGenerator.nextInt(restArr.size()));
        return temp;
    }

    /*
    First api call. has to be asynchronous so that way it doesn't block the application.
     */
    public class GoogleAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        // BACKGROUND TASK.
        protected String doInBackground(String... strings) {
            Networker getter = new Networker();
            String tobeparsed;
            try {
                tobeparsed = getter.get(strings[0].toString()); // returns a string  of data using the networker

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return tobeparsed;
        }


        @Override
        // Once the background task is finished, do the following.
        protected void onPostExecute(String result) {
            restaurants = (JsonObject) JsonParser.parseString(result); // convert string to JSON object
            System.out.println(restaurants.toString()); // debugging
            System.out.println(restaurants.get("results").getAsJsonArray().size()==0);
            if(restaurants.get("results").getAsJsonArray().size()==0){
                Toast roast = Toast.makeText(getContext(), "Zero results were found. Try expanding your search radius or refining your keywords.", Toast.LENGTH_LONG);
                roast.show();
                NavHostFragment.findNavController(FoodPickerFragment.this).navigate(R.id.action_food_picker_to_food_picker_input, bundlecopy); // navigate to the next fragment
                return;
            }
            JsonObject chosenRestaurant = getRestaurant().getAsJsonObject(); // randomly pick restaurant


            binding.restaurantName.setText(chosenRestaurant.get("name").toString()); // update gui to reflect name of restaurant
            restadd = chosenRestaurant.get("vicinity").toString();
            binding.restaurantAddress.setText(chosenRestaurant.get("vicinity").toString());
            restlat = chosenRestaurant.get("geometry").getAsJsonObject().get("location").getAsJsonObject().get("lat").getAsDouble();
            restlon = chosenRestaurant.get("geometry").getAsJsonObject().get("location").getAsJsonObject().get("lng").getAsDouble();
            String place_id = chosenRestaurant.get("place_id").toString(); // need this for next api call

            // Build string, same as last time, but do a photo search using the place_id
            StringBuilder googlePlacesUrl=new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
            googlePlacesUrl.append("place_id="+place_id.replaceAll("\"", ""));
            googlePlacesUrl.append("&fields=photo");
            googlePlacesUrl.append("&key=" + "AIzaSyDln1uHXQm5lIEwR-ElwShFQ0F2WSNyxzM");
            new GoogleGetPicturesTask().execute(googlePlacesUrl.toString()); // Next async task

        }
    }

    /*
        Second async task. Use the networker to make an api call about a specific place using the place_id fetched before,
        and then get the photo ids.
     */
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
        // Build a photo list using the information fetched from the call.
        protected void onPostExecute(String result) {
            JsonObject photoArr = (JsonObject) JsonParser.parseString(result);
            List<String> stringList = new ArrayList<>();
            for(int i = 0 ; i<3; i++) { // get three photos from the list of photos, and add them to a string.
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

            new GooglePictureAsyncTask().execute(stringList); // start a new asynchronous task that is dependent on the list of strings.

        }
    }
    public class GooglePictureAsyncTask extends AsyncTask<List<String>, Void, List<Bitmap> >{

        @Override
        /*
            Iterate through the strings and make an api call for each.
         */
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
        /*
         This will not execute until that arrayList is populated with something.
         */
        protected void onPostExecute(List<Bitmap> result) {
            /*
            This is mainly gui stuff. Notify datasetchanged is so that the recyclerview rebinds with the new pictures
             */
            adapter = new FoodPickerAdapter(result);
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            System.out.println(result);
            binding.progressBar1.setVisibility(View.GONE);
            binding.retry.setVisibility(View.VISIBLE);
            binding.letsgo.setVisibility(View.VISIBLE);
        }
    }
    /*
        Note from George: Everything below this point was based off of this youtube tutorial because I could not figure out how
        to do it on my own. I tried and tried, but couldn't crack it. The documentation wasn't the best because it didn't cover all of the
        moving parts.

        Link: https://www.youtube.com/watch?v=mbQd6frpC3g
     */
    @SuppressLint("MissingPermission")
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (isGPSEnabled()) {
                LocationServices.getFusedLocationProviderClient(getActivity())
                        .requestLocationUpdates(locationRequest, new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                super.onLocationResult(locationResult);

                                LocationServices.getFusedLocationProviderClient(getActivity())
                                        .removeLocationUpdates(this);



                                if (locationResult != null && locationResult.getLocations().size() >0){

                                    int index = locationResult.getLocations().size() - 1;
                                    lat = locationResult.getLocations().get(index).getLatitude();
                                    lon = locationResult.getLocations().get(index).getLongitude();
                                    System.out.println("lat "+ lat + lon);
                                    try {
                                        searchForFood(); // begin api calls
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, Looper.getMainLooper());
            }else{
                turnOnGPS();
            }
        }else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                if (isGPSEnabled()) {

                    getLocation();

                }else {

                    turnOnGPS();
                }
            }
        }


    }
    private void turnOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    //Toast.makeText(MainActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(getActivity(), 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }

}