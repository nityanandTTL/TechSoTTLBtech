package com.dhb.dao.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dhb.models.data.LabAlertMasterModel;
import com.dhb.utils.api.Logger;

import java.util.ArrayList;

/**
 * Created by Pratik Ambhore on 24/11/15.
 */
public class LabAlertMasterDao {

	private SQLiteDatabase db;


	// DB TABLE NAME

	private String TABLE_NAME = "lab_alert_master";


	// DB TABLE COLUMN INFO

	String LAB_ALERT_ID = "LabAlertId";
	String LAB_ALERT = "LabAlert";
	String CREATED_AT = "createdAt";
	String CREATED_BY = "createdBy";
	String UPDATED_AT = "updatedAt";
	String UPDATED_BY = "updatedBy";
	String RECORD_STATUS = "recordStatus";
	String SYNC_STATUS = "syncStatus";
	String SYNC_ACTION = "syncAction";


	// Constructors

	public LabAlertMasterDao() {
	}

	public LabAlertMasterDao(SQLiteDatabase db) {
		this.db = db;
	}

	// getters and setters


	// get model from cursor

	private LabAlertMasterModel getModelFromCursor(Cursor cursor) {
		LabAlertMasterModel labAlertMasterModel = new LabAlertMasterModel();
		labAlertMasterModel.setLabAlertId(cursor.getInt(cursor.getColumnIndex(LAB_ALERT_ID)));
		labAlertMasterModel.setLabAlert(cursor.getString(cursor.getColumnIndex(LAB_ALERT)));
		labAlertMasterModel.setCreatedAt(cursor.getLong(cursor.getColumnIndex(CREATED_AT)));
		labAlertMasterModel.setCreatedBy(cursor.getString(cursor.getColumnIndex(CREATED_BY)));
		labAlertMasterModel.setUpdatedAt(cursor.getLong(cursor.getColumnIndex(UPDATED_AT)));
		labAlertMasterModel.setUpdatedBy(cursor.getString(cursor.getColumnIndex(UPDATED_BY)));
		labAlertMasterModel.setRecordStatus(cursor.getString(cursor.getColumnIndex(RECORD_STATUS)));
		labAlertMasterModel.setSyncStatus(cursor.getString(cursor.getColumnIndex(SYNC_STATUS)));
		labAlertMasterModel.setSyncAction((cursor.getString(cursor.getColumnIndex(SYNC_ACTION))));
		return labAlertMasterModel;
	}

	public ContentValues getContentValuesFromModel(LabAlertMasterModel LabAlertMasterModel) {
		ContentValues values = new ContentValues();
		values.put(LAB_ALERT_ID, LabAlertMasterModel.getLabAlertId());
		values.put(LAB_ALERT, LabAlertMasterModel.getLabAlert());
		values.put(CREATED_AT, LabAlertMasterModel.getCreatedAt());
		values.put(CREATED_BY, LabAlertMasterModel.getCreatedBy());
		values.put(UPDATED_AT, LabAlertMasterModel.getUpdatedAt());
		values.put(UPDATED_BY, LabAlertMasterModel.getUpdatedBy());
		values.put(RECORD_STATUS, LabAlertMasterModel.getRecordStatus());
		values.put(SYNC_STATUS, LabAlertMasterModel.getSyncStatus());
		values.put(SYNC_ACTION, LabAlertMasterModel.getSyncAction());
		return values;
	}

	public LabAlertMasterModel getModelFromId(String id) {

		LabAlertMasterModel labAlertMasterModel = null;
		String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + LAB_ALERT_ID + "=? AND " + RECORD_STATUS + "=?";
		String[] whereParams = new String[] {id,"A"};
		Cursor cursor = this.db.rawQuery(query, whereParams);
		if (cursor != null && (cursor.moveToFirst())){
			do {
				labAlertMasterModel = getModelFromCursor(cursor);
			} while ((cursor.moveToNext()));
			cursor.close();
		}
		return labAlertMasterModel;
	}

	public ArrayList<LabAlertMasterModel> getAllModels() {
		ArrayList<LabAlertMasterModel> LabAlertMasterModels = new ArrayList<>();
		String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + RECORD_STATUS + "=?";
		String[] whereParams = new String[] {"A"};
		Cursor cursor = this.db.rawQuery(query, whereParams);
		if (cursor != null && (cursor.moveToFirst())){
			do {
				LabAlertMasterModel model = getModelFromCursor(cursor);
				if (model != null){
					LabAlertMasterModels.add(model);
				}
			} while ((cursor.moveToNext()));
			cursor.close();
		}
		return LabAlertMasterModels;
	}

	// update if exists or insert barcode model to db

	public boolean insertOrUpdate(LabAlertMasterModel model) {
		ContentValues contentValues = new ContentValues();
		LabAlertMasterModel barcodeDetailsModel;
		if(model!=null && model.getLabAlertId()!=0) {
			barcodeDetailsModel = getModelFromId(model.getLabAlertId()+"");
			if (barcodeDetailsModel!=null) {
				contentValues = this.getContentValuesFromModel(model);
				int updateValue = (int) db.update(TABLE_NAME, contentValues, LAB_ALERT_ID + "="+model.getLabAlertId(), new String[] {});
				Logger.debug("insertOrUpdateLabAlertMasterModel: update : " + updateValue);
			}
			else{
				contentValues = this.getContentValuesFromModel(model);
				int insertValue = (int) db.insert(TABLE_NAME, null, contentValues);
				Logger.debug("insertOrUpdateLabAlertMasterModel: insert : " + insertValue);
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

	public void deleteById(String id){
		String query = "" + LAB_ALERT_ID + " = ?";
		Logger.debug("Query - DELETE FROM " + TABLE_NAME + " WHERE " + query);
		String[] whereParams = new String[]{id};
		int deleteValue = db.delete(TABLE_NAME,query, whereParams);
		Logger.debug("DeleteLabAlertById: "+deleteValue);
	}
}