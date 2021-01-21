package io.pivotal.pal.tracker;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.Objects;

@Table("time_entries")
public class TimeEntry {

  @Id
  @Column("id")
  private final long id;

  @Column("project_id")
  private final long projectId;

  @Column("user_id")
  private final long userId;

  @Column("date")
  private final LocalDate date;

  @Column("hours")
  private final int hours;

  public TimeEntry(long id, long projectId, long userId, LocalDate date, int hours) {
    this.id = id;
    this.projectId = projectId;
    this.userId = userId;
    this.date = date;
    this.hours = hours;
  }

  public long getId() {
    return id;
  }

  public long getProjectId() {
    return projectId;
  }

  public long getUserId() {
    return userId;
  }

  public LocalDate getDate() {
    return date;
  }

  public int getHours() {
    return hours;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TimeEntry timeEntry = (TimeEntry) o;
    return id == timeEntry.id
        && projectId == timeEntry.projectId
        && userId == timeEntry.userId
        && hours == timeEntry.hours
        && date.equals(timeEntry.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, projectId, userId, date, hours);
  }

  @Override
  public String toString() {
    return "TimeEntry{"
        + "id="
        + id
        + ", projectId="
        + projectId
        + ", userId="
        + userId
        + ", date="
        + date
        + ", hours="
        + hours
        + '}';
  }
}
