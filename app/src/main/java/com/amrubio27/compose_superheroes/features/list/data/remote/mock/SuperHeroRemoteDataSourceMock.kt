package com.amrubio27.compose_superheroes.features.list.data.remote.mock

import com.amrubio27.compose_superheroes.features.list.domain.Appearance
import com.amrubio27.compose_superheroes.features.list.domain.Biography
import com.amrubio27.compose_superheroes.features.list.domain.Connections
import com.amrubio27.compose_superheroes.features.list.domain.Images
import com.amrubio27.compose_superheroes.features.list.domain.PowerStats
import com.amrubio27.compose_superheroes.features.list.domain.SuperHero
import com.amrubio27.compose_superheroes.features.list.domain.Work
import org.koin.core.annotation.Single

@Single
class SuperHeroRemoteDataSourceMock {

    fun getSuperHeroes(): Result<List<SuperHero>> {
        return Result.success(
            mockSuperHeroes
        )
    }

    val mockSuperHeroes = listOf(
        SuperHero(
            id = 1,
            name = "A-Bomb",
            slug = "1-a-bomb",
            powerstats = PowerStats(38, 100, 17, 80, 24, 64),
            appearance = Appearance(
                gender = "Male",
                race = "Human",
                height = listOf("6'8", "203 cm"),
                weight = listOf("980 lb", "441 kg"),
                eyeColor = "Yellow",
                hairColor = "No Hair"
            ),
            biography = Biography(
                fullName = "Richard Milhouse Jones",
                alterEgos = "No alter egos found.",
                aliases = listOf("Rick Jones"),
                placeOfBirth = "Scarsdale, Arizona",
                firstAppearance = "Hulk Vol 2 #2 (April, 2008) (as A-Bomb)",
                publisher = "Marvel Comics",
                alignment = "good"
            ),
            work = Work(
                occupation = "Musician, adventurer, author; formerly talk show host", base = "-"
            ),
            connections = Connections(
                groupAffiliation = "Hulk Family; Excelsior (sponsor), Avengers (honorary member); formerly partner of the Hulk, Captain America and Captain Marvel; Teen Brigade; ally of Rom",
                relatives = "Marlo Chandler-Jones (wife); Polly (aunt); Mrs. Chandler (mother-in-law); Keith Chandler, Ray Chandler, three unidentified others (brothers-in-law); unidentified father (deceased); Jackie Shorr (alleged mother; unconfirmed)"
            ),
            images = Images(
                xs = "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/xs/1-a-bomb.jpg",
                sm = "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/sm/1-a-bomb.jpg",
                md = "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/md/1-a-bomb.jpg",
                lg = "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/1-a-bomb.jpg"
            )
        ), SuperHero(
            id = 2,
            name = "Abe Sapien",
            slug = "2-abe-sapien",
            powerstats = PowerStats(88, 28, 35, 65, 100, 85),
            appearance = Appearance(
                gender = "Male",
                race = "Icthyo Sapien",
                height = listOf("6'3", "191 cm"),
                weight = listOf("145 lb", "65 kg"),
                eyeColor = "Blue",
                hairColor = "No Hair"
            ),
            biography = Biography(
                fullName = "Abraham Sapien",
                alterEgos = "No alter egos found.",
                aliases = listOf("Langdon Everett Caul", "Abraham Sapien", "Langdon Caul"),
                placeOfBirth = "-",
                firstAppearance = "Hellboy: Seed of Destruction (1993)",
                publisher = "Dark Horse Comics",
                alignment = "good"
            ),
            work = Work(
                occupation = "Paranormal Investigator", base = "-"
            ),
            connections = Connections(
                groupAffiliation = "Bureau for Paranormal Research and Defense",
                relatives = "Edith Howard (wife, deceased)"
            ),
            images = Images(
                xs = "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/xs/2-abe-sapien.jpg",
                sm = "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/sm/2-abe-sapien.jpg",
                md = "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/md/2-abe-sapien.jpg",
                lg = "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/2-abe-sapien.jpg"
            )
        ), SuperHero(
            id = 3,
            name = "Abin Sur",
            slug = "3-abin-sur",
            powerstats = PowerStats(50, 90, 53, 64, 99, 65),
            appearance = Appearance(
                gender = "Male",
                race = "Ungaran",
                height = listOf("6'1", "185 cm"),
                weight = listOf("200 lb", "90 kg"),
                eyeColor = "Blue",
                hairColor = "No Hair"
            ),
            biography = Biography(
                fullName = "",
                alterEgos = "No alter egos found.",
                aliases = listOf("Lagzia"),
                placeOfBirth = "Ungara",
                firstAppearance = "Showcase #22 (October, 1959)",
                publisher = "DC Comics",
                alignment = "good"
            ),
            work = Work(
                occupation = "Green Lantern, former history professor", base = "Oa"
            ),
            connections = Connections(
                groupAffiliation = "Green Lantern Corps, Black Lantern Corps",
                relatives = "Amon Sur (son), Arin Sur (sister), Thaal Sinestro (brother-in-law), Soranik Natu (niece)"
            ),
            images = Images(
                xs = "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/xs/3-abin-sur.jpg",
                sm = "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/sm/3-abin-sur.jpg",
                md = "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/md/3-abin-sur.jpg",
                lg = "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/3-abin-sur.jpg"
            )
        )
    )
}