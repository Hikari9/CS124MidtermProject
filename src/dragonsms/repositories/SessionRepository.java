package dragonsms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dragonsms.session.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
//    Session findByName(String name);
}
