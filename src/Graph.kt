import java.util.*

class Graph<E>(val allowDuplicateEdges: Boolean = true, val undirected: Boolean = false, val allowSelfConnection: Boolean = true) {
    var vertices: ArrayList<Vertex<E>> = ArrayList()
    var edges: ArrayList<Edge<E>> = ArrayList()

    enum class ToStingTypes {
        BLANK,
        MINIMAL_BOX,
        FULL_BOX,
        FULL_BOX_PROPERTIES
    }

    override fun toString(): String {
        return toString(ToStingTypes.FULL_BOX_PROPERTIES)
    }

    fun toString(method: ToStingTypes): String {
        var string = GraphMaker.makeStringFromGraph(this)

        // Get max size of line
        var maxLineLength = 0
        for (line in string.lines()) {
            maxLineLength = maxLineLength.coerceAtLeast(line.length)
        }

        when (method) {
            ToStingTypes.BLANK -> return string
            ToStingTypes.MINIMAL_BOX -> {
                return "─".repeat(maxLineLength) + "\n" + string + "\n" + "─".repeat(maxLineLength)
            }

            ToStingTypes.FULL_BOX, ToStingTypes.FULL_BOX_PROPERTIES -> {
                var out = StringBuilder()

                // Properties
                var propertiesString = "DupEdges: $allowDuplicateEdges, ${if (undirected) "Undir" else "Dired"}, SelfCon: $allowSelfConnection"
                if (method == ToStingTypes.FULL_BOX_PROPERTIES) maxLineLength = maxLineLength.coerceAtLeast(propertiesString.length)

                // Top Line
                var middleLineRepeat = "─".repeat(maxLineLength)
                out.append("┌$middleLineRepeat┐\n")

                // Properties if applicable
                if (method == ToStingTypes.FULL_BOX_PROPERTIES) {
                    out.append("│" + propertiesString + " ".repeat(maxLineLength - propertiesString.length) + "│\n")
                    out.append("├$middleLineRepeat┤\n")
                }


                // Middle
                for (line in string.lines()) {
                    out.append("│" + line + " ".repeat(maxLineLength-line.length) + "│\n")
                }


                // Bottom
                out.append("└$middleLineRepeat┘")

                return out.toString()
            }
        }

        return ""
    }


    fun getVertexFromVale(value: E): Vertex<E>? {
        for (vertex: Vertex<E> in vertices) {
            if (vertex.value == value) {
                return vertex
            }
        }
        return null
    }

    fun hasVertexWithValue(value: E): Boolean {
        return getVertexFromVale(value) != null
    }

    fun addEdge(source: Vertex<E>, destination: Vertex<E>, doContinueAdd: Boolean = true) {
        var edge: Edge<E> = Edge(source, destination)

        var add: Boolean = true

        // Duplicate edges protection
         if (!allowDuplicateEdges) {
             for (edgeCheck in edges) {
                 if (edge == edgeCheck) add = false
             }
         }

        // Self Connection protection
        if (!allowSelfConnection) {
            if (edge.source == edge.destination) {
                add = false
            }
        }

        // Actually add (if able to)
        if (add) {
            source.connections.add(edge)
            edges.add(edge)

            // Add other direction
            if (undirected && doContinueAdd) {
                addEdge(edge.destination, edge.source, false)
            }
        }
    }

    fun addVertex(value: E, edges: List<E>) {
        var vertexToAdd = Vertex(value)
        vertices.add(vertexToAdd)

        for (item: E in edges) {
            vertexToAdd.addEdge(getVertexFromVale(item)!!)
        }

    }

    fun resetVisit() {
        for (vertext in vertices) {
            vertext.visited = false
        }
    }

    fun depthFirstSearch(): ArrayList<ArrayList<Vertex<E>>> {
        return depthFirstSearch(vertices)
    }

    fun depthFirstSearch(verticies: ArrayList<Vertex<E>>): ArrayList<ArrayList<Vertex<E>>> {
        var list = ArrayList<ArrayList<Vertex<E>>>()

        for (vertex in verticies) {
            list.add(depthFirstSearch(vertex))
        }

        return list
    }

    fun depthFirstSearch(start: Vertex<E>): ArrayList<Vertex<E>> {
        var list = ArrayList<Vertex<E>>()
        var working: Stack<Vertex<E>> = Stack<Vertex<E>>()

        start.visited = true
        working.add(start)

        while (working.isNotEmpty()) {
            var item = working.pop()
            list.add(item)
            for (edge in item.connections.reversed()) {
                var currentItemInItem = edge.destination
                if (!currentItemInItem.visited) {
                    currentItemInItem.visited = true
                    working.add(currentItemInItem)
                }
            }
        }

        resetVisit()
        return list
    }

    fun breadthFirstSearch(): ArrayList<ArrayList<Vertex<E>>> {
        return breadthFirstSearch(vertices)
    }

    fun breadthFirstSearch(verticies: ArrayList<Vertex<E>>): ArrayList<ArrayList<Vertex<E>>> {
        var list = ArrayList<ArrayList<Vertex<E>>>()

        for (vertex in verticies) {
            list.add(breadthFirstSearch(vertex))
        }

        return list
    }

    fun breadthFirstSearchVertices(start: Vertex<E>, end: Vertex<E>? = null): ArrayList<Edge<E>> {
        var list = ArrayList<Edge<E>>()
        var working: Queue<Vertex<E>> = LinkedList<Vertex<E>>()

        start.visited = true
        working.add(start)

        while (working.isNotEmpty()) {
            var item = working.poll()

            for (edge in item.connections) {
                var currentItemInItem = edge.destination
                if (!currentItemInItem.visited) {
                    currentItemInItem.visited = true
                    working.add(currentItemInItem)
                    list.add(edge)

                    if (currentItemInItem == end) {
                        working = LinkedList<Vertex<E>>()
                    }
                }
            }
        }

        resetVisit()

        return if (end != null) removeFalsePaths(list, end) else list
    }

    fun breadthFirstSearch(start: Vertex<E>, end: Vertex<E>? = null): ArrayList<Vertex<E>> {
        return edgePathToVertices(breadthFirstSearchVertices(start, end))
    }

    fun edgePathToVertices(list: ArrayList<Edge<E>>): ArrayList<Vertex<E>> {
        var listVertices = ArrayList<Vertex<E>>()

        if (list.size > 0) {
            listVertices.add(list[0].source)
            for (item in list) {
                listVertices.add(item.destination)
            }
        }
        return listVertices
    }

    fun removeFalsePaths(list: ArrayList<Edge<E>>, end: Vertex<E>): ArrayList<Edge<E>> {
        var listEdit = ArrayList(list)
        var last = end
        for (i in listEdit.size-1 downTo 0) {
            if (listEdit[i].destination != last) {
                listEdit.removeAt(i)
            } else {
                last = listEdit[i].source
            }
        }
        return listEdit
    }
}