package de.helfenkannjeder.helfomat.web.controller

import de.helfenkannjeder.helfomat.core.ProfileRegistry
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

/**
 * @author Valentin Zickner
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(ProfileRegistry.TEST)
class SearchControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @Throws(Exception::class)
    fun searchQuestions_withNoMoreInformation_returnsListOfQuestions() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/questions"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(5))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0]").isMap)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].question").value("Möchtest Du Dich in medizinischer Hilfe ausbilden lassen und diese leisten?"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].question").value("Du hast keine Angst vor Feuer, nur Respekt. Möchtest Du lernen, wie man es löscht?"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[2].question").value("Hast Du Interesse daran, Menschen bei Verkehrsunfällen aus Fahrzeugen zu retten?"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[3].question").value("Kannst Du sehr kurzfristig, ggf. auch innerhalb Deiner Arbeitszeiten, für Einsätze zur Verfügung stehen?"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[4].question").value("Ist Kochen eine Deiner Leidenschaften?"))
    }
}