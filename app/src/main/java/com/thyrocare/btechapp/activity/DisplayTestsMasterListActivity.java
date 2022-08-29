package com.thyrocare.btechapp.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;

import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.adapter.ExpandableTestMasterListDisplayAdapter;
import com.thyrocare.btechapp.dao.DhbDao;
import com.thyrocare.btechapp.dao.models.BeneficiaryDetailsDao;
import com.thyrocare.btechapp.dao.models.BrandMasterDao;
import com.thyrocare.btechapp.dao.models.OrderDetailsDao;
import com.thyrocare.btechapp.dao.models.TestRateMasterDao;
import com.thyrocare.btechapp.delegate.EditTestExpandListAdapterCheckboxDelegate;
import com.thyrocare.btechapp.models.data.BeneficiaryDetailsModel;
import com.thyrocare.btechapp.models.data.BeneficiarySampleTypeDetailsModel;
import com.thyrocare.btechapp.models.data.BeneficiaryTestDetailsModel;
import com.thyrocare.btechapp.models.data.BrandMasterModel;
import com.thyrocare.btechapp.models.data.ChildTestsModel;
import com.thyrocare.btechapp.models.data.OrderDetailsModel;
import com.thyrocare.btechapp.models.data.TestClinicalHistoryModel;
import com.thyrocare.btechapp.models.data.TestRateMasterModel;
import com.thyrocare.btechapp.models.data.TestSampleTypeModel;
import com.thyrocare.btechapp.models.data.TestTypeWiseTestRateMasterModelsList;
import com.thyrocare.btechapp.uiutils.AbstractActivity;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.util.ArrayList;


