data class Edge<E>(var source: Vertex<E>, var destination: Vertex<E>) {
    override fun toString(): String {
        return "{$source -> $destination}"
    }
}