package com.thyrocare.dao;

import android.content.Context;

import com.thyrocare.dao.models.BeneficiaryDetailsDao;
import com.thyrocare.dao.models.OrderDetailsDao;
import com.thyrocare.utils.app.AppPreferenceManager;

public class DhbDao extends AbstractDao {

	/*Patient Info Table*/
	private Context context;
	AppPreferenceManager appPreferenceManager;


	public DhbDao(Context context) {
		this.mDbHelper = DbHelper.sharedDbHelper();
		openDatabase();
		this.context = context;
		appPreferenceManager = new AppPreferenceManager(context);
	}

	public void deleteTablesonLogout(){
		BeneficiaryDetailsDao beneficiaryDetailsDao = new BeneficiaryDetailsDao(getDb());
		beneficiaryDetailsDao.deleteAll();

		OrderDetailsDao orderDetailsDao = new OrderDetailsDao(getDb());
		orderDetailsDao.deleteAll();

		/* String myPath = "dhb_db.db";
		   SQLiteDatabase.deleteDatabase(new File(context.getDatabasePath(DbHelper.DB_NAME).getAbsolutePath()));*/

//		db.execSQL("delete from " + TABLE_PATIENT_INFO);
//		db.execSQL("delete from " + TABLE_USER_DOCUMENTS);

		/*QueueDao queueDao = new QueueDao(getDb(), appPreferenceManager);
		queueDao.deleteAll();*/
	}


}