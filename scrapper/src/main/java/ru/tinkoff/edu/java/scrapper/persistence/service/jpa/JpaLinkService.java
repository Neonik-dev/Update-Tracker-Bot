package ru.tinkoff.edu.java.scrapper.persistence.service.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.link_parser.GeneralParseLink;
import ru.tinkoff.edu.java.link_parser.responses.BaseParseResponse;
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
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(
                        () -> new ForeignKeyNotExistsException(
                                String.format("Отсутствует пользователь с таким (chat_id)=(%d)", chatId)
                        )
                );
        Link link = linkRepository.findByLink(url.toString()).orElseGet(() -> createLink(url));

        ChatLink chatLink = new ChatLink(chat, link);
        chatLinkRepository.save(chatLink);

        return new LinkResponse(link.getId(), link.getLink());
    }

    private Link createLink(URI url) {
        Link link = new Link();
        link.setLink(url.toString());
        Domain domain = domainRepository.findByName(url.getHost());
        link.setDomain(domain);

        BaseParseResponse parseResponse = new GeneralParseLink().start(link.getLink());
        BaseSiteClient client = sitesMap.getClient(url.getHost());
        link.setPageUpdatedDate(client.getUpdatedDate(parseResponse));
        link.setDataChanges(client.getUpdates(parseResponse));

        link.setSchedulerUpdateDate(OffsetDateTime.now());
        link.setUserCheckDate(OffsetDateTime.now());
        return linkRepository.save(link);
    }

    @Override
    @Transactional
    public LinkResponse remove(long chatId, URI url) {
        Link link;
        link = linkRepository.findByLink(url.toString()).orElseThrow(
                () -> new EmptyResultException("Данная ссылка никем не зарегистрирована")
        );

        try {
            chatLinkRepository.deleteById(new ChatLinkPK(chatId, link.getId()));
            if (link.getChats().size() == 1) {
                linkRepository.delete(link);
            }

            return new LinkResponse(chatId, link.getLink());
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultException(
                    String.format(
                            "Ссылка с (link_id)=(%d) не отслеживается у пользователя (chat_id)=(%d)",
                            link.getId(),
                            chatId
                    )
            );
        }
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
        List<Link> page = linkRepository.findAll(
                PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "schedulerUpdateDate"))
        ).getContent();
        if (page.isEmpty()) {
            return Optional.empty();
        }

        Link link = page.get(0);
        link.setSchedulerUpdateDate(OffsetDateTime.now());
        linkRepository.save(link);

        return Optional.of(link);
    }
}
