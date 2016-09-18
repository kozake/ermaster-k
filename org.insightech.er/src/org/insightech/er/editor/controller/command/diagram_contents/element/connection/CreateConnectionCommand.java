package org.insightech.er.editor.controller.command.diagram_contents.element.connection;

import org.insightech.er.editor.model.ERDiagram;
import org.insightech.er.editor.model.diagram_contents.element.connection.ConnectionElement;
import org.insightech.er.editor.model.diagram_contents.element.node.category.Category;
import org.insightech.er.editor.model.settings.Settings;

public class CreateConnectionCommand extends AbstractCreateConnectionCommand {

	private ERDiagram diagram;

	private ConnectionElement connection;
	
	private Settings oldSettings;
	
	public CreateConnectionCommand(ERDiagram diagram, ConnectionElement connection) {
		super();
		this.diagram = diagram;
		this.connection = connection;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		
		Settings settings = diagram.getDiagramContents().getSettings();
		this.oldSettings = settings.clone();
		
		this.connection.setSource(this.getSourceModel());
		this.connection.setTarget(this.getTargetModel());

		for (Category category : settings.getCategorySetting().getAllCategories()) {
			connection.removeCategory(category);
			connection.addCategory(category);
		}

		this.getTargetModel().refreshTargetConnections();
		this.getSourceModel().refreshSourceConnections();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doUndo() {
		this.connection.setSource(null);
		this.connection.setTarget(null);

		diagram.getDiagramContents().setSettings(oldSettings);
		
		this.getTargetModel().refreshTargetConnections();
		this.getSourceModel().refreshSourceConnections();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String validate() {
		return null;
	}

}
