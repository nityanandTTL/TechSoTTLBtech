package com.thyrocare.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thyrocare.R;
import com.thyrocare.Retrofit.GetAPIInteface;
import com.thyrocare.Retrofit.PostAPIInteface;
import com.thyrocare.Retrofit.RetroFit_APIClient;
import com.thyrocare.adapter.DisplayVideoListAdapter;
import com.thyrocare.dao.utils.ConnectionDetector;
import com.thyrocare.models.api.request.GetVideoLanguageWiseRequestModel;
import com.thyrocare.models.api.response.VideoLangaugesResponseModel;
import com.thyrocare.models.api.response.VideosResponseModel;
import com.thyrocare.utils.app.Global;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tcking.github.com.giraffeplayer2.GiraffePlayer;
import tcking.github.com.giraffeplayer2.Option;
import tcking.github.com.giraffeplayer2.PlayerListener;
import tcking.github.com.giraffeplayer2.VideoInfo;
import tcking.github.com.giraffeplayer2.VideoView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

import static com.thyrocare.network.AbstractApiModel.B2B;


public class ThyrocareVideos extends AppCompatActivity {

    Activity mActivity;
    VideoView videoView;
    private int a = 0;
    private Global globalclass;
    private ArrayList<VideoLangaugesResponseModel.Outputlang> VideoLangArylist;
    private RecyclerView recView;
    ArrayList<VideosResponseModel.Outputlang> VideosArylist;
    private DisplayVideoListAdapter videoListAdapter;
    private TextView tv_languageSelected;
    private SharedPreferences prefs_Language;
    private TextView tv_noDatafound;
    ConnectionDetector cd;
    View parentLayout;
    private Snackbar internetErrorSnackbar;
    private int b = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thyrocare_videos);
        parentLayout = findViewById(android.R.id.content);
        mActivity = ThyrocareVideos.this;
        cd = new ConnectionDetector(mActivity);
        internetErrorSnackbar = Snackbar.make(parentLayout, "Unable to Play this Video. Please check your Internet Connection or try after sometime.", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (videoView.getVideoInfo().getUri() != null){
                            videoView.getPlayer().start();
                        }
                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ));

        prefs_Language = mActivity.getSharedPreferences("Video_lang_pref", 0);
        globalclass = new Global(mActivity);
        videoView = (VideoView) findViewById(R.id.video_view1);
        recView = (RecyclerView) findViewById(R.id.recView);
        tv_noDatafound = (TextView) findViewById(R.id.tv_noDatafound);
        initToolbar();
        if (TextUtils.isEmpty(prefs_Language.getString("LanguageSelected", ""))) {
            if (cd.isConnectingToInternet()){
                CallVideoLanguagesAPI();
            }else{
                globalclass.showCustomToast(mActivity,"Please check internet connection.");
            }
        } else {
            tv_languageSelected.setText(prefs_Language.getString("LanguageSelected", ""));
            if (cd.isConnectingToInternet()){
                GetVideosBasedonLanguage(prefs_Language.getString("LanguageID", ""));
            }else{
                globalclass.showCustomToast(mActivity,"Please check internet connection.");
            }
            tv_noDatafound.setVisibility(View.VISIBLE);
            if (videoView.getVideoInfo().getUri() != null && videoView.getPlayer().isPlaying()) {
                videoView.getPlayer().stop();
                videoView.getPlayer().release();
            }
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarVideo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_languageSelected = (TextView) findViewById(R.id.tv_languageSelected);
        tv_languageSelected.setPaintFlags(tv_languageSelected.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        tv_languageSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()){
                    CallVideoLanguagesAPI();
                }else{
                    globalclass.showCustomToast(mActivity,"Please check internet connection.");
                }
            }
        });
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Toolbar", "Clicked");
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }*/

    private void CallVideoLanguagesAPI() {
        globalclass.showProgressDialog(mActivity, "please wait..");
        GetAPIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, B2B).create(GetAPIInteface.class);
        Call<VideoLangaugesResponseModel> responseCall = apiInterface.getVideoLanguages();
        responseCall.enqueue(new Callback<VideoLangaugesResponseModel>() {
            @Override
            public void onResponse(Call<VideoLangaugesResponseModel> call, Response<VideoLangaugesResponseModel> response) {

                globalclass.hideProgressDialog();

                VideoLangArylist = new ArrayList<>();
                if (response.isSuccessful()) {
                    VideoLangaugesResponseModel model = response.body();
                    if (model != null && model.getResId() != null && model.getResId().equalsIgnoreCase("RSS0000") && model.getOutput() != null && model.getOutput().size() > 0) {
                        VideoLangArylist = model.getOutput();
                    }
                }

                if (VideoLangArylist.size() > 0) {
                    SharedPreferences pref = mActivity.getSharedPreferences("Video_lang_pref", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(VideoLangArylist);
                    editor.putString("LanguageArryList", json);
                    editor.apply();

                    if (TextUtils.isEmpty(prefs_Language.getString("LanguageSelected", ""))) {
                        showLanguageDialogList("Select Language", VideoLangArylist, tv_languageSelected, false);
                        tv_noDatafound.setVisibility(View.GONE);
                    } else {
                        showLanguageDialogList("Select Language", VideoLangArylist, tv_languageSelected, true);
                    }
                } else {
                    Gson gson = new Gson();
                    String json = prefs_Language.getString("LanguageArryList", "");
                    VideoLangArylist = gson.fromJson(json, new TypeToken<List<VideoLangaugesResponseModel.Outputlang>>() {}.getType());
                    if (VideoLangArylist != null && VideoLangArylist.size() > 0) {
                        if (TextUtils.isEmpty(prefs_Language.getString("LanguageSelected", ""))) {
                            showLanguageDialogList("Select Language", VideoLangArylist, tv_languageSelected, false);
                            tv_noDatafound.setVisibility(View.VISIBLE);
                        } else {
                            showLanguageDialogList("Select Language", VideoLangArylist, tv_languageSelected, true);
                        }
                    } else {
                        globalclass.showcenterCustomToast(mActivity, "Unable to fetch languages from server. Please try after sometime.",Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<VideoLangaugesResponseModel> call, Throwable t) {
                globalclass.hideProgressDialog();
                recView.setVisibility(View.GONE);
                tv_noDatafound.setVisibility(View.VISIBLE);
                globalclass.showcenterCustomToast(mActivity, "Unable to fetch data from server.",Toast.LENGTH_LONG);
            }
        });
    }

    private void showLanguageDialogList(String title, ArrayList<VideoLangaugesResponseModel.Outputlang> list, final TextView textView, boolean ShowCancelOption) {
        if (list != null && !list.isEmpty()) {
            final AlertDialog.Builder builderSingle = new AlertDialog.Builder(mActivity);
            builderSingle.setTitle(title);
            builderSingle.setCancelable(false);
            final ArrayAdapter<VideoLangaugesResponseModel.Outputlang> arrayAdapter = new ArrayAdapter<>(mActivity, android.R.layout.select_dialog_singlechoice);
            arrayAdapter.addAll(list);
            if (ShowCancelOption) {
                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }


            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which < arrayAdapter.getCount()) {
                        String strName = arrayAdapter.getItem(which).getLANGUAGE();
                        textView.setText(strName);
                        if (cd.isConnectingToInternet()){
                            GetVideosBasedonLanguage(arrayAdapter.getItem(which).getIID_NEW());
                            if (videoView.getVideoInfo().getUri() != null) {
                                videoView.getPlayer().stop();
                                videoView.getPlayer().release();
                            }
                        }else{
                            globalclass.showCustomToast(mActivity,"Please check internet connection.");
                        }
                        SharedPreferences pref = mActivity.getSharedPreferences("Video_lang_pref", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("LanguageSelected", arrayAdapter.getItem(which).getLANGUAGE());
                        editor.putString("LanguageID", arrayAdapter.getItem(which).getIID_NEW());
                        editor.apply();
                    }
                    dialog.dismiss();
                }
            });
            builderSingle.show();
        } else {
            globalclass.showCustomToast(mActivity, "List is not available");
        }
    }

    private void GetVideosBasedonLanguage(String LanguageID) {

        GetVideoLanguageWiseRequestModel model = new GetVideoLanguageWiseRequestModel();
        model.setApp("3"); // TODO Change this to 3 during live
        model.setLanguage(LanguageID);

        PostAPIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, B2B).create(PostAPIInteface.class);
        Call<VideosResponseModel> responseCall = apiInterface.getVideobasedOnLanguage(model);
        globalclass.showProgressDialog(mActivity, mActivity.getResources().getString(R.string.loading));
        responseCall.enqueue(new Callback<VideosResponseModel>() {
            @Override
            public void onResponse(Call<VideosResponseModel> call, Response<VideosResponseModel> response) {
                globalclass.hideProgressDialog();
                if (response.isSuccessful() && response.body() != null) {
                    VideosResponseModel responseModel = response.body();
                    if (!TextUtils.isEmpty(responseModel.getResId()) && responseModel.getResId().equalsIgnoreCase("RSS0000") && responseModel.getOutput() != null && responseModel.getOutput().size() > 0) {
                        tv_noDatafound.setVisibility(View.GONE);
                        VideosArylist = new ArrayList<>();
                        VideosArylist = responseModel.getOutput();
                        DisplayVideosInList();
                    } else {
                        recView.setVisibility(View.GONE);
                        tv_noDatafound.setVisibility(View.VISIBLE);

                    }
                } else {
                    tv_noDatafound.setVisibility(View.VISIBLE);
                    System.out.println("No Videos found");
                }
            }

            @Override
            public void onFailure(Call<VideosResponseModel> call, Throwable t) {
                globalclass.hideProgressDialog();
                tv_noDatafound.setVisibility(View.VISIBLE);

            }
        });

    }

    private void DisplayVideosInList() {

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        recView.setLayoutManager(mLayoutManager);
        recView.setItemAnimator(new DefaultItemAnimator());
        recView.addItemDecoration(new DividerItemDecoration(mActivity, LinearLayoutManager.VERTICAL));
        recView.setHasFixedSize(true);
        recView.setLayoutManager(mLayoutManager);
        recView.setVisibility(View.VISIBLE);

        videoListAdapter = new DisplayVideoListAdapter(mActivity, VideosArylist);
        videoListAdapter.setOnItemClickListener(new DisplayVideoListAdapter.OnItemClickListener() {
            @Override
            public void OnVideoItemSelected(ArrayList<VideosResponseModel.Outputlang> VideoArrylist1, VideosResponseModel.Outputlang SelectedVideo) {


                if (videoView.getVideoInfo().getUri() != null && videoView.getVideoInfo().getUri().toString().equalsIgnoreCase(SelectedVideo.getPath()) ){

                    if (videoView.getPlayer().isPlaying()){
//                        globalclass.showcenterCustomToast(mActivity,"Already Playing");
                    }else{

                        if (videoView.getPlayer().getCurrentState() == GiraffePlayer.STATE_PLAYBACK_COMPLETED){
                            initializePlayer(SelectedVideo);
                            VideosArylist = VideoArrylist1;
                            videoListAdapter.notifyDataSetChanged();
                        }else{
                            videoView.getPlayer().start();
                        }

                    }

                }else{
                    initializePlayer(SelectedVideo);
                    VideosArylist = VideoArrylist1;
                    videoListAdapter.notifyDataSetChanged();
                }


            }
        });
        recView.setAdapter(videoListAdapter);

    }

    private void initializePlayer(final VideosResponseModel.Outputlang Video) {


        if (videoView.getVideoInfo().getUri() != null) {
            videoView.getPlayer().stop();
            videoView.getPlayer().release();
        }

        videoView.setVisibility(View.VISIBLE);

        videoView.setVideoPath(Video.getPath()).getPlayer().setDisplayModel(GiraffePlayer.DISPLAY_NORMAL);
        videoView.getPlayer().getVideoInfo().setTitle(Video.getTitle()).setAspectRatio(VideoInfo.AR_ASPECT_FIT_PARENT).setBgColor(Color.BLACK).setShowTopBar(true).addOption(Option.create(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "multiple_requests", 1L));
        videoView.getPlayer().start();

        videoView.setPlayerListener(new PlayerListener() {
            @Override
            public void onPrepared(GiraffePlayer giraffePlayer) {
                System.out.println("tejas >>> onPrepared");
            }

            @Override
            public void onBufferingUpdate(GiraffePlayer giraffePlayer, int percent) {
//               System.out.println("tejas >>> onBufferingUpdate");
            }

            @Override
            public boolean onInfo(GiraffePlayer giraffePlayer, int what, int extra) {
                System.out.println("tejas >>> onInfo");
                return false;
            }

            @Override
            public void onCompletion(GiraffePlayer giraffePlayer) {
                System.out.println("tejas >>> onCompletion");

            }

            @Override
            public void onSeekComplete(GiraffePlayer giraffePlayer) {
                System.out.println("tejas >>> onSeekComplete");
            }

            @Override
            public boolean onError(GiraffePlayer giraffePlayer, int what, int extra) {
                System.out.println("tejas >>> onError");
                if (a == 0){
                    a = giraffePlayer.getCurrentPosition();

                }

                long milliseconds = giraffePlayer.getCurrentPosition();
                long minutes = (milliseconds / 1000) / 60;
                long seconds = (milliseconds / 1000) % 60;


                long milliseconds1 =  giraffePlayer.getDuration();
                long minutes1 = (milliseconds1 / 1000) / 60;
                long seconds1 = (milliseconds1 / 1000) % 60;

                if (minutes != minutes1 && seconds != seconds1){
//                    globalclass.showcenterCustomToast(mActivity,"Error! Unable to play this video. Please Check your internet connection or try after sometime.",Toast.LENGTH_SHORT);
                    internetErrorSnackbar.show();
                }

                System.out.println("tejas >>> CurrentPosition  "+a +" duration : "+giraffePlayer.getDuration() );
                System.out.println("tejas >>> CurrentPositionMin  "+minutes +" CurrentPositionSec : "+seconds);
                System.out.println("tejas >>> durationMin  "+minutes1 +" durationSec : "+seconds1);


                return false;
            }

            @Override
            public void onPause(GiraffePlayer giraffePlayer) {

                if (VideosArylist != null && VideosArylist.size() > 0) {
                    for (int i = 0; i < VideosArylist.size(); i++) {
                        if (VideosArylist.get(i).getId().equalsIgnoreCase(Video.getId())) {
                            VideosArylist.get(i).setVideoPaused(true);
                        }
                    }
                    videoListAdapter.notifyDataSetChanged();
                }
                System.out.println("tejas >>> onPause");
            }

            @Override
            public void onRelease(GiraffePlayer giraffePlayer) {
                System.out.println("tejas >>> onRelease");


            }

            @Override
            public void onStart(GiraffePlayer giraffePlayer) {

                if (internetErrorSnackbar != null && internetErrorSnackbar.isShown()){
                    internetErrorSnackbar.dismiss();
                }
                if (VideosArylist != null && VideosArylist.size() > 0) {
                    for (int i = 0; i < VideosArylist.size(); i++) {

                        if (VideosArylist.get(i).getId().equalsIgnoreCase(Video.getId())) {
                            VideosArylist.get(i).setVideoPlaying(true);
                            VideosArylist.get(i).setVideoPaused(false);
                        } else {
                            VideosArylist.get(i).setVideoPlaying(false);
                        }

                    }
                    System.out.println("tejas >>> seek Time : " + a);
                    /*if (a > 0){
                        if (cd.isConnectingToInternet()){
                            giraffePlayer.seekTo(a);
                            a = 0;
                        }
                    }*/
                    videoView.setVisibility(View.VISIBLE);
                    videoListAdapter.notifyDataSetChanged();
                }
                System.out.println("tejas >>> onStart");
            }

            @Override
            public void onTargetStateChange(int oldState, int newState) {
                System.out.println("tejas >>> onTargetStateChange");
            }

            @Override
            public void onCurrentStateChange(int oldState, int newState) {

                if (oldState == GiraffePlayer.STATE_ERROR && newState == GiraffePlayer.STATE_PLAYBACK_COMPLETED) {

                    long milliseconds = videoView.getPlayer().getCurrentPosition();
                    long minutes = (milliseconds / 1000) / 60;
                    long seconds = (milliseconds / 1000) % 60;


                    long milliseconds1 =  videoView.getPlayer().getDuration();
                    long minutes1 = (milliseconds1 / 1000) / 60;
                    long seconds1 = (milliseconds1 / 1000) % 60;

                    if (minutes == minutes1 && seconds == seconds1){

                        if (VideosArylist != null && VideosArylist.size() > 0) {
                            for (int i = 0; i < VideosArylist.size(); i++) {
                                VideosArylist.get(i).setVideoPlaying(false);
                                VideosArylist.get(i).setVideoPaused(false);
                            }
                            videoListAdapter.notifyDataSetChanged();
                        }
                        a = 0;
                        videoView.setVisibility(View.GONE);
                    }else{
                        if (VideosArylist != null && VideosArylist.size() > 0) {
                            for (int i = 0; i < VideosArylist.size(); i++) {

                                if (VideosArylist.get(i).getId().equalsIgnoreCase(Video.getId())) {
                                    VideosArylist.get(i).setVideoPlaying(true);
                                    VideosArylist.get(i).setVideoPaused(true);
                                } else {
                                    VideosArylist.get(i).setVideoPlaying(false);
                                }
                            }
                            videoListAdapter.notifyDataSetChanged();
                        }
                        videoView.setVisibility(View.VISIBLE);
                    }


                }else if ((oldState == GiraffePlayer.STATE_PLAYING && newState == GiraffePlayer.STATE_PLAYBACK_COMPLETED )|| (oldState == GiraffePlayer.DISPLAY_NORMAL && newState == GiraffePlayer.STATE_RELEASE)) {
                    videoView.setVisibility(View.GONE);
                    if (VideosArylist != null && VideosArylist.size() > 0) {
                        for (int i = 0; i < VideosArylist.size(); i++) {

                            VideosArylist.get(i).setVideoPlaying(false);
                        }
                        videoListAdapter.notifyDataSetChanged();
                    }
                } else if (oldState == GiraffePlayer.STATE_PREPARED && newState == GiraffePlayer.STATE_PLAYING) {
                    if (VideosArylist != null && VideosArylist.size() > 0) {
                        for (int i = 0; i < VideosArylist.size(); i++) {

                            if (VideosArylist.get(i).getId().equalsIgnoreCase(Video.getId())) {
                                VideosArylist.get(i).setVideoPlaying(true);
                                VideosArylist.get(i).setVideoPaused(false);
                            } else {
                                VideosArylist.get(i).setVideoPlaying(false);
                            }
                        }
                        if (b>0){
                            videoView.getPlayer().seekTo(459495495);
                            b = 0;
                        }
                        videoListAdapter.notifyDataSetChanged();
                    }
                }else if (oldState == GiraffePlayer.STATE_IDLE && (newState == GiraffePlayer.STATE_LAZYLOADING || newState == GiraffePlayer.STATE_PREPARING)) {
                    if (VideosArylist != null && VideosArylist.size() > 0) {
                        for (int i = 0; i < VideosArylist.size(); i++) {

                            if (VideosArylist.get(i).getId().equalsIgnoreCase(Video.getId())) {
                                VideosArylist.get(i).setVideoPlaying(true);
                                VideosArylist.get(i).setVideoPaused(true);
                            } else {
                                VideosArylist.get(i).setVideoPlaying(false);
                            }
                        }
                        videoListAdapter.notifyDataSetChanged();
                    }
                    videoView.setVisibility(View.VISIBLE);
                }else if (oldState == GiraffePlayer.STATE_PLAYBACK_COMPLETED &&  newState == GiraffePlayer.STATE_PLAYING) {
                    if (VideosArylist != null && VideosArylist.size() > 0) {
                        for (int i = 0; i < VideosArylist.size(); i++) {

                            if (VideosArylist.get(i).getId().equalsIgnoreCase(Video.getId())) {
                                VideosArylist.get(i).setVideoPlaying(true);
                                VideosArylist.get(i).setVideoPaused(false);
                            } else {
                                VideosArylist.get(i).setVideoPlaying(false);
                            }
                        }
                        if (a > 0){
                            videoView.getPlayer().seekTo(a);
                        }
                        videoListAdapter.notifyDataSetChanged();
                    }
                    videoView.setVisibility(View.VISIBLE);
                } else{
                    videoView.setVisibility(View.VISIBLE);
                }
                System.out.println("tejas >>> onCurrentStateChange " + oldState + "new State : " + newState);
            }

            @Override
            public void onDisplayModelChange(int oldModel, int newModel) {
                System.out.println("tejas >>> onDisplayModelChange");
            }

            @Override
            public void onPreparing(GiraffePlayer giraffePlayer) {
                System.out.println("tejas >>> onPreparing");
            }

            @Override
            public void onTimedText(GiraffePlayer giraffePlayer, IjkTimedText text) {
                System.out.println("tejas >>> onTimedText");
            }

            @Override
            public void onLazyLoadProgress(GiraffePlayer giraffePlayer, int progress) {
                System.out.println("tejas >>> onLazyLoadProgress");
            }

            @Override
            public void onLazyLoadError(GiraffePlayer giraffePlayer, String message) {
                System.out.println("tejas >>> onLazyLoadError");
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (videoView.getVideoInfo().getUri() != null) {
            videoView.getPlayer().release();
        }

    }

    @Override
    public void onBackPressed() {

        if (videoView.getVideoInfo().getUri() != null && videoView.getPlayer().onBackPressed()) {
            return;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {

        if (videoView.getVideoInfo().getUri() != null){
            System.out.println("tejas >>> currentposition : "+videoView.getPlayer().getCurrentPosition());
            b = videoView.getPlayer().getCurrentPosition();
        }
        super.onPause();

    }

    @Override
    protected void onResume() {

        if (videoView.getVideoInfo().getUri() != null && b > 0){
            System.out.println("tejas >>> seek to : "+b);
        }
        super.onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (videoView.getVideoInfo().getUri() != null && videoView.getPlayer().isPlaying()) {
                videoView.getPlayer().setDisplayModel(GiraffePlayer.DISPLAY_FULL_WINDOW);
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (videoView.getVideoInfo().getUri() != null && videoView.getPlayer().isPlaying()) {
                videoView.getPlayer().setDisplayModel(GiraffePlayer.DISPLAY_NORMAL);
            }
        }

    }
}