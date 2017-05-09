package com.dhb.dao.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dhb.models.data.BarcodeDetailsModel;
import com.dhb.models.data.BeneficiaryDetailsModel;
import com.dhb.models.data.BeneficiarySampleTypeDetailsModel;
import com.dhb.models.data.TestRateMasterModel;
import com.dhb.utils.api.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Pratik Ambhore on 24/11/15.
 */
public class BeneficiaryDetailsDao {

	private SQLiteDatabase db;


	// DB TABLE NAME

	private String TABLE_NAME = "beneficiary_details";


	// DB TABLE COLUMN INFO

	String ORDER_NO = "OrderNo";
	String BEN_ID = "benId";
	String NAME = "Name";
	String AGE = "Age";
	String GENDER = "Gender";
	String TESTS = "tests";
	String TESTS_CODE = "testsCode";
	String TESTS_LIST = "testsList";
	String FASTING = "Fasting";
	String VENEPUNCTURE = "Venepuncture";
	String BARCODE_DTL = "barcodedtl";
	String SAMPLE_TYPE = "sampleType";
	String CREATED_AT = "createdAt";
	String CREATED_BY = "createdBy";
	String UPDATED_AT = "updatedAt";
	String UPDATED_BY = "updatedBy";
	String RECORD_STATUS = "recordStatus";
	String SYNC_STATUS = "syncStatus";
	String SYNC_ACTION = "syncAction";


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
		beneficiaryDetailsModel.setOrderNo(cursor.getString(cursor.getColumnIndex(ORDER_NO)));
		beneficiaryDetailsModel.setTests(cursor.getString(cursor.getColumnIndex(TESTS)));
		beneficiaryDetailsModel.setTestsCode(cursor.getString(cursor.getColumnIndex(TESTS_CODE)));
		beneficiaryDetailsModel.setFasting(cursor.getString(cursor.getColumnIndex(FASTING)));
		beneficiaryDetailsModel.setVenepuncture(cursor.getBlob(cursor.getColumnIndex(VENEPUNCTURE)));

		TypeToken<ArrayList<BarcodeDetailsModel>> tokenBarcode = new TypeToken<ArrayList<BarcodeDetailsModel>>(){};
		ArrayList<BarcodeDetailsModel> bmArr =new Gson().fromJson(cursor.getString(cursor.getColumnIndex(BARCODE_DTL)),tokenBarcode.getType());

		TypeToken<ArrayList<BeneficiarySampleTypeDetailsModel>> tokenSampleTypes = new TypeToken<ArrayList<BeneficiarySampleTypeDetailsModel>>(){};
		ArrayList<BeneficiarySampleTypeDetailsModel> bstArr =new Gson().fromJson(cursor.getString(cursor.getColumnIndex(SAMPLE_TYPE)),tokenSampleTypes.getType());

		TypeToken<ArrayList<TestRateMasterModel>> tokenTestsList = new TypeToken<ArrayList<TestRateMasterModel>>(){};
		ArrayList<TestRateMasterModel> tstArr =new Gson().fromJson(cursor.getString(cursor.getColumnIndex(TESTS_LIST)),tokenTestsList.getType());

		beneficiaryDetailsModel.setBarcodedtl(bmArr);
		beneficiaryDetailsModel.setSampleType(bstArr);
		beneficiaryDetailsModel.setTestsList(tstArr);

		beneficiaryDetailsModel.setCreatedAt(cursor.getLong(cursor.getColumnIndex(CREATED_AT)));
		beneficiaryDetailsModel.setCreatedBy(cursor.getString(cursor.getColumnIndex(CREATED_BY)));
		beneficiaryDetailsModel.setUpdatedAt(cursor.getLong(cursor.getColumnIndex(UPDATED_AT)));
		beneficiaryDetailsModel.setUpdatedBy(cursor.getString(cursor.getColumnIndex(UPDATED_BY)));
		beneficiaryDetailsModel.setRecordStatus(cursor.getString(cursor.getColumnIndex(RECORD_STATUS)));
		beneficiaryDetailsModel.setSyncStatus(cursor.getString(cursor.getColumnIndex(SYNC_STATUS)));
		beneficiaryDetailsModel.setSyncAction((cursor.getString(cursor.getColumnIndex(SYNC_ACTION))));

