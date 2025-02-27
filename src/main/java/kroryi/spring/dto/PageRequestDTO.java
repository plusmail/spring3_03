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
    private int size = 10;

    @Builder.Default
    @Min(value = 2)
    @Max(value = 20)
    @Positive
    private int pageListSize = 10;

    private String link;

    private String type;
    private String keyword;
    private boolean finished;
    private LocalDate from ;
    private LocalDate to ;

    public String[] getTypes(){
        if(type== null || type.isEmpty()){
            return null;
        }
        // "tw,t,twc"
        // split(",")
        return type.split("");
    }

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

    public String getLink(){
        // 페이지 번호에 링크를 걸기 위한 것
        // ?page=${page}&size=10&type=tw
        if(link == null){
            StringBuilder builder = new StringBuilder();
            builder.append("page=" + this.page);
            builder.append("&size=" + this.size);
            if(type != null && type.length() > 0){
                builder.append("&type=" + type);
            }
            if(keyword != null){
                try{
                    // ?page=1&size=10&keyword=세종대왕
                    // 세종대왕 한글 깨지말라고 인코딩함.
                    builder.append("&keyword="+ URLEncoder.encode(keyword, "UTF-8"));
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
            }
            link = builder.toString();

        }
        return link;
    }

    public Pageable getPageable(String... props) {
        return PageRequest.of(this.page - 1, this.size, Sort.by(props).descending());

    }

}