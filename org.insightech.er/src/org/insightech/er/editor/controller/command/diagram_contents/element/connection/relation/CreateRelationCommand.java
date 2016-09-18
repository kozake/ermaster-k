package org.insightech.er.editor.controller.command.diagram_contents.element.connection.relation;

import java.util.List;

import org.insightech.er.editor.model.diagram_contents.element.connection.Relation;
import org.insightech.er.editor.model.diagram_contents.element.node.category.Category;
import org.insightech.er.editor.model.diagram_contents.element.node.table.TableView;
import org.insightech.er.editor.model.diagram_contents.element.node.table.column.NormalColumn;
import org.insightech.er.editor.model.settings.Settings;

public class CreateRelationCommand extends AbstractCreateRelationCommand {

	private Relation relation;

	private List<NormalColumn> foreignKeyColumnList;

	private Settings oldSettings;

	public CreateRelationCommand(Relation relation) {
		this(relation, null);
	}

	public CreateRelationCommand(Relation relation,
			List<NormalColumn> foreignKeyColumnList) {
		super();
		this.relation = relation;
		this.foreignKeyColumnList = foreignKeyColumnList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		// ERDiagramEditPart.setUpdateable(false);
		
		Settings settings = ((TableView) source.getModel()).getDiagram().getDiagramContents().getSettings();
		this.oldSettings = settings.clone();

		this.relation.setSource((TableView) source.getModel());

		for (Category category : settings.getCategorySetting().getAllCategories()) {
			this.relation.removeCategory(category);
			this.relation.addCategory(category);
		}

		// ERDiagramEditPart.setUpdateable(true);

		this.relation.setTargetTableView((TableView) target.getModel(),
				this.foreignKeyColumnList);

		this.getTargetModel().refresh();
		this.getSourceModel().refreshSourceConnections();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doUndo() {
		// ERDiagramEditPart.setUpdateable(false);

		this.relation.setSource(null);

		((TableView) source.getModel()).getDiagram().getDiagramContents().setSettings(this.oldSettings);

		// ERDiagramEditPart.setUpdateable(true);

		this.relation.setTargetTableView(null);

		this.getTargetModel().refresh();
		this.getSourceModel().refreshSourceConnections();
	}
}
