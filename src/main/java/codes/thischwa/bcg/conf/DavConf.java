package codes.thischwa.bcg.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.Nullable;
import org.springframework.web.util.UriComponentsBuilder;

@ConfigurationProperties(prefix = "dav")
public record DavConf(
        String user,
        String password,
        @Nullable String davPath,
        String calUrl,
        String cardUrl
) {
    
    public String getBaseUrl() {
        String baseUrl = UriComponentsBuilder.fromUriString(cardUrl)
                .replacePath(null)
                .replaceQuery(null)
                .fragment(null)
                .build()
                .toUriString();
        return baseUrl;
    }

    public  String getCalendarPath() {
        String path = calUrl.endsWith("/") ? calUrl.substring(0, calUrl.length() - 1) : calUrl;
        int idx = path.lastIndexOf('/');
        if (idx > 0) {
            path = path.substring(idx + 1, path.length());
        }
        return path;
    }

}
