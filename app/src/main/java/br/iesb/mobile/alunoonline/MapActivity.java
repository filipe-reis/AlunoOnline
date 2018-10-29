package br.iesb.mobile.alunoonline;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import br.iesb.mobile.alunoonline.Model.MercadoAPI;
import br.iesb.mobile.alunoonline.dijkstra.DistanciaMercados;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{

    private List<MercadoAPI> listaMercados = new ArrayList<>();
    private GoogleMap mMap;
    LocationManager locationManager;
    private LatLng currentLocation = new LatLng(-15.83437,-47.9141382); // Localizacao IESB
    LatLng mercadoA = new LatLng(-15.827190, -47.906108);
    LatLng mercadoB = new LatLng(-15.81152900, -47.90409170);
    LatLng mercadoC = new LatLng(-15.823375, -47.904898);

    private List<String> menorCaminho = new ArrayList<>();
    LatLng maisBarato;
    LatLng mercado2;
    LatLng mercado3;
    private long distance;
    private List<LatLng> list;
    private Polyline polyline;

    List<LatLng> rotaParaA = new ArrayList<>();
    List<LatLng> rotaParaB = new ArrayList<>();
    List<LatLng> rotaParaC = new ArrayList<>();

    public void setCurrentLocation(LatLng currentLocation) {
        this.currentLocation = currentLocation;
    }

    public static final int MAP_PERMISSION_ACCESS_COURSE_LOCATION = 9999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        DistanciaMercados distMercados = new DistanciaMercados();

        Intent it = getIntent();
        listaMercados = (ArrayList<MercadoAPI>) it.getSerializableExtra("listaMercados");

        menorCaminho = distMercados.calculaMercadoMaisProximoDaLocalizacaoAtual(listaMercados, currentLocation);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); //Fim da requisição chama o metodo onMapReady
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        maisBarato = new LatLng(listaMercados.get(0).getLatitude(), listaMercados.get(0).getLongitude());

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MAP_PERMISSION_ACCESS_COURSE_LOCATION);
            //O android gera uma tela para solicitar permissão do usuario. Quando o usuario responder ira voltar no metodo onRequestPermissionsResult

        }else{
            tracarRota();
        }

    }

    /**
     * Desenha rota no mapa do ponto de origem até o mercado mais barato.
     */
    private void tracarRota(){

        // Marca ponto inicial IESB para todos os casos
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Localizção Atual"));

        if(listaMercados.get(0).getNome().equals("Mercado A")){

            rotaParaA.add(new LatLng(-15.83437,-47.9141382));
            rotaParaA.add(new LatLng(-15.832100, -47.910353));
            rotaParaA.add(new LatLng(-15.829684, -47.906855));
            rotaParaA.add(new LatLng(-15.828558, -47.905414));
            rotaParaA.add(new LatLng(-15.828280, -47.905758));
            rotaParaA.add(new LatLng(-15.827769, -47.906024));
            rotaParaA.add(new LatLng(-15.827527, -47.905887));
            rotaParaA.add(mercadoA);
            mMap.addPolyline(new PolylineOptions().addAll(rotaParaA).color(Color.GRAY));

            mMap.addMarker(new MarkerOptions().position(maisBarato).title("Mercado A")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        }else if (listaMercados.get(0).getNome().equals("Mercado B")){
            //rota para b: Atual -> A ->  B

            rotaParaB.add(new LatLng(-15.83437,-47.9141382));
            rotaParaB.add(new LatLng(-15.832100, -47.910353));rotaParaB.add(new LatLng(-15.829684, -47.906855));rotaParaB.add(new LatLng(-15.828558, -47.905414));
            rotaParaB.add(new LatLng(-15.828280, -47.905758));rotaParaB.add(new LatLng(-15.827769, -47.906024));rotaParaB.add(new LatLng(-15.827527, -47.905887));
            rotaParaB.add(mercadoA);

            rotaParaB.add(new LatLng(-15.827190, -47.906108));rotaParaB.add(new LatLng(-15.826429, -47.906678));rotaParaB.add(new LatLng(-15.825987, -47.907060));
            rotaParaB.add(new LatLng(-15.825759, -47.907017));rotaParaB.add(new LatLng(-15.824562, -47.905391));rotaParaB.add(new LatLng(-15.824175, -47.904721));
            rotaParaB.add(new LatLng(-15.823803, -47.904565));
            rotaParaB.add(mercadoC);

            rotaParaB.add(new LatLng(-15.818531, -47.908613));rotaParaB.add(new LatLng(-15.818493, -47.908645));rotaParaB.add(new LatLng(-15.818457, -47.908613));
            rotaParaB.add(new LatLng(-15.818401, -47.908592));rotaParaB.add(new LatLng(-15.818349, -47.908613));rotaParaB.add(new LatLng(-15.818332, -47.908626));
            rotaParaB.add(new LatLng(-15.818163, -47.908421));rotaParaB.add(new LatLng(-15.818069, -47.908268));rotaParaB.add(new LatLng(-15.817913, -47.907985));
            rotaParaB.add(new LatLng(-15.817909, -47.907962));rotaParaB.add(new LatLng(-15.817937, -47.907935));rotaParaB.add(new LatLng(-15.817947, -47.907904));
            rotaParaB.add(new LatLng(-15.817934, -47.907855));rotaParaB.add(new LatLng(-15.817901, -47.907831));rotaParaB.add(new LatLng(-15.817855, -47.907834));

            rotaParaB.add(new LatLng(-15.817847, -47.907813));rotaParaB.add(new LatLng(-15.816687, -47.906163));rotaParaB.add(new LatLng(-15.816619, -47.906096));
            rotaParaB.add(new LatLng(-15.816615, -47.906057));rotaParaB.add(new LatLng(-15.816607, -47.905993));rotaParaB.add(new LatLng(-15.816578, -47.905943));
            rotaParaB.add(new LatLng(-15.816549, -47.905903));rotaParaB.add(new LatLng(-15.816505, -47.905874));rotaParaB.add(new LatLng(-15.816421, -47.905866));
            rotaParaB.add(new LatLng(-15.816394, -47.905894));rotaParaB.add(new LatLng(-15.816388, -47.905954));rotaParaB.add(new LatLng(-15.816406, -47.906007));
            rotaParaB.add(new LatLng(-15.816417, -47.906054));rotaParaB.add(new LatLng(-15.816012, -47.906374));rotaParaB.add(new LatLng(-15.814832, -47.907272));
            rotaParaB.add(new LatLng(-15.814762, -47.907264));rotaParaB.add(new LatLng(-15.814626, -47.907232));rotaParaB.add(new LatLng(-15.814558, -47.907199));
            rotaParaB.add(new LatLng(-15.814473, -47.907200));rotaParaB.add(new LatLng(-15.814392, -47.907220));rotaParaB.add(new LatLng(-15.814241, -47.907338));
            rotaParaB.add(new LatLng(-15.814063, -47.907472));rotaParaB.add(new LatLng(-15.813675, -47.906921));rotaParaB.add(new LatLng(-15.812639, -47.905501));
            rotaParaB.add(new LatLng(-15.812602, -47.905461));rotaParaB.add(new LatLng(-15.812564, -47.905459));rotaParaB.add(new LatLng(-15.812517, -47.905478));
            rotaParaB.add(new LatLng(-15.812394, -47.905575));rotaParaB.add(new LatLng(-15.812329, -47.905610));rotaParaB.add(new LatLng(-15.812275, -47.905593));
            rotaParaB.add(new LatLng(-15.812237, -47.905559));rotaParaB.add(new LatLng(-15.812167, -47.905463));rotaParaB.add(new LatLng(-15.811813, -47.904986));
            rotaParaB.add(new LatLng(-15.811789, -47.904945));rotaParaB.add(new LatLng(-15.811788, -47.904917));rotaParaB.add(new LatLng(-15.811826, -47.904869));
            rotaParaB.add(new LatLng(-15.812013, -47.904694));rotaParaB.add(new LatLng(-15.811577, -47.904065));
            rotaParaB.add(mercadoB);


            mMap.addPolyline(new PolylineOptions().addAll(rotaParaB).color(Color.GRAY));
            mMap.addMarker(new MarkerOptions().position(mercadoA).title("Mercado A"));
//            mMap.addMarker(new MarkerOptions().position(mercadoC).title("Mercado C"));
            mMap.addMarker(new MarkerOptions().position(maisBarato).title("Mercado B")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        }else if (listaMercados.get(0).getNome().equals("Mercado C")) {
            // rota para c: Atual -> A -> C
            rotaParaC.add(new LatLng(-15.83437, -47.9141382));
            rotaParaC.add(new LatLng(-15.832100, -47.910353));
            rotaParaC.add(new LatLng(-15.829684, -47.906855));
            rotaParaC.add(new LatLng(-15.828558, -47.905414));
            rotaParaC.add(new LatLng(-15.828280, -47.905758));
            rotaParaC.add(new LatLng(-15.827769, -47.906024));
            rotaParaC.add(new LatLng(-15.827527, -47.905887));
            rotaParaC.add(mercadoA);

            rotaParaC.add(new LatLng(-15.827190, -47.906108));
            rotaParaC.add(new LatLng(-15.826429, -47.906678));
            rotaParaC.add(new LatLng(-15.825987, -47.907060));
            rotaParaC.add(new LatLng(-15.825759, -47.907017));
            rotaParaC.add(new LatLng(-15.824562, -47.905391));
            rotaParaC.add(new LatLng(-15.824175, -47.904721));
            rotaParaC.add(new LatLng(-15.823803, -47.904565));
            rotaParaC.add(mercadoC);

            mMap.addPolyline(new PolylineOptions().addAll(rotaParaC).color(Color.GRAY));
            mMap.addMarker(new MarkerOptions().position(mercadoA).title("Mercado A"));
            mMap.addMarker(new MarkerOptions().position(maisBarato).title("Mercado C")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MAP_PERMISSION_ACCESS_COURSE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    getLastLocation();
//                    getUserLocation();
                    tracarRota();
                } else {
                    //Permissão negada
                }
                return;
            }
        }
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

    private void getUserLocation() {
        //Verificando permissão
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            //Toda vez que o usuario mudar de lugar a Location Listener é chamada
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    MapActivity map = new MapActivity();
                    LatLng me = new LatLng(location.getLatitude(), location.getLongitude());
                    map.setCurrentLocation(me);
                    mMap.addMarker(new MarkerOptions().position(me).title("Minha Localização."));
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
}
