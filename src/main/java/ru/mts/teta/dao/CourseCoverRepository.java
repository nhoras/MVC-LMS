package ru.mts.teta.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mts.teta.domain.CourseCover;

import java.util.Optional;

@Repository
public interface CourseCoverRepository extends JpaRepository<CourseCover, String> {

    Optional<CourseCover> findByCourse_Id(Long id);
}
