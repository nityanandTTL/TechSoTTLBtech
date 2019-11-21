package com.thyrocare.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.thyrocare.R;
import com.thyrocare.models.api.response.VideosResponseModel;
import com.thyrocare.utils.app.Global;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;


public class DisplayVideoListAdapter extends RecyclerView.Adapter<DisplayVideoListAdapter.MyViewHolder> {

    Activity activity;
    ArrayList<VideosResponseModel.Outputlang> VideosArylist;
    Global global;
    OnItemClickListener onItemClickListener;

    public DisplayVideoListAdapter(Activity activity, ArrayList<VideosResponseModel.Outputlang> VideosArylist) {
        this.activity = activity;
        this.VideosArylist = VideosArylist;
        global = new Global(activity);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.videolist_item, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {

        myViewHolder.tv_title.setText(VideosArylist.get(position).getTitle());
        myViewHolder.tv_description.setText(VideosArylist.get(position).getDescription());
        if (VideosArylist.get(position).isVideoPlaying()){
            myViewHolder.tv_title.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
            myViewHolder.tv_description.setTextColor(activity.getResources().getColor(R.color.btn_login_start_grd));
            myViewHolder.tv_nowPlaying.setVisibility(View.VISIBLE);
            if (VideosArylist.get(position).isVideoPaused()){
                myViewHolder.tv_nowPlaying.setText("Paused");
                myViewHolder.GIF_VideoPlaying.setVisibility(View.GONE);
            }else{
                myViewHolder.tv_nowPlaying.setText("Now Playing..");
                myViewHolder.GIF_VideoPlaying.setVisibility(View.VISIBLE);
            }
        }else{
            myViewHolder.tv_title.setTextColor(activity.getResources().getColor(R.color.black));
            myViewHolder.tv_description.setTextColor(activity.getResources().getColor(R.color.black));
            myViewHolder.GIF_VideoPlaying.setVisibility(View.GONE);
            myViewHolder.tv_nowPlaying.setVisibility(View.GONE);
        }

        global.DisplayImagewithoutDefaultImage(activity,VideosArylist.get(position).getImageurl(),myViewHolder.img_thumbnail);

        myViewHolder.rel_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < VideosArylist.size(); i++) {
                    if (i == position){
                        VideosArylist.get(i).setVideoPlaying(true);
                    }else{
                        VideosArylist.get(i).setVideoPlaying(false);
                    }
                }

                if (onItemClickListener != null) {
                    onItemClickListener.OnVideoItemSelected(VideosArylist,VideosArylist.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return VideosArylist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rel_main;
        ImageView img_thumbnail;
        TextView tv_title, tv_description,tv_nowPlaying;
        GifImageView GIF_VideoPlaying;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            rel_main = (RelativeLayout) itemView.findViewById(R.id.rel_main);
            img_thumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
            tv_nowPlaying = (TextView) itemView.findViewById(R.id.tv_nowPlaying);
            GIF_VideoPlaying = (GifImageView) itemView.findViewById(R.id.GIF_VideoPlaying);

        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnVideoItemSelected(ArrayList<VideosResponseModel.Outputlang> VideoArrylist, VideosResponseModel.Outputlang SelectedVideo);
    }

}