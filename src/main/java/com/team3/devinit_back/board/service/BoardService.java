package com.team3.devinit_back.board.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.devinit_back.board.dto.BoardRequestDto;
import com.team3.devinit_back.board.dto.BoardDetailResponseDto;
import com.team3.devinit_back.board.dto.BoardResponseDto;
import com.team3.devinit_back.board.entity.*;
import com.team3.devinit_back.board.repository.BoardRepository;
import com.team3.devinit_back.board.repository.CategoryRepository;
import com.team3.devinit_back.board.repository.RecommendationRepository;
import com.team3.devinit_back.comment.repository.CommentRepository;
import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.member.entity.Member;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;
    private final RecommendationRepository recommendationRepository;
    private final CommentRepository commentRepository;
    private final TagService tagService;
    private final JPAQueryFactory queryFactory;

    // 게시글 생성
    @Transactional
    public BoardDetailResponseDto createBoard(Member member, BoardRequestDto boardRequestDto) {

        Category category = getCategoryById(boardRequestDto.getCategoryId());
        Board board = Board.builder()
                .title(boardRequestDto.getTitle())
                .content(boardRequestDto.getContent())
                .thumbnail(extractImageUrl(boardRequestDto.getContent()))
                .member(member)
                .category(category)
                .build();

        makeTag(board, boardRequestDto);

        Board savedBoard = boardRepository.save(board);
        return BoardDetailResponseDto.fromEntity(savedBoard);

    }

    // 전체 게시물 조회
    public Page<BoardResponseDto> getAllBoard(Pageable pageable, List<String> tagNames, String contents) {
        QBoard board = QBoard.board;
        QTagBoard tagBoard = QTagBoard.tagBoard;

        BooleanBuilder builder = new BooleanBuilder();

        if ((tagNames != null && !tagNames.isEmpty()) || (contents != null && !contents.isEmpty())) {
            builder = buildDynamicQuery(tagNames, contents, null);
        }

        return fetchBoards(pageable, builder, tagNames);
    }

    // 카테고리별 게시물 조회
    public Page<BoardResponseDto> getBoardByCategory(Pageable pageable, List<String> tagNames,
                                                        String contents,Long categoryId ) {
        QBoard board = QBoard.board;
        QTagBoard tagBoard = QTagBoard.tagBoard;
        BooleanBuilder builder = new BooleanBuilder();

        if ((tagNames != null && !tagNames.isEmpty()) || (contents != null && !contents.isEmpty())) {
            builder = buildDynamicQuery(tagNames, contents, categoryId);
        }

        return fetchBoards(pageable, builder,tagNames);
    }

    //게시물 상세 조회
    public BoardDetailResponseDto getBoardDetail(Long id){
        Board board = getBoardByIdWithComment(id);
        return BoardDetailResponseDto.fromEntity(board);
    }

    // 게시글 수정
    @Transactional
    public void updateBoard(String memberId, Long id,BoardRequestDto boardRequestDto){
        Category category = getCategoryById(boardRequestDto.getCategoryId());
        Board board = isAuthorized(id, memberId);
        board.setTitle(boardRequestDto.getTitle());
        board.setContent(boardRequestDto.getContent());
        board.setCategory(category);

        makeTag(board, boardRequestDto);

        boardRepository.save(board);
    }

    //게시글 삭제
    @Transactional
    public void deleteBoard(Long id, String memberId) {
        Board board = isAuthorized(id, memberId);
        boardRepository.deleteById(board.getId());
    }

    // 게시글 추천 토글
    @Transactional
    public boolean toggleRecommend(Long id, Member member) {
        Board board = getBoardById(id);
        Optional<Recommendation> recommendation = recommendationRepository.findByBoardAndMember(board, member);
        if(recommendation.isPresent()){
            recommendationRepository.delete(recommendation.get());
            board.setUpCnt(board.getUpCnt() - 1);
            boardRepository.save(board);
            return false;
        }else{
            Recommendation newRecommendation = Recommendation.builder()
                    .board(board)
                    .member(member)
                    .build();
            recommendationRepository.save(newRecommendation);
            board.setUpCnt(board.getUpCnt() + 1);
            boardRepository.save(board);
            return true;
        }
    }


    // 게시글 추천수 조회
    public  int getRecommendationCount(Long id){
        Board board = getBoardById(id);
        return recommendationRepository.countByBoard(board);
    }

    // boardId -> 게시글객체 조회
    private Board getBoardById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
    }

    // boardId -> 게시글객체 조회 + 댓글포함
    private Board getBoardByIdWithComment(Long id){
        return boardRepository.findByIdWithComments(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
    }

    //CategoryId -> 카테고리객체 조회
    private Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    //태그설정
    private void makeTag(Board board, BoardRequestDto boardRequestDto){
        if (boardRequestDto.getTags() != null) {
            board.getTagBoards().clear();
            for (String tagName : boardRequestDto.getTags()) {
                Tag tag = tagService.findTag(tagName);
                TagBoard tagBoard = new TagBoard(board, tag);
                board.getTagBoards().add(tagBoard);
            }
        }
    }

    //컨텐츠(마크다운) 첫번째 이미지(썸네일) 추출
    private String extractImageUrl(String content){
        String imageUrl;
        String regex = "!\\[.*?\\]\\((.*?)\\)";  // !\[로 시작하고, ] 뒤에 (와 )로 감싸인 URL을 추출.
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        if(matcher.find()){ return matcher.group(1); }
        return null;
    }

    //조건절 생성
    private BooleanBuilder buildDynamicQuery(List<String> tagNames, String contents, Long categoryId) {
        QBoard board = QBoard.board;
        QTagBoard tagBoard = QTagBoard.tagBoard;
        QTag tag = QTag.tag;
        BooleanBuilder builder = new BooleanBuilder();

        // 카테고리 조건
        if (categoryId != null) {
            builder.and(board.category.id.eq(categoryId)); //카테고리 ID와 같은 ID값만 추출
        }

        // 태그 조건 : 각 태그를 모두(AND)를 포함한 게시글
        // select board_id from tag_board
        //where tag_id in(select id from tag where name in("a","b"))
        //GROUP BY board_id HAVING COUNT(tag_id) = {입력받은 태그 사이즈};  ->  태그 개수 확인

        // 태그이름 리스트 -> ID변환
        if (tagNames != null && !tagNames.isEmpty()) {
            // 태그 이름을 기반으로 태그 ID 조회
            List<Long> tagIds = queryFactory
                    .select(tag.id)
                    .from(tag)
                    .where(tag.name.in(tagNames))
                    .fetch();


            // 태그가 모두 포함된 게시글 필터링
            builder.and(board.id.in(
                    queryFactory
                            .select(tagBoard.board.id)
                            .from(tagBoard)
                            .where(tagBoard.tag.id.in(tagIds))
                            .groupBy(tagBoard.board.id)
                            .having(tagBoard.tag.id.count().eq((long) tagNames.size()))
                            .fetch()
            ));
        }

        // 검색어(게시글 내용) 조건 : 해당 내용이 포함된 게시글(대소문자 구분X)
        // select board_id from board
        // where Lower(board.content) like concat('%',LOWER(:contents,'%')
        if (contents != null && !contents.isEmpty()) {
            builder.and(board.content.containsIgnoreCase(contents)); // = Like %LOWER{contents}%
        }

        return builder;
    }
    // 빌더 적용 패치함수
    private PageImpl<BoardResponseDto> fetchBoards(Pageable pageable, BooleanBuilder builder, List<String> tagNames) {
        QBoard board = QBoard.board;
        QTagBoard tagBoard = QTagBoard.tagBoard;

        JPAQuery<Board> query = queryFactory
                .selectDistinct(board)
                .from(board);
        if (tagNames != null && !tagNames.isEmpty()) {
            query =query.leftJoin(board.tagBoards, tagBoard);
        }
        List<Board> boards = query
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .selectDistinct(board.id)
                .from(board);

        if (tagNames != null && !tagNames.isEmpty()) {
            countQuery = countQuery.leftJoin(board.tagBoards, tagBoard);
        }

        long total = countQuery
                .where(builder)
                .fetchCount();

        List<BoardResponseDto> content = boards.stream()
                .map(BoardResponseDto::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, total);
    }

    // 권한 검사
    private Board isAuthorized(Long id, String memberId) {
        Board board = getBoardById(id);
        if (!board.getMember().getId().equals(memberId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        return board;
    }
}
