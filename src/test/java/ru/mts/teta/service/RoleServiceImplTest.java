package ru.mts.teta.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.mts.teta.dao.RoleRepository;
import ru.mts.teta.domain.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoleServiceImplTest {

    private static RoleServiceImpl roleService;

    private static final Role ROLE_STUDENT = new Role(1L, Role.STUDENT);
    private static final Role ROLE_ADMIN = new Role(2L, Role.ADMIN);
    private static final List<Role> ALL_ROLES_LIST = List.of(ROLE_STUDENT, ROLE_ADMIN);

    @BeforeAll
    static void setUp() {
        RoleRepository roleRepositoryMock = Mockito.mock(RoleRepository.class);

        Mockito.when(roleRepositoryMock.findAll()).thenReturn(ALL_ROLES_LIST);

        roleService = new RoleServiceImpl(roleRepositoryMock);
    }

    @Test
    void findAll() {
        assertEquals(roleService.findAll(), ALL_ROLES_LIST);
    }
}