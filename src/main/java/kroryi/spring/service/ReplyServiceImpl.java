package kroryi.spring.service;

import kroryi.spring.dto.PageRequestDTO;
import kroryi.spring.dto.PageResponseDTO;
import kroryi.spring.dto.ReplyDTO;
import kroryi.spring.entity.Board;
import kroryi.spring.entity.Reply;
import kroryi.spring.repository.BoardRepository;
import kroryi.spring.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReplyServiceImpl implements ReplyService {

    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;
    private final ModelMapper modelMapper;

    @Override
    public Long register(ReplyDTO dto) {
        Board board = boardRepository
                .findById(dto.getBno()).orElseThrow(() ->
                        new IllegalArgumentException("게시물 없음.")
                );

        Reply reply = modelMapper.map(dto, Reply.class);
        reply.setBoard(board);

        log.info(reply);
        return replyRepository.save(reply).getRno();
    }

    @Override
    public ReplyDTO read(Long rno) {
        Optional<Reply> replyOptional = replyRepository.findById(rno);
        Reply reply = replyOptional.orElseThrow();
        return modelMapper.map(reply, ReplyDTO.class);
    }

    @Override
    public void modify(ReplyDTO dto) {
        Optional<Reply> replyOptional = replyRepository.findById(dto.getRno());
        Reply reply = replyOptional.orElseThrow();
        reply.changeText(dto.getReplyText());
//        reply.changeReplier(dto.getReplier());
//        reply.changeReplierAndReplyText(dto.getReplier(), dto.getReplyText());
        replyRepository.save(reply);

    }

    @Override
    public void remove(Long rno) {
        replyRepository.deleteById(rno);
    }

    @Override
    public PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() <= 0 ? 0 : pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("rno").ascending());
        log.info(pageable); // [number:2, size: 5, sort: rno:ASC ]

        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);
        List<ReplyDTO> dtoList = result.getContent().stream()
                .map(
                reply -> {
                    try {
                        return modelMapper.map(reply, ReplyDTO.class);

                    }catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).toList();

        return PageResponseDTO.<ReplyDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }
}
