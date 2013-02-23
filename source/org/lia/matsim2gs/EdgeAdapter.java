package org.lia.matsim2gs;

import org.graphstream.graph.implementations.AbstractEdge;
import org.graphstream.graph.implementations.AbstractGraph;
import org.matsim.api.core.v01.network.Link;

/**
 * Edge adapter that bind MATSim link to an Graph Stream edge structure.
 * @author Felix Voituret (felix.voituret@univ-avignon.fr)
 */
public class EdgeAdapter extends AbstractEdge {

	/**
	 * Default constructor.
	 * @param graph The owner of the edge.
	 * @param link The source link to bind.
	 */
	public EdgeAdapter(final AbstractGraph graph, final Link link) {
		super(link.getId().toString(), new NodeAdapter(graph, link.getFromNode()), new NodeAdapter(graph, link.getToNode()), true);
	}

}