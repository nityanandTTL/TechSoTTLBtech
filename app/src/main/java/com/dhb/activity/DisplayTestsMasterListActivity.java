package com.dhb.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import com.dhb.R;
import com.dhb.adapter.ExpandableTestMasterListDisplayAdapter;
import com.dhb.dao.DhbDao;
import com.dhb.dao.models.BeneficiaryDetailsDao;
import com.dhb.dao.models.BrandMasterDao;
import com.dhb.dao.models.OrderDetailsDao;
import com.dhb.dao.models.TestRateMasterDao;
import com.dhb.delegate.EditTestExpandListAdapterCheckboxDelegate;
import com.dhb.models.data.BeneficiaryDetailsModel;
import com.dhb.models.data.BeneficiarySampleTypeDetailsModel;
import com.dhb.models.data.BeneficiaryTestDetailsModel;
import com.dhb.models.data.BrandMasterModel;
import com.dhb.models.data.ChildTestsModel;
import com.dhb.models.data.OrderDetailsModel;
import com.dhb.models.data.TestClinicalHistoryModel;
import com.dhb.models.data.TestRateMasterModel;
import com.dhb.models.data.TestSampleTypeModel;
import com.dhb.models.data.TestTypeWiseTestRateMasterModelsList;
import com.dhb.uiutils.AbstractActivity;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.BundleConstants;
import com.dhb.utils.app.InputUtils;

import java.util.ArrayList;


public class DisplayTestsMasterListActivity extends AbstractActivity{
    private Spinner spBrandMasters;
    private DhbDao dhbDao;
    private Activity activity;
    private ExpandableListView expandList;
    private ExpandableTestMasterListDisplayAdapter expAdapter;
    private Button btnSave;
    private OrderDetailsModel orderDetailsModel;
    private BeneficiaryDetailsModel benDetailsModel;
    private BeneficiaryDetailsDao bdd;
    private boolean isEdit = false;
    private BrandMasterModel brandMasterModel;
    private ArrayList<BeneficiaryTestDetailsModel> selectedTestDetailsArr = new ArrayList<>();
    private ArrayList<TestRateMasterModel> selectedTestsList = new ArrayList<>();

