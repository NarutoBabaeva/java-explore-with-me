package ru.tokmakov.dto.complation;

import lombok.experimental.UtilityClass;
import ru.tokmakov.dto.event.EventMapper;
import ru.tokmakov.model.Compilation;

@UtilityClass
public class CompilationMapper {
    public CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        if (compilation.getEvents() != null && !compilation.getEvents().isEmpty()) {
            compilationDto.setEvents(compilation.getEvents().stream().map(EventMapper::toEventShortDto).toList());
        }
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        return compilationDto;
    }
}
