package ru.boyda.popov.searchcinemas.interfaces;


import java.util.List;

import ru.boyda.popov.searchcinemas.parser.geo.CinemaDetails;

public interface CinemaDetailsListener {

    void onReady(List<CinemaDetails> result);
}
