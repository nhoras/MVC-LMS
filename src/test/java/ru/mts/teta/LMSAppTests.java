package ru.mts.teta;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.mts.teta.controller.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LMSAppTests {

    @Autowired
    CourseController courseController;
    @Autowired
    ExceptionController exceptionController;
    @Autowired
    LessonController lessonController;
    @Autowired
    ProfileController profileController;
    @Autowired
    RootController rootController;
    @Autowired
    UserController userController;

    @Test
    public void contextLoads() {
        assertNotNull(courseController, "courseController can't be null");
        assertNotNull(exceptionController, "exceptionController can't be null");
        assertNotNull(lessonController, "lessonController can't be null");
        assertNotNull(profileController, "profileController can't be null");
        assertNotNull(rootController, "rootController can't be null");
        assertNotNull(userController, "userController can't be null");
    }
}
