package com.pandawarrior.spotifystreamer.app;

import android.os.Parcel;
import android.os.Parcelable;
import kaaes.spotify.webapi.android.models.Track;

import java.util.List;

/**
 * Created by jt on 7/3/15.
 */
public class SaveTracks implements Parcelable{
    List<Track> mTrackList;

    public SaveTracks(List<Track> trackList) {
        mTrackList = trackList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeList(mTrackList);
    }

    private SaveTracks(Parcel in){
        in.readList(mTrackList, null);
    }

    public static final Parcelable.Creator<SaveTracks> CREATOR = new Parcelable.Creator<SaveTracks>(){

        @Override
        public SaveTracks createFromParcel(Parcel source) {
            return new SaveTracks(source);
        }

        @Override
        public SaveTracks[] newArray(int size) {
            return new SaveTracks[size];
        }
    };
}