public class DisplayTestsMasterListActivity extends AbstractActivity {
    private Spinner spBrandMasters;
    private DhbDao dhbDao;
    private Activity activity;
    private ExpandableListView expandList;
    private ExpandableTestMasterListDisplayAdapter expAdapter;
    private Button btnSave;
    private SearchView svTestsList;
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
        if (getIntent().getExtras() != null) {
            selectedTestDetailsArr = getIntent().getExtras().getParcelableArrayList(BundleConstants.SELECTED_TESTS_LIST);
            orderDetailsModel = getIntent().getExtras().getParcelable(BundleConstants.ORDER_DETAILS_MODEL);
            benDetailsModel = getIntent().getExtras().getParcelable(BundleConstants.BENEFICIARY_DETAILS_MODEL);
            isEdit = getIntent().getExtras().getBoolean(BundleConstants.IS_TEST_EDIT, true);
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
                int AmountDue = 0;
                for (TestRateMasterModel trmm :
                        selectedTestsList) {
                    AmountDue = AmountDue + trmm.getRate();
                    BeneficiaryTestDetailsModel btdm = new BeneficiaryTestDetailsModel();
                    btdm.setFasting(trmm.getFasting());
                    btdm.setChldtests(trmm.getChldtests() != null ? trmm.getChldtests() : new ArrayList<ChildTestsModel>());
                    btdm.setTests(trmm.getTestCode());
                    btdm.setTestType(trmm.getTestType());
                    btdm.setProjId("");
                    btdm.setSampleType(trmm.getSampltype() != null ? trmm.getSampltype() : new ArrayList<TestSampleTypeModel>());
                    btdm.setTstClinicalHistory(trmm.getTstClinicalHistory() != null ? trmm.getTstClinicalHistory() : new ArrayList<TestClinicalHistoryModel>());
                    if (!InputUtils.isNull(trmm.getTestType()) && trmm.getTestType().equalsIgnoreCase("offer")) {
                        btdm.setProjId(trmm.getTestCode());
                        btdm.setTests(trmm.getDescription());
                        benDetailsModel.setProjId(trmm.getTestCode());
                    }
                    selectedTestDetailsArr.add(btdm);
                }
                benDetailsModel.setTestSampleType(selectedTestDetailsArr);
                if (!isEdit) {
                    orderDetailsModel.setAmountDue(orderDetailsModel.getAmountDue() + AmountDue);
                }
                orderDetailsModel.setTestEdit(isEdit);

                //jai
                benDetailsModel.setTestEdit(isEdit);
                //jai
                try {
                    orderDetailsModel.setBrandId(brandMasterModel.getBrandId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new OrderDetailsDao(dhbDao.getDb()).insertOrUpdate(orderDetailsModel);

                boolean isFasting = false;
                String testsCode = "";
                if (selectedTestsList != null) {
                    for (TestRateMasterModel testRateMasterModel :
                            selectedTestsList) {
                        if (InputUtils.isNull(testsCode)) {
                            if (!InputUtils.isNull(testRateMasterModel.getTestType()) && testRateMasterModel.getTestType().equals("OFFER")) {
                                testsCode = testRateMasterModel.getDescription();
                                benDetailsModel.setProjId(testRateMasterModel.getTestCode());
                            } else {
                                testsCode = testRateMasterModel.getTestCode();
                            }
                        } else {
                            if (!InputUtils.isNull(testRateMasterModel.getTestType()) && testRateMasterModel.getTestType().equals("OFFER")) {
                                testsCode = testsCode + "," + testRateMasterModel.getDescription();
                                benDetailsModel.setProjId(testRateMasterModel.getTestCode());
                            } else {
                                testsCode = testsCode + "," + testRateMasterModel.getTestCode();

                            }
                        }
                    }
                    if (InputUtils.isNull(benDetailsModel.getProjId())) {
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
                            if (!samples.contains(bstdm)) {
                                samples.add(bstdm);
                            }
                        }
                    }
                    benDetailsModel.setSampleType(samples);
                    for (TestRateMasterModel trmm :
                            selectedTestsList) {
                        if (!trmm.getFasting().toLowerCase().contains("non")) {
                            isFasting = true;
                            break;
                        }
                    }
                }
                benDetailsModel.setTestsCode(testsCode);
                benDetailsModel.setTests(testsCode);
                if (isFasting) {
                    benDetailsModel.setFasting("Fasting");
                } else {
                    benDetailsModel.setFasting("Non-Fasting");
                }
                bdd.insertOrUpdate(benDetailsModel);
                Intent intentFinish = new Intent();
//                intentFinish.putExtra(BundleConstants.BRAND_ID,brandMasterModel.getBrandId());
                setResult(BundleConstants.ADD_TESTS_FINISH, intentFinish);
                finish();
            }
        });
    }

    private void initData() {
        selectedTestsList = new ArrayList<>();
        if (selectedTestDetailsArr != null) {
            for (BeneficiaryTestDetailsModel btdm :
                    selectedTestDetailsArr) {
                TestRateMasterModel trmm = new TestRateMasterModel();
                trmm.setFasting(btdm.getFasting());
                trmm.setChldtests(btdm.getChldtests() != null ? btdm.getChldtests() : new ArrayList<ChildTestsModel>());
                trmm.setTestCode(btdm.getTests());
                trmm.setTestType(btdm.getTestType());
                trmm.setSampltype(btdm.getSampleType() != null ? btdm.getSampleType() : new ArrayList<TestSampleTypeModel>());
                trmm.setTstClinicalHistory(btdm.getTstClinicalHistory() != null ? btdm.getTstClinicalHistory() : new ArrayList<TestClinicalHistoryModel>());
                if (!InputUtils.isNull(btdm.getProjId())) {
                    trmm.setTestCode(btdm.getProjId());
                    trmm.setDescription(btdm.getTests());
                    trmm.setTestType("OFFER");
                }
                selectedTestsList.add(trmm);
            }
        }
        BrandMasterDao brandMasterDao = new BrandMasterDao(dhbDao.getDb());
        final ArrayList<BrandMasterModel> brandMasterModels = brandMasterDao.getAllModels();
        ArrayAdapter<BrandMasterModel> adapter = new ArrayAdapter<BrandMasterModel>(this, android.R.layout.simple_spinner_item, brandMasterModels);
        spBrandMasters.setAdapter(adapter);
        if (orderDetailsModel != null && !orderDetailsModel.isAddBen()) {
            for (int i = 0; i < brandMasterModels.size(); i++) {
                if (orderDetailsModel.getBrandId() == brandMasterModels.get(i).getBrandId()) {
                    spBrandMasters.setSelection(i);
                    break;
                }
            }
        }
//        if(isEdit){
        spBrandMasters.setEnabled(false);
//        }
//        else{
//            spBrandMasters.setEnabled(true);
//        }
    }

    @Override
    public void initUI() {
        super.initUI();
        spBrandMasters = (Spinner) findViewById(R.id.sp_tests);
        expandList = (ExpandableListView) findViewById(R.id.exp_list);
        btnSave = (Button) findViewById(R.id.btn_save);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        svTestsList = (SearchView) findViewById(R.id.sv_testsList);
        svTestsList.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        svTestsList.setIconifiedByDefault(false);
        svTestsList.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           /* @Override
            public boolean onQueryTextSubmit(String query) {
                expAdapter.filterData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                expAdapter.filterData(query);
                return false;
            }
        });
        svTestsList.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                expAdapter.filterData("");
                return false;
            }
        });*/

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (expAdapter != null) {
                    expAdapter.filterData(query);
                    if (!query.isEmpty()) {
                        for (int i = 0; i < expAdapter.getGroupCount(); i++)
                            expandList.expandGroup(i);
                    } else {
                        for (int i = 0; i < expAdapter.getGroupCount(); i++)
                            expandList.collapseGroup(i);
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (expAdapter != null) {
                    expAdapter.filterData(query);
                    if (!query.isEmpty()) {
                        for (int i = 0; i < expAdapter.getGroupCount(); i++)
                            expandList.expandGroup(i);
                    } else {
                        for (int i = 0; i < expAdapter.getGroupCount(); i++)
                            expandList.collapseGroup(i);
                    }

                }
                return false;
            }
        });
        svTestsList.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (expAdapter != null) {
                    expAdapter.filterData("");
                    for (int i = 0; i < expAdapter.getGroupCount(); i++)
                        expandList.collapseGroup(i);
                }
                return false;
            }
        });
    }

    private class PopulateTestDataAsyncTask extends AsyncTask<Void, Void, Void> {
        private Object item;

        public PopulateTestDataAsyncTask(Object item) {
            this.item = item;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(activity, "Please wait while we load the Tests List...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (item != null) {
                Logger.error("click");
                Logger.error("Brand ID : " + brandMasterModel.getBrandId());
                TestRateMasterDao testRateMasterDao = new TestRateMasterDao(dhbDao.getDb());
                ArrayList<String> testTypesArr = new ArrayList<String>();
                ArrayList<TestTypeWiseTestRateMasterModelsList> testRateMasterModels = new ArrayList<TestTypeWiseTestRateMasterModelsList>();
                testTypesArr = testRateMasterDao.getAllTestTypesFromBrandId(brandMasterModel.getBrandId() + "");
                for (String testType :
                        testTypesArr) {
                    ArrayList<TestRateMasterModel> testTypeWiseTestRateMasterModels = testRateMasterDao.getModelsFromTestType(testType);

                    ArrayList<TestRateMasterModel> testTypeWiseTestRateMasterModels_new = new ArrayList<>();
                    try {
                        if (orderDetailsModel.getUserAccessCode() != 0) {
                            if (testTypeWiseTestRateMasterModels != null) {
                                for (int i = 0; i < testTypeWiseTestRateMasterModels.size(); i++) {
                                    if (testTypeWiseTestRateMasterModels.get(i).getAccessUserCode() != null) {
                                        if (testTypeWiseTestRateMasterModels.get(i).getAccessUserCode().size() != 0) {
                                            for (int j = 0; j < testTypeWiseTestRateMasterModels.get(i).getAccessUserCode().size(); j++) {
                                                if (Integer.parseInt(testTypeWiseTestRateMasterModels.get(i).getAccessUserCode().get(j).getAccessCode()) == orderDetailsModel.getUserAccessCode()) {
                                                    testTypeWiseTestRateMasterModels_new.add(testTypeWiseTestRateMasterModels.get(i));
                                                }
                                            }
                                        } else {
                                            testTypeWiseTestRateMasterModels_new.add(testTypeWiseTestRateMasterModels.get(i));
                                        }
                                    }
                                }
                            }
                        } else {
                            testTypeWiseTestRateMasterModels_new = testTypeWiseTestRateMasterModels;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        testTypeWiseTestRateMasterModels_new = testTypeWiseTestRateMasterModels;
                    }

                    if (testTypeWiseTestRateMasterModels_new.size() != 0) {
                        TestTypeWiseTestRateMasterModelsList testTypeWiseTestRateMasterModelsList = new TestTypeWiseTestRateMasterModelsList();
                        testTypeWiseTestRateMasterModelsList.setTestType(testType);
                        testTypeWiseTestRateMasterModelsList.setTestRateMasterModels(testTypeWiseTestRateMasterModels_new);
                        testRateMasterModels.add(testTypeWiseTestRateMasterModelsList);
                    }
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
