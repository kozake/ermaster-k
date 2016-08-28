package org.insightech.er.editor.view.action.category;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Event;
import org.insightech.er.editor.ERDiagramEditor;
import org.insightech.er.editor.controller.command.diagram_contents.element.node.category.AddRemoveCategoryCommand;
import org.insightech.er.editor.controller.editpart.element.node.NodeElementEditPart;
import org.insightech.er.editor.model.ERDiagram;
import org.insightech.er.editor.model.diagram_contents.element.node.NodeElement;
import org.insightech.er.editor.model.diagram_contents.element.node.category.Category;
import org.insightech.er.editor.model.settings.Settings;
import org.insightech.er.editor.view.action.AbstractBaseAction;

public class CategoryAddRemoveAction extends AbstractBaseAction {

	public static final String ID = CategoryAddRemoveAction.class.getName();
	
	private final Category category;
	
	private final List<NodeElement> nodeElements;
	
	private final boolean containsAll;

	public CategoryAddRemoveAction(ERDiagramEditor editor, Category category) {
		super(ID, category.getName(), editor);
		
		this.category = category;
		
		this.nodeElements = this.getSelectedNodeElements(editor);
		
		int containsNum = 0;
		for (NodeElement nodeElement : nodeElements) {
			if (category.contains(nodeElement)) {
				containsNum++;
			}
		}
		
		this.containsAll = containsNum != 0;
		this.setChecked(containsAll);
		this.setEnabled(nodeElements.size() != 0 && (containsNum == 0 || containsNum == nodeElements.size()));
	}

	private List<NodeElement> getSelectedNodeElements(final ERDiagramEditor editor) {
		List<NodeElement> nodeElements = new ArrayList<NodeElement>();
		
		GraphicalViewer viewer = (GraphicalViewer) editor
				.getAdapter(GraphicalViewer.class);

		ISelection selection = viewer.getSelection();
		if (selection instanceof IStructuredSelection) {
			List selectedObjects = ((IStructuredSelection) selection).toList();
			for (Object selected : selectedObjects) {
				if (selected instanceof NodeElementEditPart) {
					NodeElement nodeElement = (NodeElement) ((NodeElementEditPart) selected).getModel();
					if (!(nodeElement instanceof Category)) {
						nodeElements.add(nodeElement);
					}
				}
			}
		}
		
		return nodeElements;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Event event) {
		ERDiagram diagram = this.getDiagram();

		Settings settings = (Settings) diagram.getDiagramContents()
				.getSettings().clone();
		
		settings.getCategorySetting().getAllCategories();

		AddRemoveCategoryCommand command = new AddRemoveCategoryCommand(diagram, category, nodeElements, !containsAll);
		this.execute(command);
	}

}
