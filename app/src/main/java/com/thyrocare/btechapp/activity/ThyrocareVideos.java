package com.thyrocare.btechapp.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.media.ThumbnailUtils;
import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.adapter.DisplayVideoListAdapter;
import com.thyrocare.btechapp.dao.utils.ConnectionDetector;
import com.thyrocare.btechapp.models.api.request.GetVideoLanguageWiseRequestModel;
import com.thyrocare.btechapp.models.api.response.VideoLangaugesResponseModel;
import com.thyrocare.btechapp.models.api.response.VideosResponseModel;
import com.thyrocare.btechapp.utils.app.Global;


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


public class ThyrocareVideos extends AppCompatActivity {

    Activity mActivity;
    VideoView videoView;
    ArrayList<VideosResponseModel.Outputlang> VideosArylist;
    ConnectionDetector cd;
    View parentLayout;
    TextView tv_toolbar;
    ImageView iv_back, iv_home;
    private int a = 0;
    private Global globalclass;
    private ArrayList<VideoLangaugesResponseModel.Outputlang> VideoLangArylist;
    private RecyclerView recView;
    private DisplayVideoListAdapter videoListAdapter;
    //    private TextView tv_languageSelected;
    private SharedPreferences prefs_Language;
    private TextView tv_noDatafound;
    private Snackbar internetErrorSnackbar;
    private int b = 0;
    private Spinner spn_purpose;

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
                        if (videoView.getVideoInfo().getUri() != null) {
                            videoView.getPlayer().start();
                        }
                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light));

        spn_purpose = findViewById(R.id.spn_purpose1);
        prefs_Language = mActivity.getSharedPreferences("Video_lang_pref", 0);
        globalclass = new Global(mActivity);
        videoView = (VideoView) findViewById(R.id.video_view1);
        recView = (RecyclerView) findViewById(R.id.recView);
        tv_noDatafound = (TextView) findViewById(R.id.tv_noDatafound);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        iv_back = findViewById(R.id.iv_back);
        iv_home = findViewById(R.id.iv_home);
        tv_toolbar.setText("Videos");
