package com.nbp.timelineservice.repository;

import com.nbp.timelineservice.model.cassandra.likes.Liked;
import com.nbp.timelineservice.model.cassandra.likes.PrimaryKeyLiked;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikedRepository extends CassandraRepository<Liked, PrimaryKeyLiked> {

}
