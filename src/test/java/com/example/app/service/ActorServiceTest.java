package com.example.app.service;


import org.example.model.Actor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.SQLException;

import static com.example.app.service.constants.TestConstants.USER_NAME;

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
        Pageable pageable ;
        Page<Actor> actorPage;

        actorService.create(USER_NAME, anActor());

        pageable = PageRequest.of(0,5);
        actorPage = actorService.list(USER_NAME,pageable);
        Assertions.assertEquals(1, actorPage.getTotalElements());

        for (int i = 0; i <10; i++) {
            actorService.create(USER_NAME, anActor());
        }
        pageable = PageRequest.of(1,4);
        actorPage = actorService.list(USER_NAME,pageable);
        Assertions.assertEquals(11, actorPage.getTotalElements());
        Assertions.assertEquals(3, actorPage.getTotalPages());
        Assertions.assertEquals(4, actorPage.getContent().size());
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
    void delete() throws SQLException {
        final Actor actor = actorService.create(USER_NAME,
                anActor());
        actorService.delete(USER_NAME, actor.getActorId());
        Assertions.assertFalse(actorService.read(USER_NAME, actor.getActorId()).isPresent(), "Deleted");
    }

    Actor anActor() {
        Actor actor = new Actor();
        actor.setFirstName("vj");
        actor.setLastName("vijay");
        return actor;
    }
}