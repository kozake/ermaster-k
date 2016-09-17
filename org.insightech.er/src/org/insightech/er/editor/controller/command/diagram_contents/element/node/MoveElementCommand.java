package org.insightech.er.editor.controller.command.diagram_contents.element.node;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.geometry.Rectangle;
import org.insightech.er.editor.controller.command.AbstractCommand;
import org.insightech.er.editor.model.ERDiagram;
import org.insightech.er.editor.model.diagram_contents.element.node.Location;
import org.insightech.er.editor.model.diagram_contents.element.node.NodeElement;
import org.insightech.er.editor.model.diagram_contents.element.node.category.Category;

public class MoveElementCommand extends AbstractCommand {

	protected int x;

	protected int oldX;

	protected int y;

	protected int oldY;

	protected int width;

	protected int oldWidth;

	protected int height;

	protected int oldHeight;

	private NodeElement element;

	private Map<Category, Location> oldCategoryLocationMap;

	private Map<Category, Location> newCategoryLocationMap;

	protected ERDiagram diagram;
	
	private Category targetCategory;

	private Rectangle bounds;

	public MoveElementCommand(ERDiagram diagram, Rectangle bounds, int x,
			int y, int width, int height, NodeElement element) {

		this.element = element;
		this.setNewRectangle(x, y, width, height);

		this.targetCategory = diagram.getCurrentCategory();
		
		this.oldX = element.getX(targetCategory);
		this.oldY = element.getY(targetCategory);
		this.oldWidth = element.getWidth(targetCategory);
		this.oldHeight = element.getHeight(targetCategory);

		this.bounds = bounds;
		this.diagram = diagram;

		this.oldCategoryLocationMap = new HashMap<Category, Location>();
		this.newCategoryLocationMap = new HashMap<Category, Location>();
	}

	protected void setNewRectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	private void initCategory(ERDiagram diagram, Location elementLocation) {
		for (Category category : diagram.getDiagramContents().getSettings()
				.getCategorySetting().getSelectedCategories()) {
			if (category.contains(element)) {

				Location newCategoryLocation = category
						.getNewCategoryLocation(elementLocation);

				if (newCategoryLocation != null) {
					this.newCategoryLocationMap.put(category,
							newCategoryLocation);
					this.oldCategoryLocationMap.put(category,
							category.getLocation());
				}

			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		if (this.bounds != null && targetCategory == null) {
			Location elementLocation = new Location(x, y, bounds.width,
					bounds.height);

			if (elementLocation.width < width) {
				elementLocation.width = width;
			}
			if (elementLocation.height < height) {
				elementLocation.height = height;
			}

			this.initCategory(diagram, elementLocation);
		}

		for (Category category : this.newCategoryLocationMap.keySet()) {
			category.setLocation(this.newCategoryLocationMap.get(category));
			category.refreshVisuals();
		}

		this.element.setLocation(targetCategory, new Location(x, y, width, height));

		this.element.refreshVisuals();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doUndo() {
		this.element.setLocation(targetCategory, new Location(oldX, oldY, oldWidth, oldHeight));
		this.element.refreshVisuals();

		for (Category category : this.oldCategoryLocationMap.keySet()) {
			category.setLocation(this.oldCategoryLocationMap.get(category));
			category.refreshVisuals();
		}
	}
}
