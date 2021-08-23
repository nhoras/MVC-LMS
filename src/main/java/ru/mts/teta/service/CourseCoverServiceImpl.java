package ru.mts.teta.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mts.teta.dao.CourseCoverRepository;
import ru.mts.teta.dao.CourseRepository;
import ru.mts.teta.domain.Course;
import ru.mts.teta.domain.CourseCover;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.*;

@Service
public class CourseCoverServiceImpl implements CourseCoverService {

    private final CourseCoverRepository coverRepository;
    private final CourseRepository courseRepository;
    private static final Logger logger = LoggerFactory.getLogger(CourseCoverServiceImpl.class);

    @Autowired
    public CourseCoverServiceImpl(CourseCoverRepository coverRepository, CourseRepository courseRepository) {
        this.coverRepository = coverRepository;
        this.courseRepository = courseRepository;
    }

    @Value("${file.storage.path}")
    private String path;

    @Override
    public Optional<String> getContentTypeByCourseId(Long id) {
        return coverRepository.findByCourse_Id(id)
                .map(CourseCover::getContentType);
    }

    @Override
    public Optional<byte[]> getCoverImageByCourseId(Long id) {
        return coverRepository.findByCourse_Id(id)
                .map(CourseCover::getFilename)
                .map(filename -> {
                    try {
                        return Files.readAllBytes(Path.of(path, filename));
                    } catch (IOException ex) {
                        logger.error("Can't read file {}", filename, ex);
                        throw new IllegalStateException(ex);
                    }
                });
    }

    @Transactional
    @Override
    public void save(Long courseId, String contentType, InputStream is) {
        Optional<CourseCover> opt = coverRepository.findByCourse_Id(courseId);
        CourseCover courseCover;
        String filename;
        if (opt.isEmpty()) {
            filename = UUID.randomUUID().toString();
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(IllegalArgumentException::new);
            courseCover = new CourseCover(null, contentType, filename, course);
        } else {
            courseCover = opt.get();
            filename = courseCover.getFilename();
            courseCover.setContentType(contentType);
        }
        coverRepository.save(courseCover);

        try (OutputStream os = Files.newOutputStream(Path.of(path, filename), CREATE, WRITE, TRUNCATE_EXISTING)) {
            is.transferTo(os);
        } catch (Exception ex) {
            logger.error("Can't write to file {}", filename, ex);
            throw new IllegalStateException(ex);
        }
    }
}
