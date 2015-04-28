package gui.settings

import org.ini4j.Ini
import groovy.util.logging.Log4j

@Log4j
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
        ini.put('Redmine','timeout', settings.redmineTimeout)
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
        checkConfigFileExists()

        Ini ini = new Ini(configFile)

        return new Settings(
            redmineUrl: ini.get('Redmine','url'),
            redmineApiKey: ini.get('Redmine', 'apiKey'),
            redmineTimeout: ini.get('Redmine', 'timeout', int.class),
            taigaUrl: ini.get('Taiga', 'url'),
            taigaUsername: ini.get('Taiga', 'username'),
            taigaPassword: ini.get('Taiga', 'password')
        )

    }

    boolean areServicesUp(String... urls) throws Exception {
        log.debug("Checking services ${urls}")
        return urls.every { url ->
            try {
                def host = new URL(url).host

                log.debug("Checking ${host}")

                def address = InetAddress.getByName(host)
                def reachable = address.isReachable(3000)

                if(!reachable) {
                    // awful race condition
                    Thread.sleep(3000)
                }

                if (reachable) {
                    log.debug("Service ${host} seems to be up and running")
                } else {
                    log.error("Host ${host} seems to be down. Please check your connections")
                }

                return reachable
            } catch(e) {
                log.error("Exception while checking host ${host}")
                return false
            }
        }
    }

}
