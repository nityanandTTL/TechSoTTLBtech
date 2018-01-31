package com.thyrocare.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.thyrocare.utils.api.Logger;


public class CreateOrUpgradeDbTask extends AsyncTask<Void, Void, Boolean> {

	private DbTaskDelegate delegate;
	private Context context;

	public CreateOrUpgradeDbTask(DbTaskDelegate delegate, Context context) {
		this.delegate = delegate;
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		DbHelper.init(context.getApplicationContext());

		try {
			SQLiteDatabase db = DbHelper.sharedDbHelper().openDatabase();
			db.close();
			return true;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			DbHelper.sharedDbHelper().closeDatabase();
			Logger.error("CreateOrUpgradeDbTask.doInBackground Exception Occured: "
					+ sqle.getMessage());
			return false;
		}
	}

	@Override
	protected void onPostExecute(Boolean result) {
		delegate.dbTaskCompletedWithResult(result);
	}

	public static interface DbTaskDelegate {
		public void dbTaskCompletedWithResult(Boolean result);
	}

}
