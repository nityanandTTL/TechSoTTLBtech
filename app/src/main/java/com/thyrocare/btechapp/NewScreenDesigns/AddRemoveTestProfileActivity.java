package com.thyrocare.btechapp.NewScreenDesigns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.DisplayAllTestApdter;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.dao.utils.ConnectionDetector;
import com.thyrocare.btechapp.models.data.BrandTestMasterModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;
import com.thyrocare.btechapp.models.data.TestRateMasterModel;
import com.thyrocare.btechapp.models.data.TestTypeWiseTestRateMasterModelsList;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.GPSTracker;
import com.thyrocare.btechapp.utils.app.Global;

import java.util.ArrayList;
import java.util.HashSet;

import retrofit2.Call;
import retrofit2.Callback;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.CheckInternetConnectionMsg;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;

public class AddRemoveTestProfileActivity extends AppCompatActivity {

    private Activity mActivity;
    private Global globalclass;
    private ConnectionDetector cd;
    private GPSTracker gpsTracker;
    private AppPreferenceManager appPreferenceManager;
    private SearchManager searchManager;
    private TextView btnClose,tv_Test,tv_Profile,tv_POP,tv_offers,tvTestName,tvAppRate,tv_noDatafound;
    private Button btnSave;
    private OrderVisitDetailsModel orderVisitDetailsModel;
    private boolean FlagADDEditBen = true;

