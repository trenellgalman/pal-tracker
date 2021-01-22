package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Update;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;
import static org.springframework.data.relational.core.sql.SqlIdentifier.unquoted;

@Repository
public class R2dbcTimeEntryRepository implements ReactiveTimeEntryRepository {

  private final R2dbcEntityTemplate template;

  @Autowired
  public R2dbcTimeEntryRepository(R2dbcEntityTemplate template) {
    this.template = template;
  }

  @Override
  public Mono<TimeEntry> create(TimeEntry timeEntry) {
    return template.insert(TimeEntry.class).using(timeEntry);
  }

  @Override
  public Mono<TimeEntry> find(long id) {
    return template.selectOne(query(where("id").is(id)), TimeEntry.class);
  }

  @Override
  public Flux<TimeEntry> list() {
    return template.select(TimeEntry.class).all();
  }

  @Override
  public Mono<TimeEntry> update(long id, TimeEntry timeEntry) {
    Map<SqlIdentifier, Object> assignments =
        Map.of(
            unquoted("project_id"), timeEntry.getProjectId(),
            unquoted("user_id"), timeEntry.getUserId(),
            unquoted("date"), timeEntry.getDate(),
            unquoted("hours"), timeEntry.getHours());
    return template
        .update(TimeEntry.class)
        .matching(query(where("id").is(id)))
        .apply(Update.from(assignments))
        .transform(integerMono -> find(id));
  }

  @Override
  public Mono<Integer> delete(long id) {
    return template.delete(TimeEntry.class).matching(query(where("id").is(id))).all();
  }
}
