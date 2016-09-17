package org.insightech.er.editor.controller.editpolicy.element.node.note;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.insightech.er.editor.controller.command.diagram_contents.element.node.MoveElementCommand;
import org.insightech.er.editor.controller.command.diagram_contents.element.node.note.NoteEditCommand;
import org.insightech.er.editor.model.ERDiagram;
import org.insightech.er.editor.model.diagram_contents.element.node.category.Category;
import org.insightech.er.editor.model.diagram_contents.element.node.note.Note;

public class NoteDirectEditPolicy extends DirectEditPolicy {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		CompoundCommand command = new CompoundCommand();
		
		String text = (String) request.getCellEditor().getValue();

		Note note = (Note) getHost().getModel();
		NoteEditCommand noteEditCommand = new NoteEditCommand(note, text);
		command.add(noteEditCommand);
		
		ERDiagram diagram = (ERDiagram) this.getHost().getRoot().getContents().getModel();
		Category currentCategory = diagram.getCurrentCategory();
		MoveElementCommand autoResizeCommand = new MoveElementCommand(
				diagram,
				this.getHostFigure().getBounds(),
				note.getX(currentCategory),
				note.getY(currentCategory),
				-1,
				-1,
				note);

		command.add(autoResizeCommand);

		return command.unwrap();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {
	}
}
