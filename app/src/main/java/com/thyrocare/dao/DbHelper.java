package com.thyrocare.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.thyrocare.utils.api.Logger;

import java.util.concurrent.atomic.AtomicInteger;


public class DbHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "dhb_db";

	public static final int DB_VERSION = 15;

	private static DbHelper dbHelper;

	public Context context;

	private SQLiteDatabase mDatabase;

	private AtomicInteger mOpenCounter = new AtomicInteger();

	private OperationResult migrationResult = new OperationResult(
	        OperationResult.OPERATION_SUCCESSFUL);

	public static synchronized DbHelper sharedDbHelper() {
		if (dbHelper == null){
			throw new IllegalStateException(
			              "DbHelper is not initialized. Did you forget to call DbHelper.init(Context context) method?");
		}

		return dbHelper;
	}

	public static void init(Context context) {
		dbHelper = new DbHelper(context);
	}

	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
//        Logger.debug("Database Path : " + context.getDatabasePath(DB_NAME).getAbsolutePath());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		DbMigrationHelper migrationHelper = new DbMigrationHelper(db, context.getApplicationContext());
		OperationResult result = migrationHelper.migrateToDbVersion(1);
		migrationResult = result;

		int currentRequiredVersion = DB_VERSION;

		if (currentRequiredVersion > 1){
			result = migrationHelper.upgradeToVersion(1, currentRequiredVersion);
			migrationResult = result;
		}

		if (migrationResult.getResult() == OperationResult.OPERATION_SUCCESSFUL){
			Logger.info("Successfully created DB with starting version: "
			            + currentRequiredVersion);
			Logger.debug("Successfully created DB with starting version: ");
		} else {
			Logger.info("Failed to create DB with starting version: "
			            + currentRequiredVersion);
			Logger.debug("Failed to create DB with starting version: \"");
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		DbMigrationHelper migrationHelper = new DbMigrationHelper(db, context.getApplicationContext());
		OperationResult result = migrationHelper.upgradeToVersion(oldVersion,
		                                                          newVersion);
		migrationResult = result;

		if (migrationResult.getResult() == OperationResult.OPERATION_SUCCESSFUL){
			Logger.info("Successfully migrated DB with from version: "
			            + oldVersion + " to " + newVersion);
		} else {
			Logger.info("Failed to migrated DB with from version: "
			            + oldVersion + " to " + newVersion);
		}
	}

	public OperationResult getMigrationResult() {
		return migrationResult;
	}

	public synchronized SQLiteDatabase openDatabase() {
		if (mOpenCounter.incrementAndGet() == 1){
			// Opening new database
			mDatabase = dbHelper.getWritableDatabase();

		}

		if (mDatabase == null || (mDatabase != null && !mDatabase.isOpen())){

			mDatabase = dbHelper.getWritableDatabase();

		}

		return mDatabase;
	}

	public synchronized void closeDatabase() {
		if (mOpenCounter.decrementAndGet() == 0){
			// Closing database
			mDatabase.close();

		}
	}

}