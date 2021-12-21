package mangpo.server.service.book;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RunWith(MockitoJUnitRunner.class)
class BookComplexServiceTest {

    @InjectMocks
    private BookComplexService bookComplexService;


    @Test
    void createBookAndRelated() {
    }

    @Test
    void deleteBookAndRelated() {
    }
}