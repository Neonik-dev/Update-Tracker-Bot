package ru.tinkoff.edu.java.parsers;

import ru.tinkoff.edu.java.responses.BaseResponse;

public interface Parser {
    BaseResponse parseUrl(String url);
}
