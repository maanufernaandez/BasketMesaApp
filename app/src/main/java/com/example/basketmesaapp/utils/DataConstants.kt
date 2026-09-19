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
        CategoriaConfig("Copa Navarra Femenina", 25.45, 0.0),
        CategoriaConfig("Copa Navarra Masculina", 25.45, 0.0),
        CategoriaConfig("Selección Navarra", 0.0, 0.0)
    )

    val categoriasData = mapOf(
        "LF Challenge" to listOf(
            Equipo("Al-Qázeres Ext", emptyList()),
            Equipo("Ardoi", listOf("Arrosadía", "Municipal (Zizur)").sorted()),
            Equipo("Bosonit Unibasket", emptyList()),
            Equipo("Cajasol Sevilla", emptyList()),
            Equipo("Domusa Teknik ISB", emptyList()),
            Equipo("Fustecma Castelló", emptyList()),
            Equipo("Juventut Badalona", emptyList()),
            Equipo("La Laguna Toyota", emptyList()),
            Equipo("Lima-Horta Barcelona", emptyList()),
            Equipo("Melilla", emptyList()),
            Equipo("Mipelletymas Leon", emptyList()),
            Equipo("Recoletas Zamora", emptyList()),
            Equipo("Spar Gran Canaria", emptyList()),
            Equipo("Sparking Truth Maristas", emptyList()),
            Equipo("Unicaja Mijas", emptyList()),
            Equipo("Valencia Basket", emptyList())
        ).sortedBy { it.nombre },

        "Liga Eba" to listOf(
            Equipo("Baloncesto La Flecha", emptyList()),
            Equipo("Cantbasket04", emptyList()),
            Equipo("Deusto Loiola", emptyList()),
            Equipo("Hotel 4Postes Avila", emptyList()),
            Equipo("Lis Data Solutions Bezana", emptyList()),
            Equipo("Los Arcos CB Solares", emptyList()),
            Equipo("Megacalzado Ardoi", listOf("Municipal (Zizur)")),
            Equipo("Piélagos", emptyList()),
            Equipo("San Prudencio", emptyList()),
            Equipo("Teknei Bizkaia Zornotza", emptyList()),
            Equipo("Ulacia ZKE", emptyList()),
            Equipo("Valle de Egüés", listOf("Sarriguren"))
        ).sortedBy { it.nombre },

        "1ª Division Femenina" to listOf(
            Equipo("Aranguren Mutilbasket", listOf("Mutilva (Piscinas)")),
            Equipo("Araski", emptyList()),
            Equipo("Askartza Claret", emptyList()),
            Equipo("Chubby Apps Araba", emptyList()),
            Equipo("Deusto Loiola", emptyList()),
            Equipo("Gernika KESB", emptyList()),
            Equipo("Graficas Juaristi", emptyList()),
            Equipo("Hondarribia Ikasbasket", emptyList()),
            Equipo("Ibaeta Basket Easo", emptyList()),
            Equipo("La Salle Versia", emptyList()),
            Equipo("Navarro Villoslada", listOf("Ermitagaña", "Iribarren").sorted()),
            Equipo("San Ignacio", listOf("San Ignacio")),
            Equipo("Tabirako Baque", emptyList()),
            Equipo("Vascons Getxo", emptyList())
        ).sortedBy { it.nombre },

        "1ª Division Masculina" to listOf(
            Equipo("Aisla Armeti Ibaizabal", emptyList()),
            Equipo("Araberri Basket Club", emptyList()),
            Equipo("Askartza Claret", emptyList()),
            Equipo("Baskonia", emptyList()),
            Equipo("CB Santurtzi SK", emptyList()),
            Equipo("Disdira Bitxitegia ISB", emptyList()),
            Equipo("Easo Basket", emptyList()),
            Equipo("Leioa SBT", emptyList()),
            Equipo("Mondragon Unibertsitatea", emptyList()),
            Equipo("Navasket 1DM", listOf("Arrosadia", "Teresianas").sorted()),
            Equipo("Spirit Hotels Santutxu", emptyList()),
            Equipo("Urgatzi", emptyList()),
            Equipo("Valle de Egüés", listOf("Sarriguren")),
            Equipo("Vascons Getxo", emptyList())
        ).sortedBy { it.nombre },

        "2ª Division Femenina" to listOf(
            Equipo("Alkivent Toju", emptyList()),
            Equipo("Atletico San Sebastian", emptyList()),
            Equipo("Axa Ortiz de Zarate Amurrio", emptyList()),
            Equipo("Escolapios Bilbao", emptyList()),
            Equipo("Fisioterapia Unamuno", emptyList()),
            Equipo("Gazte Berriak", listOf("Idaki (Ansoain)")),
            Equipo("Goierri Aldapa", emptyList()),
            Equipo("Lagunak", listOf("Municipal (Barañain)")),
            Equipo("Liceo Monjardín", listOf("Liceo Monjardín")),
            Equipo("Megacalzado Ardoi", listOf("Municipal (Zizur)")),
            Equipo("Navasket 2DF", listOf("Teresianas")),
            Equipo("Ondoan Ointxe", emptyList()),
            Equipo("Spirit Hotels Santutxu", emptyList()),
            Equipo("Tabirako Baque", emptyList()),
            Equipo("Valle de Egüés", listOf("Maristas", "Olaz", "Salesianos", "Sarriguren").sorted()),
            Equipo("Zornotza St Bilbao Basket", emptyList())
        ).sortedBy { it.nombre },

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
            Equipo("Navarro Villoslada A", listOf("Ermitagaña", "Iribarren").sorted()),
            Equipo("San Cernin", listOf("San Cernin")),
            Equipo("San Ignacio-Nasabi", listOf("San Ignacio"))
        ).sortedBy { it.nombre },

        "Senior Masculino 1ª" to listOf(
            Equipo("Berriozar MKE", listOf("Berriozar")),
            Equipo("CB Noain", listOf("Municipal (Noain)")),
            Equipo("CBSA El Navarrico", listOf("Alfonso X El Sabio (San Adrián)")),
            Equipo("Cendea de Galar", listOf("Esquiroz")),
            Equipo("IES Valle del Ebro", listOf("IES Valle del Ebro (Tudela)")),
            Equipo("Lagunak A", listOf("Lagunak (Piscinas)", "Municipal (Barañain)").sorted()),
            Equipo("Larraona Claret", listOf("Larraona")),
            Equipo("Liceo Monjardín \"B\"", listOf("Liceo Monjardin")),
            Equipo("Megacalzado Ardoi", listOf("Municipal (Zizur)")),
            Equipo("Mendillorri Egües", listOf("Maristas", "Olaz", "Salesianos", "Sarriguren").sorted()),
            Equipo("Muthiko Alaiak", listOf("Iribarren")),
            Equipo("Navasket SM'old", listOf("Teresianas")),
            Equipo("Payvi Taberna Sanduzelai", listOf("San Jorge")),
            Equipo("San Ignacio", listOf("San Ignacio")),
            Equipo("Txoribeltz Sanduzelai", listOf("San Jorge")),
            Equipo("Valle de Egüés A", listOf("Maristas", "Olaz", "Salesianos", "Sarriguren").sorted())
        ).sortedBy { it.nombre },

        "Senior Femenino 1ª" to listOf(
            Equipo("C.D. Universidad de Navarra", listOf("UNAV")),
            Equipo("CB Noain A", listOf("Municipal (Noain)")),
            Equipo("CB Oncineda SK", listOf("Lizarreria (Estella)")),
            Equipo("Cantolagua", listOf("Municipal (Sangüesa)")),
            Equipo("CDB Gares", listOf("Municipal (Puente la Reina)")),
            Equipo("Lagunak", listOf("Lagunak (Piscinas)", "Municipal (Barañain)").sorted()),
            Equipo("Larraona Claret", listOf("Larraona")),
            Equipo("Liceo Monjardín", listOf("Liceo Monjardín")),
            Equipo("Megacalzado Ardoi", listOf("Municipal (Zizur)")),
            Equipo("Navarro Villoslada A", listOf("Ermitagaña", "Iribarren").sorted()),
            Equipo("Navasket SF'Old", listOf("Teresianas")),
            Equipo("Payvi Taberna Sanduzelai", listOf("San Jorge")),
            Equipo("San Ignacio", listOf("San Ignacio")),
            Equipo("Valle de Egüés A", listOf("Maristas", "Olaz", "Salesianos", "Sarriguren").sorted())
        ).sortedBy { it.nombre },

        "Senior Masculino 2ª" to listOf(
            Equipo("AZK Alde Zaharreko Kluba", listOf("Buztintxuri", "Rochapea").sorted()),
            Equipo("Ademar Maristas", listOf("Maristas", "Olaz", "Salesianos", "Sarriguren").sorted()),
            Equipo("Berriozar MKE", listOf("Berriozar")),
            Equipo("Burlada A", listOf("Elizgibela")),
            Equipo("Burlada B", listOf("Elizgibela")),
            Equipo("CB Oncineda SK", listOf("Lizarreria (Estella)")),
            Equipo("CBP Selco Electrónica", listOf("Municipal (Peralta)")),
            Equipo("CDB Gares", listOf("Municipal (Puente la Reina)")),
            Equipo("Cantolagua", listOf("Municipal (Sangüesa)")),
            Equipo("Gazte Berriak", listOf("Idaki")),
            Equipo("Lagunak B", listOf("Lagunak (Piscinas)")),
            Equipo("Larraona Claret", listOf("Larraona")),
            Equipo("Navarro Villoslada B", listOf("IES Navarro Villoslada (Frontón)")),
            Equipo("Navasket SM'New", listOf("Rochapea", "Teresianas").sorted()),
            Equipo("Oberena", listOf("Oberena")),
            Equipo("San Cernin", listOf("San Cernin")),
            Equipo("Valle De Egüés B", listOf("Maristas", "Olaz", "Salesianos", "Sarriguren", "Trinkete").sorted()),
            Equipo("Zona Media Tafalla", listOf("Velodromo (Tafalla)"))
        ).sortedBy { it.nombre },

        "Senior Femenino 2ª" to listOf(
            Equipo("Aranguren Mutilbasket", listOf("Irulegui")),
            Equipo("Burlada", listOf("Elizgibela")),
            Equipo("CBP Inregal", listOf("Municipal (Peralta)")),
            Equipo("CD Noain", listOf("AIT Sport Center (Torres de Elorz)", "Municipal (Noain)").sorted()),
            Equipo("Gazte Berriak", listOf("Idaki")),
            Equipo("Inregal Ardoi", listOf("IES Zizur Mayor", "Municipal (Zizur)").sorted()),
            Equipo("Liceo Monjardín 'H'", listOf("Liceo Monjardín")),
            Equipo("Liceo Monjardín \"S\"", listOf("Liceo Monjardín")),
            Equipo("Mendillorri Egüés", listOf("Maristas", "Olaz", "Salesianos", "Sarriguren").sorted()),
            Equipo("Navarro Villoslada B", listOf("Ermitagaña", "IES Navarro Villoslada (Frontón)", "Iribarren").sorted()),
            Equipo("Navarro Villoslada C", listOf("Ermitagaña", "IES Navarro Villoslada (Frontón)", "Iribarren").sorted()),
            Equipo("Navasket SF'New", listOf("Teresianas")),
            Equipo("Navasket SF'Y78", listOf("Rochapea", "Teresianas").sorted()),
            Equipo("Sagrado Corazon", listOf("Sagrado Corazon"))
        ).sortedBy { it.nombre },

        "Junior Masculino 1ª" to listOf(
            Equipo("Anaquel IES Valle del Ebro", listOf("IES Valle del Ebro (Tudela)")),
            Equipo("Aranguren Mutilbasket A", listOf("Irulegui")),
            Equipo("Arenas", listOf("SDR Arenas (Tudela)")),
            Equipo("Gazte Berriak", listOf("Idaki")),
            Equipo("Larraona Claret", listOf("Larraona")),
            Equipo("Megacalzado Ardoi", listOf("Municipal (Zizur)")),
            Equipo("Navarro Villoslada A", listOf("Ermitagaña", "Iribarren").sorted()),
            Equipo("San Cernin", listOf("San Cernin")),
            Equipo("San Ignacio Liceo Monjardín \"A\"", listOf("Liceo Monjardin")),
            Equipo("Valle de Egüés Ibasaga", listOf("Maristas", "Olaz", "Salesianos", "Sarriguren").sorted())
        ).sortedBy { it.nombre },

        "Junior Femenino 1ª" to listOf(
            Equipo("ALZ Motors Mutilbasket A", listOf("Irulegui")),
            Equipo("EGA Perfil Oncineda", listOf("Lizarreria (Estella)")),
            Equipo("Gazte Berriak", listOf("Idaki")),
            Equipo("Liceo Monjardín \"A\"", listOf("Liceo Monjardin")),
            Equipo("Megacalzado Ardoi", listOf("Municipal (Zizur)")),
            Equipo("Navarro Villoslada A", listOf("Ermitagaña", "Iribarren").sorted()),
            Equipo("Navasket JF'Old", listOf("Teresianas")),
            Equipo("Valle de Egüés A", listOf("Maristas", "Olaz", "Salesianos", "Sarriguren").sorted())
        ).sortedBy { it.nombre },

        "Junior Masculino 2ª" to listOf(
            Equipo("Aranguren Mutilbasket B", listOf("Irulegui")),
            Equipo("Avia Zizur Ardoi", listOf("IES Zizur Mayor", "Municipal (Zizur)").sorted()),
            Equipo("Berriozar MKE", listOf("Berriozar")),
            Equipo("Biurdana Navasket JM'New", listOf("Rochapea", "Teresianas").sorted()),
            Equipo("Biurdana Navasket JM'Old", listOf("Rochapea", "Teresianas").sorted()),
            Equipo("Burlada", listOf("Elizgibela")),
            Equipo("CBASK", listOf("Zelandi (Alsasua)")),
            Equipo("CDB Gares", listOf("Municipal (Puente la Reina)")),
            Equipo("Cantolagua", listOf("Municipal (Sangüesa)")),
            Equipo("EGA Perfil Oncineda", listOf("Lizarreria (Estella)")),
            Equipo("Lagunak", listOf("Lagunak (Piscinas)", "Municipal (Barañain)").sorted()),
            Equipo("Larraona Claret", listOf("Larraona")),
            Equipo("Liceo Monjardín \"I\"", listOf("Liceo Monjardin")),
            Equipo("Navarro Villoslada B", listOf("Ermitagaña", "IES Navarro Villoslada (Frontón)", "Iribarren").sorted()),
            Equipo("Valle de Egüés", listOf("Maristas", "Olaz", "Salesianos", "Sarriguren").sorted()),
            Equipo("Zona Media Tafalla", listOf("Velodromo (Tafalla)"))
        ).sortedBy { it.nombre },

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
        ).sortedBy { it.nombre },

        "Cadete Masculino 1ª" to listOf(
            Equipo("Ademar Hermes", listOf("Larrabide", "Maristas", "Olaz", "Salesianos", "Sarriguren").sorted()),
            Equipo("Aranguren Mutilbasket A", listOf("Irulegui", "Larrabide").sorted()),
            Equipo("Arenas", listOf("Larrabide", "SDR Arenas (Tudela)").sorted()),
            Equipo("Gazte Berriak", listOf("Idaki", "Larrabide").sorted()),
            Equipo("IES Valle del Ebro", listOf("IES Valle del Ebro (Tudela)", "Larrabide").sorted()),
            Equipo("Liceo Monjardín", listOf("Larrabide", "Liceo Monjardín").sorted()),
            Equipo("Megacalzado Ardoi", listOf("IES Zizur Mayor", "Larrabide", "Municipal (Zizur)").sorted()),
            Equipo("San Ignacio", listOf("Larrabide", "San Ignacio").sorted()),
            Equipo("Valle de Egües Sarriguren", listOf("Larrabide", "Maristas", "Olaz", "Salesianos", "Sarriguren").sorted())
        ).sortedBy { it.nombre },

        "Cadete Femenino 1ª" to listOf(
            Equipo("Aranguren Mutilbasket A", listOf("Irulegui", "Larrabide").sorted()),
            Equipo("Aranguren Mutilbasket B", listOf("Irulegui", "Larrabide").sorted()),
            Equipo("Gazte Berriak", listOf("Idaki", "Larrabide").sorted()),
            Equipo("Liceo Monjardín", listOf("Larrabide", "Liceo Monjardín").sorted()),
            Equipo("Megacalzado Ardoi", listOf("Larrabide", "Municipal (Zizur)").sorted()),
            Equipo("Navarro Villoslada A", listOf("Ermitagaña", "Iribarren", "Larrabide").sorted()),
            Equipo("Navasket CF'old", listOf("Larrabide", "Teresianas").sorted()),
            Equipo("San Cernin \"A\"", listOf("Larrabide", "San Cernin").sorted()),
            Equipo("San Ignacio", listOf("Larrabide", "San Ignacio").sorted()),
            Equipo("Valle de Egües Eransus", listOf("Larrabide", "Maristas", "Olaz", "Salesianos", "Sarriguren").sorted())
        ).sortedBy { it.nombre },

        "Torneo Veteranos" to listOf(
            Equipo("Ademar Apolo", listOf("Maristas")),
            Equipo("Aranguren Mutilbasket A", listOf("Irulegui")),
            Equipo("Gazte Berriak", listOf("Idaki")),
            Equipo("Megacalzado Ardoi", listOf("Municipal (Zizur)")),
            Equipo("Navarro Villoslada A", listOf("Ermitagaña", "Iribarren").sorted()),
            Equipo("San Cernin \"A\"", listOf("San Cernin"))
        ).sortedBy { it.nombre },

        "Torneo Veteranas" to listOf(
            Equipo("Ademar Apolo", listOf("Maristas")),
            Equipo("Aranguren Mutilbasket A", listOf("Irulegui")),
            Equipo("Gazte Berriak", listOf("Idaki")),
            Equipo("Megacalzado Ardoi", listOf("Municipal (Zizur)")),
            Equipo("Navarro Villoslada A", listOf("Ermitagaña", "Iribarren").sorted()),
            Equipo("San Cernin \"A\"", listOf("San Cernin"))
        ).sortedBy { it.nombre },

        "Copa Navarra Femenina" to listOf(
            Equipo("Aranguren Mutilbasket", listOf("Aranguren (Piscinas)", "Irulegui").sorted()),
            Equipo("C.B Navasket S.K", listOf("Teresianas")),
            Equipo("Gazte Berriak", listOf("Idaki")),
            Equipo("Lagunak", listOf("Municipal (Barañain)")),
            Equipo("Liceo Monjardín", listOf("Liceo Monjardín")),
            Equipo("Megacalzado Ardoi", listOf("Municipal (Zizur)")),
            Equipo("Navarro Villoslada", listOf("Ermitagaña", "Iribarren").sorted()),
            Equipo("San Ignacio Premo", listOf("San Ignacio")),
            Equipo("Valle de Egües", listOf("Maristas", "Olaz", "Salesianos", "Sarriguren").sorted())
        ).sortedBy { it.nombre },

        "Copa Navarra Masculina" to listOf(
            Equipo("C.B Navasket S.K", listOf("Teresianas")),
            Equipo("Megacalzado Ardoi", listOf("Municipal (Zizur)")),
            Equipo("Valle de Egües 1ª DM", listOf("Maristas", "Olaz", "Salesianos", "Sarriguren").sorted()),
            Equipo("Valle de Egües 3ª FEB", listOf("Maristas", "Olaz", "Salesianos", "Sarriguren").sorted())
        ).sortedBy { it.nombre }
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
        "Alsasua" to Pair(38.0, 9.0),
        "Estella" to Pair(32.68, 7.74),
        "Peralta" to Pair(44.84, 10.62),
        "Puente" to Pair(16.72, 3.96),
        "San Adrián" to Pair(61.56, 14.58),
        "Sangüesa" to Pair(34.20, 8.10),
        "Tafalla" to Pair(26.60, 6.30),
        "Tudela" to Pair(71.44, 16.92)
    )

    val dietasPorCategoria = mapOf(
        "senior" to 14.0,
        "2ªdivisionmas" to 14.0,
        "junior" to 10.0,
        "cadete" to 5.0
    )
}