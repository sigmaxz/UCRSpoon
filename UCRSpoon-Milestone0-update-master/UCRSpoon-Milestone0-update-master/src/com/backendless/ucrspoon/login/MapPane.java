package com.backendless.ucrspoon.login;



import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import android.app.Activity;
import android.os.Bundle;

public class MapPane extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        // Get a handle to the Map Fragment
        GoogleMap map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        double lat = getIntent().getDoubleExtra("lat", 0);
        double longt = getIntent().getDoubleExtra("longt", 0);
        LatLng res = new LatLng(lat, longt);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(res, 13));

        map.addMarker(new MarkerOptions()
                .title(getIntent().getStringExtra("rn"))
                .snippet(getIntent().getStringExtra("rd"))
                .position(res));
    }
}

