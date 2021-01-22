package io.pivotal.pal.tracker;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveTimeEntryRepository {

  Mono<TimeEntry> create(TimeEntry timeEntry);

  Mono<TimeEntry> find(long id);

  Flux<TimeEntry> list();

  Mono<TimeEntry> update(long id, TimeEntry timeEntry);

  Mono<Integer> delete(long id);
}
