package org.lia.matsim2gs;

import java.util.Iterator;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.AbstractEdge;
import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.AbstractNode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.basic.v01.IdImpl;

/**
 * Network adapter that link MATSim network to an Graph Stream graph structure.
 * @author Felix Voituret (felix.voituret@univ-avignon.fr)
 */
public class NetworkGraphAdapter extends AbstractGraph {

	// Source network from MATSim framework.
	protected Network sourceNetwork;

	/**
	 * Default constructor.
	 * @param id The id of the graph.
	 * @param network The source network to use.
	 */
	public NetworkGraphAdapter(final String id, final Network network) {
		super(id);
		sourceNetwork = network;
	}

	/**
	 * Source network getter.
	 * @return The network use for this graph.
	 */
	public Network getSourceNetwork() {
		return sourceNetwork;
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractGraph#addEdgeCallback(org.graphstream.graph.implementations.AbstractEdge)
	 */
	@Override
	protected void addEdgeCallback(final AbstractEdge edge) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractGraph#addNodeCallback(org.graphstream.graph.implementations.AbstractNode)
	 */
	@Override
	protected void addNodeCallback(final AbstractNode node) {
		// Do nothing.		
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractGraph#clearCallback()
	 */
	@Override
	protected void clearCallback() {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractGraph#getEdge(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Edge> T getEdge(String id) {
		return (T) (new EdgeAdapter(this, sourceNetwork.getLinks().get(new IdImpl(id))));
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractGraph#getEdge(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Edge> T getEdge(int index) {
		return (T) (new EdgeAdapter(this, (Link) sourceNetwork.getLinks().values().toArray()[index]));
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractGraph#getEdgeCount()
	 */
	@Override
	public int getEdgeCount() {
		return sourceNetwork.getLinks().size();
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractGraph#getNode(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Node> T getNode(final String id) {
		return (T) (new NodeAdapter(this, sourceNetwork.getNodes().get(new IdImpl(id))));
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractGraph#getNode(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Node> T getNode(int index) {
		return (T) (new NodeAdapter(this, (org.matsim.api.core.v01.network.Node) sourceNetwork.getNodes().values().toArray()[index]));
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractGraph#getNodeCount()
	 */
	@Override
	public int getNodeCount() {
		return sourceNetwork.getNodes().size();
	}
	
	/**
	 * Custom edge iterator that plug the original map iterator.
	 * @author Felix Voituret (felix.voituret@univ-avignon.fr)
	 * @param <T> Type that extends Edge interface.
	 */
	static class EdgeIterator<T extends Edge> implements Iterator<T> {

		// Original iterator we plug to.
		final Iterator<? extends Link> sourceIterator;

		// The source graph of the iterator.
		final AbstractGraph sourceGraph;

		/**
		 * Default constructor.
		 * @param graph The source graph object.
		 */
		public EdgeIterator(final AbstractGraph graph, final Iterator<? extends Link> iterator) {
			sourceIterator = iterator;
			sourceGraph = graph;
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			return sourceIterator.hasNext();
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		@SuppressWarnings("unchecked")
		@Override
		public T next() {
			return (T) (new EdgeAdapter(sourceGraph, sourceIterator.next()));
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			sourceIterator.remove();
		}

	}

	/**
	 * Custom node iterator that plug the original map iterator.
	 * @author Felix Voituret (felix.voituret@univ-avignon.fr)
	 * @param <T> Type that extends Node interface.
	 */
	class NodeIterator<T extends Node> implements Iterator<T> {

		// Original iterator we plug to.
		final Iterator<? extends org.matsim.api.core.v01.network.Node> sourceIterator;

		// The source graph of the iterator.
		final NetworkGraphAdapter sourceGraph;

		/**
		 * Default constructor.
		 * @param graph The source graph object.
		 */
		public NodeIterator(final NetworkGraphAdapter graph) {
			sourceIterator = sourceNetwork.getNodes().values().iterator();
			sourceGraph = graph;
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			return sourceIterator.hasNext();
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		@SuppressWarnings("unchecked")
		@Override
		public T next() {
			return (T) (new NodeAdapter(sourceGraph, sourceIterator.next()));
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			sourceIterator.remove();
		}

	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractGraph#getNodeIterator()
	 */
	@Override
	public <T extends Node> Iterator<T> getNodeIterator() {
		return new NodeIterator<T>(this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractGraph#getEdgeIterator()
	 */
	@Override
	public <T extends Edge> Iterator<T> getEdgeIterator() {
		return new EdgeIterator<T>(this, sourceNetwork.getLinks().values().iterator());
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractGraph#removeEdgeCallback(org.graphstream.graph.implementations.AbstractEdge)
	 */
	@Override
	protected void removeEdgeCallback(AbstractEdge edge) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * @see org.graphstream.graph.implementations.AbstractGraph#removeNodeCallback(org.graphstream.graph.implementations.AbstractNode)
	 */
	@Override
	protected void removeNodeCallback(AbstractNode node) {
		// Do nothing.
	}

}
