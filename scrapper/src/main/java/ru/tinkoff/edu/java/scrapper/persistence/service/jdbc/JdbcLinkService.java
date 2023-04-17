package ru.tinkoff.edu.java.scrapper.persistence.service.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.EmptyResultException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.ForeignKeyNotExistsException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.ChatLinkData;
import ru.tinkoff.edu.java.scrapper.persistence.entity.DomainData;
import ru.tinkoff.edu.java.scrapper.persistence.entity.LinkData;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.DomainRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.ChatLinkService;
import ru.tinkoff.edu.java.scrapper.persistence.service.LinkService;

import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JdbcLinkService implements LinkService {
    private final DomainRepository domainRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final LinkRepository linkRepository;
    private final ChatLinkService chatLinkService;
    private final Map<String, String> dataChanges = Map.of("commits", "0", "comments", "0" );

    @Override
    @Transactional
    public LinkData add(long chatId, URI url) throws EmptyResultException, ForeignKeyNotExistsException, BadEntityException, DuplicateUniqueFieldException {
        LinkData linkData = new LinkData();
        linkData.setLink(url.toString());
        linkData.setDataChanges(dataChanges);

        try {
            DomainData domainData = domainRepository.getByName(url.getHost());
            linkData.setDomainId(domainData.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultException("Программа пока не может отслеживать ссылки с доменом " + url.getHost());
        }

        try {
            linkRepository.add(linkData);
        } catch (DuplicateKeyException e) {
            log.warn(String.format("This (link)=(%s) already exists", linkData.getLink()));
        }

        LinkData result = linkRepository.getByLink(linkData.getLink());
        ChatLinkData chatLinkData = new ChatLinkData();
        chatLinkData.setLinkId(result.getId());
        chatLinkData.setChatId(chatId);
        try {
            chatLinkRepository.add(chatLinkData);
        } catch (DuplicateKeyException e) {
            throw new DuplicateUniqueFieldException("У пользователя уже отслуживается данная ссылка" );
        } catch (DataIntegrityViolationException e) {
            throw new ForeignKeyNotExistsException(
                    String.format("Отсутствует пользователь с таким (chat_id)=(%d)", chatLinkData.getChatId())
            );
        }

        return result;
    }

    @Override
    @Transactional
    public LinkData remove(long chatId, URI url) throws EmptyResultException {
        try {
            LinkData linkData = linkRepository.getByLink(url.toString());
            chatLinkRepository.remove(chatId, linkData.getId());
            return linkData;
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultException(String.format("Ссылка (%s) отсутвствует в базе данных", url));
        }
    }

    @Override
    public Collection<LinkData> listAll(long tgChatId) {
        return linkRepository.getByLinkIds(chatLinkService.getAllLink(tgChatId));
    }

    @Override
    @Transactional
    public Optional<LinkData> getOldestUpdateLink() {
        LinkData linkData = linkRepository.findAll("scheduler_updated_date", false, 1).get(0);
        linkRepository.updateUpdatedDateLink(linkData.getId());
        return Optional.of(linkData);
    }
}
