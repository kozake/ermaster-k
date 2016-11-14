package org.insightech.er.db.impl.as400;

import org.insightech.er.db.impl.db2.DB2EclipseDBManager;

public class AS400EclipseDBManager extends DB2EclipseDBManager {

	@Override
	public String getId() {
		return AS400DBManager.ID;
	}
}
