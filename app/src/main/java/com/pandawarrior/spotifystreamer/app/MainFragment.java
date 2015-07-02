package com.pandawarrior.spotifystreamer.app;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.*;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jt on 6/25/15.
 */
public class MainFragment extends Fragment {

    private static final String LOGCAT = MainFragment.class.getSimpleName();
    @InjectView(R.id.main_recyclerview)
    RecyclerView mRecyclerViewMain;

    MainRecyclerAdapter mAdapter;

    List<Artist> mArtists;

    Toast mToast;

    SearchView searchView;
    MenuItem searchMenuItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToast = Toast.makeText(getActivity(), "No Result", Toast.LENGTH_SHORT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mArtists = new ArrayList<>();
        ButterKnife.inject(this, view);
        initRecyclerView();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        searchMenuItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(LOGCAT, query);
                searchView.setIconified(true);
                searchView.onActionViewCollapsed();
                searchArtist(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerViewMain.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new MainRecyclerAdapter(getActivity(), mArtists);
        mRecyclerViewMain.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MainRecyclerAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), TopTenActivity.class);
                intent.putExtra("artistId", mArtists.get(position).id);
                intent.putExtra("artistName", mArtists.get(position).name);
                startActivity(intent);
            }
        });

    }

    protected void searchArtist(String query) {
        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        spotify.searchArtists(query, new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager artistsPager, Response response) {
                mArtists.clear();
                List<Artist> artists = artistsPager.artists.items;
                if (artists.size() > 0) {
                    mArtists.addAll(artistsPager.artists.items);
                    mAdapter.notifyDataSetChanged();
                } else {
                    mToast.show();
                }

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SaveArtist stateData = new SaveArtist(mArtists);
        outState.putParcelable("artistsList", stateData);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null){
            SaveArtist stateData = savedInstanceState.getParcelable("artistsList");
            mArtists.addAll(stateData.mArtist);
        }

    }
}
