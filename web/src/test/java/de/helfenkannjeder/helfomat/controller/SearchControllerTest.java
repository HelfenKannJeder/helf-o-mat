package de.helfenkannjeder.helfomat.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequest;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateResponse;
import org.elasticsearch.client.Client;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Valentin Zickner
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchControllerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private Client client;


    @Value("${elasticsearch.index}")
    private String index;

    @Value("${elasticsearch.type.organisation}")
    private String typeOrganisation;

    @Value("classpath:/organisations.json")
    private Resource organisations;

    @Value("classpath:/template.json")
    private Resource template;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    private String getTemplate() throws IOException {
        return new String(ByteStreams.toByteArray(template.getInputStream()), "UTF-8");
    }

    private void createTemplate(String templateName) throws IOException {
        PutIndexTemplateResponse response = client.admin().indices().putTemplate(
                new PutIndexTemplateRequest(templateName).source(getTemplate())).actionGet();
        if (!response.isAcknowledged()) {
            throw new RuntimeException("Error creating template");
        }
    }


    @Before
    public void setUpElasticsearchContent() throws Exception {

        JsonNode organisations = objectMapper.readTree(this.organisations.getFile());
        createTemplate("helfomat*");

        for (JsonNode organisation : organisations) {
            client.prepareIndex()
                    .setIndex(index)
                    .setType(typeOrganisation)
                    .setId(organisation.get("id").asText())
                    .setSource(objectMapper.writeValueAsString(organisation))
                    .execute()
                    .actionGet();
        }

    }

    @Test
    public void searchQuestions_withNoMoreInforation_returnsListOfQuestions() throws Exception {
        // Arrange


        // Act
        mockMvc.perform(get("/questions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(18))
                .andExpect(jsonPath("$[0]").isMap())
                // @formatter:off
                .andExpect(jsonPath("$[0].question").value("Möchtest Du gerne Einsatzfahrzeuge - ggf. auch mit Blaulicht und Martinshorn - fahren?"))
                .andExpect(jsonPath("$[1].question").value("Möchtest Du Dich in medizinischer Hilfe ausbilden lassen und diese leisten?"))
                .andExpect(jsonPath("$[2].question").value("Ist Kochen eine Deiner Leidenschaften?"))
                .andExpect(jsonPath("$[3].question").value("Du hast keine Angst vor Feuer, nur Respekt. Möchtest Du lernen, wie man es löscht?"))
                .andExpect(jsonPath("$[4].question").value("Kannst Du sehr kurzfristig, ggf. auch innerhalb Deiner Arbeitszeiten, für Einsätze zur Verfügung stehen?"))
                .andExpect(jsonPath("$[5].question").value("Hast Du Interesse daran, Menschen bei Verkehrsunfällen aus Fahrzeugen zu retten?"))
                .andExpect(jsonPath("$[6].question").value("Möchtest Du aktiv im Naturschutz mitwirken?"))
                .andExpect(jsonPath("$[7].question").value("Klettern, Bergsteigen, Verletzte retten - auch aus der Kletterwand? Wäre das etwas für Dich?"))
                .andExpect(jsonPath("$[8].question").value("Bist Du handwerklich begabt? Den Umgang mit Kettensäge und Trennschleifer möchtest Du erlernen?"))
                .andExpect(jsonPath("$[9].question").value("Deinen Hund zum Suchhund ausbilden, mit ihm vermisste Menschen orten und retten? Interesse?"))
                .andExpect(jsonPath("$[10].question").value("Hast Du was für Wassersport übrig? Boot fahren, Retten von Menschen aus Gewässern?"))
                .andExpect(jsonPath("$[11].question").value("Möchtest Du aktiv als Besatzungsmitglied eines Rettungs- oder Krankentransportwagens mitfahren?"))
                .andExpect(jsonPath("$[12].question").value("Magst Du die Bergwelt? Kannst Du Dir vorstellen, hier Menschen zu retten?"))
                .andExpect(jsonPath("$[13].question").value("Bist Du handwerklich interessiert und kannst Strom, Heizung, Wasser, etc. in Notunterkünften installieren?"));
                // @formatter:on
    }
}