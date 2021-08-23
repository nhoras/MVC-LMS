package ru.mts.teta.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mts.teta.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
