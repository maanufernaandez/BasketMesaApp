package com.example.basketmesaapp.utils

import com.example.basketmesaapp.model.CategoriaConfig
import com.example.basketmesaapp.model.Equipo

object DataConstants {
    val listaCategoriasFijas = listOf(
        CategoriaConfig("LF Challenge", 64.0, 0.0),
        CategoriaConfig("Liga Eba", 38.83, 0.0),
        CategoriaConfig("1ª Division Femenina", 39.50, 0.0),
        CategoriaConfig("1ª Division Masculina", 39.50, 0.0),
        CategoriaConfig("2ª Division Femenina", 31.60, 0.0),
        CategoriaConfig("2ª Division Masculina", 25.0, 0.0),
        CategoriaConfig("Senior Masculino 1ª", 19.70, 0.0),
        CategoriaConfig("Senior Femenino 1ª", 19.70, 0.0),
        CategoriaConfig("Senior Masculino 2ª", 19.20, 0.0),
        CategoriaConfig("Senior Femenino 2ª", 19.20, 0.0),
        CategoriaConfig("Junior Masculino 1ª", 17.0, 0.0),
        CategoriaConfig("Junior Femenino 1ª", 17.0, 0.0),
        CategoriaConfig("Junior Masculino 2ª", 15.90, 0.0),
        CategoriaConfig("Junior Femenino 2ª", 15.90, 0.0),
        CategoriaConfig("Cadete Masculino 1ª", 11.30, 0.0),
        CategoriaConfig("Cadete Femenino 1ª", 11.30, 0.0),
        CategoriaConfig("Torneo Veteranos", 13.35, 0.0),
        CategoriaConfig("Torneo Veteranas", 13.35, 0.0),
        CategoriaConfig("Copa Navarra", 24.95, 0.0),
        CategoriaConfig("Selección Navarra", 0.0, 0.0)
    )

