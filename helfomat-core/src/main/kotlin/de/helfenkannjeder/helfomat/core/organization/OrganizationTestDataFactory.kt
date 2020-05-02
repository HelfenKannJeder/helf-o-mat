package de.helfenkannjeder.helfomat.core.organization

import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint
import de.helfenkannjeder.helfomat.core.picture.PictureId
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.*

/**
 * @author Valentin Zickner
 */
object OrganizationTestDataFactory {
    private val ORGANIZATION_1_ADDRESS = Address(
        street = "Grünhutstr. 9",
        city = "Karlsruhe",
        zipcode = "76187",
        location = GeoPoint(49.03765869140625, 8.352746963500977),
        telephone = "0721 9712834"
    )
    private val ORGANIZATION_1_OV_STAB = Group(name = "OV-Stab", description = "Der OV-Stab bildet die Verwaltungseinheit eines Ortsverbands ...")
    private val ORGANIZATION_1_JUGENDGRUPPE = Group(name = "Jugendgruppe", description = "Die THW-Jugendgruppe vermittelt Heranwachsenden im Alter von zehn bis 17 Jahren ...")
    private val ORGANIZATION_1_BAMBINIGRUPPE = Group(name = "Bambini-/Mini-Jugendgruppe", description = "Für Kinder unter zehn Jahren besteht die Möglichkeit, in einer Mini-/Bambini-Jugendgruppe mitzumachen. ...")
    private val ORGANIZATION_1_ZUGTRUPP = Group(name = "Zugtrupp", description = "Der Zugtrupp ist für die Koordination von THW-Einsätzen zuständig ...")
    private val ORGANIZATION_1_BERGRUNG_1 = Group(name = "1. Bergungsgruppe", description = "Die 1. Bergungsgruppe (1. BGr) ist ...")
    private val ORGANIZATION_1_R = Group(name = "Fachgruppe Räumen (FGr R, Typ A und B)", description = "Mit ihren leistungsfähigen Baumaschinen ist die Fachgruppe Räumen an fast ...")
    private val ORGANIZATION_1_BEL = Group(name = "Fachgruppe Beleuchtung (FGr Bel)", description = "Die Fachgruppe Beleuchtung macht die Nacht zum Tag. Sie ...")
    private val ORGANIZATION_1_ABSTUETZEN = Group(name = "Abstützen", description = "Mit unserem Abstützsystem aus Holzelementen lassen sich Bauwerke bis 15 ...")
    private val ORGANIZATION_1_FELDKOCHHERD = Group(name = "Feldkochherd", description = "Mit unserer mobilen Feldküche sind wir in der Lage, ...")
    private val ORGANIZATION_1_THV = Group(name = "Technische Hilfe auf Verkehrswegen (THV-Dienst)", description = "Wir unterstützen die Polizei auf den Bundesautobahnen vor allem in ...")

