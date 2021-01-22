package io.pivotal.pal.tracker;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.support.TransactionTemplate;
import reactor.test.StepVerifier;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(classes = PalTrackerApplication.class)
class R2dbcTimeEntryRepositoryTest {

  @Autowired private ReactiveTimeEntryRepository timeEntryRepository;
  @Autowired private JdbcTemplate jdbcTemplate;
  @Autowired private TransactionTemplate transactionTemplate;

  @BeforeEach
  public void setUp() {
    jdbcTemplate.execute("DELETE FROM time_entries");
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }

  @Test
  void create() {
    TimeEntry newTimeEntry = new TimeEntry(0, 123, 321, LocalDate.parse("2017-01-09"), 8);
    timeEntryRepository
        .create(newTimeEntry)
        .as(StepVerifier::create)
        .assertNext(
            entry -> {
              Map<String, Object> foundEntry =
                  jdbcTemplate.queryForMap(
                      "Select * from time_entries where id = ?", entry.getId());

              assertThat(foundEntry.get("id")).isEqualTo(entry.getId());
              assertThat(foundEntry.get("project_id")).isEqualTo(123L);
              assertThat(foundEntry.get("user_id")).isEqualTo(321L);
              assertThat(((Date) foundEntry.get("date")).toLocalDate())
                  .isEqualTo(LocalDate.parse("2017-01-09"));
              assertThat(foundEntry.get("hours")).isEqualTo(8);
            })
        .verifyComplete();
  }

  @Test
  void find() {
    timeEntryRepository
        .find(999L)
        .as(StepVerifier::create)
        .assertNext(
            timeEntry -> {
              assertThat(timeEntry.getId()).isEqualTo(999L);
              assertThat(timeEntry.getProjectId()).isEqualTo(123L);
              assertThat(timeEntry.getUserId()).isEqualTo(321L);
              assertThat(timeEntry.getDate()).isEqualTo(LocalDate.parse("2017-01-09"));
              assertThat(timeEntry.getHours()).isEqualTo(8);
            })
        .verifyComplete();
  }

  @Test
  void list() {
    jdbcTemplate.execute(
        "INSERT INTO time_entries (id, project_id, user_id, date, hours) "
            + "VALUES (999, 123, 321, '2017-01-09', 8), (888, 456, 678, '2017-01-08', 9)");

    timeEntryRepository
        .list()
        .as(StepVerifier::create)
        .assertNext(
            timeEntry -> {
              assertThat(timeEntry.getId()).isEqualTo(888L);
              assertThat(timeEntry.getProjectId()).isEqualTo(456L);
              assertThat(timeEntry.getUserId()).isEqualTo(678L);
              assertThat(timeEntry.getDate()).isEqualTo(LocalDate.parse("2017-01-08"));
              assertThat(timeEntry.getHours()).isEqualTo(9);
            })
        .assertNext(
            timeEntry -> {
              assertThat(timeEntry.getId()).isEqualTo(999L);
              assertThat(timeEntry.getProjectId()).isEqualTo(123L);
              assertThat(timeEntry.getUserId()).isEqualTo(321L);
              assertThat(timeEntry.getDate()).isEqualTo(LocalDate.parse("2017-01-09"));
              assertThat(timeEntry.getHours()).isEqualTo(8);
            })
        .verifyComplete();
  }

  @Test
  void update() {
    transactionTemplate.execute(
        status -> {
          jdbcTemplate.execute(
              "INSERT INTO time_entries (id, project_id, user_id, date, hours) "
                  + "VALUES (1000, 123, 321, '2017-01-09', 8)");
          return null;
        });

    TimeEntry timeEntryUpdates = new TimeEntry(0, 456, 987, LocalDate.parse("2017-01-10"), 10);

    timeEntryRepository
        .update(1000L, timeEntryUpdates)
        .as(StepVerifier::create)
        .assertNext(
            timeEntry -> {
              assertThat(timeEntry.getId()).isEqualTo(1000L);
              assertThat(timeEntry.getProjectId()).isEqualTo(456L);
              assertThat(timeEntry.getUserId()).isEqualTo(987L);
              assertThat(timeEntry.getDate()).isEqualTo(LocalDate.parse("2017-01-10"));
              assertThat(timeEntry.getHours()).isEqualTo(10);
            })
        .verifyComplete();
  }

  @Test
  @Sql(statements = "INSERT INTO time_entries (id, project_id, user_id, date, hours) "
          + "VALUES (999, 123, 321, '2017-01-09', 8), (888, 456, 678, '2017-01-08', 9)")
  void delete() {
    timeEntryRepository
        .delete(999L)
        .as(StepVerifier::create)
        .assertNext(
            rowsRemoved -> {
              assertThat(rowsRemoved).isEqualTo(1);
              JdbcTestUtils.countRowsInTable(jdbcTemplate, "time_entries");
            })
        .verifyComplete();
  }
}
