package com.smingsming.chat;

import com.smingsming.chat.domain.room.contoller.RoomController;
import com.smingsming.chat.domain.room.domain.Room;
import com.smingsming.chat.domain.room.dto.RequestCreateRoomDto;
import com.smingsming.chat.domain.room.dto.ResponseRoomDto;
import com.smingsming.chat.domain.room.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@SpringBootTest
class ChatApplicationTests {

	@Autowired
	RoomRepository roomRepository;

	@Autowired
	RoomController roomController;

	ModelMapper mapper = new ModelMapper();

	@Test
	void contextLoads() {
	}

	@Test
	void saveTest() {
		Room room = new Room(
				"tesT", "", true, LocalDateTime.now(), "asqqqqqqqqqdasd", 1L, 1L
		);
		RequestCreateRoomDto reqRoomDto = mapper.map(room, RequestCreateRoomDto.class);
		ResponseEntity<?> resRoomDto = roomController.createRoom(reqRoomDto);
	}

	@Test
	void deleteTest() {
		roomRepository.deleteById("632d51a7b083427a14d03548");
	}

//
//	@Test
//	void inputTest() {
//		Room room = new Room(
//				"tesT", "", true, LocalDateTime.now(), "asdasd", 1L, 1L
//		);
//
//		roomRepository.save(room);
//	}

}