    private boolean isM = false;
    private boolean isRHC = false;
    ArrayList<TestRateMasterModel> selectedTestsList = new ArrayList<>();
    ArrayList<TestRateMasterModel> edit_selectedTestsList = new ArrayList<>();
    private EditText ed_Search;
    private LinearLayout lin_profileCategory,lin_selectedTest;
    private RecyclerView recycle_TestList;
    private DisplayAllTestApdter displayAllTestApdter;
    private BrandTestMasterModel brandTestMasterModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove_test_profile);

        initContext();
        iniView();
        initListener();
        initData();
    }

    private void initContext() {
        mActivity = AddRemoveTestProfileActivity.this;
        globalclass = new Global(mActivity);
        cd = new ConnectionDetector(mActivity);
        gpsTracker = new GPSTracker(mActivity);
        appPreferenceManager = new AppPreferenceManager(mActivity);
        orderVisitDetailsModel = getIntent().getExtras().getParcelable(BundleConstants.VISIT_ORDER_DETAILS_MODEL);
        selectedTestsList = getIntent().getParcelableArrayListExtra(BundleConstants.ADD_BEN_SELECTED_TESTLIST);
        edit_selectedTestsList = getIntent().getParcelableArrayListExtra(BundleConstants.EDIT_BEN_SELECTED_TESTLIST);
        FlagADDEditBen = getIntent().getBooleanExtra("IsAddBen",true);
        if (!FlagADDEditBen){
            if (orderVisitDetailsModel == null || edit_selectedTestsList == null || edit_selectedTestsList.size() == 0 ){
                finish();
            }
        }else{
            if (orderVisitDetailsModel == null ){
                finish();
            }
            if (selectedTestsList == null){
                selectedTestsList = new ArrayList<>();
            }
        }

    }

    private void iniView() {
        searchManager = (SearchManager) mActivity.getSystemService(Context.SEARCH_SERVICE);
        btnClose = (TextView) findViewById(R.id.txtClose);
        btnSave = (Button) findViewById(R.id.btn_save);
        ed_Search = (EditText) findViewById(R.id.ed_Search);
        lin_profileCategory = (LinearLayout) findViewById(R.id.lin_profileCategory);
        tv_Test = (TextView) findViewById(R.id.tv_Test);
        tv_Profile = (TextView) findViewById(R.id.tv_Profile);
        tv_POP = (TextView) findViewById(R.id.tv_POP);
        tv_offers = (TextView) findViewById(R.id.tv_offers);
        tv_noDatafound = (TextView) findViewById(R.id.tv_noDatafound);
        recycle_TestList = (RecyclerView) findViewById(R.id.recycle_TestList);
        lin_selectedTest = (LinearLayout) findViewById(R.id.lin_selectedTest);
        tvTestName = (TextView) findViewById(R.id.tvTestName);
        tvTestName.setMovementMethod(new ScrollingMovementMethod());
        tvAppRate = (TextView) findViewById(R.id.tvAppRate);
    }

    private void initListener() {

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isTestNotAvailble = false;
                if (!FlagADDEditBen){
                    if (edit_selectedTestsList.size() == 0 ){
                        isTestNotAvailble = true;
                    }
                }else{
                    if (selectedTestsList.size() == 0 ){
                        isTestNotAvailble = true;
                    }
                }

                if (isTestNotAvailble){
                    globalclass.showalert_OK("Please select atleast one test to proceed.",mActivity);
                }else{
                    Intent ResultIntent = new Intent();
                    ResultIntent.putParcelableArrayListExtra(BundleConstants.ADD_BEN_SELECTED_TESTLIST, selectedTestsList);
                    ResultIntent.putParcelableArrayListExtra(BundleConstants.EDIT_BEN_SELECTED_TESTLIST, edit_selectedTestsList);
                    setResult(Activity.RESULT_OK,ResultIntent);
                    finish();
                }

            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ed_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*if (expAdapter != null) {
                    expAdapter.filterData(s.toString());
                    if (!StringUtils.isNull(s.toString())) {
                        for (int i = 0; i < expAdapter.getGroupCount(); i++)
                            expandList.expandGroup(i);
                    } else {
                        for (int i = 0; i < expAdapter.getGroupCount(); i++)
                            expandList.collapseGroup(i);
                    }
                }*/

                if (displayAllTestApdter != null) {
                    if (!StringUtils.isNull(s.toString())){
                        displayAllTestApdter.filterData(s.toString());
                       lin_profileCategory.setVisibility(View.GONE);
                    }else{
                        SetSelectedCategory(tv_Test);
                        SetUnSelectedCategory(tv_offers);
                        SetUnSelectedCategory(tv_POP);
                        SetUnSelectedCategory(tv_Profile);
                        InitTestListView(brandTestMasterModel,"Test");
                        lin_profileCategory.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!StringUtils.isNull(s.toString())){
                    if (displayAllTestApdter != null && displayAllTestApdter.getItemCount() > 0){
                        recycle_TestList.setVisibility(View.VISIBLE);
                        tv_noDatafound.setVisibility(View.GONE);
                    }else{
                        recycle_TestList.setVisibility(View.GONE);
                        tv_noDatafound.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        tv_Test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetSelectedCategory(tv_Test);
                SetUnSelectedCategory(tv_offers);
                SetUnSelectedCategory(tv_POP);
                SetUnSelectedCategory(tv_Profile);
                InitTestListView(brandTestMasterModel,"Test");

            }
        });

        tv_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetSelectedCategory(tv_Profile);
                SetUnSelectedCategory(tv_Test);
                SetUnSelectedCategory(tv_POP);
                SetUnSelectedCategory(tv_offers);
                InitTestListView(brandTestMasterModel,"Profile");
            }
        });

        tv_POP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetSelectedCategory(tv_POP);
                SetUnSelectedCategory(tv_Test);
                SetUnSelectedCategory(tv_Profile);
                SetUnSelectedCategory(tv_offers);
                InitTestListView(brandTestMasterModel,"Pop");
            }
        });

        tv_offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetSelectedCategory(tv_offers);
                SetUnSelectedCategory(tv_Test);
                SetUnSelectedCategory(tv_Profile);
                SetUnSelectedCategory(tv_POP);
                InitTestListView(brandTestMasterModel,"Offer");
            }
        });
    }

    private void SetSelectedCategory(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setBackgroundColor(getResources().getColor(R.color.colorOrange));
    }
    private void SetUnSelectedCategory(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.colorOrange));
        textView.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void initData() {

        if (!FlagADDEditBen){
            if ( edit_selectedTestsList != null && edit_selectedTestsList.size() > 0 ){
                DisplaySelectedProducts(edit_selectedTestsList);
            }
        }
        if (cd.isConnectingToInternet()) {
            CallGetTechsoProductsAPI();
        } else {
            globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg);
        }
    }

    private void CallGetTechsoProductsAPI() {

        try {
            GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.DecodeString64(getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
            Call<BrandTestMasterModel> responseCall = apiInterface.CallGetTechsoPRoductsAPI("Bearer " + appPreferenceManager.getLoginResponseModel().getAccess_token());
            globalclass.showProgressDialog(mActivity, "Fetching products. Please wait..");
            responseCall.enqueue(new Callback<BrandTestMasterModel>() {
                @Override
                public void onResponse(Call<BrandTestMasterModel> call, retrofit2.Response<BrandTestMasterModel> response) {
                    globalclass.hideProgressDialog(mActivity);

                    if (response.isSuccessful() && response.body() != null) {
                        brandTestMasterModel = new BrandTestMasterModel();
                        brandTestMasterModel = getBrandTestMaster(response.body());
                        InitTestListView(brandTestMasterModel,"Test");
                        SetSelectedCategory(tv_Test);
                        SetUnSelectedCategory(tv_offers);
                        SetUnSelectedCategory(tv_POP);
                        SetUnSelectedCategory(tv_Profile);
                    } else {
                        globalclass.showcenterCustomToast(mActivity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                    }
                }

                @Override
                public void onFailure(Call<BrandTestMasterModel> call, Throwable t) {
                    globalclass.hideProgressDialog(mActivity);
                    globalclass.showcenterCustomToast(mActivity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BrandTestMasterModel getBrandTestMaster(BrandTestMasterModel brandTestMasterModel) {
        Gson gson = new Gson();

        BrandTestMasterModel brandTestMasterModelFinal = new BrandTestMasterModel();
        ArrayList<TestRateMasterModel> tstratemaster = new ArrayList<>();

        if (brandTestMasterModel != null && brandTestMasterModel.getTstratemaster() != null && brandTestMasterModel.getTstratemaster().size() > 0) {
            for (int i = 0; i < brandTestMasterModel.getTstratemaster().size(); i++) {
                if (brandTestMasterModel.getTstratemaster().get(i).getAccessUserCode() != null && brandTestMasterModel.getTstratemaster().get(i).getAccessUserCode().size() > 0) {
                    for (int j = 0; j < brandTestMasterModel.getTstratemaster().get(i).getAccessUserCode().size(); j++) {
                        try {
                            if (Integer.parseInt(brandTestMasterModel.getTstratemaster().get(i).getAccessUserCode().get(j).getAccessCode()) == orderVisitDetailsModel.getAllOrderdetails().get(0).getUserAccessCode()) {
                                tstratemaster.add(brandTestMasterModel.getTstratemaster().get(i));
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        brandTestMasterModelFinal.setTstratemaster(tstratemaster);
        brandTestMasterModelFinal.setBrandId(brandTestMasterModel.getBrandId());
        brandTestMasterModelFinal.setBrandName(brandTestMasterModel.getBrandName());

        return brandTestMasterModelFinal;
    }

    private void InitTestListView(BrandTestMasterModel result,String Category) {

        ArrayList<TestRateMasterModel> allEvents = new ArrayList<>();
        if (result != null && result.getTstratemaster() != null){
            allEvents = result.getTstratemaster();
        }

        ArrayList<String> testTypesArr = new ArrayList<String>();
        try {
            if (allEvents != null){
                for (int i = 0; i < allEvents.size(); i++) {
                    testTypesArr.add(allEvents.get(i).getTestType());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<String> testTypesNew = new ArrayList<>();
        testTypesNew = removeDuplicates(testTypesArr);

        ArrayList<TestTypeWiseTestRateMasterModelsList> testRateMasterModels = new ArrayList<TestTypeWiseTestRateMasterModelsList>();
        for (String testType :
                testTypesNew) {
            ArrayList<TestRateMasterModel> testTypeWiseTestRateMasterModels = new ArrayList<TestRateMasterModel>();

            for (int k = 0; k < allEvents.size(); k++) {
                if (testType.toString().trim().equalsIgnoreCase(allEvents.get(k).getTestType().toString().trim())) {
                    testTypeWiseTestRateMasterModels.add(allEvents.get(k));
                }
            }
            TestTypeWiseTestRateMasterModelsList testTypeWiseTestRateMasterModelsList = new TestTypeWiseTestRateMasterModelsList();
            testTypeWiseTestRateMasterModelsList.setTestType(testType);
            testTypeWiseTestRateMasterModelsList.setTestRateMasterModels(testTypeWiseTestRateMasterModels);
            testRateMasterModels.add(testTypeWiseTestRateMasterModelsList);
        }


        ArrayList<TestRateMasterModel> CategoryWiseProductList  = new ArrayList<>();
        ArrayList<TestRateMasterModel> AllProductList  = new ArrayList<>();

        if (testRateMasterModels != null && testRateMasterModels.size() > 0){
            if (!StringUtils.isNull(Category)){
                for (int i = 0; i < testRateMasterModels.size(); i++) {
                    if ( Category.equalsIgnoreCase(testRateMasterModels.get(i).getTestType())){
                        CategoryWiseProductList.addAll(testRateMasterModels.get(i).getTestRateMasterModels());
                        break;
                    }
                }
            }
            for (int i = 0; i < testRateMasterModels.size(); i++) {
                AllProductList.addAll(testRateMasterModels.get(i).getTestRateMasterModels());
            }
        }

        if (CategoryWiseProductList != null && CategoryWiseProductList.size() > 0){
            if (FlagADDEditBen) {
                displayAllTestApdter = new DisplayAllTestApdter(mActivity, CategoryWiseProductList,AllProductList,selectedTestsList,isM);
            }else{
                displayAllTestApdter = new DisplayAllTestApdter(mActivity, CategoryWiseProductList,AllProductList,edit_selectedTestsList,isM);
            }
            displayAllTestApdter.setOnItemClickListener(new DisplayAllTestApdter.OnClickListeners() {
                @Override
                public void onCheckChange(ArrayList<TestRateMasterModel> selectedTests) {
                    if (FlagADDEditBen) {
                        selectedTestsList = selectedTests;
                        displayAllTestApdter.notifyDataSetChanged();
                        DisplaySelectedProducts(selectedTestsList);
                    }else{
                        edit_selectedTestsList = selectedTests;
                        displayAllTestApdter.notifyDataSetChanged();
                        DisplaySelectedProducts(edit_selectedTestsList);
                    }


                }
            });

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
            recycle_TestList.setLayoutManager(mLayoutManager);
            recycle_TestList.setAdapter(displayAllTestApdter);
            recycle_TestList.setVisibility(View.VISIBLE);
            tv_noDatafound.setVisibility(View.GONE);
        }else{
            tv_noDatafound.setVisibility(View.VISIBLE);
            recycle_TestList.setVisibility(View.GONE);
        }

    }

    private void DisplaySelectedProducts(ArrayList<TestRateMasterModel> selectedTestsList) {
        int totalRate = 0;
        String selected_products = "";
        if (selectedTestsList != null && selectedTestsList.size() > 0){
            lin_selectedTest.setVisibility(View.VISIBLE);
            for (int i = 0; i <= selectedTestsList.size() - 1; i++) {

                if(!StringUtils.isNull(selectedTestsList.get(i).getTestType())&&(selectedTestsList.get(i).getTestType().equals("TEST")||selectedTestsList.get(i).getTestType().equals("OFFER"))
                        && !StringUtils.isNull(selectedTestsList.get(i).getDescription())){

                    selected_products = selected_products +", "+selectedTestsList.get(i).getDescription();
                }else{
                    selected_products = selected_products +", "+selectedTestsList.get(i).getTestCode();
                }
                totalRate = totalRate + selectedTestsList.get(i).getRate();
            }

        }else{
            lin_selectedTest.setVisibility(View.GONE);
        }
        tvTestName.setText(""+StringUtils.removeFirstCharacter(selected_products));
        tvAppRate.setText(""+totalRate);
    }

    static ArrayList<String> removeDuplicates(ArrayList<String> list) {

        // Store unique items in result.
        ArrayList<String> result = new ArrayList<>();

        // Record encountered Strings in HashSet.
        HashSet<String> set = new HashSet<>();

        // Loop over argument list.
        for (String item : list) {

            // If String is not in set, add it to the list and the set.
            if (!set.contains(item)) {
                result.add(item);
                set.add(item);
            }
        }
        return result;
    }

}
