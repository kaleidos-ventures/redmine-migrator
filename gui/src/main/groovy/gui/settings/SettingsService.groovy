package gui.settings

import org.ini4j.Ini

class SettingsService {

    static File configFile =
        new File(
            System.getProperty('user.home'),
            '.taiga-settings.ini'
        )

    void saveSettings(final Settings settings) {
        checkConfigFileExists()

        Ini ini = new Ini(configFile)

        ini.put('Redmine','url', settings.redmineUrl)
        ini.put('Redmine','apiKey', settings.redmineApiKey)
        ini.put('Taiga','url', settings.taigaUrl)
        ini.put('Taiga','username', settings.taigaUsername)
        ini.put('Taiga','password', settings.taigaPassword)

        ini.store(configFile)
    }

    void checkConfigFileExists() {
        if (!configFile.exists()) {
            configFile.createNewFile()
        }
    }

    Settings loadSettings() {

        Ini ini = new Ini(configFile)

        return new Settings(
            redmineUrl: ini.get('Redmine','url'),
            redmineApiKey: ini.get('Redmine', 'apiKey'),
            taigaUrl: ini.get('Taiga', 'url'),
            taigaUsername: ini.get('Taiga', 'username'),
            taigaPassword: ini.get('Taiga', 'password')
        )

    }

}
