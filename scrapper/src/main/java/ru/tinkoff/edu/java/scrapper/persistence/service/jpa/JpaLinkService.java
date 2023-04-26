package ru.tinkoff.edu.java.scrapper.persistence.service.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.GeneralParseLink;
import ru.tinkoff.edu.java.responses.BaseParseResponse;
import ru.tinkoff.edu.java.scrapper.clients.clients.site.BaseSiteClient;
import ru.tinkoff.edu.java.scrapper.clients.clients.site.SitesMap;
import ru.tinkoff.edu.java.scrapper.dto.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.EmptyResultException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.ForeignKeyNotExistsException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.*;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaDomainRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.LinkService;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final JpaLinkRepository linkRepository;
    private final JpaDomainRepository domainRepository;
    private final JpaChatLinkRepository chatLinkRepository;
    private final JpaChatRepository chatRepository;
    private final SitesMap sitesMap;

    @Override
    @Transactional
    public LinkResponse add(long chatId, URI url) {
        Link link = new Link();
        link.setLink(url.toString());
        Domain domain = domainRepository.findByName(url.getHost());
        link.setDomain(domain);

        BaseParseResponse parseResponse = new GeneralParseLink().start(link.getLink());
        BaseSiteClient client = sitesMap.getClient(url.getHost());
        link.setPageUpdatedDate(OffsetDateTime.parse(client.getUpdatedDate(parseResponse)));
        link.setDataChanges(client.getUpdates(parseResponse));

        Chat chat = chatRepository.findById(chatId).orElseThrow(
                () -> new ForeignKeyNotExistsException(String.format("Отсутствует пользователь с таким (chat_id)=(%d)", chatId))
        );
        link.setSchedulerUpdateDate(OffsetDateTime.now());
        link.setUserCheckDate(OffsetDateTime.now());
        Link saveLink = linkRepository.save(link);
        ChatLink chatLink = new ChatLink(chat, saveLink);
        chatLinkRepository.save(chatLink);

        return new LinkResponse(saveLink.getId(), saveLink.getLink());
    }

    @Override
    @Transactional
    public LinkResponse remove(long chatId, URI url) {
        Link link = linkRepository.findByLink(url.toString());
        chatLinkRepository.deleteById(new ChatLinkPK(chatId, link.getId()));
        if (link.getChats().size() == 0)
            linkRepository.delete(link);
        return new LinkResponse(chatId, link.getLink());
    }

    @Override
    @Transactional(readOnly = true)
    public ListLinksResponse listAll(long tgChatId) {
        List<Link> links = linkRepository.findAllById(chatLinkRepository.findAllByChatId(tgChatId));
        if (links.isEmpty()) {
            return new ListLinksResponse(null, 0);
        }

        List<LinkResponse> listLinksResponse = links.stream().map(
                (value) -> (new LinkResponse(value.getId(), value.getLink()))
        ).collect(Collectors.toList());
        return new ListLinksResponse(listLinksResponse, listLinksResponse.size());
    }

    @Override
    @Transactional
    public void updateDataChanges(Map<String, String> dataChanges, OffsetDateTime updatedDate, Long linkId) {
        Link link = linkRepository.findById(linkId).orElseThrow(
                () -> new EmptyResultException(String.format("Ссылки с таким (link_id)=(%s) не существует", linkId)));
        link.setDataChanges(dataChanges);
        link.setPageUpdatedDate(updatedDate);
        linkRepository.save(link);
    }

    @Override
    @Transactional
    public Optional<Link> getOldestUpdateLink() {
        Page<Link> page = linkRepository.findAll(
                PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "schedulerUpdateDate"))
        );
        Link link = page.getContent().get(0);
        link.setSchedulerUpdateDate(OffsetDateTime.now());
        linkRepository.save(link);

        return Optional.of(link);
    }
}
