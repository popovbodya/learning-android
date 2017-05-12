package ru.boyda.popov.searchcinemas.interfaces;


import java.util.List;

import ru.boyda.popov.searchcinemas.parser.desc.CinemaDetails;

public interface CinemaDetailsListener {

    void onReady(List<CinemaDetails> result);
}
