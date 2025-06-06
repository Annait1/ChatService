object ChatService {
    private val chats = mutableListOf<Chat>()
    private val messages = mutableListOf<Message>()
    private var nextMessageId = 1

    fun sendMessage(senderId: Int, receiverId: Int, text: String): Message {
        var chat = chats.find {
            (it.userId1 == senderId && it.userId2 == receiverId) ||
                    (it.userId1 == receiverId && it.userId2 == senderId)
        }

        if (chat == null) {
            chat = Chat(userId1 = senderId, userId2 = receiverId)
            chats.add(chat)
        }

        val message = Message(
            id = nextMessageId++,
            chatId = chat.id,
            senderId = senderId,
            receiverId = receiverId,
            text = text
        )

        messages.add(message)

        chat.messages.add(message)
        return message
    }


    fun getChats(): List<Chat> {
        return chats
    }


    fun getLastMessagesFromChats(): List<String> {
        return chats
            .asSequence()
            .map { chat ->
                chat.messages.lastOrNull()?.let {
                    "Из чата ${chat.id}: ${it.text}"
                } ?: "Из чата ${chat.id}: нет сообщений"
            }
            .toList()
    }


    fun editMessage(messageId: Int, newText: String): Boolean {
        val index = messages.indexOfFirst { it.id == messageId }

        if (index == -1) return false

        val oldMessage = messages[index]
        val updatedMessage = oldMessage.copy(text = newText)

        messages[index] = updatedMessage
        return true
    }

    fun deleteMessage(messageId: Int): Boolean {
        val index = messages.indexOfFirst { it.id == messageId }

        if (index == -1) return false

        messages.removeAt(index)
        return true
    }

    fun getUnreadChatsCount(userId: Int): Int {
        return chats.asSequence()
            .filter { chat ->
                messages.any { it.chatId == chat.id && it.receiverId == userId && !it.isRead }
            }
            .count()
    }


    fun getChats(userId: Int): List<Chat> {
        return chats.filter { it.userId1 == userId || it.userId2 == userId }
    }

    fun getLastMessages(userId: Int): List<String> {
        return chats
            .asSequence()
            .filter { it.userId1 == userId || it.userId2 == userId }
            .map { chat ->
                val lastMessage = chat.messages
                    .asSequence()
                    .filter {
                        (it.senderId == chat.userId1 && it.receiverId == chat.userId2) ||
                                (it.senderId == chat.userId2 && it.receiverId == chat.userId1)
                    }
                    .lastOrNull()
                "Из чата ${chat.id}: ${lastMessage?.text ?: "нет сообщений"}"
            }
            .toList()
    }


    fun getMessagesFromChat(chatId: Int, userId: Int, count: Int): List<Message> {
        return messages
            .asSequence()
            .filter { it.chatId == chatId }
            .toList()
            .takeLast(count)
            .onEach { if (it.receiverId == userId) it.isRead = true }

    }


    fun deleteChat(chatId: Int): Boolean {
        val chat = chats.find { it.id == chatId } ?: return false
        chats.remove(chat)
        messages.removeIf { it.chatId == chatId }
        return true
    }

    fun clearAll() {
        chats.clear()
        messages.clear()
        nextMessageId = 1
    }

}

fun main() {
    ChatService.sendMessage(1, 2, "Привет!")
    ChatService.sendMessage(2, 1, "Привет, как дела?")
    ChatService.sendMessage(1, 3, "Здорово!")

    /*ChatService.clearAll()*/

    println("Список чатов:")
    ChatService.getChats(1).forEach { println(it) }

    println("\nПоследние сообщения:")
    ChatService.getLastMessages(1).forEach { println(it) }
}
