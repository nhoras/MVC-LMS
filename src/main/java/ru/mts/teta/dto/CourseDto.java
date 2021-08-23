package ru.mts.teta.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mts.teta.annotation.TitleCase;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {

    private Long id;

    @NotBlank(message = "Укажи автора курса")
    private String author;

    @TitleCase()
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseDto courseDto = (CourseDto) o;
        return Objects.equals(id, courseDto.id) && Objects.equals(author, courseDto.author) && Objects.equals(title, courseDto.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, title);
    }
}
