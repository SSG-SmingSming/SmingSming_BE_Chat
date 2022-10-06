package com.smingsming.chat.domain.room.repository;

import com.smingsming.chat.domain.room.domain.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomRepository extends MongoRepository<Room, String> {
}
