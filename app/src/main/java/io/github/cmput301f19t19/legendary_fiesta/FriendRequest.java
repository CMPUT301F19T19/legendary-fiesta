package io.github.cmput301f19t19.legendary_fiesta;

/*
 * FriendRequest is a class
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class FriendRequest implements Parcelable {
    public static final Creator<FriendRequest> CREATOR = new Creator<FriendRequest>() {
        @Override
        public FriendRequest createFromParcel(Parcel in) {
            return new FriendRequest(in);
        }

        @Override
        public FriendRequest[] newArray(int size) {
            return new FriendRequest[size];
        }
    };
    private Date date;
    private String from;
    private boolean status;
    private String to;

    /**
     * Non-argument default constructor for FireBase
     */
    public FriendRequest() {
        this.date = null;
        this.from = null;
        this.status = false;
        this.to = null;
    }

    /**
     * @param date   timestamp
     * @param from   UID from which the user sent the request
     * @param status approval status
     * @param to     UID to which user should receive the request
     */
    public FriendRequest(Date date, String from, boolean status, String to) {
        this.from = from;
        this.to = to;
        this.status = status;
        this.date = date;
    }

    protected FriendRequest(Parcel in) {
        long date = in.readLong();
        if (date == -1) {
            this.date = null;
        } else {
            this.date = new Date(date);
        }
        from = in.readString();
        status = in.readByte() != 0;
        to = in.readString();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(date == null ? -1 : date.getTime());
        parcel.writeString(from);
        parcel.writeByte((byte) (status ? 1 : 0));
        parcel.writeString(to);
    }
}