    @Override
    public void onBackPressed() {
        btnSave.performClick();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_edit_test_list);
        activity = this;
        dhbDao = new DhbDao(activity);
        bdd = new BeneficiaryDetailsDao(dhbDao.getDb());
        appPreferenceManager = new AppPreferenceManager(activity);
        orderDetailsModel = new OrderDetailsModel();
        if(getIntent().getExtras()!=null){
            selectedTestDetailsArr = getIntent().getExtras().getParcelableArrayList(BundleConstants.SELECTED_TESTS_LIST);
            orderDetailsModel = getIntent().getExtras().getParcelable(BundleConstants.ORDER_DETAILS_MODEL);
            benDetailsModel=getIntent().getExtras().getParcelable(BundleConstants.BENEFICIARY_DETAILS_MODEL);
            isEdit = getIntent().getExtras().getBoolean(BundleConstants.IS_TEST_EDIT,true);
        }
        initUI();
        initData();
        initListener();
    }

    private void initListener() {
        spBrandMasters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                brandMasterModel = (BrandMasterModel) parent.getItemAtPosition(position);
                Object item = parent.getItemAtPosition(position);
                new PopulateTestDataAsyncTask(item).execute();
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
                selectedTestDetailsArr = new ArrayList<BeneficiaryTestDetailsModel>();
                benDetailsModel.setProjId("");
                for (TestRateMasterModel trmm:
                        selectedTestsList) {
                    BeneficiaryTestDetailsModel btdm = new BeneficiaryTestDetailsModel();
                    btdm.setFasting(trmm.getFasting());
                    btdm.setChldtests(trmm.getChldtests()!=null?trmm.getChldtests():new ArrayList<ChildTestsModel>());
                    btdm.setTests(trmm.getTestCode());
                    btdm.setTestType(trmm.getTestType());
                    btdm.setProjId("");
                    btdm.setSampleType(trmm.getSampltype()!=null?trmm.getSampltype():new ArrayList<TestSampleTypeModel>());
                    btdm.setTstClinicalHistory(trmm.getTstClinicalHistory()!=null?trmm.getTstClinicalHistory():new ArrayList<TestClinicalHistoryModel>());
                    if(!InputUtils.isNull(trmm.getTestType())&&trmm.getTestType().equalsIgnoreCase("offer")){
                        btdm.setProjId(trmm.getTestCode());
                        btdm.setTests(trmm.getDescription());
                        benDetailsModel.setProjId(trmm.getTestCode());
                    }
                    selectedTestDetailsArr.add(btdm);
                }
                benDetailsModel.setTestSampleType(selectedTestDetailsArr);
                orderDetailsModel.setTestEdit(isEdit);
                orderDetailsModel.setBrandId(brandMasterModel.getBrandId());
                new OrderDetailsDao(dhbDao.getDb()).insertOrUpdate(orderDetailsModel);

                boolean isFasting = false;
                String testsCode = "";
                if(selectedTestsList!=null) {
                    for (TestRateMasterModel testRateMasterModel :
                            selectedTestsList) {
                        if (InputUtils.isNull(testsCode)) {
                            if(!InputUtils.isNull(testRateMasterModel.getTestType())&&testRateMasterModel.getTestType().equals("OFFER")) {
                                testsCode = testRateMasterModel.getDescription();
                                benDetailsModel.setProjId(testRateMasterModel.getTestCode());
                            }
                            else{
                                testsCode = testRateMasterModel.getTestCode();
                            }
                        } else {
                            if(!InputUtils.isNull(testRateMasterModel.getTestType())&&testRateMasterModel.getTestType().equals("OFFER")) {
                                testsCode = testsCode + "," + testRateMasterModel.getDescription();
                                benDetailsModel.setProjId(testRateMasterModel.getTestCode());
                            }
                            else{
                                testsCode = testsCode + "," + testRateMasterModel.getTestCode();

                            }
                        }
                    }
                    if(InputUtils.isNull(benDetailsModel.getProjId())){
                        benDetailsModel.setProjId("");
                    }
                    ArrayList<BeneficiarySampleTypeDetailsModel> samples = new ArrayList<>();
                    for (TestRateMasterModel trmm :
                            selectedTestsList) {
                        for (TestSampleTypeModel tstm :
                                trmm.getSampltype()) {
                            BeneficiarySampleTypeDetailsModel bstdm = new BeneficiarySampleTypeDetailsModel();
                            bstdm.setBenId(benDetailsModel.getBenId());
                            bstdm.setSampleType(tstm.getSampleType());
                            bstdm.setId(tstm.getId());
                            if(!samples.contains(bstdm)){
                                samples.add(bstdm);
                            }
                        }
                    }
                    benDetailsModel.setSampleType(samples);
                    for (TestRateMasterModel trmm:
                            selectedTestsList) {
                        if(!trmm.getFasting().toLowerCase().contains("non")){
                            isFasting = true;
                            break;
                        }
                    }
                }
                benDetailsModel.setTestsCode(testsCode);
                benDetailsModel.setTests(testsCode);
                if(isFasting) {
                    benDetailsModel.setFasting("Fasting");
                }
                else{
                    benDetailsModel.setFasting("Non-Fasting");
                }
                bdd.insertOrUpdate(benDetailsModel);
                Intent intentFinish = new Intent();
//                intentFinish.putExtra(BundleConstants.BRAND_ID,brandMasterModel.getBrandId());
                setResult(BundleConstants.ADD_TESTS_FINISH,intentFinish);
                finish();
            }
        });
    }

    private void initData() {
        selectedTestsList = new ArrayList<>();
        for (BeneficiaryTestDetailsModel btdm:
                selectedTestDetailsArr) {
            TestRateMasterModel trmm = new TestRateMasterModel();
            trmm.setFasting(btdm.getFasting());
            trmm.setChldtests(btdm.getChldtests()!=null?btdm.getChldtests():new ArrayList<ChildTestsModel>());
            trmm.setTestCode(btdm.getTests());
            trmm.setTestType(btdm.getTestType());
            trmm.setSampltype(btdm.getSampleType()!=null?btdm.getSampleType():new ArrayList<TestSampleTypeModel>());
            trmm.setTstClinicalHistory(btdm.getTstClinicalHistory()!=null?btdm.getTstClinicalHistory():new ArrayList<TestClinicalHistoryModel>());
            if(!InputUtils.isNull(btdm.getProjId())){
                trmm.setTestCode(btdm.getProjId());
                trmm.setDescription(btdm.getTests());
                trmm.setTestType("OFFER");
            }
            selectedTestsList.add(trmm);
        }

        BrandMasterDao brandMasterDao = new BrandMasterDao(dhbDao.getDb());
        final ArrayList<BrandMasterModel> brandMasterModels = brandMasterDao.getAllModels();
        ArrayAdapter<BrandMasterModel> adapter = new ArrayAdapter<BrandMasterModel>(this, android.R.layout.simple_spinner_item, brandMasterModels);
        spBrandMasters.setAdapter(adapter);
        if(orderDetailsModel!=null && !orderDetailsModel.isAddBen()){
            for(int i=0;i<brandMasterModels.size();i++){
                if(orderDetailsModel.getBrandId()==brandMasterModels.get(i).getBrandId()){
                    spBrandMasters.setSelection(i);
                    break;
                }
            }
        }
        if(isEdit){
            spBrandMasters.setEnabled(false);
        }
        else{
            spBrandMasters.setEnabled(true);
        }
    }

    @Override
    public void initUI() {
        super.initUI();
        spBrandMasters = (Spinner) findViewById(R.id.sp_tests);
        expandList = (ExpandableListView) findViewById(R.id.exp_list);
        btnSave = (Button) findViewById(R.id.btn_save);
    }

    private class PopulateTestDataAsyncTask extends AsyncTask<Void,Void,Void>{
        private Object item;
        public PopulateTestDataAsyncTask(Object item) {
            this.item = item;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(activity,"Please wait while we load the Tests List...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (item != null) {
                Logger.error("click");
                Logger.error("Brand ID : " + brandMasterModel.getBrandId());
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
                expAdapter = new ExpandableTestMasterListDisplayAdapter(activity, testRateMasterModels, selectedTestsList, new EditTestExpandListAdapterCheckboxDelegate() {
                    @Override
                    public void onCheckChange(ArrayList<TestRateMasterModel> selectedTests) {
                        Logger.error("check changed");
                        selectedTestsList = selectedTests;
                        expAdapter.notifyDataSetChanged();
                    }
                });
            } else {
                Logger.error("item null");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            expandList.setAdapter(expAdapter);
            closeProgressDialog();
        }
    }
}
