package com.dubizzle.map.widget

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.dubizzle.map.MapInteractor
import com.dubizzle.map.MapLatLng
import com.dubizzle.map.R
import com.huawei.hms.maps.CameraUpdate
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.LatLng

class MapImpl(
    val frameLayout: android.widget.FrameLayout,
    var mapInteractor: MapInteractor? = null,
    val owner: Lifecycle
) : DefaultLifecycleObserver, OnMapReadyCallback {
    
    private var map: HuaweiMap? = null
    var newLatLngZoom: CameraUpdate? = null
    var mapView: MapView? = null

    init {
        mapView = frameLayout.findViewById<MapView>(R.id.mapView)
        owner.addObserver(this)
        mapView?.onCreate(null)
        mapView?.getMapAsync(this)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        mapView?.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        mapView?.onPause()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        mapView?.onStart()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        mapView?.onStop()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        mapView?.onDestroy()
    }

    override fun onMapReady(p0: HuaweiMap) {
        map = p0.apply {
            setOnMapClickListener {
                mapInteractor?.onMapClicked(it.toMapLatLng())
            }
        }
        latLngZoom(newLatLngZoom)
        mapInteractor?.onMapReady()
        map?.uiSettings?.isZoomControlsEnabled = false
        map?.uiSettings?.isZoomGesturesEnabled = false
        map?.uiSettings?.isCompassEnabled = false
        map?.uiSettings?.isMapToolbarEnabled = false
        map?.uiSettings?.isRotateGesturesEnabled = false
        map?.uiSettings?.isTiltGesturesEnabled = false
        map?.uiSettings?.isScrollGesturesEnabledDuringRotateOrZoom = false
        map?.uiSettings?.isMyLocationButtonEnabled = false
        map?.uiSettings?.isScrollGesturesEnabled = false
    }

    fun moveCamera(mapLatLng: MapLatLng, mapZoomLevel: Float) {
        newLatLngZoom = CameraUpdateFactory.newLatLngZoom(mapLatLng.toLatLng(), mapZoomLevel)
        latLngZoom(newLatLngZoom)
    }

    private fun latLngZoom(newLatLngZoom: CameraUpdate?) {
        if (map != null && newLatLngZoom != null) {
            map?.moveCamera(newLatLngZoom)
        }
    }
}

fun MapLatLng.toLatLng(): LatLng {
    return LatLng(this.latitude, this.longitude)
}

private fun LatLng.toMapLatLng(): MapLatLng {
    return MapLatLng(this.latitude, this.longitude)
}



