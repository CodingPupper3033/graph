class Vertex<E>(var value: E? = null, var connections : ArrayList<Edge<E>> = ArrayList()) {
    override fun toString() : String {
        return value.toString();
    }

    fun connectionsToString() : String {
        var out = StringBuilder()

        for (connection in connections) {
            out.append(connection.destination)

            out.append(",")
        }
        if (connections.size > 0) out.replace(out.length-1, out.length, "")

        return out.toString()
    }

    fun addEdge(edge: Edge<E>) {
        connections.add(edge)
    }
}