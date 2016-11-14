package org.insightech.er.db.impl.as400;

import org.insightech.er.db.impl.db2.DB2TableImportManager;

public class AS400TableImportManager extends DB2TableImportManager {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getViewDefinitionSQL(String schema) {
		return null;
	}
}
