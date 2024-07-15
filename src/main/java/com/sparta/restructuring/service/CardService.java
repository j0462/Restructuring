package com.sparta.restructuring.service;

import com.sparta.restructuring.dto.CardRequest;
import com.sparta.restructuring.dto.CardResponse;
import com.sparta.restructuring.entity.Card;
import com.sparta.restructuring.entity.Columns;
import com.sparta.restructuring.entity.User;
import com.sparta.restructuring.repository.CardRepository;
import com.sparta.restructuring.repository.ColumnRepository;
import com.sparta.restructuring.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ColumnRepository columnRepository;
    @Autowired
    private UserRepository userRepository;

    public List<CardResponse> getAllCards() {
        List<Card> cards = cardRepository.findAll();
        List<CardResponse> responses = new ArrayList<>();
        for (Card card : cards) {
            CardResponse cardResponse = CardResponse.builder()
                    .id(card.getId())
                    .title(card.getTitle())
                    .content(card.getContent())
                    .date(card.getDate())
                    .build();
            responses.add(cardResponse);
        }
        return responses;
    }

    public List<CardResponse> getCardsByStatus(String status) {
        Columns column = columnRepository.findByColumnName(status);
        List<Card> cards = cardRepository.findByColumn(column);
        List<CardResponse> responses = new ArrayList<>();
        for (Card card : cards) {
            CardResponse cardResponse = CardResponse.builder()
                    .id(card.getId())
                    .title(card.getTitle())
                    .content(card.getContent())
                    .date(card.getDate())
                    .build();
            responses.add(cardResponse);
        }
        return responses;
    }

    public List<CardResponse> getCardsByUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        List<Card> cards = cardRepository.findByUser(user);
        List<CardResponse> responses = new ArrayList<>();
        for (Card card : cards) {
            CardResponse cardResponse = CardResponse.builder()
                    .id(card.getId())
                    .title(card.getTitle())
                    .content(card.getContent())
                    .date(card.getDate())
                    .build();
            responses.add(cardResponse);
        }
        return responses;
    }

    @Transactional
    public CardResponse createCard(CardRequest request, User user) {
        Columns column = columnRepository.findByColumnName(request.getColumnStatus());
        Card card = Card.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .date(request.getDate())
                .column(column)
                .user(user)
                .build();
        cardRepository.save(card);
        return CardResponse.builder()
                .id(card.getId())
                .title(card.getTitle())
                .content(card.getContent())
                .date(card.getDate())
                .build();
    }

    @Transactional
    public CardResponse updateCard(Long id, CardRequest updatedCard, User user) {
        Optional<Card> existingCard = cardRepository.findById(id);
        if (existingCard.isEmpty()) {
            throw new IllegalArgumentException("Card를 찾을 수 없습니다");
        }
        Card card = existingCard.get();
        if(card.getUser().getId() != user.getId()) {
            throw new IllegalArgumentException("작성자가 아닙니다");
        }
        card.setTitle(updatedCard.getTitle());
        card.setContent(updatedCard.getContent());
        card.setDate(updatedCard.getDate());
        cardRepository.save(card);
        return CardResponse.builder()
            .id(card.getId())
            .title(card.getTitle())
            .content(card.getContent())
            .date(card.getDate())
            .build();
    }

    @Transactional
    public Long deleteCard(Long id, User user) {
        Optional<Card> existingCard = cardRepository.findById(id);
        if (existingCard.isEmpty()) {
            throw new IllegalArgumentException("Card를 찾을 수 없습니다");
        }
        Card card = existingCard.get();
        if(card.getUser().getId() != user.getId()) {
            throw new IllegalArgumentException("작성자가 아닙니다");
        }
        cardRepository.delete(card);
        return card.getId();
    }
}
