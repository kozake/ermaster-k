package org.insightech.er.editor.controller.command.diagram_contents.element.connection.relation;

import org.eclipse.gef.EditPart;
import org.insightech.er.editor.model.ERDiagram;
import org.insightech.er.editor.model.diagram_contents.element.connection.Bendpoint;
import org.insightech.er.editor.model.diagram_contents.element.connection.Relation;
import org.insightech.er.editor.model.diagram_contents.element.node.category.Category;
import org.insightech.er.editor.model.diagram_contents.element.node.table.ERTable;
import org.insightech.er.editor.model.settings.Settings;

public class CreateSelfRelationCommand extends AbstractCreateRelationCommand {

	private Relation relation;
	
	private ERDiagram diagram;

	private Category targetCategory;
	
	private Settings oldSettings;

	public CreateSelfRelationCommand(Relation relation) {
		super();
		this.relation = relation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSource(EditPart source) {
		this.source = source;
		this.target = source;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		// ERDiagramEditPart.setUpdateable(false);

		boolean anotherSelfRelation = false;

		ERTable sourceTable = (ERTable) this.source.getModel();
		this.diagram = sourceTable.getDiagram();
		this.targetCategory = diagram.getCurrentCategory();

		for (Relation otherRelation : sourceTable.getOutgoingRelations()) {
			if (otherRelation.getSource() == otherRelation.getTarget()) {
				anotherSelfRelation = true;
				break;
			}
		}

		int rate = 0;

		if (anotherSelfRelation) {
			rate = 50;

		} else {
			rate = 100;
		}

		Bendpoint bendpoint0 = new Bendpoint(rate, rate);
		bendpoint0.setRelative(true);

		int xp = 100 - (rate / 2);
		int yp = 100 - (rate / 2);

		relation.setSourceLocationp(targetCategory, 100, yp);
		relation.setTargetLocationp(targetCategory, xp, 100);

		relation.addBendpoint(targetCategory, 0, bendpoint0);

		relation.setSource((ERTable) sourceTable);

		// ERDiagramEditPart.setUpdateable(true);

		relation.setTargetTableView((ERTable) this.target.getModel());

		// sourceTable.setDirty();

		Settings settings = this.diagram.getDiagramContents().getSettings();
		this.oldSettings = settings.clone();

		for (Category category : settings.getCategorySetting().getAllCategories()) {
			this.relation.removeCategory(category);
			this.relation.addCategory(category);
		}

		this.getTargetModel().refresh();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doUndo() {
		// ERDiagramEditPart.setUpdateable(false);

		relation.setSource(null);

		// ERDiagramEditPart.setUpdateable(true);

		relation.setTargetTableView(null);

		this.relation.removeBendpoint(this.targetCategory, 0);
		this.diagram.getDiagramContents().setSettings(this.oldSettings);

		// ERTable targetTable = (ERTable) this.target.getModel();
		// targetTable.setDirty();

		this.getTargetModel().refresh();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canExecute() {
		return source != null && target != null;
	}

}
