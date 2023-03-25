package ru.tinkoff.edu.java.scrapper.clients.visitors;

import ru.tinkoff.edu.java.scrapper.clients.dto.GitHubResponse;

public interface LinkVisitor {
    GitHubResponse getInformation();
}