		return beneficiaryDetailsModel;
	}

	public ContentValues getContentValuesFromModel(BeneficiaryDetailsModel orderDetailsModel) {

		ContentValues values = new ContentValues();
		values.put(ORDER_NO, orderDetailsModel.getOrderNo());
		values.put(BEN_ID, orderDetailsModel.getBenId());
		values.put(NAME, orderDetailsModel.getName());
		values.put(AGE, orderDetailsModel.getAge());
		values.put(GENDER, orderDetailsModel.getGender());
		values.put(TESTS, orderDetailsModel.getTests());
		values.put(TESTS_CODE, orderDetailsModel.getTestsCode());
		values.put(FASTING, orderDetailsModel.getFasting());
		values.put(VENEPUNCTURE, orderDetailsModel.getVenepuncture());
		values.put(BARCODE_DTL, new Gson().toJson(orderDetailsModel.getBarcodedtl()));
		values.put(SAMPLE_TYPE, new Gson().toJson(orderDetailsModel.getSampleType()));
		values.put(TESTS_LIST, new Gson().toJson(orderDetailsModel.getTestsList()));
		values.put(CREATED_AT, orderDetailsModel.getCreatedAt());
		values.put(CREATED_BY, orderDetailsModel.getCreatedBy());
		values.put(UPDATED_AT, orderDetailsModel.getUpdatedAt());
		values.put(UPDATED_BY, orderDetailsModel.getUpdatedBy());
		values.put(RECORD_STATUS, orderDetailsModel.getRecordStatus());
		values.put(SYNC_STATUS, orderDetailsModel.getSyncStatus());
		values.put(SYNC_ACTION, orderDetailsModel.getSyncAction());
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
					TestRateMasterDao testRateMasterDao = new TestRateMasterDao(db);
					ArrayList<TestRateMasterModel> testsList = testRateMasterDao.getModelsFromTestCodes(beneficiaryDetailsModel.getTestsCode());
					beneficiaryDetailsModel.setTestsList(testsList);
					beneficiaryDetailsModels.add(beneficiaryDetailsModel);
				}
			} while ((cursor.moveToNext()));
			cursor.close();
		}
		return beneficiaryDetailsModels;
	}

	public ArrayList<BeneficiaryDetailsModel> getModelsFromBenId(String benId) {

		ArrayList<BeneficiaryDetailsModel> beneficiaryDetailsModelsArr = new ArrayList<>();
		String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + BEN_ID + "=? AND " + RECORD_STATUS + "=?";
		String[] whereParams = new String[] {benId,"A"};
		Cursor cursor = this.db.rawQuery(query, whereParams);
		if (cursor != null && (cursor.moveToFirst())){
			do {
				BeneficiaryDetailsModel beneficiaryDetailsModel = getModelFromCursor(cursor);
				if (beneficiaryDetailsModel != null){
					TestRateMasterDao testRateMasterDao = new TestRateMasterDao(db);
					ArrayList<TestRateMasterModel> testsList = testRateMasterDao.getModelsFromTestCodes(beneficiaryDetailsModel.getTestsCode());
					beneficiaryDetailsModel.setTestsList(testsList);
					beneficiaryDetailsModelsArr.add(beneficiaryDetailsModel);
				}
			} while ((cursor.moveToNext()));
			cursor.close();
		}
		return beneficiaryDetailsModelsArr;
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
					TestRateMasterDao testRateMasterDao = new TestRateMasterDao(db);
					ArrayList<TestRateMasterModel> testsList = testRateMasterDao.getModelsFromTestCodes(beneficiaryDetailsModel.getTestsCode());
					beneficiaryDetailsModel.setTestsList(testsList);
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
			}
			else{
				contentValues = this.getContentValuesFromModel(model);
				int insertValue = (int) db.insert(TABLE_NAME, null, contentValues);
				Logger.debug("insertOrUpdateBeneficiaryDetailsModel: insert : " + insertValue);
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
		String query = "DELETE FROM " + TABLE_NAME + " WHERE " + ORDER_NO + " = ?";
		Logger.debug("Query - " + query);
		String[] whereParams = new String[]{orderNo};
		Cursor cursor = this.db.rawQuery(query, whereParams);
		if (cursor != null && !cursor.isClosed()){
			cursor.close();
		}
	}

	public void deleteByBenId(String benId){
		String query = "DELETE FROM " + TABLE_NAME + " WHERE " + BEN_ID + " = ?";
		Logger.debug("Query - " + query);
		String[] whereParams = new String[]{benId};
		Cursor cursor = this.db.rawQuery(query, whereParams);
		if (cursor != null && !cursor.isClosed()){
			cursor.close();
		}
	}

}