package dragonsms.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import dragonsms.repositories.SessionRepository;

@Component
final class SessionDao { // only visible to SessionManager

    @Autowired
    protected SessionRepository dao;

    protected static SessionRepository daoInstance;

    public static SessionRepository getRepository() {
        return daoInstance;
    }

    @PostConstruct
    protected void postConstruct() {
        System.err.println("INFO: Autowired SessionRepository dao to SessionDao instance");
        daoInstance = dao;
    }

}
