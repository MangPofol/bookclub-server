package mangpo.server.service.book;

import mangpo.server.entity.book.BookInfo;
import mangpo.server.repository.book.BookInfoRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

@Transactional
@ExtendWith(MockitoExtension.class)
class BookInfoServiceTest {

    @InjectMocks
    private BookInfoService bookInfoService;

    @Mock
    private BookInfoRepository bookInfoRepository;

    private static BookInfo bookInfo;

    @BeforeAll
    static void setup() {
//        MockitoAnnotations.openMocks(this);

        Long id = 1L;
        String name = "bookInfo_1";
        String isbn = "isbn_1";

        bookInfo = BookInfo.builder()
                .id(id)
                .name(name)
                .isbn(isbn)
                .build();
    }

    @Test
    void createBookInfo_정상() {
        //given
        given(bookInfoRepository.save(any())).willReturn(bookInfo);
        given(bookInfoRepository.findByIsbn(any())).willReturn(Optional.empty());

        //when
        Long resultId = bookInfoService.createBookInfo(bookInfo);

        //then
        assertThat(resultId).isEqualTo(bookInfo.getId());

        then(bookInfoRepository).should(times(1)).findByIsbn(bookInfo.getIsbn());
        then(bookInfoRepository).should(times(1)).save(bookInfo);
    }

    @Test
    void createBookInfo_중복() {
        //given
        given(bookInfoRepository.findByIsbn(any())).willReturn(Optional.of(bookInfo));

        //when then
        assertThatThrownBy(() -> bookInfoService.createBookInfo(bookInfo))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 등록된 책 정보입니다.");

        then(bookInfoRepository).should(times(1)).findByIsbn(bookInfo.getIsbn());
        then(bookInfoRepository).should(never()).save(any());
    }

    @Test
    void createBookInfo_isbn_누락() {
        //given
        BookInfo noIsbn = BookInfo.builder().build();

        //when then
        assertThatThrownBy(() -> bookInfoService.createBookInfo(noIsbn))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("isbn 정보가 없습니다.");

        then(bookInfoRepository).should(never()).save(any());
    }

    @Test
    void findBookInfoByIsbn() {
        //given
        given(bookInfoRepository.findByIsbn(any())).willReturn(Optional.of(bookInfo));

        //when
        BookInfo bi = bookInfoService.findBookInfoByIsbn(bookInfo.getIsbn());

        //then
        then(bookInfoRepository).should(times(1)).findByIsbn(any());
    }

    @Test
    void findBookInfoById() {
        //given
        given(bookInfoRepository.findById(any())).willReturn(Optional.of(bookInfo));

        //when
        BookInfo bi = bookInfoService.findBookInfoById(bookInfo.getId());

        //then
        then(bookInfoRepository).should(times(1)).findById(any());
    }

    @Test
    void createOrFindBookInfo_정상() {
        //given
        given(bookInfoRepository.findByIsbn(any())).willReturn(Optional.empty());
        given(bookInfoRepository.save(any())).willReturn(bookInfo);

        //when
        BookInfo orFindBookInfo = bookInfoService.createOrFindBookInfo(bookInfo);

        //then
        assertThat(orFindBookInfo).isEqualTo(bookInfo);
    }

    @Test
    void createOrFindBookInfo_isbn_중복() {
        //given
        given(bookInfoRepository.findByIsbn(any())).willReturn(Optional.of(bookInfo));
//        willDoNothing().given(bookComplexService).deleteBookAndRelated(anyLong());

        //when
        BookInfo orFindBookInfo = bookInfoService.createOrFindBookInfo(bookInfo);

        //then
        assertThat(orFindBookInfo).isEqualTo(bookInfo);
    }


}