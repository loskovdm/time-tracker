package io.github.loskovdm.domain.model

enum class AppLanguage(val tag: String) {
    ENGLISH("en"),
    RUSSIAN("ru"),
    ;

    companion object {
        fun fromSystemLanguageTag(languageTag: String): AppLanguage {
            val language = languageTag.lowercase().substringBefore('-')
            return if (language == "ru") RUSSIAN else ENGLISH
        }

        fun fromStoredValue(stored: String?, systemLanguageTag: String): AppLanguage {
            if (stored == null || stored == LEGACY_SYSTEM) {
                return fromSystemLanguageTag(systemLanguageTag)
            }
            return runCatching { valueOf(stored) }.getOrNull()
                ?: fromSystemLanguageTag(systemLanguageTag)
        }

        private const val LEGACY_SYSTEM = "SYSTEM"
    }
}
