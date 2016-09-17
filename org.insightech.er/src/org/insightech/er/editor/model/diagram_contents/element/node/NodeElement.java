package org.insightech.er.editor.model.diagram_contents.element.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.insightech.er.editor.model.ERDiagram;
import org.insightech.er.editor.model.ObjectModel;
import org.insightech.er.editor.model.ViewableModel;
import org.insightech.er.editor.model.diagram_contents.element.connection.ConnectionElement;
import org.insightech.er.editor.model.diagram_contents.element.node.category.Category;

public abstract class NodeElement extends ViewableModel implements ObjectModel {

	private static final long serialVersionUID = -5143984125818569247L;

	private Location location;
	
	private List<ConnectionElement> incomings = new ArrayList<ConnectionElement>();

	private List<ConnectionElement> outgoings = new ArrayList<ConnectionElement>();

	private ERDiagram diagram;

	public NodeElement() {
		this.location = new Location(0, 0, 0, 0);
	}

	public void setDiagram(ERDiagram diagram) {
		this.diagram = diagram;
	}

	public ERDiagram getDiagram() {
		return diagram;
	}

	public int getX(Category category) {
		return this.getLocationInternal(category).x;
	}

	public int getY(Category category) {
		return this.getLocationInternal(category).y;
	}

	public int getWidth(Category category) {
		return this.getLocationInternal(category).width;
	}

	public int getHeight(Category category) {
		return this.getLocationInternal(category).height;
	}

	public void setLocation(Category category, Location location) {
		if (category != null && category.contains(this)) {
			category.putNodeLocation(this, location);
		} else {
			this.location = location;
		}
	}

	public Location getLocation(Category category) {
		Location location = getLocationInternal(category);
		return new Location(location.x, location.y, location.width, location.height);
	}

	protected Location getLocationInternal(Category category) {
		if (category != null && category.contains(this)) {
			Location location = category.getNodeLocation(this);
			if (location!= null) {
				return location;
			}
		}
		return this.location;
	}

	public Location getLocationInAll() {
		return new Location(this.location.x, this.location.y, this.location.width, this.location.height);
	}

	public List<ConnectionElement> getIncomings() {
		return incomings;
	}

	public List<ConnectionElement> getOutgoings() {
		return outgoings;
	}

	public void setIncoming(List<ConnectionElement> relations) {
		this.incomings = relations;
	}

	public void setOutgoing(List<ConnectionElement> relations) {
		this.outgoings = relations;
	}

	public void addIncoming(ConnectionElement relation) {
		this.incomings.add(relation);
	}

	public void removeIncoming(ConnectionElement relation) {
		this.incomings.remove(relation);
	}

	public void addOutgoing(ConnectionElement relation) {
		this.outgoings.add(relation);
	}

	public void removeOutgoing(ConnectionElement relation) {
		this.outgoings.remove(relation);
	}

	public List<NodeElement> getReferringElementList() {
		List<NodeElement> referringElementList = new ArrayList<NodeElement>();

		for (ConnectionElement connectionElement : this.getOutgoings()) {
			NodeElement targetElement = connectionElement.getTarget();

			referringElementList.add(targetElement);
		}

		return referringElementList;
	}

	public List<NodeElement> getReferedElementList() {
		List<NodeElement> referedElementList = new ArrayList<NodeElement>();

		for (ConnectionElement connectionElement : this.getIncomings()) {
			NodeElement sourceElement = connectionElement.getSource();

			referedElementList.add(sourceElement);
		}

		return referedElementList;
	}

	public void refreshSourceConnections() {
		if (isUpdateable()) {
			this.firePropertyChange("refreshSourceConnections", null, null);
		}
	}

	public void refreshTargetConnections() {
		if (isUpdateable()) {
			this.firePropertyChange("refreshTargetConnections", null, null);
		}
	}

	public void refreshCategory() {
		if (isUpdateable()) {
			if (this.diagram != null) {
				for (Category category : this.diagram.getDiagramContents()
						.getSettings().getCategorySetting()
						.getSelectedCategories()) {
					if (category.contains(this)) {
						category.refreshVisuals();
					}
				}
			}
		}
	}

	public void sortRelations() {
		Collections.sort(this.incomings);
	}
	

	public void addCategory(Category category) {
		category.putNodeLocation(this, getLocation(null));
		for (ConnectionElement conn : incomings) {
			conn.addCategory(category);
		}
		for (ConnectionElement conn : outgoings) {
			conn.addCategory(category);
		}
	}
	
	public void removeCategory(Category category) {
		category.removeNodeLocation(this);
		for (ConnectionElement conn : incomings) {
			conn.removeCategory(category);
		}
		for (ConnectionElement conn : outgoings) {
			conn.removeCategory(category);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeElement clone() {
		NodeElement clone = (NodeElement) super.clone();

		clone.location = this.location.clone();
		clone.setIncoming(new ArrayList<ConnectionElement>());
		clone.setOutgoing(new ArrayList<ConnectionElement>());

		return clone;
	}

}
