package no.roedt

import jakarta.enterprise.context.ApplicationScoped
import net.fellbaum.jemoji.EmojiManager

@ApplicationScoped
class Emojifjerner {
    companion object Fjerner {
        fun fjernEmojis(input: String?): String? =
            if (input == null) {
                null
            } else {
                EmojiManager.removeAllEmojis(input)
            }
    }
}
