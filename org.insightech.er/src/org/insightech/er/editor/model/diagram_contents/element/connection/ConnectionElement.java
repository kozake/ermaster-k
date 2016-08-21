package org.insightech.er.editor.model.diagram_contents.element.connection;

import java.util.List;

import org.insightech.er.editor.model.AbstractModel;
import org.insightech.er.editor.model.diagram_contents.element.node.NodeElement;
import org.insightech.er.editor.model.diagram_contents.element.node.category.Category;
import org.insightech.er.editor.model.diagram_contents.element.node.table.TableView;

public abstract class ConnectionElement extends AbstractModel implements
		Comparable<ConnectionElement> {

	private static final long serialVersionUID = -5418951773059063716L;

	protected NodeElement source;

	protected NodeElement target;
	
	private ConnectionElementLocation location = new ConnectionElementLocation();

	private int[] color;

	public ConnectionElement() {
		this.setColor(0, 0, 0);
	}

	public int compareTo(ConnectionElement other) {
		NodeElement source1 = this.getSource();
		NodeElement source2 = other.getSource();

		if (source1 != source2) {
			if (source1 == null) {
				return 1;
			}
			if (source2 == null) {
				return -1;
			}

			if (!(source1 instanceof TableView)) {
				return 1;
			}
			if (!(source2 instanceof TableView)) {
				return -1;
			}

			TableView tableView1 = (TableView) source1;
			TableView tableView2 = (TableView) source2;

			return tableView1.compareTo(tableView2);
		}

		NodeElement target1 = this.getTarget();
		NodeElement target2 = other.getTarget();

		if (target1 != target2) {
			if (target1 == null) {
				return 1;
			}
			if (target2 == null) {
				return -1;
			}

			if (!(target1 instanceof TableView)) {
				return 1;
			}
			if (!(target2 instanceof TableView)) {
				return -1;
			}

			TableView tableView1 = (TableView) target1;
			TableView tableView2 = (TableView) target2;

			return tableView1.compareTo(tableView2);
		}

		return 0;
	}

	public NodeElement getSource() {
		return source;
	}

	public void setSource(NodeElement source) {
		if (this.source != null) {
			this.source.removeOutgoing(this);
		}

		this.source = source;

		if (this.source != null) {
			this.source.addOutgoing(this);
		}
		Category currentCategory = getCurrentCategory();
		if (currentCategory != null) {
			removeCategory(currentCategory);
			addCategory(currentCategory);
		}
	}

	public void setSourceAndTarget(NodeElement source, NodeElement target) {
		this.source = source;
		this.target = target;
	}

	public void setTarget(NodeElement target) {
		if (this.target != null) {
			this.target.removeIncoming(this);
		}

		this.target = target;

		if (this.target != null) {
			this.target.addIncoming(this);
		}
		Category currentCategory = getCurrentCategory();
		if (currentCategory != null) {
			removeCategory(currentCategory);
			addCategory(currentCategory);
		}
	}

	public NodeElement getTarget() {
		return target;
	}

	public void delete() {
		source.removeOutgoing(this);
		target.removeIncoming(this);
	}

	public void connect() {
		if (this.source != null) {
			source.addOutgoing(this);
		}
		if (this.target != null) {
			target.addIncoming(this);
		}
	}

	public void addBendpoint(int index, Bendpoint point) {
		getCurrentCategoryLocation().addBendpoint(index, point);
	}

	public void setBendpoints(List<Bendpoint> points) {
		getCurrentCategoryLocation().setBendpoints(points);
	}

	public List<Bendpoint> getBendpoints() {
		return getCurrentCategoryLocation().getBendpoints();
	}

	public void removeBendpoint(int index) {
		getCurrentCategoryLocation().getBendpoints().remove(index);
	}

	public void replaceBendpoint(int index, Bendpoint point) {
		getCurrentCategoryLocation().getBendpoints().set(index, point);
	}

	public int getSourceXp() {
		return getCurrentCategoryLocation().getSourceXp();
	}

	public void setSourceLocationp(int sourceXp, int sourceYp) {
		ConnectionElementLocation location = getCurrentCategoryLocation();
		location.setSourceXp(sourceXp);
		location.setSourceYp(sourceYp);
	}

	public int getSourceYp() {
		return getCurrentCategoryLocation().getSourceYp();
	}

	public int getTargetXp() {
		return getCurrentCategoryLocation().getTargetXp();
	}

	public void setTargetLocationp(int targetXp, int targetYp) {
		ConnectionElementLocation location = getCurrentCategoryLocation();
		location.setTargetXp(targetXp);
		location.setTargetYp(targetYp);
	}

	public int getTargetYp() {
		return getCurrentCategoryLocation().getTargetYp();
	}

	public boolean isSourceAnchorMoved() {
		return getSourceXp() != -1;
	}

	public boolean isTargetAnchorMoved() {
		return getTargetXp() != -1;
	}

	public void setColor(int red, int green, int blue) {
		this.color = new int[3];
		this.color[0] = red;
		this.color[1] = green;
		this.color[2] = blue;
	}

	public int[] getColor() {
		return this.color;
	}

	public void refreshBendpoint() {
		if (isUpdateable()) {
			this.firePropertyChange("refreshBendpoint", null, null);
		}
	}

	public void addCategory(Category category) {
		if (category != null
				&& !category.getConnectionLocationMap().containsKey(this)
			    && category.contains(this.source)
			    && category.contains(this.target)) {

			category.putConnectionElementLocation(this, location.clone());
		}
	}
	
	public void removeCategory(Category category) {
		category.removeConnectionElementLocation(this);
	}

	public ConnectionElementLocation getLocation() {
		return location;
	}

	public void setLocation(ConnectionElementLocation location) {
		this.location = location;
	}

	private Category getCurrentCategory() {

		return source != null ? source.getCurrentCategory() : 
			   target != null ? target.getCurrentCategory() : null;
	}
	
	private ConnectionElementLocation getCurrentCategoryLocation() {

		Category currentCategory = getCurrentCategory();
		if (currentCategory != null && !currentCategory.getConnectionLocationMap().containsKey(this)) {
			addCategory(currentCategory);
		}
		
		ConnectionElementLocation currentLocation = null;
		if (currentCategory != null) {
			currentLocation = currentCategory.getConnectionElementLocation(this);
		}

		return currentLocation != null ? currentLocation : location;
	}

	@Override
	public ConnectionElement clone() {
		ConnectionElement clone = (ConnectionElement) super.clone();
		clone.location = location.clone();

		if (this.color != null) {
			clone.color = new int[] { this.color[0], this.color[1],
					this.color[2] };
		}

		return clone;
	}
}
