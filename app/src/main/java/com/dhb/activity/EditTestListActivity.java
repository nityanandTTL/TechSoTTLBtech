package com.dhb.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import com.dhb.R;
import com.dhb.adapter.EditTestExpandListAdapter;
import com.dhb.dao.DhbDao;
import com.dhb.dao.models.BrandMasterDao;
import com.dhb.dao.models.TestRateMasterDao;
import com.dhb.delegate.EditTestExpandListAdapterCheckboxDelegate;
import com.dhb.models.data.BrandMasterModel;
import com.dhb.models.data.TestRateMasterModel;
import com.dhb.models.data.TestTypeWiseTestRateMasterModelsList;
import com.dhb.uiutils.AbstractActivity;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.BundleConstants;

import java.util.ArrayList;


public class EditTestListActivity extends AbstractActivity implements EditTestExpandListAdapterCheckboxDelegate {
    Spinner sp_tests;
    DhbDao dhbDao;
    Activity activity;
    ExpandableListView expandList;
    EditTestExpandListAdapter expAdapter;
    int brandMasterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_test_list);
        activity = this;
        initUI();

        dhbDao = new DhbDao(activity);
        BrandMasterDao brandMasterDao = new BrandMasterDao(dhbDao.getDb());
        final ArrayList<BrandMasterModel> brandMasterModels = brandMasterDao.getAllModels();
        ArrayAdapter<BrandMasterModel> adapter = new ArrayAdapter<BrandMasterModel>(this, android.R.layout.simple_spinner_item, brandMasterModels);
        sp_tests.setAdapter(adapter);

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
                    expAdapter = new EditTestExpandListAdapter(activity, testRateMasterModels, new EditTestExpandListAdapterCheckboxDelegate() {
                        @Override
                        public void onCheckChange(ArrayList<TestRateMasterModel> selectedTests) {
                            Intent intentFinish = new Intent();
                            intentFinish.putExtra(BundleConstants.TESTS_LIST,selectedTests);
                            setResult(BundleConstants.EDIT_TESTS_FINISH,intentFinish);
                            finish();
                            Logger.error("check changed ");
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
    }

    @Override
    public void initUI() {
        super.initUI();
        sp_tests = (Spinner) findViewById(R.id.sp_tests);
        expandList = (ExpandableListView) findViewById(R.id.exp_list);
    }

    @Override
    public void onCheckChange(ArrayList<TestRateMasterModel> selectedTests) {
        Logger.error(selectedTests.get(0).getBrandName());
    }
}
