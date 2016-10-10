package de.helfenkannjeder.helfomat.typo3.batch;

import de.helfenkannjeder.helfomat.domain.Address;
import de.helfenkannjeder.helfomat.domain.GeoPoint;
import de.helfenkannjeder.helfomat.domain.Organisation;
import de.helfenkannjeder.helfomat.domain.Question;
import de.helfenkannjeder.helfomat.service.ListCache;
import de.helfenkannjeder.helfomat.typo3.domain.TGroupOfGroupTemplate;
import de.helfenkannjeder.helfomat.typo3.domain.TGroupTemplate;
import de.helfenkannjeder.helfomat.typo3.domain.TOrganisation;
import de.helfenkannjeder.helfomat.typo3.domain.TQuestion;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Valentin Zickner
 */
@Component
@JobScope
public class Typo3OrganisationProcessor implements ItemProcessor<TOrganisation, Organisation> {

    private final ListCache<TQuestion> allQuestions;

    @Autowired
    public Typo3OrganisationProcessor(ListCache<TQuestion> allQuestions) {
        this.allQuestions = allQuestions;
    }

    @Override
    public Organisation process(TOrganisation tOrganisation) throws Exception {
        Organisation organisation = new Organisation();
        organisation.setId(UUID.randomUUID().toString());
        organisation.setName(tOrganisation.getName());
        organisation.setDescription(tOrganisation.getDescription());
        organisation.setLogo(tOrganisation.getLogo());
        organisation.setWebsite(tOrganisation.getWebsite());
        String pictures = tOrganisation.getPictures();

        if (tOrganisation.getOrganisationtype() != null) {
            organisation.setMapPin(tOrganisation.getOrganisationtype().getPicture());
        }

        if (pictures != null) {
            organisation.setPictures(Arrays.asList(pictures.split(",")));
        }

        organisation.setAddresses(tOrganisation.getAddresses().stream().map(tAddress -> {
            Address address = new Address();
            address.setWebsite(tAddress.getWebsite());
            address.setTelephone(tAddress.getTelephone());
            address.setStreet(tAddress.getStreet());
            address.setAddressAppendix(tAddress.getAddressappendix());
            address.setCity(tAddress.getCity());
            address.setZipcode(tAddress.getZipcode());
            address.setLocation(new GeoPoint(tAddress.getLatitude(), tAddress.getLongitude()));
            return address;
        }).collect(Collectors.toList()));

        final List<Question> questionList = allQuestions.getAll().stream()
                .map(tQuestion1 -> {
                    Question question1 = new Question();
                    question1.setUid(tQuestion1.getUid());
                    question1.setQuestion(tQuestion1.getQuestion());
                    question1.setDescription(tQuestion1.getDescription());
                    question1.setAnswer(Question.Answer.NO);
                    question1.setPosition(tQuestion1.getSort());
                    return question1;
                })
                .collect(Collectors.toList());

        tOrganisation.getGroups().forEach(tGroup -> {
            TGroupTemplate template = tGroup.getTemplate();
            if (template == null || template.getGroupOfGroupTemplate() == null) {
                return;
            }
            TGroupOfGroupTemplate groupOfGroupTemplate = template.getGroupOfGroupTemplate();

            Stream.concat(groupOfGroupTemplate.getPositive().stream(),
                    groupOfGroupTemplate.getNegativeNot().stream())
                    .forEach(tQuestion -> questionList.stream()
                            .filter(question -> question.getUid() == tQuestion.getUid())
                            .forEach(question -> question.setAnswer(Question.Answer.YES)));
        });

        organisation.setQuestions(questionList);

        return organisation;
    }

}
