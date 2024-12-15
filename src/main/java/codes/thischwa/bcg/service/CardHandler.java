package codes.thischwa.bcg.service;

import codes.thischwa.bcg.Person;
import codes.thischwa.bcg.conf.DavConf;
import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.vcard.VCard;
import net.fortuna.ical4j.vcard.VCardBuilder;
import net.fortuna.ical4j.vcard.property.BDay;
import net.fortuna.ical4j.vcard.property.Fn;
import net.fortuna.ical4j.vcard.property.N;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CardHandler {

  private static final DateTimeFormatter birthdayFormatter =
      DateTimeFormatter.ofPattern("yyyyMMdd");

  private final DavConf davConf;
  private final Sardine sardine;

  public CardHandler(DavConf davConf) {
    this.sardine = SardineFactory.begin(davConf.user(), davConf.password());
    this.davConf = davConf;
  }

  List<Person> readPeopleWithBirthday() throws IllegalArgumentException {
    List<Person> people = new ArrayList<>();
    try {
      List<DavResource> addressbookItems = sardine.list(davConf.cardUrl());
      log.info("Contacts found: {}", addressbookItems.size());

      for (DavResource contact : addressbookItems) {
        if (contact.isDirectory()) {
          continue;
        }
        log.info("Contact - display name: {}", contact.getDisplayName());
        URI hRef = new URI(davConf.getBaseUrl() + contact.getHref().toString());
        try (InputStream vCardStream = sardine.get(hRef.toString())) {
          String vcfContent = IOUtils.toString(vCardStream, StandardCharsets.UTF_8);
          VCardBuilder cardBuilder =
              new VCardBuilder(
                  new ByteArrayInputStream(vcfContent.getBytes(StandardCharsets.UTF_8)));
          VCard vCard = cardBuilder.build();
          BDay birthday = vCard.getProperty(net.fortuna.ical4j.vcard.Property.Id.BDAY);
          if (birthday == null) {
            log.debug("No birthday found for {}", contact.getDisplayName());
            continue;
          }
          Fn displayName = vCard.getProperty(net.fortuna.ical4j.vcard.Property.Id.FN);
          N name = vCard.getProperty(net.fortuna.ical4j.vcard.Property.Id.N);
          String firstName = name.getGivenName();
          String lastName = name.getFamilyName();
          people.add(
              new Person(
                  firstName,
                  lastName,
                  displayName.getValue(),
                  LocalDate.parse(birthday.getValue(), birthdayFormatter)));
        }
      }
      return people;
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }
}
