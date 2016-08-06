package org.insightech.er.editor.model.diagram_contents.element.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.insightech.er.editor.model.ERDiagram;
import org.insightech.er.editor.model.ObjectModel;
import org.insightech.er.editor.model.ViewableModel;
import org.insightech.er.editor.model.diagram_contents.element.connection.ConnectionElement;
import org.insightech.er.editor.model.diagram_contents.element.node.category.Category;

public abstract class NodeElement extends ViewableModel implements ObjectModel {

	private static final long serialVersionUID = -5143984125818569247L;

	private Location location;
	
	private Map<Category, Location> categoryLocationMap = new HashMap<Category, Location>();

	private Location actualLocation;

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

	public Category getCurrentCategory() {
		return this.diagram != null ? diagram.getCurrentCategory() : null;
	}

	public int getX() {
		return this.getLocationInCurrentCategory().x;
	}

	public int getY() {
		return this.getLocationInCurrentCategory().y;
	}

	public int getWidth() {
		return this.getLocationInCurrentCategory().width;
	}

	public int getHeight() {
		return this.getLocationInCurrentCategory().height;
	}

	public void setLocation(Location location) {
		Category category = getCurrentCategory();
		if (category != null && category.contains(this)) {
			categoryLocationMap.put(category, location);
		} else {
			this.location = location;
		}
	}

	public Location getLocation() {
		Location location = getLocationInCurrentCategory();
		return new Location(location.x, location.y, location.width, location.height);
	}

	public Location getLocation(Category category) {
		if (category != null && category.contains(this)) {
			Location location = categoryLocationMap.get(category);
			if (location!= null) {
				return location;
			}
		}
		return this.location;
	}

	public Location getLocationInAll() {
		return new Location(this.location.x, this.location.y, this.location.width, this.location.height);
	}

	private Location getLocationInCurrentCategory() {
		return getLocation(getCurrentCategory());
	}

	public Location getActualLocation() {
		return actualLocation;
	}

	public void setActualLocation(Location actualLocation) {
		this.actualLocation = actualLocation;
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
		categoryLocationMap.put(category, getLocation());
	}
	
	public void removeCategory(Category category) {
		categoryLocationMap.remove(category);
	}
	
	public Map<Category, Location> getCategoryLocationMap() {
		return categoryLocationMap;
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
