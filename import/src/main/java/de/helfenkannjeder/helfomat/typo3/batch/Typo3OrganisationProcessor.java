package de.helfenkannjeder.helfomat.typo3.batch;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.helfenkannjeder.helfomat.domain.AddressBuilder;
import de.helfenkannjeder.helfomat.domain.GeoPoint;
import de.helfenkannjeder.helfomat.domain.Group;
import de.helfenkannjeder.helfomat.domain.Organisation;
import de.helfenkannjeder.helfomat.domain.OrganisationBuilder;
import de.helfenkannjeder.helfomat.domain.Question;
import de.helfenkannjeder.helfomat.service.ListCache;
import de.helfenkannjeder.helfomat.typo3.domain.TGroupOfGroupTemplate;
import de.helfenkannjeder.helfomat.typo3.domain.TGroupTemplate;
import de.helfenkannjeder.helfomat.typo3.domain.TOrganisation;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Valentin Zickner
 */
@Component
@JobScope
public class Typo3OrganisationProcessor implements ItemProcessor<TOrganisation, Organisation> {

    private final ListCache<Question> allQuestions;

    @Autowired
    public Typo3OrganisationProcessor(ListCache<Question> allQuestions) {
        this.allQuestions = allQuestions;
    }

    @Override
    public Organisation process(TOrganisation tOrganisation) throws Exception {
        // We don't want to import organisations without a specified type
        if(tOrganisation.getOrganisationtype() == null) {
            return null;
        }

        OrganisationBuilder organisation = new OrganisationBuilder()
                .setId(UUID.randomUUID().toString())
                .setName(tOrganisation.getName())
                .setType(tOrganisation.getOrganisationtype().getName())
                .setDescription(tOrganisation.getDescription())
                .setLogo(tOrganisation.getLogo())
                .setWebsite(tOrganisation.getWebsite())
                .setMapPin(tOrganisation.getOrganisationtype().getPicture());

        String pictures = tOrganisation.getPictures();
        if (pictures != null) {
            organisation.setPictures(Arrays.asList(pictures.split(",")));
        }

        organisation.setAddresses(tOrganisation.getAddresses().stream().map(tAddress -> new AddressBuilder()
                .setWebsite(tAddress.getWebsite())
                .setTelephone(tAddress.getTelephone())
                .setStreet(tAddress.getStreet())
                .setAddressAppendix(tAddress.getAddressappendix())
                .setCity(tAddress.getCity())
                .setZipcode(tAddress.getZipcode())
                .setLocation(new GeoPoint(tAddress.getLatitude(), tAddress.getLongitude()))
                .build()).collect(Collectors.toList()));

        final List<Question> questionList = allQuestions.getAll().stream()
                .map(tQuestion1 -> new Question(tQuestion1, Question.Answer.NO))
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

        organisation.setGroups(tOrganisation.getGroups().stream().map(tGroup -> {
            Group group = new Group();
            group.setName(tGroup.getName());
            group.setDescription(tGroup.getDescription());
            return group;
        }).collect(Collectors.toList()));

        return organisation.build();
    }

}
