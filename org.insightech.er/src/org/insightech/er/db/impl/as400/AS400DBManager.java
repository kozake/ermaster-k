package org.insightech.er.db.impl.as400;

import org.insightech.er.db.impl.db2.DB2DBManager;
import org.insightech.er.editor.model.dbimport.ImportFromDBManager;
import org.insightech.er.editor.model.dbimport.PreImportFromDBManager;

public class AS400DBManager extends DB2DBManager {

	public static final String ID = "AS400";

	@Override
	public String getId() {
		return ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDriverClassName() {
		return "com.ibm.as400.access.AS400JDBCDriver";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getURL() {
		return "jdbc:as400://<SERVER NAME>/JDBC";
	}

	@Override
	public ImportFromDBManager getTableImportManager() {
		return new AS400TableImportManager();
	}

	@Override
	public PreImportFromDBManager getPreTableImportManager() {
		return new AS400PreTableImportManager();
	}
}