    @JvmField
    val ORGANIZATION_1 = Organization.Builder(
        id = OrganizationId("50758f69-093e-49b4-a514-c7acbd898036"),
        name = "THW Karlsruhe",
        urlName = "thw-karlsruhe",
        organizationType = OrganizationType.THW
    )
        .setDescription("Wir sind die Hilfsorganisation des Bundes und ....")
        .setWebsite("http://www.thw-karlsruhe.de")
        .setLogo(PictureId("499346d3-8d5e-4028-956c-e6d329634e10"))
        .setPictures(Arrays.asList(
            PictureId("d81c17d7-8eb2-4d88-b353-3f68b856d5ec"),
            PictureId("14275025-1474-4398-bf7a-8258356f8d9a"),
            PictureId("1d3061f1-d141-4a7c-990e-1cea8cdbe3df"),
            PictureId("30201024-09af-43e0-b228-cd59e2a978d6"),
            PictureId("68abd0ae-d2ed-4051-bb9c-18cb0d9f4af2"),
            PictureId("306636ec-470d-4902-92c9-0f60b2dd4364"),
            PictureId("5e9771f4-7fc7-46c1-9853-d575954c7193"),
            PictureId("6605aaf4-ade6-4d11-8264-cdc83ff1aa03"),
            PictureId("19787e61-cfe7-4754-8289-cfa7c9e5076e"),
            PictureId("98edc609-8b54-44be-a89e-ad1b0b6c9f5e"),
            PictureId("de38596d-0428-44da-a318-6492397edd36"),
            PictureId("c961acaf-9123-4d5f-ac61-abae758d9afe"),
            PictureId("cd5c5a20-0c5d-420f-9521-74f6352a91f3"),
            PictureId("eb3d0f92-2792-4655-8b8c-d9d20549c4a4"),
            PictureId("82a7c111-9f1f-43c7-b084-0891386075e3")
        ))
        .setContactPersons(listOf(
            ContactPerson(
                firstname = "David",
                lastname = "Mustermann",
                rank = "Beauftragter für Öffentlichkeitsarbeit",
                mail = "nospam@thw-karlsruhe.de",
                picture = PictureId("e4bc0a17-71de-4dc4-b91b-e82286f11fbc")
            )
        ))
        .setDefaultAddress(ORGANIZATION_1_ADDRESS)
        .setAddresses(listOf(ORGANIZATION_1_ADDRESS))
        .setGroups(Arrays.asList(
            ORGANIZATION_1_OV_STAB,
            ORGANIZATION_1_JUGENDGRUPPE,
            ORGANIZATION_1_BAMBINIGRUPPE,
            ORGANIZATION_1_ZUGTRUPP,
            ORGANIZATION_1_BERGRUNG_1,
            ORGANIZATION_1_R,
            ORGANIZATION_1_BEL,
            ORGANIZATION_1_ABSTUETZEN,
            ORGANIZATION_1_FELDKOCHHERD,
            ORGANIZATION_1_THV
        ))
        .setAttendanceTimes(Arrays.asList(
            AttendanceTime(
                day = DayOfWeek.TUESDAY,
                start = LocalTime.of(19, 30),
                end = LocalTime.of(22, 30),
                note = "unregelmäßig (Dienstplan siehe Homepage)",
                groups = Arrays.asList(
                    ORGANIZATION_1_BERGRUNG_1,
                    ORGANIZATION_1_ZUGTRUPP,
                    ORGANIZATION_1_R,
                    ORGANIZATION_1_BEL,
                    ORGANIZATION_1_JUGENDGRUPPE,
                    ORGANIZATION_1_OV_STAB
                )
            ),
            AttendanceTime(
                day = DayOfWeek.THURSDAY,
                start = LocalTime.of(18, 0),
                end = LocalTime.of(19, 30),
                note = "Jeder 1. und 3. Donnerstag im Monat",
                groups = listOf(ORGANIZATION_1_BAMBINIGRUPPE)
            ),
            AttendanceTime(
                day = DayOfWeek.SATURDAY,
                start = LocalTime.of(8, 0),
                end = LocalTime.of(16, 0),
                note = "unregelmäßig, Termine siehe Homepage",
                groups = Arrays.asList(
                    ORGANIZATION_1_BERGRUNG_1,
                    ORGANIZATION_1_ZUGTRUPP,
                    ORGANIZATION_1_R,
                    ORGANIZATION_1_BEL
                )
            )
        ))
        .setVolunteers(Arrays.asList(
            Volunteer(
                firstname = "Carolin",
                motivation = "Das THW ist nur für Männer... von wegen. ...",
                picture = PictureId("e37d8e15-eedf-48ad-b98f-64165d144347")
            ),
            Volunteer(
                firstname = "David",
                motivation = "Viele Menschen, die mein Leben bereichern, hätte ich ohne das THW nie ...",
                picture = PictureId("b2c915dd-c9c4-4559-9e9a-1265e775949f")
            ),
            Volunteer(
                firstname = "Ilona",
                motivation = "Unimog fahren, Rettungsgeräte einsetzen, bei interessanten Übungen mitmachen, im Einsatz ...",
                picture = PictureId("d52afe09-9303-47e3-b18a-d5575d39fd91")
            ),
            Volunteer(
                firstname = "Lisa",
                motivation = "Seit 2006 bin ich beim THW und habe hier schon viele nette Leute kennengelernt. Schön ...",
                picture = PictureId("251aa6b3-f844-444b-9c87-8eceb6dbea34")
            ),
            Volunteer(
                firstname = "Steffen",
                motivation = "Verantwortung übernehmen zu können, technisches Rettungsgerät einzusetzen und ab und an unter ...",
                picture = PictureId("c9213d2a-4498-43f8-af18-07cede4b3714")
            ),
            Volunteer(
                firstname = "Thilo",
                motivation = "Ich bin seit 2006 beim THW dabei. Hier habe ich ein neues Hobby und viele neue Freunde gefunden, ...",
                picture = PictureId("5858ef6e-a580-4e00-9867-255889fe24b1")
            ),
            Volunteer(
                firstname = "Timo",
                motivation = "Der Zusammenhalt und die Kameradschaft sind die Hauptgründe, warum ich im THW bin. Außerdem gibt ...",
                picture = PictureId("1d86181a-b5c1-4951-8f33-7477cd835e18")
            )
        ))
        .build()
}