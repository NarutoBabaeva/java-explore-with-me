package ru.tokmakov.controller.guest;

import jakarta.validation.constraints.Min;
import ru.tokmakov.dto.complation.CompilationDto;
import ru.tokmakov.service.guest.GuestCompilationsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/compilations")
public class GuestCompilationsController {
    private final GuestCompilationsService guestCompilationsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> findCompilations(@RequestParam(required = false) Boolean pinned,
                                                 @Min(value = 0, message = "The 'from' parameter must be 0 or greater")
                                                 @RequestParam(required = false, defaultValue = "0") Integer from,
                                                 @Min(value = 1, message = "The 'size' parameter must be 1 or greater")
                                                 @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Fetching compilations with pinned: {}, from: {}, size: {}", pinned, from, size);
        List<CompilationDto> compilations = guestCompilationsService.findCompilations(pinned, from, size);
        log.info("Fetched {} compilations", compilations.size());
        return compilations;
    }

    @GetMapping("{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto findCompilationsByCompId(@PathVariable Long compId) {
        log.info("Fetching compilation by compId: {}", compId);
        CompilationDto compilation = guestCompilationsService.findCompilationsByCompId(compId);
        log.info("Fetched compilation: {}", compilation);
        return compilation;
    }
}
