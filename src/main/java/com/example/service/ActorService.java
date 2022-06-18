package com.example.service;

import org.example.MovieManager;
import org.example.model.Actor;
import org.example.store.ActorStore;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * ActorService.
 */
@Service
public class ActorService {

    /**
     * Actor Store.
     */
    private final ActorStore actorStore;

    /**
     * Build ActorService.
     * @param movieManager
     */
    public ActorService(final MovieManager movieManager) {
        this.actorStore = movieManager.getActorStore();
    }

    /**
     * create a Actor .
     * @param username
     * @param actor
     * @return CreatedName
     */
    public Actor create(final String username,
                        final Actor actor) throws SQLException {
        return this.actorStore.insert().values(actor).returning();
    }

    /**
     * Read Actor By id.
     * @param username
     * @param actorId
     * @return actor
     */
    public Optional<Actor> read(final String username,
                               final Short actorId) throws SQLException {
        return this.actorStore.select(actorId);
    }

    /**
     * Update Actor By id.
     * @param username
     * @param actorId
     * @param actor
     * @return ActorUpdated
     */
    public Optional<Actor> update(final String username,
                                  final Short actorId,
                                  final Actor actor) throws SQLException {
        this.actorStore.update(actor);
        return read(username, actor.getActorId());
    }

    /**
     * Delete Actor By id.
     * @param username
     * @param actorId
     * @return IsDeleted
     */
    public boolean delete(final String username,
                        final Short actorId) throws SQLException {
        return this.actorStore.delete(actorId) == 1;
    }


    /**
     * Delete All Actors.
     * @param userName
     * @return nofOfDeleteActors
     */
    public int delete(final String userName) throws SQLException {
        return this.actorStore.delete().execute();
    }

    /**
     * Selects All.
     * @param userName
     * @return actors
     * @throws SQLException
     */
    public List<Actor> list(final String userName) throws SQLException{
        return this.actorStore.selectBy();
    }
}
