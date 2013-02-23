package org.lia.matsim2gs;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.AbstractEdge;
import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.AbstractNode;
import org.matsim.api.core.v01.network.Link;

/**
 * Node adapter that link MATSim node to an Graph Stream node structure.
 * @author Felix Voituret (felix.voituret@univ-avignon.fr)
 */
public class NodeAdapter extends AbstractNode {

	// MATSim node that source data.
	private org.matsim.api.core.v01.network.Node sourceNode;

	/**
	 * Default constructor.
	 * @param graph The owner of the node.
	 * @param id The id of the node.
	 */
	public NodeAdapter(final AbstractGraph graph, final org.matsim.api.core.v01.network.Node node) {
		super(graph, node.getId().toString());
		sourceNode = node;
		setAttribute("xy", sourceNode.getCoord().getX(), sourceNode.getCoord().getY());
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractNode#addEdgeCallback(org.graphstream.graph.implementations.AbstractEdge)
	 */
	@Override
	protected boolean addEdgeCallback(final AbstractEdge edge) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractNode#clearCallback()
	 */
	@Override
	protected void clearCallback() {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractNode#getDegree()
	 */
	@Override
	public int getDegree() {
		return (getInDegree() + getOutDegree());
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractNode#getEdge(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Edge> T getEdge(final int i) {
		final EdgeAdapter edge;
		if (i < sourceNode.getInLinks().size()) {
			edge = new EdgeAdapter((AbstractGraph) getGraph(), (Link) sourceNode.getInLinks().values().toArray()[i]);
		}
		else {
			edge = new EdgeAdapter((AbstractGraph) getGraph(), (Link) sourceNode.getOutLinks().values().toArray()[(i - sourceNode.getInLinks().size())]);
		}
		return ((T) edge);
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractNode#getEdgeBetween(org.graphstream.graph.Node)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Edge> T getEdgeBetween(final Node node) {
		EdgeAdapter edge = getEdgeFrom(node);
		if (edge == null) {
			edge = getEdgeToward(node);
		}
		return ((T) edge);
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractNode#getEdgeFrom(org.graphstream.graph.Node)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Edge> T getEdgeFrom(final Node node) {
		final NodeAdapter networkNode = (NodeAdapter) node;
		EdgeAdapter edge = null;
		for (Link link : sourceNode.getInLinks().values()) {
			if (networkNode.equals(link.getFromNode())) {
				edge = new EdgeAdapter((AbstractGraph) getGraph(), link);
				break;
			}
		}
		return ((T) edge);
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractNode#getEdgeIterator()
	 */
	@Override
	public <T extends Edge> Iterator<T> getEdgeIterator() {
		List<Link> edges = new LinkedList<Link>();
		edges.addAll(sourceNode.getInLinks().values());
		edges.addAll(sourceNode.getOutLinks().values());
		return new NetworkGraphAdapter.EdgeIterator<T>((AbstractGraph) getGraph(), edges.iterator());
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractNode#getEdgeToward(org.graphstream.graph.Node)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Edge> T getEdgeToward(Node node) {
		final NodeAdapter networkNode = (NodeAdapter) node;
		EdgeAdapter edge = null;
		for (Link link : sourceNode.getOutLinks().values()) {
			if (networkNode.equals(link.getToNode())) {
				edge = new EdgeAdapter((AbstractGraph) getGraph(), link);
				break;
			}
		}
		return ((T) edge);
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractNode#getEnteringEdge(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Edge> T getEnteringEdge(final int i) {
		return ((T) new EdgeAdapter((AbstractGraph) getGraph(), (Link) sourceNode.getInLinks().values().toArray()[i]));
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractNode#getEnteringEdgeIterator()
	 */
	@Override
	public <T extends Edge> Iterator<T> getEnteringEdgeIterator() {
		return new NetworkGraphAdapter.EdgeIterator<T>((AbstractGraph) getGraph(), sourceNode.getInLinks().values().iterator());
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractNode#getInDegree()
	 */
	@Override
	public int getInDegree() {
		return sourceNode.getInLinks().size();
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractNode#getLeavingEdge(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Edge> T getLeavingEdge(final int i) {
		return ((T) new EdgeAdapter((AbstractGraph) getGraph(), (Link) sourceNode.getOutLinks().values().toArray()[i]));
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractNode#getLeavingEdgeIterator()
	 */
	@Override
	public <T extends Edge> Iterator<T> getLeavingEdgeIterator() {
		return new NetworkGraphAdapter.EdgeIterator<T>((AbstractGraph) getGraph(), sourceNode.getOutLinks().values().iterator());
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractNode#getOutDegree()
	 */
	@Override
	public int getOutDegree() {
		return sourceNode.getOutLinks().size();
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractNode#removeEdgeCallback(org.graphstream.graph.implementations.AbstractEdge)
	 */
	@Override
	protected void removeEdgeCallback(final AbstractEdge edge) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object object) {
		final org.matsim.api.core.v01.network.Node target = (org.matsim.api.core.v01.network.Node) object;
		return (sourceNode.toString().equals(target.toString()));
	}

}
