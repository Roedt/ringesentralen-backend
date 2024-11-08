package no.roedt

import net.fellbaum.jemoji.EmojiManager

object Emojifjerner {
    fun fjernEmojis(input: String?): String? =
        if (input == null) {
            null
        } else {
            EmojiManager.removeAllEmojis(input)
        }
}