package org.insightech.er.editor.controller.command.diagram_contents.element.node.category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.Rectangle;
import org.insightech.er.editor.controller.command.diagram_contents.element.node.MoveElementCommand;
import org.insightech.er.editor.model.ERDiagram;
import org.insightech.er.editor.model.diagram_contents.element.connection.Bendpoint;
import org.insightech.er.editor.model.diagram_contents.element.connection.ConnectionElement;
import org.insightech.er.editor.model.diagram_contents.element.node.Location;
import org.insightech.er.editor.model.diagram_contents.element.node.NodeElement;
import org.insightech.er.editor.model.diagram_contents.element.node.category.Category;

public class MoveCategoryCommand extends MoveElementCommand {

	private boolean move;

	private List<NodeElement> nodeElementList;

	private Map<NodeElement, Rectangle> nodeElementOldLocationMap;

	private Category category;

	private int diffX;

	private int diffY;

	private Map<ConnectionElement, List<Bendpoint>> bendpointListMap;

	public MoveCategoryCommand(ERDiagram diagram, int x, int y, int width,
			int height, Category category, List<Category> otherCategories,
			boolean move) {
		super(diagram, null, x, y, width, height, category);

		this.nodeElementList = new ArrayList<NodeElement>(
				category.getContents());
		this.category = category;
		this.move = move;

		if (!this.move) {
			for (NodeElement nodeElement : this.nodeElementList) {
				int nodeElementX = nodeElement.getX(null);
				int nodeElementY = nodeElement.getY(null);
				int nodeElementWidth = nodeElement.getWidth(null);
				int nodeElementHeight = nodeElement.getHeight(null);

				if (x > nodeElementX) {
					nodeElementWidth += x - nodeElementX;
					x = nodeElementX;
				}
				if (y > nodeElementY) {
					nodeElementHeight += y - nodeElementY;
					y = nodeElementY;
				}

				if (nodeElementX - x + nodeElementWidth > width) {
					width = nodeElementX - x + nodeElementWidth;
				}

				if (nodeElementY - y + nodeElementHeight > height) {
					height = nodeElementY - y + nodeElementHeight;
				}

			}

			this.setNewRectangle(x, y, width, height);

		} else {
			this.nodeElementOldLocationMap = new HashMap<NodeElement, Rectangle>();
			this.diffX = x - category.getX();
			this.diffY = y - category.getY();

			for (Iterator<NodeElement> iter = this.nodeElementList.iterator(); iter
					.hasNext();) {
				NodeElement nodeElement = iter.next();
				for (Category otherCategory : otherCategories) {
					if (otherCategory.contains(nodeElement)) {
						iter.remove();
						break;
					}
				}
			}

			for (NodeElement nodeElement : this.nodeElementList) {
				this.nodeElementOldLocationMap
						.put(nodeElement, new Rectangle(nodeElement.getX(null),
								nodeElement.getY(null), nodeElement.getWidth(null),
								nodeElement.getHeight(null)));
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		if (this.move) {
			this.bendpointListMap = new HashMap<ConnectionElement, List<Bendpoint>>();

			for (NodeElement nodeElement : this.nodeElementList) {
				nodeElement.setLocation(null,
						new Location(
								nodeElement.getX(null) + diffX,
								nodeElement.getY(null) + diffY,
								nodeElement.getWidth(null),
								nodeElement.getHeight(null)));
				this.moveBendpoints(nodeElement);

				nodeElement.refreshVisuals();
				for (ConnectionElement connectionElement : this.bendpointListMap
						.keySet()) {
					connectionElement.refreshBendpoint();
				}
			}

		}

		super.doExecute();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doUndo() {
		if (this.move) {
			for (NodeElement nodeElement : this.nodeElementList) {
				Rectangle rectangle = this.nodeElementOldLocationMap
						.get(nodeElement);
				nodeElement.setLocation(null, 
						new Location(
								rectangle.x,
								rectangle.y,
								rectangle.width,
								rectangle.height));

				nodeElement.refreshVisuals();
			}

			this.restoreBendpoints();

			for (ConnectionElement connectionElement : this.bendpointListMap
					.keySet()) {
				connectionElement.refreshBendpoint();
			}

		}

		super.doUndo();
	}

	private void moveBendpoints(NodeElement source) {
		for (ConnectionElement connectionElement : source.getOutgoings()) {
			NodeElement target = connectionElement.getTarget();

			if (this.category.contains(target)) {
				List<Bendpoint> bendpointList = connectionElement
						.getBendpoints(null);

				List<Bendpoint> oldBendpointList = new ArrayList<Bendpoint>();

				for (int index = 0; index < bendpointList.size(); index++) {
					Bendpoint oldBendPoint = bendpointList.get(index);

					if (oldBendPoint.isRelative()) {
						break;
					}

					Bendpoint newBendpoint = new Bendpoint(oldBendPoint.getX()
							+ this.diffX, oldBendPoint.getY() + this.diffY);
					connectionElement.replaceBendpoint(null, index, newBendpoint);

					oldBendpointList.add(oldBendPoint);
				}

				this.bendpointListMap.put(connectionElement, oldBendpointList);
			}
		}
	}

	private void restoreBendpoints() {
		for (ConnectionElement connectionElement : this.bendpointListMap
				.keySet()) {
			List<Bendpoint> oldBendpointList = this.bendpointListMap
					.get(connectionElement);

			for (int index = 0; index < oldBendpointList.size(); index++) {
				connectionElement.replaceBendpoint(null, index,
						oldBendpointList.get(index));
			}
		}
	}
}
