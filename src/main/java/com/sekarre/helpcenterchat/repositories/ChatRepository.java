package com.sekarre.helpcenterchat.repositories;

import com.sekarre.helpcenterchat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    boolean existsByChannelId(String channelId);

    Optional<Chat> findByChannelId(String channelId);

    @Query("select c from Chat c left join fetch c.users where c.channelId = ?1")
    Optional<Chat> findByChannelIdWithUsers(String channelId);
}
