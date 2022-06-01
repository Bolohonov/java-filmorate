import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserStorage userDbStorage;

    @Test
    public void testFindUserById() {

        Optional<User> userOptional = userDbStorage.findUserById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }
    @Test
    public void testGetUsers() {
        User user1 = userDbStorage.findUserById(1).get();
        User user2 = userDbStorage.findUserById(2).get();
        Collection<User> users = userDbStorage.getUsers();
        assertThat(users).contains(user1, user2);
    }

//    @Test
//    public User testAddUser(User user) {
//        userDbStorage.addUser(user3);
//        assertThat(user3)
//                .hasValueSatisfying(user ->
//                assertThat(user).hasFieldOrPropertyWithValue("id", 3)
//        );
//    }

    @Test
    public void testDeleteUser(Integer id) {
        userDbStorage.deleteUser(3);
        assertThat(userDbStorage
                .findUserById(3))
                .isEmpty();
    }

//    @Test
//    public void testUpdateUser(User user) {
//        userDbStorage.updateUser(user1);
//        assertThat(user1)
//                .hasValueSatisfying(user ->
//                        assertThat(user).hasFieldOrPropertyWithValue("name", "newTest")
//                );
//    }
}