matsim2gs
=========

Java library that offer an interface between MATSim network data structure and GraphStream required structure. 

To use it just initialize a NetworkGraphAdapter object like this :

  NetworkGraphAdapter adapter = new NetworkGraphAdater(matsim_network_object);

And use it like any other GraphStream graph structure :

  adapter.setAttribute("ui.stylesheet", "node {size : 2px;}");
  adapter.display();  
