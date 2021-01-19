package com.thyrocare.btechapp.NewScreenDesigns.Fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.thyrocare.btechapp.NewScreenDesigns.Adapters.ExpandableListAdapter_FAQ;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.FAQandANSArray;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.dao.utils.ConnectionDetector;
import com.thyrocare.btechapp.utils.app.Global;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.CHECK_INTERNET_CONN;



/**
 * A simple {@link Fragment} subclass.
 */
public class FAQ_Fragment extends Fragment {

    public static final String TAG_FRAGMENT = "FAQ_FRAGMENT";
    Activity activity;
    Global globalClass;
    ExpandableListView expandable_list_faq;
    ExpandableListAdapter_FAQ expandableListAdapter;
    ConnectionDetector cd;

    public FAQ_Fragment() {
        // Required empty public constructor
    }

    public static FAQ_Fragment newInstance() {
        FAQ_Fragment fragment = new FAQ_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_faq, container, false);

        activity = getActivity();
        cd = new ConnectionDetector(activity);
        activity.setTitle("FAQ");
        globalClass = new Global(getContext());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }

    private void initViews(View viewMain) {
        expandable_list_faq = (ExpandableListView) viewMain.findViewById(R.id.faq_list_expandable);

        if (cd.isConnectingToInternet()) {
            getFAQData();
        } else {
            globalClass.showCustomToast(activity,CHECK_INTERNET_CONN, Toast.LENGTH_SHORT);
        }
    }

    private void getFAQData() {
        globalClass.showProgressDialog(activity, "Please wait..", false);
        GetAPIInterface getAPIInteface = RetroFit_APIClient.getInstance().getClient(getActivity(), EncryptionUtils.Dcrp_Hex(activity.getString(R.string.B2C_API_VERSION))).create(GetAPIInterface.class);
        Call<FAQandANSArray> faQandANSArrayCall = getAPIInteface.getFAQ();

        faQandANSArrayCall.enqueue(new Callback<FAQandANSArray>() {
            @Override
            public void onResponse(Call<FAQandANSArray> call, Response<FAQandANSArray> response) {

                try {
                    if (response.body().getRESPONSE().equalsIgnoreCase("SUCCESS")) {
                        if (!response.body().getPromoterFaqList().isEmpty()) {
                            globalClass.hideProgressDialog(activity);
                            expandable_list_faq.setVisibility(View.VISIBLE);
                            expandableListAdapter = new ExpandableListAdapter_FAQ(response.body().getPromoterFaqList(), activity);
                            expandable_list_faq.setAdapter(expandableListAdapter);
                        } else {
                            expandable_list_faq.setVisibility(View.GONE);
                            globalClass.hideProgressDialog(activity);
                            Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        globalClass.hideProgressDialog(activity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<FAQandANSArray> call, Throwable t) {
                globalClass.hideProgressDialog(activity);
            }
        });


    }
}
