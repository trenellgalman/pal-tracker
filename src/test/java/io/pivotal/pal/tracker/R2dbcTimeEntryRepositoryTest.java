package io.pivotal.pal.tracker;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

@Slf4j
@SpringBootTest(classes = PalTrackerApplication.class)
class R2dbcTimeEntryRepositoryTest {

  @Autowired private ReactiveTimeEntryRepository timeEntryRepository;

  @Test
  void create() {
      TimeEntry newTimeEntry = new TimeEntry(0, 123, 321, LocalDate.parse("2017-01-09"), 8);
    Mono<TimeEntry> created = timeEntryRepository.create(newTimeEntry);
    StepVerifier
            .create(created)
            .expectSubscription()
            .assertNext(System.out::println)
            .verifyComplete();

  }

  @Test
  void find() {}

  @Test
  void list() {}

  @Test
  void update() {}

  @Test
  void delete() {}
}
