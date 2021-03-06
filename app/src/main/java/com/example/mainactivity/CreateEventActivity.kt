package com.example.mainactivity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mainactivity.api.FacilityService
import com.example.mainactivity.model.MultipleFacilityItemDto
import com.example.mainactivity.model.SportDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.infowindow.InfoWindow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CreateEventActivity : AppCompatActivity(), MapEventsReceiver {

    companion object{
        var facilities: List<MultipleFacilityItemDto> = emptyList();
    }
    var REQUEST_PERMISSIONS_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        get();
    }

    private fun get() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.171:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        var service = retrofit
            .create(FacilityService::class.java);
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getFacilities();

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    facilities = response.body()!!;
                    map()
                }
                else{
                    Log.d("N", response.code().toString())
                }
            }
        }
    }

    fun map(){
        val ctx = this.applicationContext
        Configuration.getInstance()
            .load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        val map = findViewById<MapView>(R.id.createEventFacilities)
        val mapEventOverlay = MapEventsOverlay(this, this)
        map.overlays.add(0, mapEventOverlay)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.getController().setZoom(13.0)
        requestPermissionsIfNecessary(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.INTERNET
            )
        )
        map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
        map.setMultiTouchControls(true)
        val compassOverlay = CompassOverlay(this, map)
        compassOverlay.enableCompass()
        map.overlays.add(compassOverlay)

        for(el in facilities) {
            val point = GeoPoint(el.lat, el.lon)
            val startMarker = Marker(map)
            startMarker.position = point
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            startMarker.infoWindow = MyInfoWindow(R.layout.facility_popup, map, el, ctx)
            map.overlays.add(startMarker)
        }

        val startPoint = GeoPoint(50.061996, 19.937475)
        map.controller.setCenter(startPoint)
    }override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        val permissionsToRequest: ArrayList<String?> = ArrayList()
        for (i in grantResults.indices) {
            permissionsToRequest.add(permissions[i])
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toArray(arrayOfNulls<String>(0)),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    private fun requestPermissionsIfNecessary(permissions: Array<String>) {
        val permissionsToRequest: ArrayList<String> = ArrayList()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(permission)
            }
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toArray(arrayOfNulls<String>(0)),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    fun findSport(sport: List<SportDto>): Boolean{

        for(el in sport){
            if(el.name == Facilities.chosenSportString){
                return true
            }
        }

        return false;
    }

    internal class MyInfoWindow(layoutResId: Int, mapView: MapView?, val facilityItemDto: MultipleFacilityItemDto, val ctx: Context) : InfoWindow(layoutResId, mapView) {
        var index = 0
        private val de = null

        override fun onOpen(item: Any?) {
            Log.d("M", "A")

            var address = mView.findViewById<TextView>(R.id.addressFF)
            address.text = facilityItemDto.address;

            var details = mView.findViewById<Button>(R.id.details)
            val facilitiesDetails: FacilitiesDetails =
                FacilitiesDetails(ctx);
            facilitiesDetails.facilityId = facilityItemDto.facilityId
            details.setOnClickListener(facilitiesDetails)
        }

        override fun onClose() {
        }


        class FacilitiesDetails(private val ctx: Context) : View.OnClickListener{
            public var facilityId: Long = 0;

            override fun onClick(v: View?) {
                val intent = Intent(ctx, CreateEventPart2Activity::class.java);
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
                intent.putExtra("facilityId", facilityId)
                ContextCompat.startActivity(ctx, intent, null);
            }

        }
    }

    override fun longPressHelper(p: GeoPoint?): Boolean {
        return false
    }

    override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
        InfoWindow.closeAllInfoWindowsOn(findViewById<MapView>(R.id.mapContent))
        return true
    }

}