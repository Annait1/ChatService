import org.junit.Test
import org.junit.Before
import org.junit.Assert.*

class ChatServiceTest {

    @Before
    fun clearBeforeTest() {
        ChatService.clearAll()
    }

    @Test
    fun testSendMessageCreatesChat() {
        val message = ChatService.sendMessage(1, 2, "Привет")
        val chats = ChatService.getChats()
        assertEquals(1, chats.size)
        assertEquals("Привет", message.text)
    }

        @Test
        fun editMessageUpdatesTextSuccessfully() {
            val message = ChatService.sendMessage(1, 2, "Стрый")
            val result = ChatService.editMessage(message.id, "Новый")
            assertTrue(result)
            val updated = ChatService.getMessagesFromChat(message.chatId, 2, 1).first()
            assertEquals("Новый", updated.text)
        }

        @Test
        fun deleteMessageRemovesMessage() {
            val message = ChatService.sendMessage(1, 2, "Удалить")
            val result = ChatService.deleteMessage(message.id)
            assertTrue(result)
        }

        @Test
        fun getUnreadChatsCountReturnsCorrectValue() {
            ChatService.sendMessage(1, 2, "Msg1")
            ChatService.sendMessage(3, 2, "Msg2")
            val unreadCount = ChatService.getUnreadChatsCount(2)
            assertEquals(2, unreadCount)
        }

        @Test
        fun getMessagesFromChatMarksMessagesAsRead() {
            val msg = ChatService.sendMessage(1, 2, "При")
            val messages = ChatService.getMessagesFromChat(msg.chatId, 2, 1)
            assertTrue(messages.first().isRead)
        }

        @Test
        fun deleteChatRemovesChatAndMessages() {
            val msg = ChatService.sendMessage(1, 2, "НАанан")
            val result = ChatService.deleteChat(msg.chatId)
            assertTrue(result)
            assertTrue(ChatService.getChats().isEmpty())
        }

        @Test
        fun getLastMessagesFromChatsShowsLastMessageText() {
            ChatService.sendMessage(1, 2, "Первый")
            ChatService.sendMessage(1, 2, "Второй")
            val lastMessages = ChatService.getLastMessagesFromChats()
            assertTrue(lastMessages.any { it.contains("Второй") })
        }
    }
