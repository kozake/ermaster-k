package org.insightech.er.editor.controller.command.diagram_contents.element.node.category;

import java.util.List;

import org.insightech.er.editor.controller.command.AbstractCommand;
import org.insightech.er.editor.model.ERDiagram;
import org.insightech.er.editor.model.diagram_contents.element.node.NodeElement;
import org.insightech.er.editor.model.diagram_contents.element.node.category.Category;

public class AddRemoveCategoryCommand extends AbstractCommand {

	private ERDiagram diagram;

	private Category category;
	private Category backup;

	private List<NodeElement> elements;
	
	private boolean add;

	public AddRemoveCategoryCommand(ERDiagram diagram, Category category, List<NodeElement> elements, boolean add) {

		this.diagram = diagram;
		this.category = category;
		this.elements = elements;
		this.add = add;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		this.backup = category.clone();
		if (add) {
			for (NodeElement element : elements) {
				category.add(element);
			}
		} else {
			for (NodeElement element : elements) {
				category.remove(element);
			}
		}
			
		this.diagram.refreshSettings();
		this.diagram.getEditor().refreshPropertySheet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doUndo() {
		this.diagram.getDiagramContents().getSettings().getCategorySetting().replaceCategory(category, backup);
		this.diagram.refreshSettings();
		this.diagram.getEditor().refreshPropertySheet();
	}
}
