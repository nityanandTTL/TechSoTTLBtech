package com.thyrocare.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DbMigrationHelper {

	public static final String DB_MIGRATION_FILE_PREFIX = "dhb_db_version_";
	public static final String DB_MIGRATION_FILE_SUFFIX = ".sql";
    private Context context;

	private SQLiteDatabase writableDb;

	public DbMigrationHelper(SQLiteDatabase writableDb, Context context) {
		this.writableDb = writableDb;
        this.context=context;
	}

	private String fileNameForVersion(int version) {
		String path = "" + DB_MIGRATION_FILE_PREFIX + version
				+ DB_MIGRATION_FILE_SUFFIX;
		return path;
	}

	public OperationResult migrateToDbVersion(int version) {

		try {
			InputStream inputStream = context.getAssets().open(fileNameForVersion(version));

			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				inputLine = inputLine.trim();

				if (inputLine.length() == 0) {
					continue;
				}
				try {
					writableDb.execSQL(inputLine);
				} catch (SQLException sqle) {
					sqle.printStackTrace();
					return new OperationResult(
							OperationResult.OPERATION_FAILED,
							"Unexpected Database Error.");
				}
			}

			in.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return new OperationResult(OperationResult.OPERATION_FAILED,
					"Unexpected IO Error.");
		}

		return new OperationResult(OperationResult.OPERATION_SUCCESSFUL);
	}

	public OperationResult upgradeToVersion(int oldVersion, int newVersion) {
		OperationResult result = null;
		for (int i = oldVersion + 1; i <= newVersion; i++) {
			result = migrateToDbVersion(i);

			// Break the migration even if one operation fails.
			if (result.getResult() != OperationResult.OPERATION_SUCCESSFUL) {
				return result;
			}
		}

		return result;
	}

}
