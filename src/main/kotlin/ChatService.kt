object ChatService {
    private val chats = mutableListOf<Chat>()
    private val messages = mutableListOf<Message>()
    private var nextMessageId = 1

    fun sendMessage(senderId: Int, receiverId: Int, text: String): Message {
        var chat = ChatService.chats.find {
            (it.userId1 == senderId && it.userId2 == receiverId) ||
                    (it.userId1 == receiverId && it.userId2 == senderId)
        }

        if (chat == null) {
            chat = Chat(userId1 = senderId, userId2 = receiverId)
            ChatService.chats.add(chat)
        }

        val message = Message(
            id = nextMessageId++,
            chatId = chat.id,
            senderId = senderId,
            receiverId = receiverId,
            text = text
        )

        ChatService.messages.add(message)

        chat.messages.add(message)
        return message
    }



    fun getChats(): List<Chat> {
        return ChatService.chats
    }


    fun getLastMessagesFromChats(): List<String> {

        return ChatService.chats.map { chat ->
            val lastMessage = chat.messages.lastOrNull()
            if (lastMessage != null) {
                "Из чата ${chat.id}: ${lastMessage.text}"
            } else {
                "Из чата ${chat.id}: нет сообщений"

            }
        }
    }


    fun editMessage(messageId: Int, newText: String): Boolean {
        val index = ChatService.messages.indexOfFirst { it.id == messageId }

        if (index == -1) return false

        val oldMessage = ChatService.messages[index]
        val updatedMessage = oldMessage.copy(text = newText)

        ChatService.messages[index] = updatedMessage
        return true
    }

    fun deleteMessage(messageId: Int): Boolean {
        val index = ChatService.messages.indexOfFirst { it.id == messageId }

        if (index == -1) return false

        ChatService.messages.removeAt(index)
        return true
    }

    fun getUnreadChatsCount(userId: Int): Int {
        return ChatService.chats.count { chat ->
            ChatService.messages.any {
                it.chatId == chat.id &&
                        it.receiverId == userId &&
                        !it.isRead
            }
        }
    }

    fun getChats(userId: Int): List<Chat> {
        return ChatService.chats.filter { it.userId1 == userId || it.userId2 == userId }
    }

    fun getLastMessages(userId: Int): List<String> {
        return ChatService.chats.filter { it.userId1 == userId || it.userId2 == userId }
            .map { chat ->
                val chatMessages = ChatService.messages.filter { message ->
                    (message.senderId == chat.userId1 && message.receiverId == chat.userId2) ||
                            (message.senderId == chat.userId2 && message.receiverId == chat.userId1)
                }
                chatMessages.lastOrNull()?.text ?: "нет сообщений"
            }
    }


    fun getMessagesFromChat(chatId: Int, userId: Int, count: Int): List<Message> {
        val chatMessages = ChatService.messages.filter { it.chatId == chatId }.takeLast(count)

        chatMessages.forEach {
            if (it.receiverId == userId) {
                it.isRead = true
            }
        }

        return chatMessages
    }


    fun deleteChat(chatId: Int): Boolean {
        val chat = ChatService.chats.find { it.id == chatId } ?: return false
        ChatService.chats.remove(chat)
        ChatService.messages.removeIf { it.chatId == chatId }
        return true
    }

    fun clearAll() {
        ChatService.chats.clear()
        ChatService.messages.clear()
        ChatService.nextMessageId = 1
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
