package com.example.indecisive;
/*
    George Gannon
    FoodPickerFragment.java

    Responsible for displaying results of the food search as well as making associated API calls
    developed from the previous input fragment,
 */
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.indecisive.databinding.FragmentFoodPickerBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
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

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    try {
                        searchForFood();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                }
            });

    @Override
    @SuppressLint("MissingPermission")
    /*
        After the view is created, we need to do a few things:
            create an adapter for our recycler view
            prepare data for API calls
            get location
     */
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Integer seed = getArguments().getInt("magicNumber");

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

        if(seed==518200){
            randGenerator = new Random();
        }else {
            randGenerator = new Random(seed);
        }
        try {
            searchForFood(); // begin api calls
        } catch (IOException e) {
            e.printStackTrace();
        }/*
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location == null) {
                        //TODO;
                    } else {
                        try {
                            System.out.println(location.getLatitude() + " "+location.getLongitude());
                            searchForFood();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });*/



    }
    public void requestPermissions(){
        requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    public boolean getPermissions(){
        return (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);

    }
    /*
    This is the heavy lifting function. First in the chain of API calls.
        The chain required:
            Parse Input and do a nearby search -> from nearby search randomly pick one restaurant and search for more information using places search->
            finally, get the pictures from the places search and access them through the API to get their bitmaps
     */
    public void searchForFood() throws IOException {
        /* Get current location*/


        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("&location=" + "35.84897592819507" + "%2C" + "-86.36885554204993");

        //googlePlacesUrl.append("&location=-33.8670522%2C151.1957362");
        googlePlacesUrl.append("&radius=" + "30000");
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
            JsonObject chosenRestaurant = getRestaurant().getAsJsonObject(); // randomly pick restaurant
            binding.restaurantName.setText(chosenRestaurant.get("name").toString()); // update gui to reflect name of restaurant
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
        }
    }
}