package com.dhb.dao.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dhb.models.data.ChildTestsModel;
import com.dhb.models.data.TestClinicalHistoryModel;
import com.dhb.models.data.TestRateMasterModel;
import com.dhb.models.data.TestSampleTypeModel;
import com.dhb.models.data.TestSkillsModel;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.InputUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Pratik Ambhore on 24/11/15.
 */
public class TestRateMasterDao {

	private SQLiteDatabase db;


	// DB TABLE NAME

	private String TABLE_NAME = "test_rate_master";


	// DB TABLE COLUMN INFO

	String TEST_ID = "TestId";
	String BRAND_ID = "BrandId";
	String BRAND_NAME = "BrandName";
	String TEST_CODE = "TestCode";
	String TEST_TYPE = "TestType";
	String FASTING = "Fasting";
	String SAMPLE_TYPE = "sampltype";
	String LAST_MEAL_EAT = "LastMealEat";
	String RATE = "Rate";
	String DISCOUNT = "Discount";
	String INCENTIVE = "Incentive";
	String DESCRIPTION = "Description";
	String CHILD_TESTS = "chldtests";
	String TEST_SKILLS = "tstSkills";
	String TEST_CLINICAL_HISTORY = "tstClinicalHistory";
	String CREATED_AT = "createdAt";
	String CREATED_BY = "createdBy";
	String UPDATED_AT = "updatedAt";
	String UPDATED_BY = "updatedBy";
	String RECORD_STATUS = "recordStatus";
	String SYNC_STATUS = "syncStatus";
	String SYNC_ACTION = "syncAction";


	// Constructors

	public TestRateMasterDao() {
	}

	public TestRateMasterDao(SQLiteDatabase db) {
		this.db = db;
	}

	// getters and setters


	// get model from cursor

