package ru.mts.teta.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mts.teta.domain.AvatarImage;

import java.util.Optional;

@Repository
public interface AvatarImageRepository extends JpaRepository<AvatarImage, String> {

    Optional<AvatarImage> findByUser_Username(String username);
}
