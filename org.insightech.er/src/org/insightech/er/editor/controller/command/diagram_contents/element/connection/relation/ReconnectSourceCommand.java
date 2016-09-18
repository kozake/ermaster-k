package org.insightech.er.editor.controller.command.diagram_contents.element.connection.relation;

import org.insightech.er.editor.controller.command.AbstractCommand;
import org.insightech.er.editor.model.ERDiagram;
import org.insightech.er.editor.model.diagram_contents.element.connection.ConnectionElement;
import org.insightech.er.editor.model.diagram_contents.element.node.category.Category;

public class ReconnectSourceCommand extends AbstractCommand {

	private Category targetCategory;
	
	private ConnectionElement connection;

	int xp;

	int yp;

	int oldXp;

	int oldYp;

	public ReconnectSourceCommand(ERDiagram diagram, ConnectionElement connection, int xp, int yp) {
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
		this.oldXp = this.connection.getSourceXp(this.targetCategory);
		this.oldYp = this.connection.getSourceYp(this.targetCategory);

		this.connection.setSourceLocationp(this.targetCategory, this.xp, this.yp);
		this.connection.refreshVisuals();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doUndo() {
		this.connection.setSourceLocationp(this.targetCategory, this.oldXp, this.oldYp);
		this.connection.refreshVisuals();
	}

}
