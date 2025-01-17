package com.github.kotlintelegrambot.dispatcher.handlers.media

import anyMessage
import anyUpdate
import anyVideoNote
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.dispatcher.handlers.HandleVideoNote
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.jupiter.api.Test

class VideoNoteHandlerTest {

    private val handleVideoNoteMock = mockk<HandleVideoNote>(relaxed = true)

    private val sut = VideoNoteHandler(handleVideoNoteMock)

    @Test
    fun `checkUpdate returns false when there is no video note`() {
        val anyUpdateWithNoVideoNote = anyUpdate(message = anyMessage(videoNote = null))

        val checkUpdateResult = sut.checkUpdate(anyUpdateWithNoVideoNote)

        assertFalse(checkUpdateResult)
    }

    @Test
    fun `checkUpdate returns true when there is video note`() {
        val anyUpdateWithVideoNote = anyUpdate(message = anyMessage(videoNote = anyVideoNote()))

        val checkUpdateResult = sut.checkUpdate(anyUpdateWithVideoNote)

        assertTrue(checkUpdateResult)
    }

    @Test
    fun `video note is properly dispatched to the handler function`() {
        val botMock = mockk<Bot>()
        val anyVideoNote = anyVideoNote()
        val anyMessageWithVideoNote = anyMessage(videoNote = anyVideoNote)
        val anyUpdateWithVideoNote = anyUpdate(message = anyMessageWithVideoNote)

        sut.handlerCallback(botMock, anyUpdateWithVideoNote)

        val expectedVideoNoteHandlerEnv = MediaHandlerEnvironment(
            botMock,
            anyUpdateWithVideoNote,
            anyMessageWithVideoNote,
            anyVideoNote
        )
        verify { handleVideoNoteMock.invoke(expectedVideoNoteHandlerEnv) }
    }
}
