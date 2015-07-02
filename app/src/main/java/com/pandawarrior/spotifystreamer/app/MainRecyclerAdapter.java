package com.pandawarrior.spotifystreamer.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.squareup.picasso.Picasso;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

import java.util.List;

/**
 * Created by jt on 6/26/15.
 */
public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>{
    List<Artist> mArtists;

    Context context;

    ItemClickListener mItemClickListener;

    public MainRecyclerAdapter(Context context, List<Artist> artists) {
        this.mArtists = artists;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mArtists.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Artist artist = mArtists.get(position);
        if (artist != null){
            List<Image> images = artist.images;
            if (images.size() > 0){
                Picasso.with(context)
                        .load(images.get(0).url)
                        .error(R.drawable.abc_ic_menu_cut_mtrl_alpha)
                        .into(holder.mImageViewMain);
            } else {
                Picasso.with(context)
                        .load(R.drawable.abc_ab_share_pack_mtrl_alpha)
                        .into(holder.mImageViewMain);
            }
            holder.mTextViewMain.setText(artist.name);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @InjectView(R.id.row_main_image)
        ImageView mImageViewMain;

        @InjectView(R.id.row_main_text)
        TextView mTextViewMain;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null){
                mItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.mItemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }
}
