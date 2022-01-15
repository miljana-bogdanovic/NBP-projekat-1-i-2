package com.nbp.timelineservice.model.cassandra.likes;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("liked")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Liked {
    @PrimaryKey
    private PrimaryKeyLiked primaryKeyLiked;
}
