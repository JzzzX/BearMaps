# README

------

## **BearMaps: A Web Mapping and Navigation System**

BearMaps is a backend-driven web mapping application inspired by industry giants like Google Maps and OpenStreetMap. This project, developed as part of the **CS61B Data Structures** course, demonstrates advanced algorithms and data structures for efficient mapping, routing, and location-based search functionalities.

### **Project Features**

1. **Dynamic Map Rastering**
	 Efficiently generates map images for specific user-defined views by dynamically stitching together rasterized map tiles.
2. **Shortest Path Calculation**
	 Implements the **A\* Search Algorithm** for efficient and optimal route computation, ensuring minimal computational overhead.
3. **Turn-by-Turn Navigation**
	 Provides detailed navigation instructions, including direction changes (e.g., turn left, continue straight) and distances.
4. **Autocomplete and Search**
	- **Autocomplete**: Supports prefix-based location name suggestions with instant results.
	- **Search**: Retrieves location data, including coordinates, IDs, and full names, from OpenStreetMap datasets.
5. **High-Performance Back-End**
	 Utilizes advanced data structures like **Graphs**, **Tries**, and **Priority Queues** to ensure optimal performance and scalability.

------

### **System Design and Workflow**

BearMaps integrates multiple backend systems to handle rastering, routing, and search functionalities while maintaining modularity and extensibility.

#### **Workflow**

1. **Input Handling**
	 User inputs are processed through RESTful API calls. Queries include viewport dimensions, latitude/longitude bounds, or start/end coordinates.
2. **Map Rastering**
	 The system determines the required map depth and retrieves tiles that match the viewport. Tiles are stitched together to create a complete raster image, reducing unnecessary bandwidth and computational effort.
3. **Pathfinding**
	 The shortest path is calculated between two points using the A* algorithm. The heuristic function (great-circle distance) guides the search toward the goal while ensuring optimal efficiency.
4. **Turn-by-Turn Navigation**
	 Path segments are analyzed to provide detailed directions, including the type of turn, the road name, and the segment distance.
5. **Search and Autocomplete**
	 Location search and autocomplete functionalities rely on:
	- **Trie Data Structure** for prefix-matching efficiency.
	- Graph traversal to retrieve matching locations, including coordinates and names.

------

### **Key Components**

#### **Backend Architecture**

##### **Core Classes**

- **`MapServer`**
	 Manages API endpoints, handles HTTP requests, and integrates rastering, routing, and search functionalities. It connects user input to backend computations.
- **`Rasterer`**
	 Handles map tile selection and image rasterization, ensuring efficient and accurate rendering of user-defined map views.
- **`GraphDB`**
	 Parses OpenStreetMap XML data to construct a graph-based representation of road networks. This class manages nodes (intersections) and edges (roads).
- **`Router`**
	 Implements the A* algorithm for shortest pathfinding. Computes paths between nodes and prepares data for navigation instructions.
- **`NavigationDirection`**
	 Formats and organizes turn-by-turn navigation instructions for user-friendly output.

##### **Data Structures**

- **Graph**: Represents road networks with nodes (vertices) and edges. Enables efficient adjacency queries and shortest-path computation.
- **Trie**: Supports fast prefix-based search for location autocomplete.
- **Priority Queue**: Facilitates efficient exploration in the A* algorithm.

------

### **Technical Features**

1. **A\* Search Algorithm**
	- Combines the shortest known path (`g(n)`) with an estimated heuristic (`h(n)`), guiding the search toward the target.
	- Uses great-circle distance as the heuristic for accuracy and performance.
2. **Map Rastering**
	- Dynamically determines the optimal zoom depth based on the viewport.
	- Retrieves and stitches tiles from pre-saved images to generate complete raster images.
3. **Turn-by-Turn Directions**
	- Analyzes the path to detect direction changes based on bearings.
	- Outputs navigation instructions like "Continue straight for 1.2 miles on Main Street."
4. **Search and Autocomplete**
	- Autocomplete matches location prefixes using a Trie.
	- Full location details (name, latitude, longitude) are retrieved from the graph.

------

### **Development Process**

#### **Technologies Used**

- **Java**: Core language for backend implementation.
- **Spark Framework**: Handles RESTful API calls.
- **OpenStreetMap**: Provides real-world map data in XML format.
- **JUnit**: Ensures code reliability through rigorous unit testing.

#### **Challenges Addressed**

- Efficient rastering for large maps with minimal bandwidth usage.
- Implementing a scalable search system for millions of location names.
- Ensuring route accuracy and optimal performance in A* pathfinding.

------

### **Future Enhancements**

1. **Vector-Based Rendering**
	 Transition from rasterized images to vector tiles for better scalability and interactivity. This approach leverages technologies like WebGL for real-time rendering.
2. **Client-Side Optimizations**
	 Delegate more tasks to the frontend, such as tile assembly and caching, for improved responsiveness and reduced server load.
3. **Advanced Search Features**
	- Introduce ranking for search results based on relevance.
	- Add support for filtering results by categories or proximity.
4. **Integration with Real-Time Data**
	 Incorporate real-time traffic data to enhance routing accuracy.

------

### **Getting Started**

1. Clone the repository:

	```bash
	git clone git@github.com:JzzzX/BearMaps.git
	cd BearMaps
	```

2. Build the project using Maven:

	```bash
	mvn clean install
	```

3. Start the server:

	```bash
	java -cp target/classes MapServer
	```

4. Open your browser and navigate to:

	```
	http://localhost:4567/map.html
	```

------

### **Acknowledgments**

This project was developed as part of the **CS61B: Data Structures** course at the University of California, Berkeley. It incorporates skeleton code and OpenStreetMap data, with thanks to the course staff for guidance and resources.

