package org.insightech.er.editor.controller.command.diagram_contents.element.connection.bendpoint;

import org.insightech.er.editor.controller.command.AbstractCommand;
import org.insightech.er.editor.model.diagram_contents.element.connection.Bendpoint;
import org.insightech.er.editor.model.diagram_contents.element.connection.ConnectionElement;
import org.insightech.er.editor.model.diagram_contents.element.node.category.Category;

public class CreateBendpointCommand extends AbstractCommand {

	private Category category;
	
	private ConnectionElement connection;

	int x;

	int y;

	private int index;

	public CreateBendpointCommand(Category category, ConnectionElement connection, int x, int y,
			int index) {
		this.category = category;
		this.connection = connection;
		this.x = x;
		this.y = y;
		this.index = index;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		Bendpoint bendpoint = new Bendpoint(this.x, this.y);
		connection.addBendpoint(category, index, bendpoint);
		
		connection.refreshBendpoint();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doUndo() {
		connection.removeBendpoint(category, index);
		
		connection.refreshBendpoint();
	}
}
