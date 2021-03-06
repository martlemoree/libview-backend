package de.htwberlin.webtech.finview.web;

import de.htwberlin.webtech.finview.service.LibraryService;
import de.htwberlin.webtech.finview.web.api.Library;
import de.htwberlin.webtech.finview.web.api.LibraryManipulationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class LibraryRestController {

    private final LibraryService libraryService;

    public LibraryRestController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping(path = "/api/v1/libraries")
    public ResponseEntity<List<Library>> fetchLibraries() {
        return ResponseEntity.ok(libraryService.findAll());
    }

    @GetMapping(path = "/api/v1/libraries/{id}")
    public ResponseEntity<Library> fetchLibraryById(@PathVariable Long id) {
        var library = libraryService.findById(id);
        return library != null? ResponseEntity.ok(library) : ResponseEntity.notFound().build();
    }

    @PostMapping(path = "/api/v1/libraries")
    public ResponseEntity<Void> createLibrary(@Valid @RequestBody LibraryManipulationRequest request) throws URISyntaxException {
        var valid = validate(request);
        if (valid) {
            var library = libraryService.create(request);
            URI uri = new URI("/api/v1/libraries/" + library.getId());
            return ResponseEntity.created(uri).build();
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(path = "/api/v1/libraries/{id}")
    public ResponseEntity<Library> updateLibrary(@PathVariable Long id, @RequestBody LibraryManipulationRequest request) {
        var library = libraryService.update(id, request);
        return library != null? ResponseEntity.ok(library) : ResponseEntity.notFound().build();
    }

    @DeleteMapping(path ="/api/v1/libraries/{id}")
    public ResponseEntity<Void> deleteLibrary(@PathVariable Long id) {
        boolean successful = libraryService.deleteById(id);
        return successful? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    private boolean validate(LibraryManipulationRequest request) {
        return request.getLibraryName() != null
                && !request.getLibraryName().isBlank()
                && request.getProgrammingLanguage() != null
                && !request.getProgrammingLanguage().isBlank()
                && request.getLatestVersion() != null
                && !request.getLatestVersion().isBlank()
                && request.getUseField() != null
                && !request.getUseField().isBlank();
    }
}
