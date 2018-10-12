package br.iesb.mobile.alunoonline;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    LocationManager locationManager;

    public static final int MAP_PERMISSION_ACCESS_COURSE_LOCATION = 9999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); //Fim da requisição chama o metodo onMapReady
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        /*if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MAP_PERMISSION_ACCESS_COURSE_LOCATION);
            //O android gera uma tela para solicitar permissão do usuario
            //Quando o usuario responder ira voltar no metodo onRequestPermissionsResult
        } else {
            //getLastLocation();
            getLocation();
        }*/

        List<LatLng> decodedPath = new ArrayList<>();
        decodedPath.add(new LatLng(-15.8318566 ,-47.9109529)); // latitude e longitude
        decodedPath.add(new LatLng(-15.832100, -47.910353)); // latitude e longitude
        decodedPath.add(new LatLng(-15.829684, -47.906855)); // latitude e longitude
        decodedPath.add(new LatLng(-15.828558, -47.905414));
        decodedPath.add(new LatLng(-15.828280, -47.905758));
        decodedPath.add(new LatLng(-15.827769, -47.906024));
        decodedPath.add(new LatLng(-15.827527, -47.905887));
        decodedPath.add(new LatLng(-15.827190, -47.906108));


        decodedPath.add(new LatLng(-15.827190, -47.906108));
        decodedPath.add(new LatLng(-15.826429, -47.906678));
        decodedPath.add(new LatLng(-15.825987, -47.907060));
        decodedPath.add(new LatLng(-15.825759, -47.907017));
        decodedPath.add(new LatLng(-15.824562, -47.905391));
        decodedPath.add(new LatLng(-15.824175, -47.904721));
        decodedPath.add(new LatLng(-15.823803, -47.904565));
        decodedPath.add(new LatLng(-15.823375, -47.904898));

        mMap.addPolyline(new PolylineOptions().addAll(decodedPath).color(Color.GRAY));


        LatLng bigbox = new LatLng(-15.827190, -47.906108);
        LatLng iesbSul = new LatLng(-15.8318566 ,-47.9109529);
        LatLng mercado2 = new LatLng(-15.823375, -47.904898);

        mMap.addMarker(new MarkerOptions().position(iesbSul).title("IESB Sul"));
        mMap.addMarker(new MarkerOptions().position(bigbox).title("Big Box"));
        mMap.addMarker(new MarkerOptions().position(mercado2).title("Big Box"));


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(iesbSul, 15));


    }

    private void getLastLocation() {
        //Verificando se a permissao do android manifest esta com estado de permitido
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(lastKnownLocation != null){
                LatLng me = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(me).title("Eu estava aqui quando o anrdoid me localizou pela última vez!!!"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 40));
            }
        }

    }

    private void getLocation() {
        //Verificando permissão
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            //Toda vez que o usuario mudar de lugar a Location Listener é chamada
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    LatLng me = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(me).title("Estou Aqui!!!"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 20));
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {}

                public void onProviderEnabled(String provider) {}

                public void onProviderDisabled(String provider) {}
            };
            //Informa tempo minimo e distancia para ir plotando pino no mapa
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 15, 30, locationListener);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MAP_PERMISSION_ACCESS_COURSE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();
                    getLocation();
                } else {
                    //Permissão negada
                }
                return;
            }
        }
    }
}
