class Graph<E> {
    var vertices: ArrayList<Vertex<E>> = ArrayList()
    var edges: ArrayList<Edge<E>> = ArrayList()

    enum class toStingTypes {
        BLANK,
        MINIMAL_BOX,
        FULL_BOX
    }

    override fun toString(): String {
        return toString(toStingTypes.FULL_BOX)
    }

    fun toString(method: toStingTypes): String {
        var string = GraphMaker.makeStringFromGraph(this)

        // Get max size of line
        var maxLineLength = 0
        for (line in string.lines()) {
            maxLineLength = maxLineLength.coerceAtLeast(line.length)
        }

        when (method) {
            toStingTypes.BLANK -> return string
            toStingTypes.MINIMAL_BOX -> {
                return "─".repeat(maxLineLength) + "\n" + string + "\n" + "─".repeat(maxLineLength)
            }

            toStingTypes.FULL_BOX -> {
                var out = StringBuilder()

                // Top Line
                var middleLineRepeat = "─".repeat(maxLineLength)
                out.append("┌$middleLineRepeat┐\n")

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

    fun addEdge(source: Vertex<E>, destination: Vertex<E>) {
        var edge: Edge<E> = Edge(source, destination)

        source.connections.add(edge)
        edges.add(edge)
    }
}