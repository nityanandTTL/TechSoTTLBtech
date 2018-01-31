package com.thyrocare.dao.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.thyrocare.models.data.BrandCurrencyModel;
import com.thyrocare.models.data.BrandMasterModel;
import com.thyrocare.utils.api.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Orion on 24/11/15.
 */
public class BrandMasterDao {

	private SQLiteDatabase db;


	// DB TABLE NAME

	private String TABLE_NAME = "brand_master";


	// DB TABLE COLUMN INFO
	
	String BRAND_ID = "BrandId";
	String BRAND_NAME = "BrandName";
	String BRAND_ADDRESS = "BrandAddress";
	String CANCELLATION_FEE = "cancellationFee";
	String BOOKING_INTERVAL = "bookingInterval";
	String COLLECTION_INTERVAL = "collectionInterval";
	String IS_AUTHENTICATION = "isAuthentication";
	String WALLET = "Wallet";
	String MULTIPLE_PAYMENTS = "multiplePayments";
	String CURRENCY_DETAILS = "crncydetls";
	String RESPONSE = "Response";
	String CREATED_AT = "createdAt";
	String CREATED_BY = "createdBy";
	String UPDATED_AT = "updatedAt";
	String UPDATED_BY = "updatedBy";
	String RECORD_STATUS = "recordStatus";
	String SYNC_STATUS = "syncStatus";
	String SYNC_ACTION = "syncAction";



	// Constructors

	public BrandMasterDao() {
	}

	public BrandMasterDao(SQLiteDatabase db) {
		this.db = db;
	}

	// getters and setters


	// get model from cursor

	private BrandMasterModel getModelFromCursor(Cursor cursor) {
		BrandMasterModel testRateMasterModel = new BrandMasterModel();
		testRateMasterModel.setBrandId(cursor.getInt(cursor.getColumnIndex(BRAND_ID)));
		testRateMasterModel.setBrandName(cursor.getString(cursor.getColumnIndex(BRAND_NAME)));
		testRateMasterModel.setBrandAddress(cursor.getString(cursor.getColumnIndex(BRAND_ADDRESS)));
		testRateMasterModel.setAuthentication(cursor.getString(cursor.getColumnIndex(IS_AUTHENTICATION)).equals("1"));
		testRateMasterModel.setCancellationFee(cursor.getInt(cursor.getColumnIndex(CANCELLATION_FEE)));
		testRateMasterModel.setBookingInterval(cursor.getInt(cursor.getColumnIndex(BOOKING_INTERVAL)));
		testRateMasterModel.setCollectionInterval(cursor.getInt(cursor.getColumnIndex(COLLECTION_INTERVAL)));
		testRateMasterModel.setResponse(cursor.getString(cursor.getColumnIndex(RESPONSE)));
		testRateMasterModel.setWallet(cursor.getString(cursor.getColumnIndex(WALLET)).equals("1"));
		testRateMasterModel.setMultiplePayments(cursor.getString(cursor.getColumnIndex(MULTIPLE_PAYMENTS)).equals("1"));
		TypeToken<ArrayList<BrandCurrencyModel>> tokenBCM = new TypeToken<ArrayList<BrandCurrencyModel>>(){};
		ArrayList<BrandCurrencyModel> bcmArr =new Gson().fromJson(cursor.getString(cursor.getColumnIndex(CURRENCY_DETAILS)),tokenBCM.getType());
		testRateMasterModel.setCrncydetls(bcmArr);
		testRateMasterModel.setCreatedAt(cursor.getLong(cursor.getColumnIndex(CREATED_AT)));
		testRateMasterModel.setCreatedBy(cursor.getString(cursor.getColumnIndex(CREATED_BY)));
		testRateMasterModel.setUpdatedAt(cursor.getLong(cursor.getColumnIndex(UPDATED_AT)));
		testRateMasterModel.setUpdatedBy(cursor.getString(cursor.getColumnIndex(UPDATED_BY)));
		testRateMasterModel.setRecordStatus(cursor.getString(cursor.getColumnIndex(RECORD_STATUS)));
		testRateMasterModel.setSyncStatus(cursor.getString(cursor.getColumnIndex(SYNC_STATUS)));
		testRateMasterModel.setSyncAction((cursor.getString(cursor.getColumnIndex(SYNC_ACTION))));

		return testRateMasterModel;
	}

