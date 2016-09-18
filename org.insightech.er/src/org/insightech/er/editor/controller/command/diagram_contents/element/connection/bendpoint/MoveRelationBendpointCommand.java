package org.insightech.er.editor.controller.command.diagram_contents.element.connection.bendpoint;

import org.insightech.er.editor.controller.command.AbstractCommand;
import org.insightech.er.editor.controller.editpart.element.connection.RelationEditPart;
import org.insightech.er.editor.model.ERDiagram;
import org.insightech.er.editor.model.diagram_contents.element.connection.Bendpoint;
import org.insightech.er.editor.model.diagram_contents.element.connection.Relation;
import org.insightech.er.editor.model.diagram_contents.element.node.category.Category;

public class MoveRelationBendpointCommand extends AbstractCommand {

	private Category targetCategory;
	
	private RelationEditPart editPart;

	private Bendpoint bendPoint;

	private Bendpoint oldBendpoint;

	private int index;

	public MoveRelationBendpointCommand(
			ERDiagram diagram, RelationEditPart editPart, int x, int y, int index) {
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
		Relation relation = (Relation) editPart.getModel();
		boolean relative = relation.getBendpoints(targetCategory).get(0).isRelative();

		if (relative) {
			this.oldBendpoint = relation.getBendpoints(targetCategory).get(0);

			this.bendPoint.setRelative(true);

			float rateX = (100f - (bendPoint.getX() / 2)) / 100;
			float rateY = (100f - (bendPoint.getY() / 2)) / 100;

			relation.setSourceLocationp(targetCategory, 100, (int) (100 * rateY));
			relation.setTargetLocationp(targetCategory, (int) (100 * rateX), 100);

			// relation.setParentMove();

			relation.replaceBendpoint(targetCategory, 0, this.bendPoint);

		} else {
			this.oldBendpoint = relation.getBendpoints(targetCategory).get(index);
			relation.replaceBendpoint(targetCategory, index, this.bendPoint);
		}

		if (relation.isSelfRelation()) {
			relation.getSource().refreshVisuals();

		} else {
			relation.refreshBendpoint();

		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doUndo() {
		Relation relation = (Relation) editPart.getModel();
		boolean relative = relation.getBendpoints(targetCategory).get(0).isRelative();

		if (relative) {
			float rateX = (100f - (this.oldBendpoint.getX() / 2)) / 100;
			float rateY = (100f - (this.oldBendpoint.getY() / 2)) / 100;

			relation.setSourceLocationp(targetCategory, 100, (int) (100 * rateY));
			relation.setTargetLocationp(targetCategory, (int) (100 * rateX), 100);

			// relation.setParentMove();

			relation.replaceBendpoint(targetCategory, 0, this.oldBendpoint);

		} else {
			relation.replaceBendpoint(targetCategory, index, this.oldBendpoint);
		}

		relation.refreshBendpoint();

		if (relation.isSelfRelation()) {
			relation.getSource().refreshVisuals();
		}

	}

}
