package com.kanyideveloper.settings.presentation.settings


sealed interface SettingsUiEvents {
    class SendFeedbackClicked(val feedback: String) :
        SettingsUiEvents {

    }

    object ChangeThemeClicked : SettingsUiEvents {

    }

    object ReportOrSuggestClicked : SettingsUiEvents {

    }

    object LogoutClicked : SettingsUiEvents {

    }

    object RateUsClicked : SettingsUiEvents {

    }

    object ShareAppClicked : SettingsUiEvents {

    }

    object OnDismissThemesDialog :
        SettingsUiEvents {

    }

    object OnDismissFeedbackDialog :
        SettingsUiEvents {

    }

    data class OnFeedbackChanged(val feedback: String) : SettingsUiEvents {

    }

    data class OnSelectTheme(val themeValue: Int) : SettingsUiEvents {

    }

}