	public ContentValues getContentValuesFromModel(BrandMasterModel brandMasterModel) {
		ContentValues values = new ContentValues();
		values.put(BRAND_ID, brandMasterModel.getBrandId());
		values.put(BRAND_NAME, brandMasterModel.getBrandName());
		values.put(BRAND_ADDRESS, brandMasterModel.getBrandAddress());
		values.put(CURRENCY_DETAILS, new Gson().toJson(brandMasterModel.getCrncydetls()));
		values.put(IS_AUTHENTICATION, brandMasterModel.isAuthentication()?"1":"0");
		values.put(CANCELLATION_FEE, brandMasterModel.getCancellationFee());
		values.put(BOOKING_INTERVAL, brandMasterModel.getBookingInterval());
		values.put(COLLECTION_INTERVAL, brandMasterModel.getCollectionInterval());
		values.put(RESPONSE, brandMasterModel.getResponse());
		values.put(WALLET, brandMasterModel.isWallet()?"1":"0");
		values.put(MULTIPLE_PAYMENTS, brandMasterModel.isMultiplePayments()?"1":"0");
		values.put(CREATED_AT, brandMasterModel.getCreatedAt());
		values.put(CREATED_BY, brandMasterModel.getCreatedBy());
		values.put(UPDATED_AT, brandMasterModel.getUpdatedAt());
		values.put(UPDATED_BY, brandMasterModel.getUpdatedBy());
		values.put(RECORD_STATUS, brandMasterModel.getRecordStatus());
		values.put(SYNC_STATUS, brandMasterModel.getSyncStatus());
		values.put(SYNC_ACTION, brandMasterModel.getSyncAction());
		return values;
	}

	public ArrayList<BrandMasterModel> getModelsFromBrandId(String brandId) {

		ArrayList<BrandMasterModel> brandMasterModels = new ArrayList<>();
		String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + BRAND_ID + "=? AND " + RECORD_STATUS + "=?";
		String[] whereParams = new String[] {brandId,"A"};
		Cursor cursor = this.db.rawQuery(query, whereParams);
		if (cursor != null && (cursor.moveToFirst())){
			do {
				BrandMasterModel orderDetailsModel = getModelFromCursor(cursor);
				if (orderDetailsModel != null){
					brandMasterModels.add(orderDetailsModel);
				}
			} while ((cursor.moveToNext()));
			cursor.close();
		}
		return brandMasterModels;
	}

	public ArrayList<BrandMasterModel> getAllModels() {
		ArrayList<BrandMasterModel> brandMasterModels = new ArrayList<>();
		String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + RECORD_STATUS + "=?";
		String[] whereParams = new String[] {"A"};
		Cursor cursor = this.db.rawQuery(query, whereParams);
		if (cursor != null && (cursor.moveToFirst())){
			do {
				BrandMasterModel orderDetailsModel = getModelFromCursor(cursor);
				if (orderDetailsModel != null){
					brandMasterModels.add(orderDetailsModel);
				}
			} while ((cursor.moveToNext()));
			cursor.close();
		}
		return brandMasterModels;
	}

	public BrandMasterModel getModelFromId(int id){
		BrandMasterModel orderDetailsModel = null;
		if(id!=0) {
			String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + BRAND_ID + " = " + id + " limit 1";
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

	public boolean insertOrUpdate(BrandMasterModel model) {
		ContentValues contentValues = new ContentValues();
		BrandMasterModel barcodeDetailsModel;
		if(model!=null && model.getBrandId()!=0) {
			barcodeDetailsModel = getModelFromId(model.getBrandId());
			if (barcodeDetailsModel!=null) {
				contentValues = this.getContentValuesFromModel(model);
				int updateValue = (int) db.update(TABLE_NAME, contentValues, BRAND_ID + "="+model.getBrandId(), new String[] {});
				Logger.debug("insertOrUpdateBrandMasterModel: update : " + updateValue);
			}
			else{
				contentValues = this.getContentValuesFromModel(model);
				int insertValue = (int) db.insert(TABLE_NAME, null, contentValues);
				Logger.debug("insertOrUpdateBrandMasterModel: insert : " + insertValue);
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

	public void deleteByBrandId(String brandId){
		String query = "" + BRAND_ID + " = ?";
		Logger.debug("Query - DELETE FROM " + TABLE_NAME + " WHERE " + query);
		String[] whereParams = new String[]{brandId};
		int deleteValue = this.db.delete(TABLE_NAME,query, whereParams);
		Logger.debug("DeleteBrandMasterByBrandId: "+deleteValue);
	}

}