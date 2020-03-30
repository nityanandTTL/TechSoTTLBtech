package com.thyrocare.dao.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thyrocare.models.data.BeneficiaryBarcodeDetailsModel;
import com.thyrocare.models.data.BeneficiaryDetailsModel;
import com.thyrocare.models.data.BeneficiaryLabAlertsModel;
import com.thyrocare.models.data.BeneficiarySampleTypeDetailsModel;
import com.thyrocare.models.data.BeneficiaryTestDetailsModel;
import com.thyrocare.models.data.BeneficiaryTestWiseClinicalHistoryModel;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.CommonUtils;
import com.thyrocare.utils.app.InputUtils;

import java.util.ArrayList;

/**
 * Created by Orion on 24/11/15.
 */
public class BeneficiaryDetailsDao {

	private SQLiteDatabase db;


	// DB TABLE NAME

	private String TABLE_NAME = "beneficiary_details";


	// DB TABLE COLUMN INFO

	private String ORDER_NO = "OrderNo";
	private String BEN_ID = "benId";
	private String NAME = "Name";
	private String AGE = "Age";
	private String GENDER = "Gender";
	private String AADHAR = "Aadhar";
	private String TESTS = "tests";
	private String PROJ_ID = "ProjId";
	private String TESTS_CODE = "testsCode";
	private String TEST_SAMPLE_TYPE = "testSampleType";
	private String FASTING = "Fasting";
	private String VENEPUNCTURE = "Venepuncture";
	private String BARCODE_DTL = "barcodedtl";
	private String IS_TEST_EDIT = "isTestEdit";
	private String IS_ADD_BEN = "isAddBen";
	private String IS_TRF = "isTRF";
	private String CLINICAL_HISTORY = "clHistory";
	private String LAB_ALERT = "labAlert";
	private String SAMPLE_TYPE = "sampleType";
	private String REMARKS = "remarks";
	private String CREATED_AT = "createdAt";
	private String CREATED_BY = "createdBy";
	private String UPDATED_AT = "updatedAt";
	private String UPDATED_BY = "updatedBy";
	private String RECORD_STATUS = "recordStatus";
	private String SYNC_STATUS = "syncStatus";
	private String SYNC_ACTION = "syncAction";
	private String LEADID = "LeadId";



	// Constructors

	public BeneficiaryDetailsDao() {
	}

	public BeneficiaryDetailsDao(SQLiteDatabase db) {
		this.db = db;
	}

	// getters and setters


	// get model from cursor

