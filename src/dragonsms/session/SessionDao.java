package dragonsms.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import dragonsms.repositories.SessionRepository;

// Singleton component to access the dao in hibernate, only visible to SessionManager
@Component
final class SessionDao {

    @Autowired
    protected SessionRepository dao;
    public SessionRepository getDao() {
        return dao;
    }

    private static SessionDao instance;
    public static SessionDao getInstance() {
        return instance;
    }

    @PostConstruct
    protected void setInstance() {
        System.err.println("INFO: Autowired SessionRepository dao to SessionDao instance");
        instance = this;
    }

}
