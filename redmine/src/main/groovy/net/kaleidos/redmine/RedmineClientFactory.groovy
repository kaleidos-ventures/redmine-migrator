package net.kaleidos.redmine

import com.taskadapter.redmineapi.RedmineManagerFactory

final class RedmineClientFactory {

    public static RedmineClient newInstance(Map<String,?> settings) {
        def httpConfig =
            RedmineManagerFactory.createLongTermConfiguration(
                RedmineManagerFactory.createInsecureConnectionManager(),
                settings.redmineTimeout,
                settings.redmineTimeout - 1000
            )
        def redmineFileDownloader = new RedmineFileDownloader(httpConfig.client, settings.redmineUrl, settings.redmineApiKey)
        def redmineManager = RedmineManagerFactory.createWithApiKey(settings.redmineUrl, settings.redmineApiKey, httpConfig)

        return new RedmineClientImpl(
            redmineFileDownloader: redmineFileDownloader,
            redmineManager: redmineManager
        )
    }

}
