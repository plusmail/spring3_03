package kroryi.spring.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class PageResponseDTO<E> {
    private int page;
    private int size;
    private int total;

    private int start;
    private int end;
    private boolean prev;
    private boolean next;

    private List<E> dtoList;

    // 수동의 생성 만들어야 함. @AllArgsConstructor 사용하면 문제 발생 할 수 있다.
    // withAll 빌더 메서드 이름 변경.
    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO,
                           List<E> dtoList,
                           int total) {

        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        this.end = (int) (Math.ceil((double) this.page / pageRequestDTO.getPageListSize())) * pageRequestDTO.getPageListSize();
        this.start  = this.end - (pageRequestDTO.getPageListSize() -1);

        int last = (int) Math.ceil((double) total /size);
        this.end = end > last ? last : end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;


    }
}