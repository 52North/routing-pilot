package org.n52.testbed.routing.persistence;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends MongoRepository<MongoRoute, ObjectId> {

    List<MongoRoute> findBySubscriberNotNull();

}
