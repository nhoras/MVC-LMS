package ru.mts.teta.service;

import java.io.InputStream;
import java.util.Optional;

public interface CourseCoverService {
    Optional<String> getContentTypeByCourseId(Long id);

    Optional<byte[]> getCoverImageByCourseId(Long id);

    void save(Long courseId, String contentType, InputStream is);
}
