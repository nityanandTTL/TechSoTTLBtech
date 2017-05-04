package com.dhb.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.dhb.R;
import com.dhb.dao.DhbDao;
import com.dhb.dao.models.BrandMasterDao;
import com.dhb.models.data.BrandMasterModel;
import com.dhb.uiutils.AbstractActivity;
import com.dhb.utils.api.Logger;

import java.util.ArrayList;


public class EditTestListActivity extends AbstractActivity {
    Spinner sp_tests;
    DhbDao dhbDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_test_list);
        initUI();
        BrandMasterDao brandMasterDao = new BrandMasterDao(dhbDao.getDb());
        ArrayList<BrandMasterModel> brandMasterModels = brandMasterDao.getAllModels();
        ArrayAdapter<BrandMasterModel> adapter=new ArrayAdapter<BrandMasterModel>(this, android.R.layout.simple_spinner_item, brandMasterModels);
        sp_tests.setAdapter(adapter);
        sp_tests.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BrandMasterModel brandMasterModel = (BrandMasterModel) parent.getItemAtPosition(position);
                Logger.error("ID : "+brandMasterModel.getBrandId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Logger.error("nothing is selected");
            }
        });
    }

    @Override
    public void initUI() {
        super.initUI();
        sp_tests = (Spinner) findViewById(R.id.sp_tests);
    }
}
