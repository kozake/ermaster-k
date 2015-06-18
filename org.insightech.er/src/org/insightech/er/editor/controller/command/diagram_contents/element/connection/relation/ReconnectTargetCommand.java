package org.insightech.er.editor.controller.command.diagram_contents.element.connection.relation;

import org.insightech.er.editor.controller.command.AbstractCommand;
import org.insightech.er.editor.model.diagram_contents.element.connection.ConnectionElement;

public class ReconnectTargetCommand extends AbstractCommand {

	private ConnectionElement connection;

	int xp;

	int yp;

	int oldXp;

	int oldYp;

	public ReconnectTargetCommand(ConnectionElement connection, int xp, int yp) {
		this.connection = connection;

		this.xp = xp;
		this.yp = yp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		this.oldXp = this.connection.getTargetXp();
		this.oldYp = this.connection.getTargetYp();

		this.connection.setTargetLocationp(this.xp, this.yp);
		this.connection.refreshVisuals();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doUndo() {
		this.connection.setTargetLocationp(this.oldXp, this.oldYp);
		this.connection.refreshVisuals();
	}
}
