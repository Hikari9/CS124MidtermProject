package dragonsms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import dragonsms.entities.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
}
