package org.insightech.er.editor.controller.command.diagram_contents.element.connection.relation;

import org.insightech.er.editor.controller.command.AbstractCommand;
import org.insightech.er.editor.model.ERDiagram;
import org.insightech.er.editor.model.diagram_contents.element.connection.ConnectionElement;
import org.insightech.er.editor.model.diagram_contents.element.node.category.Category;

public class ReconnectTargetCommand extends AbstractCommand {

	private Category targetCategory;
	
	private ConnectionElement connection;

	int xp;

	int yp;

	int oldXp;

	int oldYp;

	public ReconnectTargetCommand(ERDiagram diagram, ConnectionElement connection, int xp, int yp) {
		this.targetCategory = diagram.getCurrentCategory();
		this.connection = connection;

		this.xp = xp;
		this.yp = yp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		this.oldXp = this.connection.getTargetXp(targetCategory);
		this.oldYp = this.connection.getTargetYp(targetCategory);

		this.connection.setTargetLocationp(targetCategory, this.xp, this.yp);
		this.connection.refreshVisuals();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doUndo() {
		this.connection.setTargetLocationp(targetCategory, this.oldXp, this.oldYp);
		this.connection.refreshVisuals();
	}
}
