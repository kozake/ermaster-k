package org.insightech.er.editor.controller.command.diagram_contents.element.connection.bendpoint;

import org.eclipse.gef.ConnectionEditPart;
import org.insightech.er.editor.controller.command.AbstractCommand;
import org.insightech.er.editor.model.ERDiagram;
import org.insightech.er.editor.model.diagram_contents.element.connection.Bendpoint;
import org.insightech.er.editor.model.diagram_contents.element.connection.ConnectionElement;
import org.insightech.er.editor.model.diagram_contents.element.node.category.Category;

public class MoveBendpointCommand extends AbstractCommand {

	private Category targetCategory;
	
	private ConnectionEditPart editPart;

	private Bendpoint bendPoint;

	private Bendpoint oldBendpoint;

	private int index;

	public MoveBendpointCommand(ERDiagram diagram, ConnectionEditPart editPart, int x, int y,
			int index) {
		this.targetCategory = diagram.getCurrentCategory();
		this.editPart = editPart;
		this.bendPoint = new Bendpoint(x, y);
		this.index = index;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		ConnectionElement connection = (ConnectionElement) editPart.getModel();

		this.oldBendpoint = connection.getBendpoints(targetCategory).get(index);
		connection.replaceBendpoint(targetCategory, index, this.bendPoint);
		
		connection.refreshBendpoint();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doUndo() {
		ConnectionElement connection = (ConnectionElement) editPart.getModel();
		connection.replaceBendpoint(targetCategory, index, this.oldBendpoint);
		
		connection.refreshBendpoint();
	}

}
