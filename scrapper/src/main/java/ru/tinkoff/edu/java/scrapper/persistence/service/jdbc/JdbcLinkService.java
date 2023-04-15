package ru.tinkoff.edu.java.scrapper.persistence.service.jdbc;

import lombok.RequiredArgsConstructor;
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
import ru.tinkoff.edu.java.scrapper.persistence.service.LinkService;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final DomainRepository domainRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final LinkRepository linkRepository;
    private final Map<String, String> dataChanges = Map.of("commits", "0", "comments", "0");

    @Override
    @Transactional
    public LinkData add(long chatId, URI url) throws EmptyResultException, ForeignKeyNotExistsException, BadEntityException, DuplicateUniqueFieldException {
        LinkData linkData = new LinkData();
        linkData.setLink(url.toString());
        linkData.setDataChanges(dataChanges);
        DomainData domainData = domainRepository.getByName(url.getHost());
        linkData.setDomainId(domainData.getId());
        try {
            linkRepository.add(linkData);
        } catch (DuplicateUniqueFieldException e) {
        }
        LinkData result = linkRepository.getByLink(linkData.getLink());
        ChatLinkData chatLinkData = new ChatLinkData();
        chatLinkData.setLinkId(result.getId());
        chatLinkData.setChatId(chatId);
        chatLinkRepository.add(chatLinkData);

        return result;
    }

    @Override
    public LinkData remove(long chatId, URI url) throws EmptyResultException {
        LinkData linkData = linkRepository.getByLink(url.toString());
        chatLinkRepository.remove(chatId, linkData.getId());
        return linkData;
    }

    @Override
    public Collection<LinkData> listAll(long tgChatId) {
        List<ChatLinkData> arrChatLink = chatLinkRepository.findAllByChatId(tgChatId);
        List<Long> linkIds = arrChatLink.stream().map(ChatLinkData::getLinkId).toList();
        return linkRepository.getByLinkIds(linkIds);
    }
}
