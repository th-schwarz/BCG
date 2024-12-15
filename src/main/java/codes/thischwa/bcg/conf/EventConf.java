package codes.thischwa.bcg.conf;

import codes.thischwa.bcg.Person;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;

@ConfigurationProperties(prefix = "event")
public record EventConf(String summary,
                        String description,
                        String dateFormat) {

    public String generateSummary(Person person) {
        return replace(summary, person);
    }

    public String generateDescription(Person person) {
        return replace(description, person);
    }

    private String replace(String template, Person person) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(dateFormat);
        String result = template.replace("~first-name~", person.firstName())
                .replace("~last-name~", person.lastName())
                .replace("~display-name~", person.displayName())
                .replace("~birthday~", df.format(person.birthday()));
        return result;
    }
}
