package mk.ukim.finki.wp.kol2023.g2.service.impl;

import mk.ukim.finki.wp.kol2023.g2.model.Director;
import mk.ukim.finki.wp.kol2023.g2.model.Genre;
import mk.ukim.finki.wp.kol2023.g2.model.Movie;
import mk.ukim.finki.wp.kol2023.g2.model.exceptions.InvalidDirectorIdException;
import mk.ukim.finki.wp.kol2023.g2.model.exceptions.InvalidMovieIdException;
import mk.ukim.finki.wp.kol2023.g2.repository.DirectorRepository;
import mk.ukim.finki.wp.kol2023.g2.repository.MovieRepository;
import mk.ukim.finki.wp.kol2023.g2.service.MovieService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final DirectorRepository directorRepositor;

    public MovieServiceImpl(MovieRepository movieRepository, DirectorRepository directorRepositor) {
        this.movieRepository = movieRepository;
        this.directorRepositor = directorRepositor;
    }

    @Override
    public List<Movie> listAllMovies() {
        return this.movieRepository.findAll();
    }

    @Override
    public Movie findById(Long id) {
        return this.movieRepository.findById(id).orElseThrow(InvalidMovieIdException::new);
    }

    @Override
    public Movie create(String name, String description, Double rating, Genre genre, Long director) {

        Director director1 = this.directorRepositor.findById(director).orElseThrow(InvalidDirectorIdException::new); //sekogas stavaj
        return this.movieRepository.save(new Movie(name, description, rating, genre, director1));
    }

    @Override
    public Movie update(Long id, String name, String description, Double rating, Genre genre, Long director) {
        Director director1 = this.directorRepositor.findById(director).orElseThrow(InvalidDirectorIdException::new); //sekogas stavaj
        Movie movie = this.movieRepository.findById(id).orElseThrow(InvalidMovieIdException::new);
        movie.setName(name);
        movie.setDescription(description);
        movie.setRating(rating);
        movie.setGenre(genre);
        movie.setDirector(director1);

        return this.movieRepository.save(movie);
    }

    @Override
    public Movie delete(Long id) {
        Movie movie = this.movieRepository.findById(id).orElseThrow(InvalidMovieIdException::new);
        this.movieRepository.delete(movie);
        return movie;
    }

    public Movie vote(Long id) {
        Movie movie = this.movieRepository.findById(id).orElse(null);
        if (movie != null) {
            movie.setVotes(movie.getVotes() + 1);
            movie = this.movieRepository.save(movie);
        }
        return movie;
    }


    @Override
    public List<Movie> listMoviesWithRatingLessThenAndGenre(Double rating, Genre genre) {
        List<Movie> movies;
        if (rating == null && genre == null) {
            movies = movieRepository.findAll();
        } else if (rating == null) {
            movies = movieRepository.findAllByGenre(genre);
        } else if (genre == null) {
            movies = movieRepository.findAllByRatingLessThan(rating);
        } else movies = movieRepository.findAllByRatingIsLessThanAndGenre(rating, genre);

        return movies;

    }
}

/*        if (rating == null && genre == null) {
            movies = movieRepository.findAll();
        } else if (rating == null) {
            movies = movieRepository.findAllByGenre(genre);
        } else if (genre == null) {
            movies = movieRepository.findAllByRatingLessThan(rating);
        } else
            movies = movieRepository.findAllByRatingIsLessThanAndGenre(rating, genre);*/


