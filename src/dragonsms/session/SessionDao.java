package dragonsms.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import dragonsms.repositories.SessionRepository;

// here is where spring comes in to create the dao
@Component
public final class SessionDao {

    @Autowired
    protected SessionRepository dao;
    public SessionRepository getDao() {
        return dao;
    }

    @PostConstruct
    protected void setInstance() {
        System.err.println("INFO: Autowired SessionRepository dao to SessionDao instance");
        instance = this;
    }

    private static SessionDao instance;
    public static SessionDao getInstance() {
        return instance;
    }

}
