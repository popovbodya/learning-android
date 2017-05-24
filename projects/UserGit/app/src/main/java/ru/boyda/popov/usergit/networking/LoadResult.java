package ru.boyda.popov.usergit.networking;


import java.util.List;

public class LoadResult<T> {
    private final String searchValue;
    private final List<T> result;
    private final Exception exception;

    public LoadResult(String searchValue, List<T> result, Exception exception) {
        this.searchValue = searchValue;
        this.result = result;
        this.exception = exception;
    }

    public LoadResult(LoadResult<T> result) {
        this.searchValue = result.getSearchValue();
        this.result = result.getResult();
        this.exception = result.getException();
    }

    public String getSearchValue() {
        return searchValue;
    }

    public List<T> getResult() {
        return result;
    }

    public Exception getException() {
        return exception;
    }
}