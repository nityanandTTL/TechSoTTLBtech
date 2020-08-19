package com.thyrocare.btechapp.dao.utils;

import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;

import java.util.List;

// TODO: Auto-generated Javadoc

/**
 * The Class DbConfiguration.
 */
public class DbConfiguration {

	/** The table name. */
	final private String databaseName;

	/** The Database Path. */
	final private String databasePath;

	/** The models. */
	final private List<DbModel> models;
	
	static String TAG = "DB-Config";

	public String getDatabaseName() {
		return databaseName;
	}

	public String getDatabasePath() {
		return databasePath;
	}

	public List<DbModel> getModels() {
		return models;
	}

	/**
	 * Instantiates a new db configuration.
	 */
	private DbConfiguration(final Builder builder) {
		this.databaseName = builder.databaseName;
		this.databasePath = builder.databasePath;
		this.models = builder.models;
		MessageLogger.LogError(TAG, "Database path " + builder.databasePath);
	}

	/**
	 * Builder pattern for setting all configurations
	 */
	public static class Builder {

		/** The table name. */
		private String databaseName;

		/** The Database Path. */
		private String databasePath;

		/** The models. */
		private List<DbModel> models;

		/**
		 * Sets the table name.
		 * 
		 *  the new table name
		 */
		public Builder setDatabaseName(String databaseName) {
			this.databaseName = databaseName;
			return this;
		}

		public Builder setDatabasePath(String databasePath) {
			this.databasePath = databasePath;
			return this;
		}

		/**
		 * Sets the models.
		 * 
		 * @param models
		 *            the new models
		 */
		public Builder setModels(List<DbModel> models) {
			this.models = models;
			
			MessageLogger.LogDebug(TAG, "table list size  " + models.size());
			return this;
		}

		/**
		 * Builds configuration for database and returns object.
		 * 
		 * @return the DbConfiguration object for specified configuration
		 */
		public DbConfiguration build() {
			MessageLogger.LogDebug(TAG, "config build ");
			return new DbConfiguration(this);

		}
	}

}
