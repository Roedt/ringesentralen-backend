package no.roedt.frivilligsystem

import com.vdurmont.emoji.EmojiParser
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class Emojifjerner {

    companion object Fjerner {
        fun fjernEmojis(input: String?): String? =
            if (input == null) null
            else EmojiParser.removeAllEmojis(input)
    }
}