//        initToolbar();
        listners();

        if (cd.isConnectingToInternet()) {
            CallVideoLanguagesAPI();
        } else {
            globalclass.showCustomToast(mActivity, "Please check internet connection.");
        }
       /* if (TextUtils.isEmpty(prefs_Language.getString("LanguageSelected", ""))) {

        } else {
            tv_languageSelected.setText(prefs_Language.getString("LanguageSelected", ""));
            if (cd.isConnectingToInternet()) {
                GetVideosBasedonLanguage(prefs_Language.getString("LanguageID", ""));
            } else {
                globalclass.showCustomToast(mActivity, "Please check internet connection.");
            }
            tv_noDatafound.setVisibility(View.VISIBLE);
            if (videoView.getVideoInfo().getUri() != null && videoView.getPlayer().isPlaying()) {
                videoView.getPlayer().stop();
                videoView.getPlayer().release();
            }
        }*/
    }

    private void listners() {
        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initToolbar() {
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarVideo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        tv_languageSelected = (TextView) findViewById(R.id.tv_languageSelected);
//        tv_languageSelected.setPaintFlags(tv_languageSelected.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        tv_languageSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
//                    CallVideoLanguagesAPI();
                } else {
                    globalclass.showCustomToast(mActivity, "Please check internet connection.");
                }
            }
        });
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageLogger.LogError("Toolbar", "Clicked");
            }
        });*/

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void CallVideoLanguagesAPI() {
        globalclass.showProgressDialog(mActivity, "please wait..");
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.B2B_API_VERSION))).create(GetAPIInterface.class);
        Call<VideoLangaugesResponseModel> responseCall = apiInterface.getVideoLanguages();
        responseCall.enqueue(new Callback<VideoLangaugesResponseModel>() {
            @Override
            public void onResponse(Call<VideoLangaugesResponseModel> call, Response<VideoLangaugesResponseModel> response) {

                globalclass.hideProgressDialog(mActivity);

                VideoLangArylist = new ArrayList<>();
                if (response.isSuccessful()) {
                    VideoLangaugesResponseModel model = response.body();
                    if (model != null && model.getResId() != null && model.getResId().equalsIgnoreCase("RSS0000") && model.getOutput() != null && model.getOutput().size() > 0) {
                        VideoLangArylist = model.getOutput();
                        setSpnLanguage(VideoLangArylist);
                    }
                }

                if (VideoLangArylist.size() > 0) {
                    SharedPreferences pref = mActivity.getSharedPreferences("Video_lang_pref", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(VideoLangArylist);
                    editor.putString("LanguageArryList", json);
                    editor.apply();

                    //Sushil
                    /*if (TextUtils.isEmpty(prefs_Language.getString("LanguageSelected", ""))) {
                        showLanguageDialogList("Select Language", VideoLangArylist, tv_languageSelected, false);
                        tv_noDatafound.setVisibility(View.GONE);
                    } else {
                        showLanguageDialogList("Select Language", VideoLangArylist, tv_languageSelected, true);
                    }*/
                } else {
                    Gson gson = new Gson();
                    String json = prefs_Language.getString("LanguageArryList", "");
                    VideoLangArylist = gson.fromJson(json, new TypeToken<List<VideoLangaugesResponseModel.Outputlang>>() {
                    }.getType());
                    if (VideoLangArylist != null && VideoLangArylist.size() > 0) {
                        //Sushil
                        /*if (TextUtils.isEmpty(prefs_Language.getString("LanguageSelected", ""))) {
//                            showLanguageDialogList("Select Language", VideoLangArylist, tv_languageSelected, false);
                            tv_noDatafound.setVisibility(View.VISIBLE);
                        } else {
//                            showLanguageDialogList("Select Language", VideoLangArylist, tv_languageSelected, true);
                        }*/
                    } else {
                        globalclass.showcenterCustomToast(mActivity, "Unable to fetch languages from server. Please try after sometime.", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<VideoLangaugesResponseModel> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                recView.setVisibility(View.GONE);
                tv_noDatafound.setVisibility(View.VISIBLE);
                globalclass.showcenterCustomToast(mActivity, "Unable to fetch data from server.", Toast.LENGTH_LONG);
            }
        });
    }

    private void setSpnLanguage(final ArrayList<VideoLangaugesResponseModel.Outputlang> videoLangArylist) {
        videoView.setVisibility(View.GONE);
        VideoLangaugesResponseModel.Outputlang selectValue = new VideoLangaugesResponseModel.Outputlang();
        selectValue.setIID_NEW("");
        selectValue.setLANGUAGE("Select language");
        videoLangArylist.add(0, selectValue);
        ArrayAdapter<VideoLangaugesResponseModel.Outputlang> arrayAdapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_list_item_1, videoLangArylist);
        spn_purpose.setAdapter(arrayAdapter);
        spn_purpose.setSelection(0);
        spn_purpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spn_purpose.getSelectedItemPosition() == 0) {
                    recView.setVisibility(View.GONE);
                    Toast.makeText(mActivity, "Kindly select language", Toast.LENGTH_SHORT).show();
                    tv_noDatafound.setVisibility(View.GONE);

                } else {
                    for (int i = 0; i < videoLangArylist.size(); i++) {
                        if (spn_purpose.getSelectedItem().toString().equalsIgnoreCase(videoLangArylist.get(i).getLANGUAGE())) {
                            GetVideosBasedonLanguage(videoLangArylist.get(i).getIID_NEW());
                            if (videoView.getVideoInfo().getUri() != null) {
                                videoView.getPlayer().stop();
                                videoView.getPlayer().release();
                            }
                            break;
                        }
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /*private void showLanguageDialogList(String title, ArrayList<VideoLangaugesResponseModel.Outputlang> list, final TextView textView, boolean ShowCancelOption) {
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
                        if (cd.isConnectingToInternet()) {
                            GetVideosBasedonLanguage(arrayAdapter.getItem(which).getIID_NEW());
                            if (videoView.getVideoInfo().getUri() != null) {
                                videoView.getPlayer().stop();
                                videoView.getPlayer().release();
                            }
                        } else {
                            globalclass.showCustomToast(mActivity, "Please check internet connection.");
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
*/
    private void GetVideosBasedonLanguage(String LanguageID) {

        GetVideoLanguageWiseRequestModel model = new GetVideoLanguageWiseRequestModel();
        model.setApp("3"); // TODO Change this to 3 during live
        model.setLanguage(LanguageID);

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.B2B_API_VERSION))).create(PostAPIInterface.class);
        Call<VideosResponseModel> responseCall = apiInterface.getVideobasedOnLanguage(model);
        globalclass.showProgressDialog(mActivity, mActivity.getResources().getString(R.string.loading));
        responseCall.enqueue(new Callback<VideosResponseModel>() {
            @Override
            public void onResponse(Call<VideosResponseModel> call, Response<VideosResponseModel> response) {
                globalclass.hideProgressDialog(mActivity);
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
                    MessageLogger.PrintMsg("No Videos found");
                }
            }

            @Override
            public void onFailure(Call<VideosResponseModel> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                tv_noDatafound.setVisibility(View.VISIBLE);
            }
        });

    }

    private void DisplayVideosInList() {

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(mActivity);

        recView.setVisibility(View.VISIBLE);

        videoListAdapter = new DisplayVideoListAdapter(mActivity, VideosArylist);
        videoListAdapter.setOnItemClickListener(new DisplayVideoListAdapter.OnItemClickListener() {
            @Override
            public void OnVideoItemSelected(ArrayList<VideosResponseModel.Outputlang> VideoArrylist1, VideosResponseModel.Outputlang SelectedVideo) {


                if (videoView.getVideoInfo().getUri() != null && videoView.getVideoInfo().getUri().toString().equalsIgnoreCase(SelectedVideo.getPath())) {

                    if (videoView.getPlayer().isPlaying()) {
//                        globalclass.showcenterCustomToast(mActivity,"Already Playing");
                    } else {

                        if (videoView.getPlayer().getCurrentState() == GiraffePlayer.STATE_PLAYBACK_COMPLETED) {
                            initializePlayer(SelectedVideo);
                            VideosArylist = VideoArrylist1;
                            videoListAdapter.notifyDataSetChanged();
                        } else {
                            videoView.getPlayer().start();
                        }

                    }

                } else {
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
                MessageLogger.PrintMsg("tejas >>> onPrepared");
            }

            @Override
            public void onBufferingUpdate(GiraffePlayer giraffePlayer, int percent) {
//               MessageLogger.PrintMsg("tejas >>> onBufferingUpdate");
            }

            @Override
            public boolean onInfo(GiraffePlayer giraffePlayer, int what, int extra) {
                MessageLogger.PrintMsg("tejas >>> onInfo");
                return false;
            }

            @Override
            public void onCompletion(GiraffePlayer giraffePlayer) {
                MessageLogger.PrintMsg("tejas >>> onCompletion");

            }

            @Override
            public void onSeekComplete(GiraffePlayer giraffePlayer) {
                MessageLogger.PrintMsg("tejas >>> onSeekComplete");
            }

            @Override
            public boolean onError(GiraffePlayer giraffePlayer, int what, int extra) {
                MessageLogger.PrintMsg("tejas >>> onError");
                if (a == 0) {
                    a = giraffePlayer.getCurrentPosition();

                }

                long milliseconds = giraffePlayer.getCurrentPosition();
                long minutes = (milliseconds / 1000) / 60;
                long seconds = (milliseconds / 1000) % 60;


                long milliseconds1 = giraffePlayer.getDuration();
                long minutes1 = (milliseconds1 / 1000) / 60;
                long seconds1 = (milliseconds1 / 1000) % 60;

                if (minutes != minutes1 && seconds != seconds1) {
//                    globalclass.showcenterCustomToast(mActivity,"Error! Unable to play this video. Please Check your internet connection or try after sometime.",Toast.LENGTH_SHORT);
                    internetErrorSnackbar.show();
                }

                MessageLogger.PrintMsg("tejas >>> CurrentPosition  " + a + " duration : " + giraffePlayer.getDuration());
                MessageLogger.PrintMsg("tejas >>> CurrentPositionMin  " + minutes + " CurrentPositionSec : " + seconds);
                MessageLogger.PrintMsg("tejas >>> durationMin  " + minutes1 + " durationSec : " + seconds1);


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
                MessageLogger.PrintMsg("tejas >>> onPause");
            }

            @Override
            public void onRelease(GiraffePlayer giraffePlayer) {
                MessageLogger.PrintMsg("tejas >>> onRelease");


            }

            @Override
            public void onStart(GiraffePlayer giraffePlayer) {

                if (internetErrorSnackbar != null && internetErrorSnackbar.isShown()) {
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
                    MessageLogger.PrintMsg("tejas >>> seek Time : " + a);
                    /*if (a > 0){
                        if (cd.isConnectingToInternet()){
                            giraffePlayer.seekTo(a);
                            a = 0;
                        }
                    }*/
                    videoView.setVisibility(View.VISIBLE);
                    videoListAdapter.notifyDataSetChanged();
                }
                MessageLogger.PrintMsg("tejas >>> onStart");
            }

            @Override
            public void onTargetStateChange(int oldState, int newState) {
                MessageLogger.PrintMsg("tejas >>> onTargetStateChange");
            }

            @Override
            public void onCurrentStateChange(int oldState, int newState) {

                if (oldState == GiraffePlayer.STATE_ERROR && newState == GiraffePlayer.STATE_PLAYBACK_COMPLETED) {

                    long milliseconds = videoView.getPlayer().getCurrentPosition();
                    long minutes = (milliseconds / 1000) / 60;
                    long seconds = (milliseconds / 1000) % 60;


                    long milliseconds1 = videoView.getPlayer().getDuration();
                    long minutes1 = (milliseconds1 / 1000) / 60;
                    long seconds1 = (milliseconds1 / 1000) % 60;

                    if (minutes == minutes1 && seconds == seconds1) {

                        if (VideosArylist != null && VideosArylist.size() > 0) {
                            for (int i = 0; i < VideosArylist.size(); i++) {
                                VideosArylist.get(i).setVideoPlaying(false);
                                VideosArylist.get(i).setVideoPaused(false);
                            }
                            videoListAdapter.notifyDataSetChanged();
                        }
                        a = 0;
                        videoView.setVisibility(View.GONE);
                    } else {
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


                } else if ((oldState == GiraffePlayer.STATE_PLAYING && newState == GiraffePlayer.STATE_PLAYBACK_COMPLETED) || (oldState == GiraffePlayer.DISPLAY_NORMAL && newState == GiraffePlayer.STATE_RELEASE)) {
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
                        if (b > 0) {
                            videoView.getPlayer().seekTo(459495495);
                            b = 0;
                        }
                        videoListAdapter.notifyDataSetChanged();
                    }
                } else if (oldState == GiraffePlayer.STATE_IDLE && (newState == GiraffePlayer.STATE_LAZYLOADING || newState == GiraffePlayer.STATE_PREPARING)) {
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
                } else if (oldState == GiraffePlayer.STATE_PLAYBACK_COMPLETED && newState == GiraffePlayer.STATE_PLAYING) {
                    if (VideosArylist != null && VideosArylist.size() > 0) {
                        for (int i = 0; i < VideosArylist.size(); i++) {

                            if (VideosArylist.get(i).getId().equalsIgnoreCase(Video.getId())) {
                                VideosArylist.get(i).setVideoPlaying(true);
                                VideosArylist.get(i).setVideoPaused(false);
                            } else {
                                VideosArylist.get(i).setVideoPlaying(false);
                            }
                        }
                        if (a > 0) {
                            videoView.getPlayer().seekTo(a);
                        }
                        videoListAdapter.notifyDataSetChanged();
                    }
                    videoView.setVisibility(View.VISIBLE);
                } else {
                    videoView.setVisibility(View.VISIBLE);
                }
                MessageLogger.PrintMsg("tejas >>> onCurrentStateChange " + oldState + "new State : " + newState);
            }

            @Override
            public void onDisplayModelChange(int oldModel, int newModel) {
                MessageLogger.PrintMsg("tejas >>> onDisplayModelChange");
            }

            @Override
            public void onPreparing(GiraffePlayer giraffePlayer) {
                MessageLogger.PrintMsg("tejas >>> onPreparing");
            }

            @Override
            public void onTimedText(GiraffePlayer giraffePlayer, IjkTimedText text) {
                MessageLogger.PrintMsg("tejas >>> onTimedText");
            }

            @Override
            public void onLazyLoadProgress(GiraffePlayer giraffePlayer, int progress) {
                MessageLogger.PrintMsg("tejas >>> onLazyLoadProgress");
            }

            @Override
            public void onLazyLoadError(GiraffePlayer giraffePlayer, String message) {
                MessageLogger.PrintMsg("tejas >>> onLazyLoadError");
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

        if (videoView.getVideoInfo().getUri() != null) {
            MessageLogger.PrintMsg("tejas >>> currentposition : " + videoView.getPlayer().getCurrentPosition());
            b = videoView.getPlayer().getCurrentPosition();
        }
        super.onPause();

    }

    @Override
    protected void onResume() {

        if (videoView.getVideoInfo().getUri() != null && b > 0) {
            MessageLogger.PrintMsg("tejas >>> seek to : " + b);
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