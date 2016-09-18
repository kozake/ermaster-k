package org.insightech.er.editor.controller.command.diagram_contents.element.connection.bendpoint;

import org.insightech.er.editor.controller.command.AbstractCommand;
import org.insightech.er.editor.model.ERDiagram;
import org.insightech.er.editor.model.diagram_contents.element.connection.Bendpoint;
import org.insightech.er.editor.model.diagram_contents.element.connection.ConnectionElement;
import org.insightech.er.editor.model.diagram_contents.element.node.category.Category;

public class DeleteBendpointCommand extends AbstractCommand {

	private Category targetCategory;
	
	private ConnectionElement connection;

	private Bendpoint oldBendpoint;

	private int index;

	public DeleteBendpointCommand(ERDiagram diagram, ConnectionElement connection, int index) {
		this.targetCategory = diagram.getCurrentCategory();
		this.connection = connection;
		this.index = index;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		this.oldBendpoint = this.connection.getBendpoints(targetCategory).get(index);
		this.connection.removeBendpoint(targetCategory, index);
		
		this.connection.refreshBendpoint();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doUndo() {
		this.connection.addBendpoint(targetCategory, index, oldBendpoint);
		
		this.connection.refreshBendpoint();
	}
}
