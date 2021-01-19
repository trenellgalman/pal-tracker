package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

  private final TimeEntryRepository timeEntryRepository;

  @Autowired
  public TimeEntryController(TimeEntryRepository timeEntryRepository) {
    this.timeEntryRepository = timeEntryRepository;
  }

  @PostMapping
  public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
    return new ResponseEntity<>(timeEntryRepository.create(timeEntryToCreate), HttpStatus.CREATED);
  }

  @GetMapping("/{timeEntryId}")
  public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {
    Optional<TimeEntry> timeEntry = Optional.ofNullable(timeEntryRepository.find(timeEntryId));
    return ResponseEntity.of(timeEntry);
  }

  @GetMapping
  public ResponseEntity<List<TimeEntry>> list() {
    return ResponseEntity.ok(timeEntryRepository.list());
  }

  @PutMapping("/{timeEntryId}")
  public ResponseEntity<TimeEntry> update(@PathVariable long timeEntryId, @RequestBody TimeEntry timeEntryToUpdate) {
    Optional<TimeEntry> updated = Optional.ofNullable(timeEntryRepository.update(timeEntryId, timeEntryToUpdate));
    return ResponseEntity.of(updated);
  }

  @DeleteMapping("/{timeEntryId}")
  public ResponseEntity<Void> delete(@PathVariable long timeEntryId) {
    timeEntryRepository.delete(timeEntryId);
    return ResponseEntity.noContent().build();
  }
}
