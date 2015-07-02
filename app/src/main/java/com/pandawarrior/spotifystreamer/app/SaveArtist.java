package com.pandawarrior.spotifystreamer.app;

import android.os.Parcel;
import android.os.Parcelable;
import kaaes.spotify.webapi.android.models.Artist;

import java.util.List;

/**
 * Created by jt on 6/26/15.
 */
public class SaveArtist implements Parcelable {
    List<Artist> mArtist;

    public SaveArtist(List<Artist> artist) {
        mArtist = artist;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeList(mTrackList);
    }

    private SaveArtist(Parcel in){
        in.readList(mArtist, null);
    }

    public static final Parcelable.Creator<SaveArtist> CREATOR = new Parcelable.Creator<SaveArtist>(){

        @Override
        public SaveArtist createFromParcel(Parcel source) {
            return new SaveArtist(source);
        }

        @Override
        public SaveArtist[] newArray(int size) {
            return new SaveArtist[size];
        }
    };
}
