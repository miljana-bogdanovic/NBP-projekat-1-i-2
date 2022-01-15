package com.nbp.timelineservice.repository;

import com.nbp.timelineservice.model.cassandra.retweets.PrimaryKeyRetweeted;
import com.nbp.timelineservice.model.cassandra.retweets.Retweeted;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RetweetedRepository extends CassandraRepository<Retweeted, PrimaryKeyRetweeted> {

}
