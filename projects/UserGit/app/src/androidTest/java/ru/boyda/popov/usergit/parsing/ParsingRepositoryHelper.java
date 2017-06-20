package ru.boyda.popov.usergit.parsing;


import java.util.ArrayList;
import java.util.List;

import ru.boyda.popov.usergit.pojo.Repository;

class ParsingRepositoryHelper {

    static Repository createOneSpecifiedRepo(String name, String url, String size, String id, String createdAt) {
        Repository repo = new Repository();
        repo.setName(name);
        repo.setUrl(url);
        repo.setSize(size);
        repo.setId(id);
        repo.setCreatedAt(createdAt);
        return repo;
    }

    static List<Repository> createMultiplySpecifiedUsers() {
        List<Repository> repositoryList = new ArrayList<>();

        repositoryList.add(createOneSpecifiedRepo("coursera-projects", "https://api.github.com/repos/MoonightCS/coursera-projects",
                "1285", "78668851", "2017-01-11T18:50:44Z"));

        repositoryList.add(createOneSpecifiedRepo("github-markdown-toc", "https://api.github.com/repos/MoonightCS/github-markdown-toc",
                "58", "84956073", "2017-03-14T14:05:15Z"));

        return repositoryList;
    }
}
