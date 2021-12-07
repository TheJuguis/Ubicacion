package com.example.ubicacion

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.ubicacion.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        updateLocation()

        binding.btnUpdateLocation.setOnClickListener{
            updateLocation()
        }
    }

    fun updateLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"No tiene permiso",Toast.LENGTH_SHORT).show()
            Log.d("LocationPermissions", "No tiene permiso")
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 1)
            }
            return
        }else{
            fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
                if (location != null){
                    Log.d("LocationPermissions", "Success ${location?.latitude}, ${location?.longitude}")
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    Log.d("LocationPermissions", "${addresses[0]}")
                    binding.tvLugar.setText("${addresses[0].countryName}, ${addresses[0].adminArea}, ${addresses[0].locality}")
                    binding.tvCoordenadas.setText("${location?.latitude}, ${location?.longitude}")
                }
            }
            Log.d("LocationPermissions", "Tiene permiso")
        }
    }
}