	private BeneficiaryDetailsModel getModelFromCursor(Cursor cursor) {
		BeneficiaryDetailsModel beneficiaryDetailsModel = new BeneficiaryDetailsModel();
		beneficiaryDetailsModel.setBenId(cursor.getInt(cursor.getColumnIndex(BEN_ID)));
		beneficiaryDetailsModel.setName(cursor.getString(cursor.getColumnIndex(NAME)));
		beneficiaryDetailsModel.setAge(cursor.getInt(cursor.getColumnIndex(AGE)));
		beneficiaryDetailsModel.setGender(cursor.getString(cursor.getColumnIndex(GENDER)));
		beneficiaryDetailsModel.setAadhar(cursor.getString(cursor.getColumnIndex(AADHAR)));
		beneficiaryDetailsModel.setOrderNo(cursor.getString(cursor.getColumnIndex(ORDER_NO)));
		beneficiaryDetailsModel.setTests(cursor.getString(cursor.getColumnIndex(TESTS)));
		beneficiaryDetailsModel.setProjId(cursor.getString(cursor.getColumnIndex(PROJ_ID)));
		beneficiaryDetailsModel.setTestsCode(cursor.getString(cursor.getColumnIndex(TESTS_CODE)));
		beneficiaryDetailsModel.setFasting(cursor.getString(cursor.getColumnIndex(FASTING)));
		beneficiaryDetailsModel.setRemarks(cursor.getString(cursor.getColumnIndex(REMARKS)));
		beneficiaryDetailsModel.setVenepuncture(CommonUtils.encodeImage(cursor.getBlob(cursor.getColumnIndex(VENEPUNCTURE))));
		beneficiaryDetailsModel.setTestEdit(cursor.getString(cursor.getColumnIndex(IS_TEST_EDIT)).equals("1"));
		beneficiaryDetailsModel.setAddBen(cursor.getString(cursor.getColumnIndex(IS_ADD_BEN)).equals("1"));
		beneficiaryDetailsModel.setTRF(cursor.getString(cursor.getColumnIndex(IS_TRF)).equals("1"));

		TypeToken<ArrayList<BeneficiaryBarcodeDetailsModel>> tokenBarcode = new TypeToken<ArrayList<BeneficiaryBarcodeDetailsModel>>(){};
		ArrayList<BeneficiaryBarcodeDetailsModel> bmArr =new Gson().fromJson(cursor.getString(cursor.getColumnIndex(BARCODE_DTL)),tokenBarcode.getType());

		TypeToken<ArrayList<BeneficiarySampleTypeDetailsModel>> tokenSampleTypes = new TypeToken<ArrayList<BeneficiarySampleTypeDetailsModel>>(){};
		ArrayList<BeneficiarySampleTypeDetailsModel> bstArr =new Gson().fromJson(cursor.getString(cursor.getColumnIndex(SAMPLE_TYPE)),tokenSampleTypes.getType());

		TypeToken<ArrayList<BeneficiaryTestDetailsModel>> tokenTestsDetailsList = new TypeToken<ArrayList<BeneficiaryTestDetailsModel>>(){};
		ArrayList<BeneficiaryTestDetailsModel> tstArr =new Gson().fromJson(cursor.getString(cursor.getColumnIndex(TEST_SAMPLE_TYPE)),tokenTestsDetailsList.getType());

		TypeToken<ArrayList<BeneficiaryTestWiseClinicalHistoryModel>> tokenCH = new TypeToken<ArrayList<BeneficiaryTestWiseClinicalHistoryModel>>(){};
		ArrayList<BeneficiaryTestWiseClinicalHistoryModel> tCHArr =new Gson().fromJson(cursor.getString(cursor.getColumnIndex(CLINICAL_HISTORY)),tokenCH.getType());

		TypeToken<ArrayList<BeneficiaryLabAlertsModel>> tokenLA = new TypeToken<ArrayList<BeneficiaryLabAlertsModel>>(){};
		ArrayList<BeneficiaryLabAlertsModel> tLAArr =new Gson().fromJson(cursor.getString(cursor.getColumnIndex(LAB_ALERT)),tokenLA.getType());

		beneficiaryDetailsModel.setBarcodedtl(bmArr);
		beneficiaryDetailsModel.setSampleType(bstArr);
		beneficiaryDetailsModel.setTestSampleType(tstArr);
		beneficiaryDetailsModel.setClHistory(tCHArr);
		beneficiaryDetailsModel.setLabAlert(tLAArr);

		beneficiaryDetailsModel.setCreatedAt(cursor.getLong(cursor.getColumnIndex(CREATED_AT)));
		beneficiaryDetailsModel.setCreatedBy(cursor.getString(cursor.getColumnIndex(CREATED_BY)));
		beneficiaryDetailsModel.setUpdatedAt(cursor.getLong(cursor.getColumnIndex(UPDATED_AT)));
		beneficiaryDetailsModel.setUpdatedBy(cursor.getString(cursor.getColumnIndex(UPDATED_BY)));
		beneficiaryDetailsModel.setRecordStatus(cursor.getString(cursor.getColumnIndex(RECORD_STATUS)));
		beneficiaryDetailsModel.setSyncStatus(cursor.getString(cursor.getColumnIndex(SYNC_STATUS)));
		beneficiaryDetailsModel.setSyncAction((cursor.getString(cursor.getColumnIndex(SYNC_ACTION))));
		beneficiaryDetailsModel.setLeadId(cursor.getString(cursor.getColumnIndex(LEADID)));

		return beneficiaryDetailsModel;
	}

