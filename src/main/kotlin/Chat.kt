data class Chat(
    val id: Int = ChatIdGenerator.nextId(),
    val userId1: Int,
    val userId2: Int,
    val messages: MutableList<Message> = mutableListOf()

)

object ChatIdGenerator {
    private var lastId = 1
    fun nextId(): Int = lastId++
}