package com.sekarre.helpcenterchat.services.user;

import com.sekarre.helpcenterchat.domain.User;
import com.sekarre.helpcenterchat.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static com.sekarre.helpcenterchat.factories.UserMockFactory.getDefaultUserMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void should_check_if_CURRENT_user_is_authorized_to_join_channel_and_return_true() {
        //given
        final Long[] usersIds = {1L, 2L};
        final User user = getDefaultUserMock();
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        //when
        List<User> result = userService.getUsersByIds(usersIds);

        //then
        assertNotNull(result);
        assertEquals(usersIds.length, result.size());
        assertEquals(user, result.get(result.size() - 1));
        verify(userRepository, times(1)).findById(usersIds[0]);
        verify(userRepository, times(1)).findById(usersIds[1]);
    }
}