	public ContentValues getContentValuesFromModel(BeneficiaryDetailsModel beneficiaryDetailsModel) {
		ContentValues values = new ContentValues();
		values.put(ORDER_NO, beneficiaryDetailsModel.getOrderNo());
		values.put(BEN_ID, beneficiaryDetailsModel.getBenId());
		values.put(NAME, beneficiaryDetailsModel.getName());
		values.put(AGE, beneficiaryDetailsModel.getAge());
		values.put(GENDER, beneficiaryDetailsModel.getGender());
		values.put(AADHAR, beneficiaryDetailsModel.getAadhar());
		values.put(TESTS, beneficiaryDetailsModel.getTests());
		values.put(PROJ_ID, beneficiaryDetailsModel.getProjId());
		values.put(TESTS_CODE, beneficiaryDetailsModel.getTestsCode());
		values.put(FASTING, beneficiaryDetailsModel.getFasting());
		values.put(REMARKS, beneficiaryDetailsModel.getRemarks());
        values.put(IS_TEST_EDIT, beneficiaryDetailsModel.isTestEdit() ? "1" : "0");
        values.put(IS_ADD_BEN, beneficiaryDetailsModel.isAddBen() ? "1" : "0");
        values.put(IS_TRF, beneficiaryDetailsModel.isTRF() ? "1" : "0");
		values.put(VENEPUNCTURE, CommonUtils.decodedImageBytes(InputUtils.isNull(beneficiaryDetailsModel.getVenepuncture())?"":beneficiaryDetailsModel.getVenepuncture()));
//		values.put(VENEPUNCTURE, "Tejas");
		values.put(BARCODE_DTL, new Gson().toJson(beneficiaryDetailsModel.getBarcodedtl()));
		values.put(SAMPLE_TYPE, new Gson().toJson(beneficiaryDetailsModel.getSampleType()));
		values.put(TEST_SAMPLE_TYPE, new Gson().toJson(beneficiaryDetailsModel.getTestSampleType()));
		values.put(CLINICAL_HISTORY, new Gson().toJson(beneficiaryDetailsModel.getClHistory()));
		values.put(LAB_ALERT, new Gson().toJson(beneficiaryDetailsModel.getLabAlert()));
		values.put(CREATED_AT, beneficiaryDetailsModel.getCreatedAt());
		values.put(CREATED_BY, beneficiaryDetailsModel.getCreatedBy());
		values.put(UPDATED_AT, beneficiaryDetailsModel.getUpdatedAt());
		values.put(UPDATED_BY, beneficiaryDetailsModel.getUpdatedBy());
		values.put(RECORD_STATUS, beneficiaryDetailsModel.getRecordStatus());
		values.put(SYNC_STATUS, beneficiaryDetailsModel.getSyncStatus());
		values.put(SYNC_ACTION, beneficiaryDetailsModel.getSyncAction());
		values.put(LEADID, beneficiaryDetailsModel.getLeadId());




		return values;
	}

	public ArrayList<BeneficiaryDetailsModel> getModelsFromOrderNo(String orderNo) {

		ArrayList<BeneficiaryDetailsModel> beneficiaryDetailsModels = new ArrayList<>();
		String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ORDER_NO + "=? AND " + RECORD_STATUS + "=?";
		String[] whereParams = new String[] {orderNo,"A"};
		Cursor cursor = this.db.rawQuery(query, whereParams);
		if (cursor != null && (cursor.moveToFirst())){
			do {
				BeneficiaryDetailsModel beneficiaryDetailsModel = getModelFromCursor(cursor);
				if (beneficiaryDetailsModel != null){
					beneficiaryDetailsModels.add(beneficiaryDetailsModel);
				}
			} while ((cursor.moveToNext()));
			cursor.close();
		}
		return beneficiaryDetailsModels;
	}

