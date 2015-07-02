package com.pandawarrior.spotifystreamer.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jt on 7/3/15.
 */
public class TopTenFragment extends Fragment {
    @InjectView(R.id.topten_recyclerview)
    RecyclerView mRecyclerView;

    String mQuery;

    TopTenRecyclerAdapter mAdapter;

    List<Track> mTrackList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        mQuery = intent.getStringExtra("artistId");

        mTrackList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_ten, container, false);
        ButterKnife.inject(this, view);
        initRecyclerView();
        if (savedInstanceState == null){
            searchTopTenList(mQuery);
        }else {
            SaveTracks saveTracks = savedInstanceState.getParcelable("trackList");
            mTrackList.addAll(saveTracks.mTrackList);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("trackList", new SaveTracks(mTrackList));
    }

    private void initRecyclerView() {
        mAdapter = new TopTenRecyclerAdapter(getActivity(), mTrackList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void searchTopTenList(String query) {
        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        Map<String, Object> country = new HashMap<>();
        country.put("country", "US");
        spotify.getArtistTopTrack(query, country, new Callback<Tracks>() {
            @Override
            public void success(Tracks tracks, Response response) {
                mTrackList.addAll(tracks.tracks);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


}
