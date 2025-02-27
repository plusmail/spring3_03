package kroryi.spring.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;

@Slf4j
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    // ~~~.builder
    //    .size(1)
    //    .build()
    // 이렇게 사용시 page 필드에 1이 초기값을 갖지 못할 수 있다.
    // 그래서 @Builder.Default를 사용하면 page=1의 값을 가지도록 보장 한다.
    @Builder.Default
    @Min(value = 1)
    @Positive
    private int page = 1;

    @Builder.Default
    @Min(value = 5)
    @Max(value = 100)
    @Positive
    private int size = 5;

    @Builder.Default
    @Min(value = 2)
    @Max(value = 20)
    @Positive
    private int pageListSize = 2;

    private String link;

    private String[] types;
    private String keyword;
    private boolean finished;
    private LocalDate from ;
    private LocalDate to ;


    public int getSkip() {
        return (page - 1) * size;
    }

//    public String getLink() {
//        if (link == null) {
//            StringBuffer buffer = new StringBuffer();
//            buffer.append("page=").append(page)
//                    .append("&size=").append(size)
//                    .append("&pageListSize=").append(pageListSize);
//
//            link = buffer.toString();
//        }
//        log.info("link----> {}", link);
//
//        return link;
//    }

    public String getLink() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("page=").append(this.page);
        buffer.append("&size=").append(this.size);

        if(types != null && types.length > 0) {
            for(String type : types) {
                buffer.append("&types=").append(type);
            }
        }
        if(keyword != null && !keyword.isEmpty()) {
            try {
                buffer.append("&keyword=").append(URLEncoder.encode(keyword, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return buffer.toString();

    }

    public Pageable getPageable(String... props) {
        return PageRequest.of(1, 5, Sort.by("bno").descending());

    }

}