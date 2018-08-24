package pl.blaszak.mikroservice.sessionService.database;

import org.springframework.data.repository.CrudRepository;
import pl.blaszak.mikroservice.sessionService.database.model.Session;

public interface SessionsRepository extends CrudRepository<Session, Long> {
}
