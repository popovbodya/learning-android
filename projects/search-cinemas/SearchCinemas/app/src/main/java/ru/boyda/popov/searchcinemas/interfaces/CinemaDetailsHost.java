package ru.boyda.popov.searchcinemas.interfaces;


import ru.boyda.popov.searchcinemas.CinemaDetailsType;
import ru.boyda.popov.searchcinemas.parser.desc.CinemaDetails;

public interface CinemaDetailsHost {
    void displayCinemaWithDetails(CinemaDetails cinemaDetails, CinemaDetailsType cinemaDetailsType);
}
