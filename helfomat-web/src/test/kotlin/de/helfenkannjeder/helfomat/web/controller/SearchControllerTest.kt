package de.helfenkannjeder.helfomat.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Valentin Zickner
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SearchControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    void searchQuestions_withNoMoreInforation_returnsListOfQuestions() throws Exception {
        // Arrange


        // Act
        mockMvc.perform(get("/api/questions"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(5))
            .andExpect(jsonPath("$[0]").isMap())
            // @formatter:off
            .andExpect(jsonPath("$[0].question").value("Möchtest Du Dich in medizinischer Hilfe ausbilden lassen und diese leisten?"))
            .andExpect(jsonPath("$[1].question").value("Du hast keine Angst vor Feuer, nur Respekt. Möchtest Du lernen, wie man es löscht?"))
            .andExpect(jsonPath("$[2].question").value("Hast Du Interesse daran, Menschen bei Verkehrsunfällen aus Fahrzeugen zu retten?"))
            .andExpect(jsonPath("$[3].question").value("Kannst Du sehr kurzfristig, ggf. auch innerhalb Deiner Arbeitszeiten, für Einsätze zur Verfügung stehen?"))
            .andExpect(jsonPath("$[4].question").value("Ist Kochen eine Deiner Leidenschaften?"));
            // @formatter:on
    }
}