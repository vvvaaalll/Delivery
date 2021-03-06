package vloboda.deliveryapp.delivery;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class
MapFragment extends Fragment {

    private boolean locationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;
    private Geocoder geocoder;

    ArrayList<Order> orderArrayList;


    private GoogleMap map;
    private Location lastKnownLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final LatLng defaultLocation = new LatLng(45.55111, 18.69389);

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        fStore = FirebaseFirestore.getInstance();
        fAuth     = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid().toString();
        geocoder = new Geocoder(getContext());
        orderArrayList = new ArrayList<Order>();
        EventChangeListener();


        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());








        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {


                map = googleMap;

                getLocationPermission();
                updateLocationUI();
                getDeviceLocation();
                //orderArrayList.add(new Order("String name", "String phone", "Sjenjak 39, Osijek", "String note", 1));
                //orderArrayList.add(new Order("String name", "String phone", "Vijenac petrove gore 11, Osijek", "String note", 0));





                    orderArrayList.forEach(order -> {
                        try{
                            if(order.address.isEmpty()){throw new Exception();}
                            else{
                                List<Address> addresses = geocoder.getFromLocationName(order.address, 1);
                                Address address = addresses.get(0);
                                Log.d(TAG, "onMapReady : " + address.toString());

                                LatLng position = new LatLng(address.getLatitude(), address.getLongitude());

                                MarkerOptions markerOptions = new MarkerOptions()
                                        .position(position)
                                        .title(order.name +", "+ order.address);
                                if(order.time==1){
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(200));}
                                map.addMarker(markerOptions).setTag(position);
                                }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    });

                        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
                                LatLng position= marker.getPosition();

                                orderArrayList.forEach(order -> {
                                    try{
                                        if(order.address.isEmpty()){throw new Exception();}
                                        else{

                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                });


                                return false;
                            }
                        });

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull @NotNull LatLng latLng) {
                        //when clicked on map
                        MarkerOptions markerOptions = new MarkerOptions();

                        //markerOptions.position(latLng);

                        //markerOptions.title(latLng.latitude+":" +latLng.longitude);

                        //googleMap.clear();

                        googleMap.animateCamera((CameraUpdateFactory.newLatLngZoom(latLng,16.0f)));

                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));

                       // googleMap.addMarker(markerOptions);
                    }
                });




                }
            });


        return view;
    }


    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }


    @SuppressLint("MissingPermission")
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {

        try {
            if (locationPermissionGranted) {
                @SuppressLint("MissingPermission") Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {

                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), 16));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, 16));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }


    public GeoPoint getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(getContext());
        List<Address> address;
        GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new GeoPoint((double) (location.getLatitude() * 1E6),
                    (double) (location.getLongitude() * 1E6));

            
        }catch (IOException ex) {

            ex.printStackTrace();
        }
        return p1;
    }


    private void EventChangeListener()
    {
        ArrayList<Order> orders = new ArrayList<Order>();

        fStore.collection("users").document(userID).collection("orders")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null){
                            Log.e("Firestore error",error.getMessage());
                        }

                        for(DocumentChange fStore : value.getDocumentChanges()){

                            if(fStore.getType() == DocumentChange.Type.ADDED){
                                Order order =  fStore.getDocument().toObject(Order.class);

                                order.setOrderID(fStore.getDocument().getId());
                                orderArrayList.add(order);

                            }

                        }
                    }
                });

    }


    public void AddMarker(){
    try {
        
        List<Address> addresses = geocoder.getFromLocationName("Sjenjak 39, Osijek", 1);
        Address address = addresses.get(0);
        Log.d(TAG, "onMapReady : " + address.toString());

        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(address.getLatitude(), address.getLongitude()))
                .title(address.getLocality());
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(200));
        map.addMarker(markerOptions);
    }catch (IOException e){
        e.printStackTrace();
    }
    }
}



/*
                        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
                                LatLng position= marker.getPosition();

                                orderArrayList.forEach(order -> {
                                    try{
                                        if(order.address.isEmpty()){throw new Exception();}
                                        else{
                                            List<Address> addresses = geocoder.getFromLocationName(order.address, 1);
                                            Address address = addresses.get(0);
                                            Log.d(TAG, "onMapReady : " + address.toString());

                                            LatLng positionInArray = new LatLng(address.getLatitude(), address.getLongitude());
                                            if(position == positionInArray){
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                builder.setTitle("Delivery");
                                                builder.setMessage(order.name + ", " + order.address);

                                                builder.setPositiveButton("Delivered", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //DELETE FROM FIRESTORE
                                                        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();


                                                        FirebaseFirestore.getInstance().collection("users").document(userID)
                                                                .collection("orders").document(order.getOrderID()).delete();


                                                        getContext().startActivity(new Intent(getContext(), MapFragment.class));
                                                        //startActivity(new Intent(getApplicationContext(),Tarantulas.class));
                                                        Toast.makeText(getContext(), "Succesfully deleted tarantula" , Toast.LENGTH_SHORT).show();

                                                    }
                                                })
                                                        .setNegativeButton("back to map", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                            }
                                                        });
                                                AlertDialog ad = builder.create();
                                                ad.show();


                                            }
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                });


                                return false;
                            }
 */