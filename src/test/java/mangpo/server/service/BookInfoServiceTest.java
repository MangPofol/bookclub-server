package mangpo.server.service;

import mangpo.server.entity.BookInfo;
import mangpo.server.repository.BookInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
class BookInfoServiceTest{

    @InjectMocks
    private BookInfoService bookInfoService;

    @Mock
    private BookInfoRepository mockedBookInfoRepository;
    private BookInfo bookInfo;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

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
        given(mockedBookInfoRepository.save(any())).willReturn(bookInfo);
        given(mockedBookInfoRepository.findByIsbn(any())).willReturn(Optional.empty());

        //when
        Long resultId = bookInfoService.createBookInfo(this.bookInfo);

        //then
        assertThat(resultId).isEqualTo(bookInfo.getId());
    }

    @Test
    void createBookInfo_중복() {
        //given
        given(mockedBookInfoRepository.save(any())).willReturn(bookInfo);
        given(mockedBookInfoRepository.findByIsbn(any())).willReturn(Optional.of(bookInfo));


        //when then
        assertThatThrownBy(() ->  bookInfoService.createBookInfo(this.bookInfo))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 등록된 책 정보입니다.");
    }

    @Test
    void createBookInfo_isbn_누락() {
        //given
        BookInfo noIsbn = BookInfo.builder().build();

        given(mockedBookInfoRepository.save(any())).willReturn(noIsbn);
        given(mockedBookInfoRepository.findByIsbn(any())).willReturn(Optional.empty());

        //when then
        assertThatThrownBy(() ->  bookInfoService.createBookInfo(noIsbn))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("isbn 정보가 없습니다.");
    }
}