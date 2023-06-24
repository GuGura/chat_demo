package com.chat.backend.handler;

import com.chat.backend.model.dto.ChatMessage;
import com.chat.backend.model.dto.ChatRoom;
import com.chat.backend.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSockChatHandler extends TextWebSocketHandler {

    private final ObjectMapper om;
    private final ChatService chatService;

    //여기서 돌려줄 메세지를 생성해서 WebSockConfig로 주입해서 보낸다.
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);
        ChatMessage chatMessage = om.readValue(payload, ChatMessage.class);
        ChatRoom room = chatService.findRoomById(chatMessage.getRoomId());
        room.handleActions(session,chatMessage,chatService);
    }
}
