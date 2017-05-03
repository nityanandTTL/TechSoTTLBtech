package com.dhb.dao;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dhb.utils.api.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public class AbstractDao {

	protected SQLiteDatabase db;

	protected SQLiteOpenHelper mDbHelper;

	private static AtomicInteger mOpenCounter = new AtomicInteger();


	public SQLiteDatabase getDb() {
		return db;
	}

	public void setDb(SQLiteDatabase db) {
		this.db = db;
	}

	public synchronized SQLiteDatabase openDatabase() {

		int incCount = mOpenCounter.incrementAndGet();

		Logger.debug("incCount" + incCount);
		if (incCount == 1){

			Logger.debug("Creating new object");
			db = mDbHelper.getWritableDatabase();

		}

		if (db == null || (db != null && !db.isOpen())){

			db = mDbHelper.getWritableDatabase();

		}

		return db;
	}

	public synchronized void closeDatabase() {
		int decCount = mOpenCounter.decrementAndGet();
		Logger.debug("decCount" + decCount);
		if (decCount == 0){
			// Closing database
			db.close();

		}
	}

	public synchronized int count() {
		int count = mOpenCounter.intValue();
		return count;
	}

}