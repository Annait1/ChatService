data class Message(
    val id: Int,
    val chatId: Int,
    val senderId: Int,
    val receiverId: Int,
    val text: String,
    var isRead: Boolean = false

)