	public BeneficiaryDetailsModel getModelFromId(int benId){
		BeneficiaryDetailsModel beneficiaryDetailsModel = null;
		if(benId!=0) {
			String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + BEN_ID + " = " + benId + " limit 1";
			Logger.debug("Query - " + query);
			String[] whereParams = new String[]{};
			Cursor cursor = this.db.rawQuery(query, whereParams);
			if (cursor != null && (cursor.moveToFirst())) {
				do {
					beneficiaryDetailsModel = getModelFromCursor(cursor);
				}while ((cursor.moveToNext()));
			}
			if (cursor != null && !cursor.isClosed()){
				cursor.close();
			}
		}
		return beneficiaryDetailsModel;
	}

	// update if exists or insert barcode model to db

	public boolean insertOrUpdate(BeneficiaryDetailsModel model) {
		ContentValues contentValues = new ContentValues();
		BeneficiaryDetailsModel barcodeDetailsModel;
		if(model!=null && model.getBenId()!=0) {
			barcodeDetailsModel = getModelFromId(model.getBenId());
			if (barcodeDetailsModel!=null) {
				contentValues = this.getContentValuesFromModel(model);
				int updateValue = (int) db.update(TABLE_NAME, contentValues, BEN_ID + "="+model.getBenId(), new String[] {});
				Logger.debug("insertOrUpdateBeneficiaryDetailsModel: update : " + updateValue);
                return true;
			} else{
				contentValues = this.getContentValuesFromModel(model);
				int insertValue = (int) db.insert(TABLE_NAME, null, contentValues);
				Logger.debug("insertOrUpdateBeneficiaryDetailsModel: insert : " + insertValue);
                return true;
			}
		}
		return false;
	}

	public boolean updateBeneficiaryId(int oldBeneficiaryId,BeneficiaryDetailsModel model) {
		ContentValues contentValues = new ContentValues();
		BeneficiaryDetailsModel barcodeDetailsModel;
		if(model!=null && model.getBenId()!=0) {
			barcodeDetailsModel = getModelFromId(oldBeneficiaryId);
			if (barcodeDetailsModel!=null) {
				contentValues = this.getContentValuesFromModel(model);
				int updateValue = (int) db.update(TABLE_NAME, contentValues, BEN_ID + "="+oldBeneficiaryId, new String[] {});
				Logger.debug("insertOrUpdateBeneficiaryDetailsModel: update : " + updateValue);
				return true;
			}
			else{
				return false;
			}
		}
		return false;
	}
	public long getLastUpdatedAt() {

		long updatedDate = 0;
		String query = "SELECT " + UPDATED_AT + " FROM " + TABLE_NAME
				+ " WHERE	UPDATED_AT = (SELECT max( "+ UPDATED_AT + ") FROM " + TABLE_NAME + ")"
				+ " LIMIT 1";
		String[] whereParams = new String[] {};
		Cursor cursor = this.db.rawQuery(query, whereParams);
		if (cursor != null && (cursor.moveToFirst())){

			do {
				updatedDate = cursor.getLong(cursor.getColumnIndex(UPDATED_AT));
			} while ((cursor.moveToNext()));

			cursor.close();
		}

		return updatedDate;
	}

	public void deleteAll() {
		db.execSQL("DELETE FROM " + TABLE_NAME);
	}

	public void deleteByOrderNo(String orderNo){
		String query =  ORDER_NO + " = ?";
		Logger.debug("Query - DELETE FROM " + TABLE_NAME + " WHERE " + query);
		String[] whereParams = new String[]{orderNo};
		int deleteValue = db.delete(TABLE_NAME,query, whereParams);
		Logger.debug("DeleteBeneficiaryDetailsByOrderNo: "+deleteValue);
	}

	public void deleteByBenId(String benId){
		String query = BEN_ID + " = "+Integer.parseInt(benId);
		Logger.debug("Query -  DELETE FROM " + TABLE_NAME + " WHERE " + query);
		String[] whereParams = new String[]{};
		int deleteValue = this.db.delete(TABLE_NAME,query, whereParams);
		Logger.debug("DeleteBeneficiaryDetailsByBenId: "+deleteValue);
	}

}