    val categoriasData = mapOf(
        "LF CHallenge" to listOf(
            Equipo("Al-Qázeres Extremadura", emptyList()),
            Equipo("Ardoi", listOf("Arrosadía", "Municipal (Zizur)").sorted()),
            Equipo("Bosonit Unibasket", emptyList()),
            Equipo("Cajasol Baloncesto Sevilla Fem", emptyList()),
            Equipo("Domusa Teknik ISB", emptyList()),
            Equipo("Fustema NBF Castelló", emptyList()),
            Equipo("La Cordá de Paterna NB", emptyList()),
            Equipo("La Laguna Toyota Adareva", emptyList()),
            Equipo("Lima-Horta Barcelona", emptyList()),
            Equipo("Melilla", emptyList()),
            Equipo("Recoletas Zamora", emptyList()),
            Equipo("Unicaja Mijas", emptyList())
        ),
        "Liga Eba" to listOf(
            Equipo("Blendio", emptyList()),
            Equipo("De Morro Fino Cantbasket04", emptyList()),
            Equipo("Iruki Take", emptyList()),
            Equipo("Megacalzado Ardoi", listOf("Municipal (Zizur)")),
            Equipo("Piélagos", emptyList()),
            Equipo("Recoletas Salud Carbajosa", emptyList()),
            Equipo("Restaurante los Arcos CB Solares", emptyList()),
            Equipo("Ulacia ZKE", emptyList()),
            Equipo("Universidad Deusto Loiola", emptyList()),
            Equipo("Valle de Egüés", listOf("Sarriguren"))
        ),
        "1ª Division Femenina" to listOf(
            Equipo("Aranguren Mutilbasket", listOf("Mutilva (Piscinas)")),
            Equipo("Araski", emptyList()),
            Equipo("Askartza Claret", emptyList()),
            Equipo("Chubby Apps Araba", emptyList()),
            Equipo("Graficas Juaristi", emptyList()),
            Equipo("Hondarribia Ikasbasket", emptyList()),
            Equipo("Ibaeta Basket Easo", emptyList()),
            Equipo("La Salle Versia", emptyList()),
            Equipo("Navarro Villoslada", listOf("Ermitagaña", "Iribarren").sorted()),
            Equipo("San Ignacio", listOf("San Ignacio")),
            Equipo("Tabirako Baque", emptyList())
        ),
        "1ª Division Masculina" to listOf(
            Equipo("Aisla Armeti Ibaizabal", emptyList()),
            Equipo("Askartza Claret", emptyList()),
            Equipo("Atletico San Sebastian", emptyList()),
            Equipo("CB Santurtzi SK", emptyList()),
            Equipo("Disdira Bitxitegia ISB", emptyList()),
            Equipo("Easo Basket", emptyList()),
            Equipo("Hydropyc Urgatzi", emptyList()),
            Equipo("La Salle Versia", emptyList()),
            Equipo("Leioa SBT", emptyList()),
            Equipo("Navasket 1DM", listOf("Arrosadia", "Teresianas").sorted()),
            Equipo("San Prudencio", emptyList()),
            Equipo("Tabirako Baque", emptyList()),
            Equipo("Valle de Egüés", listOf("Sarriguren")),
            Equipo("Vascons Getxo", emptyList())
        ),
        "2ª Division Femenina" to listOf(
            Equipo("Alkivent Toju", emptyList()),
            Equipo("Atletico San Sebastian", emptyList()),
            Equipo("Escolapios Bilbao", emptyList()),
            Equipo("Hostal Lola Gazte Berriak", listOf("Idaki (Ansoain)")),
            Equipo("Lagunak", listOf("Municipal (Barañain)")),
            Equipo("Liceo Monjardín", listOf("Liceo Monjardín")),
            Equipo("Megacalzado Ardoi", listOf("Municipal (Zizur)")),
            Equipo("Navasket 2DF", listOf("Teresianas")),
            Equipo("Spirit Hotels Santutxu", emptyList()),
            Equipo("Valle de Egüés", listOf("Maristas", "Olaz", "Salesianos", "Sarriguren").sorted())
        ),
        "2ª Division Masculina" to listOf(
            Equipo("Anpri Gazte Berriak", listOf("Idaki (Ansoain)")),
            Equipo("Aranguren Mutilbasket", listOf("Irulegui")),
            Equipo("C.D Universidad de Navarra", listOf("UNAV")),
            Equipo("CB Noain", listOf("Municipal (Noain)")),
            Equipo("CBASK", listOf("Zelandi (Alsasua)")),
            Equipo("Humiclima", listOf("Berriozar")),
            Equipo("Larraona Claret", listOf("Larraona")),
            Equipo("Liceo Monjardín \"R\"", listOf("Liceo Monjardin")),
            Equipo("Megacalzado Ardoi", listOf("Municipal (Zizur)")),
            Equipo("San Ignacio", listOf("San Ignacio"))
        ),
        "Senior Masculino 1ª" to listOf(
            Equipo("Avia Zizur Ardoi", listOf("Municipal (Zizur)")),
            Equipo("Cantolagua A", listOf("Municipal (Sangüesa)")),
            Equipo("CBSA El Navarrico", listOf("Alfonso X El Sabio (San Adrián)")),
            Equipo("Cendea de Galar", listOf("Esquiroz")),
            Equipo("Ega Perfil Oncineda", listOf("Lizarreria (Estella)")),
            Equipo("Lagunak A", listOf("Lagunak (Piscinas)", "Municipal (Barañain)").sorted()),
            Equipo("Larraona Claret", listOf("Larraona")),
            Equipo("Liceo Monjardín \"B\"", listOf("Liceo Monjardin")),
            Equipo("Muthiko Alaiak", listOf("Iribarren")),
            Equipo("Navasket SM'Old", listOf("Teresianas")),
            Equipo("Payvi Taberna", listOf("San Jorge")),
            Equipo("San Cernin \"A\"", listOf("San Cernin")),
            Equipo("San Ignacio", listOf("San Ignacio")),
            Equipo("Valle de Egüés A", listOf("Olaz", "Sarriguren").sorted()),
            Equipo("Valle del Ebro", listOf("IES Valle del Ebro (Tudela)"))
        ),
        "Senior Femenino 1ª" to listOf(
            Equipo("Avia Zizur Ardoi", listOf("Municipal (Zizur)")),
            Equipo("Cantolagua", listOf("Municipal (Sangüesa)")),
            Equipo("CB Noain A", listOf("Municipal (Noain)")),
            Equipo("CBASK", listOf("Zelandi (Alsasua)")),
            Equipo("C.D. Universidad de Navarra", listOf("UNAV")),
            Equipo("Lagunak", listOf("Lagunak (Piscinas)", "Municipal (Barañain)").sorted()),
            Equipo("Liceo Monjardín", listOf("Liceo Monjardin")),
            Equipo("Navarro Villoslada A", listOf("Ermitagaña", "Iribarren").sorted()),
            Equipo("Navasket SF'Old", listOf("Teresianas")),
            Equipo("San Ignacio", listOf("San Ignacio")),
            Equipo("Sanduzelai", listOf("San Jorge")),
            Equipo("Valle de Egüés A", listOf("Maristas", "Olaz", "Salesianos", "Sarriguren").sorted()),
            Equipo("Valle de Egüés C", listOf("Maristas", "Olaz", "Salesianos", "Sarriguren").sorted())
        ),
        "Senior Masculino 2ª" to listOf(
            Equipo("AZK Alde Zaharreko Kluba", listOf("Buztintxuri", "Rochapea").sorted()),
            Equipo("Berriozar MKE", listOf("Berriozar")),
            Equipo("Burlada", listOf("Elizgibela")),
            Equipo("CB Noain", listOf("AIT Sport Center (Torres de Elorz)", "Municipal (Noain)").sorted()),
            Equipo("CBP Selco Electrónica", listOf("Municipal (Peralta)")),
            Equipo("CDB Gares", listOf("Municipal (Puente la Reina)")),
            Equipo("Gazte Berriak", listOf("Idaki")),
            Equipo("Grupo Rubio Gima Arenas", listOf("SDR Arenas (Tudela)")),
            Equipo("Lagunak B", listOf("Lagunak (Piscinas)")),
            Equipo("Liceo Monjardín \"D\"", listOf("Liceo Monjardin")),
            Equipo("Mendillorri Egüés B", listOf("Olaz", "Sarriguren", "Trinkete").sorted()),
            Equipo("Navasket SM'New", listOf("Rochapea", "Teresianas").sorted()),
            Equipo("Oberena", listOf("Oberena")),
            Equipo("Sagrado Corazón", listOf("Sagrado Corazón")),
            Equipo("San Cernin \"B\"", listOf("San Cernin")),
            Equipo("Sanduzelai", listOf("San Jorge")),
            Equipo("Valle de Egüés B", listOf("Maristas", "Olaz", "Salesianos", "Sarriguren").sorted()),
            Equipo("Zona Media Tafalla", listOf("Velodromo (Tafalla)"))
        ),
        "Senior Femenino 2ª" to listOf(
            Equipo("CDB Gares", listOf("Municipal (Puente la Reina)")),
            Equipo("CD Noain B", listOf("AIT Sport Center (Torres de Elorz)", "Municipal (Noain)").sorted()),
            Equipo("CBP Inregal", listOf("Municipal (Peralta)")),
            Equipo("Gazte Berriak", listOf("Idaki")),
            Equipo("Liceo Monjardín 'H'", listOf("Liceo Monjardin")),
            Equipo("Megacalzado Ardoi", listOf("IES Zizur Mayor", "Municipal (Zizur)").sorted()),
            Equipo("Mendillorri Egüés", listOf("Maristas", "Olaz", "Salesianos", "Sarriguren").sorted()),
            Equipo("Navarro Villoslada B", listOf("Ermitagaña", "IES Navarro Villoslada (Frontón)", "Iribarren").sorted()),
            Equipo("Navarro Villoslada C", listOf("Ermitagaña", "IES Navarro Villoslada (Frontón)", "Iribarren").sorted()),
            Equipo("Navasket SF'New", listOf("Teresianas")),
            Equipo("Sagrado Corazon", listOf("Sagrado Corazon")),
            Equipo("San Cernin \"A\"", listOf("San Cernin")),
            Equipo("UD Beriain FB", listOf("Municipal (Beriain)"))
        ),
        "Junior Masculino 1ª" to listOf(
            Equipo("Anaquel IES Valle del Ebro", listOf("IES Valle del Ebro (Tudela)")),
            Equipo("Aranguren Mutilbasket A", listOf("Irulegui")),
            Equipo("CBP Inregal", listOf("Municipal (Peralta)")),
            Equipo("Gazte Berriak", listOf("Idaki")),
            Equipo("Gima Grupo Rubio Arenas", listOf("SDR Arenas (Tudela)")),
            Equipo("Larraona Claret", listOf("Larraona")),
            Equipo("Liceo Monjardín \"O\"", listOf("Liceo Monjardin")),
            Equipo("Megacalzado Ardoi", listOf("Municipal (Zizur)")),
            Equipo("Navarro Villoslada A", listOf("Ermitagaña", "Iribarren").sorted()),
            Equipo("San Ignacio", listOf("San Ignacio")),
            Equipo("Valle de Egüés A", listOf("Maristas", "Olaz", "Salesianos", "Sarriguren").sorted())
        ),
        "Junior Femenino 1ª" to listOf(
            Equipo("ALZ Motors Mutilbasket A", listOf("Irulegui")),
            Equipo("EGA Perfil Oncineda", listOf("Lizarreria (Estella)")),
            Equipo("Gazte Berriak", listOf("Idaki")),
            Equipo("Liceo Monjardín \"A\"", listOf("Liceo Monjardin")),
            Equipo("Megacalzado Ardoi", listOf("Municipal (Zizur)")),
            Equipo("Navarro Villoslada A", listOf("Ermitagaña", "Iribarren").sorted()),
            Equipo("Navasket JF'Old", listOf("Teresianas")),
            Equipo("Valle de Egüés A", listOf("Maristas", "Olaz", "Salesianos", "Sarriguren").sorted())
        ),
        "Junior Masculino 2ª" to listOf(
            Equipo("Aranguren Mutilbasket B", listOf("Irulegui")),
            Equipo("Avia Zizur Ardoi", listOf("IES Zizur Mayor", "Municipal (Zizur)").sorted()),
            Equipo("Berriozar MKE", listOf("Berriozar")),
            Equipo("Biurdana Navasket JM'New", listOf("Rochapea", "Teresianas").sorted()),
            Equipo("Biurdana Navasket JM'Old", listOf("Rochapea", "Teresianas").sorted()),
            Equipo("Burlada", listOf("Elizgibela")),
            Equipo("CBASK", listOf("Zelandi (Alsasua)")),
            Equipo("Cantolagua", listOf("Municipal (Sangüesa)")),
            Equipo("CDB Gares", listOf("Municipal (Puente la Reina)")),
            Equipo("EGA Perfil Oncineda", listOf("Lizarreria (Estella)")),
            Equipo("Lagunak", listOf("Lagunak (Piscinas)", "Municipal (Barañain)").sorted()),
            Equipo("Larraona Claret", listOf("Larraona")),
            Equipo("Liceo Monjardín \"I\"", listOf("Liceo Monjardin")),
            Equipo("Navarro Villoslada B", listOf("Ermitagaña", "IES Navarro Villoslada (Frontón)", "Iribarren").sorted()),
            Equipo("Valle de Egüés", listOf("Maristas", "Olaz", "Salesianos", "Sarriguren").sorted()),
            Equipo("Zona Media Tafalla", listOf("Velodromo (Tafalla)"))
        ),
        "Junior Femenino 2ª" to listOf(
            Equipo("ADI Burlada", listOf("Elizgibela")),
            Equipo("Ademar Nike", listOf("Maristas")),
            Equipo("Anaquel IES Valle del Ebro", listOf("IES Valle del Ebro (Tudela)")),
            Equipo("Aranguren Mutilbasket B", listOf("Irulegui")),
            Equipo("Avia Zizur Ardoi", listOf("IES Zizur Mayor", "Municipal (Zizur)").sorted()),
            Equipo("Burlada Belagua", listOf("Elizgibela")),
            Equipo("CB Noain", listOf("Municipal (Noain)")),
            Equipo("CBASK", listOf("Zelandi (Alsasua)")),
            Equipo("Cantolagua", listOf("Municipal (Sangüesa)")),
            Equipo("Gazte Berriak", listOf("Idaki")),
            Equipo("Larraona Claret", listOf("Larraona")),
            Equipo("Liceo Monjardín \"F\"", listOf("Liceo Monjardin")),
            Equipo("Liceo Monjardín \"S\"", listOf("Liceo Monjardin")),
            Equipo("Loyola", listOf("San Ignacio")),
            Equipo("Mendillorri 08 F", listOf("Trinkete")),
            Equipo("Navarro Villoslada B", listOf("Ermitagaña", "IES Navarro Villoslada (Frontón)", "Iribarren").sorted()),
            Equipo("Navarro Villoslada C", listOf("Ermitagaña", "IES Navarro Villoslada (Frontón)", "Iribarren").sorted()),
            Equipo("Navasket JF'2K89", listOf("Rochapea", "Teresianas").sorted()),
            Equipo("Navasket JF'K78", listOf("Rochapea", "Teresianas").sorted()),
            Equipo("Sagrado Corazon Orhi", listOf("Sagrado Corazon")),
            Equipo("San Cernin \"A\"", listOf("San Cernin")),
            Equipo("San Ignacio", listOf("San Ignacio")),
            Equipo("Valle de Egües B", listOf("Maristas", "Olaz", "Salesianos", "Sarriguren").sorted())
        ),
        "Cadete Masculino 1ª" to listOf(
            Equipo("Ademar Apolo", listOf("Maristas")),
            Equipo("Aranguren Mutilbasket A", listOf("Irulegui")),
            Equipo("Gazte Berriak", listOf("Idaki")),
            Equipo("Megacalzado Ardoi", listOf("Municipal (Zizur)")),
            Equipo("Navarro Villoslada A", listOf("Ermitagaña", "Iribarren").sorted()),
            Equipo("San Cernin \"A\"", listOf("San Cernin"))
        ),
        "Cadete Femenino 1ª" to listOf(
            Equipo("Ademar Apolo", listOf("Maristas")),
            Equipo("Aranguren Mutilbasket A", listOf("Irulegui")),
            Equipo("Gazte Berriak", listOf("Idaki")),
            Equipo("Megacalzado Ardoi", listOf("Municipal (Zizur)")),
            Equipo("Navarro Villoslada A", listOf("Ermitagaña", "Iribarren").sorted()),
            Equipo("San Cernin \"A\"", listOf("San Cernin"))
        ),
        "Torneo Veteranos" to listOf(
            Equipo("Ademar Apolo", listOf("Maristas")),
            Equipo("Aranguren Mutilbasket A", listOf("Irulegui")),
            Equipo("Gazte Berriak", listOf("Idaki")),
            Equipo("Megacalzado Ardoi", listOf("Municipal (Zizur)")),
            Equipo("Navarro Villoslada A", listOf("Ermitagaña", "Iribarren").sorted()),
            Equipo("San Cernin \"A\"", listOf("San Cernin"))
        ),
        "Torneo Veteranas" to listOf(
            Equipo("Ademar Apolo", listOf("Maristas")),
            Equipo("Aranguren Mutilbasket A", listOf("Irulegui")),
            Equipo("Gazte Berriak", listOf("Idaki")),
            Equipo("Megacalzado Ardoi", listOf("Municipal (Zizur)")),
            Equipo("Navarro Villoslada A", listOf("Ermitagaña", "Iribarren").sorted()),
            Equipo("San Cernin \"A\"", listOf("San Cernin"))
        ),
        "Copa Navarra" to listOf(
            Equipo("Ademar Apolo", listOf("Maristas")),
            Equipo("Aranguren Mutilbasket A", listOf("Irulegui")),
            Equipo("Gazte Berriak", listOf("Idaki")),
            Equipo("Megacalzado Ardoi", listOf("Municipal (Zizur)")),
            Equipo("Navarro Villoslada A", listOf("Ermitagaña", "Iribarren").sorted()),
            Equipo("San Cernin \"A\"", listOf("San Cernin"))
        )
    )

    val festivosTemporada = listOf(
        "2026-10-12",
        "2026-11-01",
        "2026-11-02",
        "2026-12-03",
        "2026-12-06",
        "2026-12-07",
        "2026-12-08",
        "2026-12-25",
        "2027-01-01",
        "2027-01-06",
        "2027-03-19",
        "2027-03-25",
        "2027-03-26",
        "2027-03-29",
        "2027-05-01"
    )

    const val TEMPORADAINICIO: String = "2026-09-01"
    const val TEMPORADAFIN: String = "2027-05-31"

    val preciosDesplazamiento = mapOf(
        "Alsasua" to Pair(35.0, 8.0),
        "Estella" to Pair(30.10, 6.88),
        "Peralta" to Pair(50.0, 10.0),
        "Puente" to Pair(20.0, 5.0),
        "San Adrián" to Pair(56.7, 12.96),
        "Sangüesa" to Pair(50.0, 10.0),
        "Tafalla" to Pair(20.0, 5.0),
        "Tudela" to Pair(65.80, 15.04)
    )
}