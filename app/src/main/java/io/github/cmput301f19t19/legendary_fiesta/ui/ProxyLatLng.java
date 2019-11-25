package io.github.cmput301f19t19.legendary_fiesta.ui;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Replacement implementation for `LatLng`
 * <p>
 * This class could be serialized and de-serialized by FireBase
 */
public class ProxyLatLng implements Parcelable {
    public static final Creator<ProxyLatLng> CREATOR = new Creator<ProxyLatLng>() {
        @Override
        public ProxyLatLng createFromParcel(Parcel in) {
            return new ProxyLatLng(in);
        }

        @Override
        public ProxyLatLng[] newArray(int size) {
            return new ProxyLatLng[size];
        }
    };
    public double latitude;
    public double longitude;

    /**
     * @param lat latitude
     * @param lng longitude
     */
    public ProxyLatLng(double lat, double lng) {
        this.latitude = lat;
        this.longitude = lng;
    }

    /**
     * Mainly reserved for FireBase
     */
    public ProxyLatLng() {
        this.latitude = 0.0d;
        this.longitude = 0.0d;
    }

    /**
     * Conversion constructor
     *
     * @param in LatLng object to be converted
     */
    public ProxyLatLng(LatLng in) {
        this.longitude = in.longitude;
        this.latitude = in.latitude;
    }

    protected ProxyLatLng(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
