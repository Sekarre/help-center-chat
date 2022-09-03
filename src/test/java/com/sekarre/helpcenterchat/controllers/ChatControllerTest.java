package com.sekarre.helpcenterchat.controllers;

import com.sekarre.helpcenterchat.DTO.ChatCreateRequestDTO;
import com.sekarre.helpcenterchat.DTO.ChatInfoDTO;
import com.sekarre.helpcenterchat.DTO.ChatMessageDTO;
import com.sekarre.helpcenterchat.services.chat.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static com.sekarre.helpcenterchat.factories.ChatMessageMockFactory.getChatMessageDTOMock;
import static com.sekarre.helpcenterchat.factories.ChatMockFactory.*;
import static com.sekarre.helpcenterchat.util.DateUtil.getDateTimeFormatted;
import static com.sekarre.helpcenterchat.testutil.TestUtil.convertObjectToJsonBytes;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ChatControllerTest {

    @Mock
    private ChatService chatService;

    private MockMvc mockMvc;

    private ChatController chatController;

    private static final String BASE_URL = "/api/v1/chat-info/";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        chatController = new ChatController(chatService);

        mockMvc = MockMvcBuilders.standaloneSetup(chatController).build();
    }

    @Test
    void should_return_all_chat_messages_with_OK_status() throws Exception {
        //given
        final ChatMessageDTO chatMessageDTO = getChatMessageDTOMock();
        final String channelId = "randomChannelId";
        when(chatService.getAllChatMessages(any(String.class))).thenReturn(Collections.singletonList(chatMessageDTO));

        //when + then
        mockMvc.perform(get(BASE_URL + channelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message").value(chatMessageDTO.getMessage()))
                .andExpect(jsonPath("$[0].file").value(chatMessageDTO.getFile()))
                .andExpect(jsonPath("$[0].senderName").value(chatMessageDTO.getSenderName()))
                .andExpect(jsonPath("$[0].senderLastname").value(chatMessageDTO.getSenderLastname()))
                .andExpect(jsonPath("$[0].senderId").value(chatMessageDTO.getSenderId()))
                .andExpect(jsonPath("$[0].createdDateTime").value(getDateTimeFormatted(chatMessageDTO.getCreatedDateTime())));

        verify(chatService, times(1)).getAllChatMessages(channelId);
    }

    @Test
    void should_join_chat_and_return_OK_status() throws Exception {
        //given
        final String channelId = "randomChannelId";

        //when + then
        mockMvc.perform(patch(BASE_URL + channelId))
                .andExpect(status().isOk());

        verify(chatService, times(1)).joinChat(channelId);
    }

    @Test
    void should_create_new_chat_and_return_chat_info_with_OK_status() throws Exception {
        //given
        final ChatCreateRequestDTO chatCreateRequestDTO = getChatCreateRequestDTOMock();
        final ChatInfoDTO chatInfoDTO = getChatInfoDTOMock();
        byte[] content = convertObjectToJsonBytes(chatCreateRequestDTO);
        when(chatService.createNewChatWithUsers(any(ChatCreateRequestDTO.class))).thenReturn(chatInfoDTO);

        //when + then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(chatInfoDTO.getId()))
                .andExpect(jsonPath("$.channelId").value(chatInfoDTO.getChannelId()))
                .andExpect(jsonPath("$.channelName").value(chatInfoDTO.getChannelName()))
                .andExpect(jsonPath("$.issueId").value(chatInfoDTO.getIssueId()));

        verify(chatService, times(1)).createNewChatWithUsers(chatCreateRequestDTO);
    }

    @Test
    void should_return_all_channels_ids_with_OK_status() throws Exception {
        //given
        final ChatInfoDTO chatInfoDTO = getChatInfoDTOMock();
        when(chatService.getChatChannels()).thenReturn(Collections.singletonList(chatInfoDTO));

        //when + then
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(chatInfoDTO.getId()))
                .andExpect(jsonPath("$[0].channelId").value(chatInfoDTO.getChannelId()))
                .andExpect(jsonPath("$[0].channelName").value(chatInfoDTO.getChannelName()))
                .andExpect(jsonPath("$[0].issueId").value(chatInfoDTO.getIssueId()));

        verify(chatService, times(1)).getChatChannels();
    }
}