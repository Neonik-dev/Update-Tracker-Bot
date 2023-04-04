package ru.tinkoff.edu.java.parsers;

import ru.tinkoff.edu.java.responses.BaseResponse;

import java.util.Optional;

public interface Parser {
    BaseResponse parseUrl(Optional<String> url);
}
