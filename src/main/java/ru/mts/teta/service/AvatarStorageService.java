package ru.mts.teta.service;

import java.io.InputStream;
import java.util.Optional;

public interface AvatarStorageService {
    void save(String username, String contentType, InputStream is);

    Optional<String> getContentTypeByUsername(String username);

    Optional<byte[]> getAvatarImageByUsername(String username);
}
