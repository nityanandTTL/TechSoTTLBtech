package com.dhb.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import com.dhb.R;
import com.dhb.adapter.EditTestExpandListAdapter;
import com.dhb.dao.DhbDao;
import com.dhb.dao.models.BrandMasterDao;
import com.dhb.dao.models.TestRateMasterDao;
import com.dhb.delegate.EditTestExpandListAdapterCheckboxDelegate;
import com.dhb.models.data.BeneficiarySampleTypeDetailsModel;
import com.dhb.models.data.BrandMasterModel;
import com.dhb.models.data.ChildTestsModel;
import com.dhb.models.data.TestRateMasterModel;
import com.dhb.models.data.TestTypeWiseTestRateMasterModelsList;
import com.dhb.uiutils.AbstractActivity;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.BundleConstants;

import java.util.ArrayList;


public class EditTestListActivity extends AbstractActivity{
    private Spinner sp_tests;
    private DhbDao dhbDao;
    private Activity activity;
    private ExpandableListView expandList;
    private EditTestExpandListAdapter expAdapter;
    private int brandMasterId;
    private ArrayList<TestRateMasterModel> selectedTestsList;
    private AppPreferenceManager appPreferenceManager;
    private Button btnSave;
    private int totalAmount;
    private ArrayList<BeneficiarySampleTypeDetailsModel> sampleTypesArr;
    private boolean isOfferSelected = false;
    private long selectedTestsCost;
    private long selectedTestsTotalCost;
    private ArrayList<TestRateMasterModel> restOfTestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_test_list);
        activity = this;
        dhbDao = new DhbDao(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        selectedTestsList = new ArrayList<>();
        restOfTestList = new ArrayList<>();
        if(getIntent().getExtras()!=null){
            selectedTestsList = getIntent().getExtras().getParcelableArrayList(BundleConstants.SELECTED_TESTS_LIST);
            restOfTestList = getIntent().getExtras().getParcelableArrayList(BundleConstants.REST_BEN_TESTS_LIST);
        }
        totalAmount = 0;
        sampleTypesArr = new ArrayList<>();
        initUI();
        initData();
        initListener();
    }

    private void initListener() {
        sp_tests.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BrandMasterModel brandMasterModel = (BrandMasterModel) parent.getItemAtPosition(position);
                Object item = parent.getItemAtPosition(position);
                if (item != null) {
                    Logger.error("click");
                    Logger.error("ID : " + brandMasterModel.getBrandId());
                    brandMasterId=brandMasterModel.getBrandId();
                    TestRateMasterDao testRateMasterDao = new TestRateMasterDao(dhbDao.getDb());
                    ArrayList<String> testTypesArr = new ArrayList<String>() ;
                    ArrayList<TestTypeWiseTestRateMasterModelsList> testRateMasterModels = new ArrayList<TestTypeWiseTestRateMasterModelsList>();
                    testTypesArr = testRateMasterDao.getAllTestTypesFromBrandId(brandMasterModel.getBrandId()+"");
                    for (String testType :
                            testTypesArr) {
                        ArrayList<TestRateMasterModel> testTypeWiseTestRateMasterModels = testRateMasterDao.getModelsFromTestType(testType);
                        TestTypeWiseTestRateMasterModelsList testTypeWiseTestRateMasterModelsList =  new TestTypeWiseTestRateMasterModelsList() ;
                        testTypeWiseTestRateMasterModelsList.setTestType(testType);
                        testTypeWiseTestRateMasterModelsList.setTestRateMasterModels(testTypeWiseTestRateMasterModels);
                        testRateMasterModels.add(testTypeWiseTestRateMasterModelsList);
                    }
                    expAdapter = new EditTestExpandListAdapter(activity, testRateMasterModels, selectedTestsList, new EditTestExpandListAdapterCheckboxDelegate() {
                        @Override
                        public void onCheckChange(ArrayList<TestRateMasterModel> selectedTests) {
                            Logger.error("check changed");
                            selectedTestsList = selectedTests;
                        }
                    });
                    expandList.setAdapter(expAdapter);
                } else {
                    Logger.error("item null");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Logger.error("nothing is selected");
            }
        });
        expandList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Logger.error("child click");
                return false;
            }
        });
        expandList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Logger.error("group collapse");
            }
        });
        expandList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Logger.error("group expand");
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trimSelectedTestsListforDuplicates(selectedTestsList);
                selectedTestsCost = calculateCost(selectedTestsList);
                long restBenTestCost = calculateCost(restOfTestList);
                selectedTestsTotalCost = restBenTestCost+selectedTestsCost;
                Intent intentFinish = new Intent();
                intentFinish.putExtra(BundleConstants.SELECTED_TESTS_LIST, selectedTestsList);
                intentFinish.putExtra(BundleConstants.SELECTED_TESTS_COST,selectedTestsCost);
                intentFinish.putExtra(BundleConstants.SELECTED_TESTS_TOTAL_COST,selectedTestsTotalCost);
                setResult(BundleConstants.EDIT_TESTS_FINISH,intentFinish);
                finish();
            }
        });
    }

    private long calculateCost(ArrayList<TestRateMasterModel> selTests) {
        //TODO  calculate the total price
        long totalCost = 0;
        for (TestRateMasterModel tt : selTests){
            if (tt.getRate() != 0){
                totalCost = totalCost + tt.getRate();
            }
        }
        return totalCost;
    }

    private boolean trimSelectedTestsListforDuplicates(ArrayList<TestRateMasterModel> selTests){
        boolean wasTrimmed = false;
        int k = 0;
        if(selTests.size()>0){
            for(int i = 0;i<selTests.size();i++){
                String duplicateString;
                duplicateString = selTests.get(i).getTestCode();
                for(int j=0;j<selTests.size();j++){
                    if(j!=i){
                        if(duplicateString.equalsIgnoreCase(selTests.get(j).getTestCode())){
                            selectedTestsList.remove(selTests.get(j));
                            k++;
                        }
                    }
                }
            }
            for(int i =0;i<selTests.size();i++){
                TestRateMasterModel tt = new TestRateMasterModel();
                tt = selTests.get(i);
                if(tt!=null){
                    String ttCode = tt.getTestCode();
                    for(int j=0;j<selTests.size();j++){
                        if(j!=i){
                            TestRateMasterModel tt2 = new TestRateMasterModel();
                            tt2 = selTests.get(j);
                            if(tt2!=null && !tt2.getTestType().equalsIgnoreCase("TEST") && tt2.getChldtests().size()>0){
                                for (ChildTestsModel c:
                                     tt2.getChldtests()) {
                                    if(c.getChildTestCode().equalsIgnoreCase(ttCode)){
                                        selectedTestsList.remove(tt);
                                        k++;
                                    }
                                }
                                if(tt.checkIfChildsMatch(tt2)){
                                    selectedTestsList.remove(tt2);
                                }
                            }
                        }
                    }
                }
            }
        }
        if(k!=0)
        {
            wasTrimmed=true;
        }
        return wasTrimmed;
    }

    private void initData() {
        BrandMasterDao brandMasterDao = new BrandMasterDao(dhbDao.getDb());
        final ArrayList<BrandMasterModel> brandMasterModels = brandMasterDao.getAllModels();
        ArrayAdapter<BrandMasterModel> adapter = new ArrayAdapter<BrandMasterModel>(this, android.R.layout.simple_spinner_item, brandMasterModels);
        sp_tests.setAdapter(adapter);
    }

    @Override
    public void initUI() {
        super.initUI();
        sp_tests = (Spinner) findViewById(R.id.sp_tests);
        expandList = (ExpandableListView) findViewById(R.id.exp_list);
        btnSave = (Button) findViewById(R.id.btn_save);
    }
}
