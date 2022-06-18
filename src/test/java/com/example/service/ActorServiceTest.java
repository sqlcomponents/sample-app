package com.example.service;

import org.example.model.Actor;
import org.example.model.Movie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.List;

import static com.example.service.constants.TestConstants.USER_NAME;

@SpringBootTest
class ActorServiceTest {

    @Autowired
    private ActorService actorService;

    @BeforeEach
    void before() throws SQLException {
        this.cleanUp();;
    }

    @AfterEach
    void after() throws SQLException {
        this.cleanUp();;
    }

    private void cleanUp() throws SQLException {
        this.actorService.delete(USER_NAME);
    }

    @Test
    void create() throws SQLException {
        Actor actor = this.actorService.create(USER_NAME,anActor());
        Assertions.assertTrue(this.actorService.read(USER_NAME,actor.getActorId()).isPresent(),"Created Actor");
    }

    @Test
    void list() throws SQLException{
        final Actor actor = actorService.create(USER_NAME,
                anActor());
        Actor newactor = anActor();
        actorService.create(USER_NAME,newactor);
        List<Actor> listOfActor = actorService.list(USER_NAME);
        Assertions.assertEquals(2, listOfActor.size());
    }

    @Test
    void update() throws SQLException {
        Actor actor = actorService.create(USER_NAME,
                anActor());
        final short actorId = actor.getActorId();
        actor.setFirstName("actorvijay");

        actorService.update(USER_NAME,actorId,actor);
        actor = actorService.read(USER_NAME,actorId).get();
        Assertions.assertEquals("actorvijay",actor.getFirstName(),"updated success fully");
    }

    @Test
    void delete() {
    }

    Actor anActor() {
        Actor actor = new Actor();
        actor.setFirstName("vj");
        actor.setLastName("vijay");
        return actor;
    }
}