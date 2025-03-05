package kroryi.spring.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import kroryi.spring.dto.BoardListReplyCountDTO;
import kroryi.spring.entity.Board;
import kroryi.spring.entity.QBoard;
import kroryi.spring.entity.QReply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.expression.spel.ast.Projection;

import java.util.List;


@Slf4j
public class BoardSearchImpl
        extends QuerydslRepositorySupport
        implements BoardSearch {
    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable) {
        QBoard board = QBoard.board;  // Entity를 선택하면 DB table지정
        JPQLQuery<Board> query = from(board);  // 쿼리 from board

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.or(board.title.contains("--1"));
        booleanBuilder.or(board.content.contains("..2"));
        query.where(booleanBuilder); // where title like contains('%', :keyword, '%')
        query.where(board.bno.gt(10L));
        this.getQuerydsl().applyPagination(pageable, query);
        List<Board> list = query.fetch();
        Long count = query.fetchCount();
        return null;
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);

        if (types != null && types.length > 0 && keyword != null && !keyword.isEmpty()) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                    case "tc":
                        booleanBuilder.or(board.title.contains(keyword))
                                .or(board.content.contains(keyword));
                        break;
                    case "wc":
                        booleanBuilder.or(board.writer.contains(keyword))
                                .or(board.content.contains(keyword));
                        break;
                    case "wct":
                        booleanBuilder.or(board.writer.contains(keyword))
                                .or(board.content.contains(keyword))
                                .or(board.title.contains(keyword));
                        break;

                }
            } // end for
            query.where(booleanBuilder);
        }
        query.where(board.bno.gt(0L));

        this.getQuerydsl().applyPagination(pageable, query);
        List<Board> list = query.fetch();

        Long count = query.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        // Board와 Reply을 조인 시키는 쿼리
        JPQLQuery<Board> query = from(board);
        query.leftJoin(reply).on(reply.board.eq(board));
        query.groupBy(board.bno);

        if (types != null && types.length > 0 && keyword != null && !keyword.isEmpty()) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                    case "tc":
                        booleanBuilder.or(board.title.contains(keyword))
                                .or(board.content.contains(keyword));
                        break;
                    case "wc":
                        booleanBuilder.or(board.writer.contains(keyword))
                                .or(board.content.contains(keyword));
                        break;
                    case "wct":
                        booleanBuilder.or(board.writer.contains(keyword))
                                .or(board.content.contains(keyword))
                                .or(board.title.contains(keyword));
                        break;

                }
            } // end for
            query.where(booleanBuilder);
        }

        query.where(board.bno.gt(0L));

        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(Projections.bean(
                BoardListReplyCountDTO.class,
                board.bno,
                board.title,
                board.writer,
                board.regDate,
                reply.count().as("replyCount")
        ));

        this.getQuerydsl().applyPagination(pageable, dtoQuery);
        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch(); // 실행

        Long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList,pageable,count);

    }


}
