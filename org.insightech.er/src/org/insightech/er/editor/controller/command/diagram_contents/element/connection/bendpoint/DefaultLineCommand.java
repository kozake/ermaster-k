package org.insightech.er.editor.controller.command.diagram_contents.element.connection.bendpoint;

import java.util.ArrayList;
import java.util.List;

import org.insightech.er.editor.controller.command.AbstractCommand;
import org.insightech.er.editor.model.ERDiagram;
import org.insightech.er.editor.model.diagram_contents.element.connection.Bendpoint;
import org.insightech.er.editor.model.diagram_contents.element.connection.ConnectionElement;
import org.insightech.er.editor.model.diagram_contents.element.connection.Relation;
import org.insightech.er.editor.model.diagram_contents.element.node.category.Category;

public class DefaultLineCommand extends AbstractCommand {

	private int sourceXp;

	private int sourceYp;

	private int targetXp;

	private int targetYp;

	private ConnectionElement connection;

	private List<Bendpoint> oldBendpointList;
	
	private Category targetCategory;

	public DefaultLineCommand(ERDiagram diagram, ConnectionElement connection) {
		
		this.targetCategory = diagram.getCurrentCategory();
		if (connection instanceof Relation) {
			Relation relation = (Relation) connection;

			this.sourceXp = relation.getSourceXp(targetCategory);
			this.sourceYp = relation.getSourceYp(targetCategory);
			this.targetXp = relation.getTargetXp(targetCategory);
			this.targetYp = relation.getTargetYp(targetCategory);
		}

		this.connection = connection;
		this.oldBendpointList = this.connection.getBendpoints(targetCategory);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		this.connection.setBendpoints(targetCategory, new ArrayList<Bendpoint>());
		if (this.connection instanceof Relation) {
			Relation relation = (Relation) this.connection;

			relation.setSourceLocationp(targetCategory, -1, -1);
			relation.setTargetLocationp(targetCategory, -1, -1);
			// relation.setParentMove();
		}

		this.connection.refreshBendpoint();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doUndo() {
		this.connection.setBendpoints(targetCategory, this.oldBendpointList);
		if (this.connection instanceof Relation) {
			Relation relation = (Relation) this.connection;

			relation.setSourceLocationp(targetCategory, this.sourceXp, this.sourceYp);
			relation.setTargetLocationp(targetCategory, this.targetXp, this.targetYp);
			// relation.setParentMove();
		}

		this.connection.refreshBendpoint();
	}
}
