package com.harifrizki.storyapp.module.story.mapsstory

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.harifrizki.storyapp.R
import com.harifrizki.storyapp.data.remote.response.GeneralResponse
import com.harifrizki.storyapp.data.remote.response.GetAllStoriesResponse
import com.harifrizki.storyapp.data.remote.response.LoginResultResponse
import com.harifrizki.storyapp.databinding.ActivityStoryMapsBinding
import com.harifrizki.storyapp.model.Story
import com.harifrizki.storyapp.module.base.BaseActivity
import com.harifrizki.storyapp.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class StoryMapsActivity : BaseActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {
    private val binding by lazy {
        ActivityStoryMapsBinding.inflate(layoutInflater)
    }
    private val userLogin: LoginResultResponse? by lazy {
        PreferencesManager.getInstance(this)
            .getPreferences(MODEL_LOGIN, LoginResultResponse::class.java)
    }

    private val viewModel by viewModel<StoryMapsViewModel>()
    private lateinit var mMap: GoogleMap

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var locationCallback: LocationCallback
    private var locationUpdateState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    { }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    private fun story() {
        if (networkConnected()) {
            userLogin?.let { userLogin ->
                viewModel.story(userLogin, Story(location = 1))
                    .observe(this) {
                        story
                    }
            }
        }
    }

    private val story = Observer<DataResource<GetAllStoriesResponse>> {
        when (it.responseStatus) {
            ResponseStatus.LOADING -> {
                loadingList(true)
            }
            ResponseStatus.SUCCESS -> {
                when (isResponseSuccess(GeneralResponse(it.data?.error, it.data?.message))) {
                    true -> {
                        if (it.data?.stories?.size!! > ZERO) setUpMap(it?.data)
                        else {
                            showEmpty(
                                getString(R.string.app_name),
                                getString(R.string.message_story_not_found)
                            )
                            loadingList(isOn = false, isGetData = false)
                        }
                    }
                    false -> {
                        showEmpty(
                            getString(R.string.message_story_not_found),
                            it.data?.message
                        )
                        loadingList(isOn = false, isGetData = false)
                    }
                }
            }
            ResponseStatus.ERROR -> {
                loadingList(isOn = false, isGetData = false)
                showEmptyError(
                    getString(R.string.message_error_something_wrong),
                    it.generalResponse?.message
                )
                wasError(it.generalResponse)
            }
            else -> {}
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        if (isNetworkConnected(this))
        {
            mMap = p0

            mMap.uiSettings.isZoomControlsEnabled = true
            mMap.setOnMarkerClickListener(this)
            mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
        }
    }

    override fun onMarkerClick(p0: Marker) = false

    private fun setUpMap(stories: GetAllStoriesResponse) {
        if (ActivityCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            showWarning(
                message = getString(R.string.message_error_permission_location),
                onClick = { onBackPressedCallback })
            return
        }
        mMap.isMyLocationEnabled = true
        for (story in stories.stories!!) {
            val currentLatLng = story.lat?.toDouble()?.let { story.long?.let { long -> LatLng(it, long.toDouble()) } }
            mMap.addMarker(
                MarkerOptions()
                    .position(currentLatLng!!)
                    .title(getString(R.string.message_your_location))
                    .icon(getBitmapDescriptorFromVector(
                        this, R.drawable.ic_round_share_location_24)))
            mMap.addMarker(
                MarkerOptions().position(currentLatLng)
                    .title(getString(R.string.message_your_location)))
        }
        /*
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLng)
                mMap.addMarker(
                    MarkerOptions().position(currentLatLng)
                    .title(getString(R.string.message_your_location)))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }
        */
    }

    private fun placeMarkerOnMap(location: LatLng) {
        val markerOptions = MarkerOptions().position(location).draggable(true)
        mMap.addMarker(markerOptions)
    }
}