	private TestRateMasterModel getModelFromCursor(Cursor cursor) {
		TestRateMasterModel testRateMasterModel = new TestRateMasterModel();
		testRateMasterModel.setTestId(cursor.getInt(cursor.getColumnIndex(TEST_ID)));
		testRateMasterModel.setTestCode(cursor.getString(cursor.getColumnIndex(TEST_CODE)));
		testRateMasterModel.setTestType(cursor.getString(cursor.getColumnIndex(TEST_TYPE)));
		testRateMasterModel.setBrandId(cursor.getInt(cursor.getColumnIndex(BRAND_ID)));
		testRateMasterModel.setBrandName(cursor.getString(cursor.getColumnIndex(BRAND_NAME)));
		testRateMasterModel.setFasting(cursor.getString(cursor.getColumnIndex(FASTING)));
		testRateMasterModel.setLastMealEat(cursor.getInt(cursor.getColumnIndex(LAST_MEAL_EAT)));
		testRateMasterModel.setRate(cursor.getInt(cursor.getColumnIndex(RATE)));
		testRateMasterModel.setDiscount(cursor.getInt(cursor.getColumnIndex(DISCOUNT)));
		testRateMasterModel.setIncentive(cursor.getInt(cursor.getColumnIndex(INCENTIVE)));
		testRateMasterModel.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));

		TypeToken<ArrayList<ChildTestsModel>> tokenBarcode = new TypeToken<ArrayList<ChildTestsModel>>(){};
		ArrayList<ChildTestsModel> ctArr =new Gson().fromJson(cursor.getString(cursor.getColumnIndex(CHILD_TESTS)),tokenBarcode.getType());

		TypeToken<ArrayList<TestSampleTypeModel>> tokenSampleTypes = new TypeToken<ArrayList<TestSampleTypeModel>>(){};
		ArrayList<TestSampleTypeModel> bstArr =new Gson().fromJson(cursor.getString(cursor.getColumnIndex(SAMPLE_TYPE)),tokenSampleTypes.getType());


		TypeToken<ArrayList<TestSkillsModel>> tokenTestSkills = new TypeToken<ArrayList<TestSkillsModel>>(){};
		ArrayList<TestSkillsModel> tsArr =new Gson().fromJson(cursor.getString(cursor.getColumnIndex(TEST_SKILLS)),tokenTestSkills.getType());

		TypeToken<ArrayList<TestClinicalHistoryModel>> tokenTestClinicalHistory = new TypeToken<ArrayList<TestClinicalHistoryModel>>(){};
		ArrayList<TestClinicalHistoryModel> tchArr =new Gson().fromJson(cursor.getString(cursor.getColumnIndex(TEST_CLINICAL_HISTORY)),tokenTestClinicalHistory.getType());

		testRateMasterModel.setChldtests(ctArr);
		testRateMasterModel.setSampltype(bstArr);
		testRateMasterModel.setTstSkills(tsArr);
		testRateMasterModel.setTstClinicalHistory(tchArr);

		testRateMasterModel.setCreatedAt(cursor.getLong(cursor.getColumnIndex(CREATED_AT)));
		testRateMasterModel.setCreatedBy(cursor.getString(cursor.getColumnIndex(CREATED_BY)));
		testRateMasterModel.setUpdatedAt(cursor.getLong(cursor.getColumnIndex(UPDATED_AT)));
		testRateMasterModel.setUpdatedBy(cursor.getString(cursor.getColumnIndex(UPDATED_BY)));
		testRateMasterModel.setRecordStatus(cursor.getString(cursor.getColumnIndex(RECORD_STATUS)));
		testRateMasterModel.setSyncStatus(cursor.getString(cursor.getColumnIndex(SYNC_STATUS)));
		testRateMasterModel.setSyncAction((cursor.getString(cursor.getColumnIndex(SYNC_ACTION))));

		return testRateMasterModel;
	}

	public ContentValues getContentValuesFromModel(TestRateMasterModel orderDetailsModel) {
		ContentValues values = new ContentValues();
		values.put(TEST_ID, orderDetailsModel.getTestId());
		values.put(TEST_CODE, orderDetailsModel.getTestCode());
		values.put(TEST_TYPE, orderDetailsModel.getTestType());
		values.put(BRAND_ID, orderDetailsModel.getBrandId());
		values.put(BRAND_NAME, orderDetailsModel.getBrandName());
		values.put(SAMPLE_TYPE, new Gson().toJson(orderDetailsModel.getSampltype()));
		values.put(CHILD_TESTS, new Gson().toJson(orderDetailsModel.getChldtests()));
		values.put(TEST_SKILLS, new Gson().toJson(orderDetailsModel.getTstSkills()));
		values.put(TEST_CLINICAL_HISTORY, new Gson().toJson(orderDetailsModel.getTstClinicalHistory()));
		values.put(LAST_MEAL_EAT, orderDetailsModel.getLastMealEat());
		values.put(RATE, orderDetailsModel.getRate());
		values.put(DISCOUNT, orderDetailsModel.getDiscount());
		values.put(INCENTIVE, orderDetailsModel.getIncentive());
		values.put(DESCRIPTION, orderDetailsModel.getDescription());
		values.put(FASTING, orderDetailsModel.getFasting());
		values.put(CREATED_AT, orderDetailsModel.getCreatedAt());
		values.put(CREATED_BY, orderDetailsModel.getCreatedBy());
		values.put(UPDATED_AT, orderDetailsModel.getUpdatedAt());
		values.put(UPDATED_BY, orderDetailsModel.getUpdatedBy());
		values.put(RECORD_STATUS, orderDetailsModel.getRecordStatus());
		values.put(SYNC_STATUS, orderDetailsModel.getSyncStatus());
		values.put(SYNC_ACTION, orderDetailsModel.getSyncAction());
		return values;
	}

	public ArrayList<TestRateMasterModel> getModelsFromBrandId(String brandId) {

		ArrayList<TestRateMasterModel> testRateMasterModels = new ArrayList<>();
		String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + BRAND_ID + "=? AND " + RECORD_STATUS + "=?";
		String[] whereParams = new String[] {brandId,"A"};
		Cursor cursor = this.db.rawQuery(query, whereParams);
		if (cursor != null && (cursor.moveToFirst())){
			do {
				TestRateMasterModel orderDetailsModel = getModelFromCursor(cursor);
				if (orderDetailsModel != null){
					testRateMasterModels.add(orderDetailsModel);
				}
			} while ((cursor.moveToNext()));
			cursor.close();
		}
		return testRateMasterModels;
	}

	public ArrayList<TestRateMasterModel> getModelsFromTestCodes(String testCodes) {

		ArrayList<TestRateMasterModel> testRateMasterModels = new ArrayList<>();
		String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + TEST_CODE + " IN (?) AND " + RECORD_STATUS + "=?";
		String[] whereParams = new String[] {testCodes,"A"};
		Cursor cursor = this.db.rawQuery(query, whereParams);
		if (cursor != null && (cursor.moveToFirst())){
			do {
				TestRateMasterModel orderDetailsModel = getModelFromCursor(cursor);
				if (orderDetailsModel != null){
					testRateMasterModels.add(orderDetailsModel);
				}
			} while ((cursor.moveToNext()));
			cursor.close();
		}
		return testRateMasterModels;
	}

	public ArrayList<TestRateMasterModel> getModelsFromTestType(String testType) {

		ArrayList<TestRateMasterModel> orderDetailsModelsArr = new ArrayList<>();
		String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + TEST_TYPE + "=? AND " + RECORD_STATUS + "=?";
		String[] whereParams = new String[] {testType,"A"};
		Cursor cursor = this.db.rawQuery(query, whereParams);
		if (cursor != null && (cursor.moveToFirst())){
			do {
				TestRateMasterModel orderDetailsModel = getModelFromCursor(cursor);
				if (orderDetailsModel != null){
					orderDetailsModelsArr.add(orderDetailsModel);
				}
			} while ((cursor.moveToNext()));
			cursor.close();
		}
		return orderDetailsModelsArr;
	}

	public ArrayList<String> getAllTestTypesFromBrandId(String brandId){
		ArrayList<String> testTypesArr = new ArrayList<>();
		String query = "SELECT DISTINCT("+TEST_TYPE+") FROM " + TABLE_NAME + " WHERE " + BRAND_ID + "=? AND " + RECORD_STATUS + "=?";
		String[] whereParams = new String[] {brandId,"A"};
		Cursor cursor = this.db.rawQuery(query, whereParams);
		if (cursor != null && (cursor.moveToFirst())){
			do {
				String testType = cursor.getString(cursor.getColumnIndex(TEST_TYPE));
				if(!InputUtils.isNull(testType)){
					testTypesArr.add(testType);
				}
			} while ((cursor.moveToNext()));
			cursor.close();
		}
		return testTypesArr;
	}

	public TestRateMasterModel getModelFromId(int testId){
		TestRateMasterModel orderDetailsModel = null;
		if(testId!=0) {
			String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + TEST_ID + " = " + testId + " limit 1";
			Logger.debug("Query - " + query);
			String[] whereParams = new String[]{};
			Cursor cursor = this.db.rawQuery(query, whereParams);
			if (cursor != null && (cursor.moveToFirst())) {

				do {
					orderDetailsModel = getModelFromCursor(cursor);
				}while ((cursor.moveToNext()));
			}
			if (cursor != null && !cursor.isClosed()){
				cursor.close();
			}
		}
		return orderDetailsModel;
	}

	// update if exists or insert barcode model to db

	public boolean insertOrUpdate(TestRateMasterModel model) {
		ContentValues contentValues = new ContentValues();
		TestRateMasterModel barcodeDetailsModel;
		if(model!=null && model.getTestId()!=0) {
			barcodeDetailsModel = getModelFromId(model.getTestId());
			if (barcodeDetailsModel!=null) {
				contentValues = this.getContentValuesFromModel(model);
				int updateValue = (int) db.update(TABLE_NAME, contentValues, TEST_ID + "="+model.getTestId(), new String[] {});
				Logger.debug("insertOrUpdateTestRateMasterModel: update : " + updateValue);
			}
			else{
				contentValues = this.getContentValuesFromModel(model);
				int insertValue = (int) db.insert(TABLE_NAME, null, contentValues);
				Logger.debug("insertOrUpdateTestRateMasterModel: insert : " + insertValue);
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

	public void deleteByTestId(String testId){
		String query = "DELETE FROM " + TABLE_NAME + " WHERE " + TEST_ID + " = ?";
		Logger.debug("Query - " + query);
		String[] whereParams = new String[]{testId};
		Cursor cursor = this.db.rawQuery(query, whereParams);
		if (cursor != null && !cursor.isClosed()){
			cursor.close();
		}
	}

	public void deleteByTestType(String testType){
		String query = "DELETE FROM " + TABLE_NAME + " WHERE " + TEST_TYPE + " = ?";
		Logger.debug("Query - " + query);
		String[] whereParams = new String[]{testType};
		Cursor cursor = this.db.rawQuery(query, whereParams);
		if (cursor != null && !cursor.isClosed()){
			cursor.close();
		}
	}

	public void deleteByBrandId(String brandId){
		String query = "DELETE FROM " + TABLE_NAME + " WHERE " + BRAND_ID + " = ?";
		Logger.debug("Query - " + query);
		String[] whereParams = new String[]{brandId};
		Cursor cursor = this.db.rawQuery(query, whereParams);
		if (cursor != null && !cursor.isClosed()){
			cursor.close();
		}
	}

}