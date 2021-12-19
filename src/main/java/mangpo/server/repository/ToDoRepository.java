package mangpo.server.repository;

import mangpo.server.entity.user.ToDo;
import mangpo.server.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {

    List<ToDo> findByUser(User user);
}
