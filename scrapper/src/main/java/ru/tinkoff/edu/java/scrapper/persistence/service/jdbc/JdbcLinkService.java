package ru.tinkoff.edu.java.scrapper.persistence.service.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.link_parser.GeneralParseLink;
import ru.tinkoff.edu.java.link_parser.responses.BaseParseResponse;
import ru.tinkoff.edu.java.scrapper.clients.clients.site.BaseSiteClient;
import ru.tinkoff.edu.java.scrapper.clients.clients.site.SitesMap;
import ru.tinkoff.edu.java.scrapper.dto.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.EmptyResultException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.ForeignKeyNotExistsException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatLink;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Domain;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.DomainRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.ChatLinkService;
import ru.tinkoff.edu.java.scrapper.persistence.service.LinkService;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final DomainRepository domainRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final LinkRepository linkRepository;
    private final ChatLinkService chatLinkService;
    private final SitesMap sitesMap;

    @Override
    @Transactional
    public LinkResponse add(long chatId, URI url) {
        Link linkData = new Link();
        linkData.setLink(url.toString());

        try {
            Domain domainData = domainRepository.getByName(url.getHost());
            linkData.setDomain(domainData);
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultException("Программа пока не может отслеживать ссылки с доменом " + url.getHost());
        }

        BaseSiteClient client = sitesMap.getClient(url.getHost());
        BaseParseResponse parseResponse = new GeneralParseLink().start(linkData.getLink());
        linkData.setPageUpdatedDate(client.getUpdatedDate(parseResponse));
        linkData.setDataChanges(client.getUpdates(parseResponse));

        try {
            linkRepository.add(linkData);
        } catch (DuplicateKeyException e) {
            log.warn(String.format("Эта (link)=(%s) уже существует", linkData.getLink()));
        }

        Link result = linkRepository.getByLink(linkData.getLink());
        ChatLink chatLinkData = new ChatLink(chatId, result.getId());

        try {
            chatLinkRepository.add(chatLinkData);
        } catch (DuplicateKeyException e) {
            throw new DuplicateUniqueFieldException("У пользователя уже отслуживается данная ссылка");
        } catch (DataIntegrityViolationException e) {
            throw new ForeignKeyNotExistsException(
                    String.format("Отсутствует пользователь с таким (chat_id)=(%d)", chatLinkData.getChatId())
            );
        }

        return new LinkResponse(result.getId(), result.getLink());
    }

    @Override
    @Transactional
    public LinkResponse remove(long chatId, URI url) {
        try {
            Link linkData = linkRepository.getByLink(url.toString());
            chatLinkRepository.remove(chatId, linkData.getId());
            return new LinkResponse(chatId, linkData.getLink());
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultException(String.format("Ссылка (%s) отсутвствует в базе данных", url));
        }
    }

    @Override
    @Transactional
    public ListLinksResponse listAll(long tgChatId) {
        List<Link> links = linkRepository.getByLinkIds(chatLinkService.getAllLink(tgChatId));
        if (links.isEmpty()) {
            return new ListLinksResponse(null, 0);
        }

        List<LinkResponse> listLinks = links.stream().map(
                (value) -> (new LinkResponse(value.getId(), value.getLink()))
        ).collect(Collectors.toList());
        return new ListLinksResponse(listLinks, listLinks.size());
    }

    @Override
    @Transactional
    public void updateDataChanges(Map<String, String> dataChanges, OffsetDateTime updatedDate, Long linkId) {
        linkRepository.updateDataChangesLink(dataChanges, updatedDate, linkId);
    }

    @Override
    @Transactional
    public Optional<Link> getOldestUpdateLink() {
        Link linkData = linkRepository.findAll("scheduler_updated_date", true, 1).get(0);
        linkRepository.updateUpdatedDateLink(linkData.getId());
        return Optional.of(linkData);
    }
}
