package com.yasinkacmaz.jetflix.ui.movies

import com.yasinkacmaz.jetflix.data.MovieResponse
import com.yasinkacmaz.jetflix.testing.parseJson
import com.yasinkacmaz.jetflix.ui.movies.movie.MovieMapper
import com.yasinkacmaz.jetflix.util.toPosterUrl
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class MovieMapperTest {
    @Test
    fun map() {
        val movieResponse: MovieResponse = parseJson("movie_detail.json")

        val movie = MovieMapper().map(movieResponse)

        expectThat(movie.id).isEqualTo(111)
        expectThat(movie.name).isEqualTo("Scarface")
        expectThat(movie.releaseDate).isEqualTo("1983-12-08")
        expectThat(movie.posterPath).isEqualTo("/iQ5ztdjvteGeboxtmRdXEChJOHh.jpg".toPosterUrl())
        expectThat(movie.voteAverage).isEqualTo(8.2)
        expectThat(movie.voteCount).isEqualTo(7633)
    }
}
