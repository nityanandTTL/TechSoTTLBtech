package com.thyrocare.activity;

/**
 * Created by E4904 on 4/22/2017.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.thyrocare.R;
import com.thyrocare.models.api.request.ChatRequestModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppPreferenceManager;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;
import static com.thyrocare.utils.api.NetworkUtils.isNetworkAvailable;

public class Chat extends AppCompatActivity {

    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    private static final String TAG_CHAT = "TAG_CHAT";
    Firebase reference1, reference2;
    String messageText;
    private TextView txtHeaderText;
    //  Users usersobj;
    FirebaseMessagingService firebaseMessagingService;
    private AppPreferenceManager appPreferenceManager;
    private boolean isFirstVisited=false;
    private Toolbar tbOBA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        isFirstVisited=false;
        appPreferenceManager = new AppPreferenceManager(this);

        layout = (LinearLayout) findViewById(R.id.layout1);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        tbOBA = (Toolbar) findViewById(R.id.toolbar);
        txtHeaderText = (TextView) tbOBA.findViewById(R.id.txt_header_text);
        Firebase.setAndroidContext(this);

      /* reference1 = new Firebase("https://finalchat-df79b.firebaseio.com/messages/" + appPreferenceManager.getLoginResponseModel().getUserName() + "_" + appPreferenceManager.getLoginResponseModel().getUserName());
      reference2 = new Firebase("https://finalchat-df79b.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);
*/          txtHeaderText.setText(""+UserDetails.chatWith);

            appPreferenceManager.setUserDetailUserName(UserDetails.username);
            appPreferenceManager.setUserDetailChatWith(UserDetails.chatWith);
        Log.e(TAG_CHAT, "onCreate: " + "Dhinchak pooja ");
        reference1 = new Firebase("https://finalchat-df79b.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        Log.e(TAG_CHAT, "onCreate1: " +UserDetails.username);
        reference2 = new Firebase("https://finalchat-df79b.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);
        Log.e(TAG_CHAT, "onCreate2: " +UserDetails.chatWith);
        /* if (getIntent().getExtras() != null) {
            // Call your NotificationActivity here..
            Intent intent = new Intent(Chat.this, FireMsgService.class);
            startActivity(intent);
        }*/
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if (userName.equals(UserDetails.username)) {
                    addMessageBox( message, 2);
                   // addMessageBox("You:-\n" + message, 1);
                } else {
                   addMessageBox( message, 1);
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void chat() {
        messageText = messageArea.getText().toString();
        if (!messageText.equals("")) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("message", messageText);
            map.put("user", UserDetails.username);
            reference1.push().setValue(map);
            reference2.push().setValue(map);
            messageArea.setText("");
            // usersobj.count.setVisibility(View.VISIBLE);
            String tkn = FirebaseInstanceId.getInstance().getToken();
            Log.d("App", "Token [" + tkn + "]");
            ChatRequestModel chatRequestModel = new ChatRequestModel();
            chatRequestModel.setFirstName(appPreferenceManager.getLoginResponseModel().getUserName());
            chatRequestModel.setLastName(UserDetails.chatWith);
            chatRequestModel.setPhone(messageText);
            chatRequestModel.setEmail(new Date().toString());
            chatRequestModel.setCode("1");
            chatRequestModel.setPassword("Null");
            Logger.error("model "+chatRequestModel.toString());
            AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(Chat.this);
            Logger.error("test1 ");
            ApiCallAsyncTask setchatDetailApiAsyncTask = asyncTaskForRequest.ChatPostapi(chatRequestModel);

            setchatDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new setApplyLeaveDetailsApiAsyncTaskDelegateResult());
            if (isNetworkAvailable(Chat.this)) {
                setchatDetailApiAsyncTask.execute(setchatDetailApiAsyncTask);
            } else {
                Toast.makeText(Chat.this, R.string.internet_connetion_error, LENGTH_SHORT).show();
            }


            isFirstVisited=true;


        }
    }


    public void addMessageBox(String message, int type) {
        TextView textView = new TextView(Chat.this);

        textView.setText(message);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 30, 0, 5);
        textView.setLayoutParams(lp);


        TextView textView1 = new TextView(this);
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentDateTimeString = sdf.format(d);
        textView1.setText(currentDateTimeString);
        textView1.setLayoutParams(lp);

        if (type == 1) {

            textView.setBackgroundResource(R.drawable.bubble_left_gray);
            textView1.setBackgroundResource(R.drawable.bubble_left_gray);

        } else {
            textView.setBackgroundResource(R.drawable.bubble_right_green);
            textView1.setBackgroundResource(R.drawable.bubble_right_green);
            lp.gravity = Gravity.RIGHT;
            textView.setLayoutParams(lp);
            textView1.setLayoutParams(lp);
        }

        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);


    }


    private class setApplyLeaveDetailsApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {

        }

        @Override
        public void onApiCancelled() {

        }
    }
}