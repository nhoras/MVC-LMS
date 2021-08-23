-- Вывод курсов Линуса Торвальдса
SELECT * FROM courses
WHERE author = 'Linus Torvalds';

CREATE INDEX idx_course_author
ON courses
USING btree (author);

-- поиск курсов, на которые записан пользователь ***USER_ID***
SELECT * FROM courses
WHERE course_id IN
    ( SELECT course_id
    FROM users-courses
    WHERE user_id = ***USER_ID*** );

-- вывод удаленных модулей, сортировка по дате удаления
SELECT * FROM modules
WHERE deleted = true
ORDER BY delete_date DESC;

-- список активных администраторов
SELECT id, full_name, nickname, e-mail FROM users
WHERE deleted = false AND administrator = true;

-- Курсы по Java
SELECT * FROM courses
WHERE title LIKE '%Java%' AND deleted = false;

CREATE INDEX idx_course_title
ON courses
USING btree (title);

-- Расчет рейтинга для курса ***COURSE_ID***
SELECT AVG(score)
FROM rating
WHERE course_id = ***COURSE_ID***;

