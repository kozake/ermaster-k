package org.insightech.er.editor.model.diagram_contents.element.connection;

import java.util.ArrayList;
import java.util.List;

import org.insightech.er.editor.model.AbstractModel;

public final class ConnectionElementLocation extends AbstractModel {

	private static final long serialVersionUID = 8277325283591345735L;

	private int sourceXp;

	private int sourceYp;

	private int targetXp;

	private int targetYp;

	private List<Bendpoint> bendpoints = new ArrayList<Bendpoint>();

	public ConnectionElementLocation() {
		this.sourceXp = -1;
		this.sourceYp = -1;
		this.targetXp = -1;
		this.targetYp = -1;
	}
	
	public int getSourceXp() {
		return sourceXp;
	}

	public void setSourceXp(int sourceXp) {
		this.sourceXp = sourceXp;
	}

	public int getSourceYp() {
		return sourceYp;
	}

	public void setSourceYp(int sourceYp) {
		this.sourceYp = sourceYp;
	}

	public int getTargetXp() {
		return targetXp;
	}

	public void setTargetXp(int targetXp) {
		this.targetXp = targetXp;
	}

	public int getTargetYp() {
		return targetYp;
	}

	public void setTargetYp(int targetYp) {
		this.targetYp = targetYp;
	}

	public List<Bendpoint> getBendpoints() {
		return bendpoints;
	}

	public void setBendpoints(List<Bendpoint> points) {
		this.bendpoints = points;
	}

	public void addBendpoint(int index, Bendpoint point) {
		bendpoints.add(index, point);
	}

	@Override
	public ConnectionElementLocation clone() {
		ConnectionElementLocation clone = (ConnectionElementLocation) super.clone();

		List<Bendpoint> cloneBendPoints = new ArrayList<Bendpoint>();
		for (Bendpoint bendPoint : getBendpoints()) {
			cloneBendPoints.add((Bendpoint) bendPoint.clone());
		}

		clone.setBendpoints(cloneBendPoints);

		return clone;
	}
}
