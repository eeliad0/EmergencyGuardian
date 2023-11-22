package com.example.emergencyguardian;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.location.Location;
import com.example.emergencyguardian.databinding.FragmentMyLocationBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;


import org.chromium.net.CronetEngine;
import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MyLocation extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient FLP;
    private Geocoder geocoder;

    private View rootView;

    private FragmentMyLocationBinding binding;

    private CronetEngine cronet;
    private CronetEngine.Builder myBuilder;
    private Executor executor;

    private String apiKey;

    private List<Station> stations;

    private String straddr;

    private Double latitude,longitude;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        geocoder= new Geocoder(requireContext(), Locale.getDefault());


        Bundle metadata = getMetadata(requireContext());
        apiKey = metadata.getString("com.google.android.geo.API_KEY");

        myBuilder = new CronetEngine.Builder(getContext());
        cronet = myBuilder.build();
        executor = Executors.newSingleThreadExecutor();

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_location, container, false);
        binding=FragmentMyLocationBinding.inflate(inflater,container,false);


        // Initialize the MapView
        MapView mapView = rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        Button sendLocationButton=rootView.findViewById(R.id.sendLocationButton);

        sendLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMessagingAppWithMessage("I need help. My address is: "+straddr);
            }
        });

        return rootView;
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Enable the "My Location" layer (blue dot) on the map
        mMap.setMyLocationEnabled(true);


        getLastLocationAndMoveMap();




    }




    public void openMessagingAppWithMessage(String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, message);

        // Verify if there's an app that can handle the intent
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            // Start the activity (in this case, the messaging app)
            startActivity(intent);
        }
    }


    private Bundle getMetadata(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), android.content.pm.PackageManager.GET_META_DATA);
            return appInfo.metaData;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Failed to load meta-data, NameNotFoundException: " + e.getMessage());
        }
    }


    @SuppressLint("MissingPermission")
    private void getLastLocationAndMoveMap() {


        FLP = LocationServices.getFusedLocationProviderClient(requireContext());

        FLP.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();


                            //Move map to current location
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 15));

                            try {
                                TextView locationText = rootView.findViewById(R.id.locationTextView);
                                List<Address> address = geocoder.getFromLocation(latitude,longitude,1);
                                locationText.setText(address.get(0).getAddressLine(0));
                                straddr=(address.get(0).getAddressLine(0));

                                getNearbyPlacesAPI();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle location retrieval failure
                        Log.e("Location", "Error getting location", e);
                    }
                });


    }





    private void getNearbyPlacesAPI(){
        String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?keyword=police&location="+latitude+"%2C"+longitude+"&radius=3500&type=police&key="+apiKey;
        Log.i("URL API",url);
        // set where the api call would be made to
        UrlRequest.Builder requestBuilder = cronet.newUrlRequestBuilder(
                url,
                new MyLocation.RequestCallback(),
                executor);

        UrlRequest request = requestBuilder.build();
        // here we make the actual api call
        request.start();
    }


    /**
     * This inner class is responsible to extend the url request callback and override the
     * appropriate methods according to the implementation we want to setup
     */
    class RequestCallback extends UrlRequest.Callback {
        private static final String TAG = "RequestCallback";

        @Override
        public void onResponseStarted(UrlRequest request, UrlResponseInfo info) {
            Log.i(TAG, "onResponseStarted method called.");
            // You should call the request.read() method before the request can be
            // further processed. The following instruction provides a ByteBuffer object
            // with a capacity of 102400 bytes for the read() method. The same buffer
            // with data is passed to the onReadCompleted() method.
            request.read(ByteBuffer.allocateDirect(102400));
        }



        @Override
        public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) {
            Log.i(TAG, "onReadCompleted method called.");

            // You should keep reading the request until there's no more data.
            byteBuffer.flip();
            byte[] bytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(bytes);
            String response = new String(bytes);



            // Let's see the JSON result in Logcat
            Log.i("Places response", response);

            // Create a Handler associated with the main thread
            Handler mainHandler = new Handler(Looper.getMainLooper());

            try {
                // Ensure UI-related code is executed on the main thread
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            handleJsonResponse(response);
                        } catch (JSONException e) {
                            //throw new RuntimeException(e);
                            Toast.makeText(requireContext(), "An error was encountered with the response from the API. Refresh page", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            byteBuffer.clear();
            request.read(byteBuffer);
        }



        @Override
        public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
            Log.i(TAG, "onSucceeded method called.");
        }



        @Override
        public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {
            Log.i(TAG, "onFailed method called.");
        }

        @Override
        public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) throws Exception {
            // do nothing for now
        }


        private void handleJsonResponse(String response) throws JSONException{
            JSONObject nearby=new JSONObject(response);
            JSONArray jsonArr=nearby.getJSONArray("results");


            stations=new ArrayList<>();

            for (int i=0;i<jsonArr.length();i++){
                JSONObject place=jsonArr.getJSONObject(i);
                String name=place.getString("name");
                Double lat=place.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                Double lon=place.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                String icon=place.getString("icon");

                stations.add(new Station(name,lat,lon,icon));

            }


            for (int i=0;i<stations.size();i++){
                Log.e("STATION",stations.get(i).getName()+"  "+stations.get(i).getIcon()+" ");
                Station station=stations.get(i);
                Marker mark = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(station.getLat(),station.getLon()))
                        .title(station.getName()));
                mark.setTag(station);


            }
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    Station station = (Station) marker.getTag();
                    DetailViewDialogFragment detailView= DetailViewDialogFragment.newInstance(station.getName(),station.getLat(),station.getLon(),station.getIcon());
                    detailView.show(getActivity().getSupportFragmentManager(), "DetailViewDialog");



                    return true;
                }
            });





        }


    }






}
