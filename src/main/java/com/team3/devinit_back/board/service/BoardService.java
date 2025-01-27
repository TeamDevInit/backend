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
import com.team3.devinit_back.profile.entity.Profile;
import com.team3.devinit_back.profile.repository.ProfileRepository;
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
    private final ProfileRepository profileRepository;
    private final TagService tagService;
    private final JPAQueryFactory queryFactory;

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

        Profile profile = getProfileByMemberId(member.getId());
        profile.incrementBoardCount();
        profileRepository.save(profile);

        return BoardDetailResponseDto.fromEntity(savedBoard);

    }

    public Page<BoardResponseDto> getAllBoard(Pageable pageable, List<String> tagNames, String contents) {
        QBoard board = QBoard.board;
        QTagBoard tagBoard = QTagBoard.tagBoard;

        BooleanBuilder builder = new BooleanBuilder();

        if ((tagNames != null && !tagNames.isEmpty()) || (contents != null && !contents.isEmpty())) {
            builder = buildDynamicQuery(tagNames, contents, null);
        }

        return fetchBoards(pageable, builder, tagNames);
    }

    public Page<BoardResponseDto> getBoardByCategory(Pageable pageable, List<String> tagNames,
                                                        String contents,Long categoryId ) {
        QBoard board = QBoard.board;
        QTagBoard tagBoard = QTagBoard.tagBoard;
        BooleanBuilder builder = new BooleanBuilder();

        if ((tagNames != null && !tagNames.isEmpty()) || (contents != null && !contents.isEmpty())) {
            builder = buildDynamicQuery(tagNames, contents, categoryId);
        }else if(categoryId != null){
                builder.and(board.category.id.eq(categoryId)); //카테고리 ID와 같은 ID값만 추출
        }

        return fetchBoards(pageable, builder,tagNames);
    }

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

    @Transactional
    public void deleteBoard(Long id, String memberId) {
        Board board = isAuthorized(id, memberId);
        boardRepository.deleteById(board.getId());
    }

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

    public  int getRecommendationCount(Long id){
        Board board = getBoardById(id);
        return recommendationRepository.countByBoard(board);
    }

    private Profile getProfileByMemberId(String memberId) {
        return profileRepository.findByMemberId(memberId)
            .orElseThrow(() -> new EntityNotFoundException("해당 멤버의 프로필을 찾을 수 없습니다. ID: " + memberId));
    }

    private Board getBoardById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
    }

    private Board getBoardByIdWithComment(Long id){
        return boardRepository.findByIdWithComments(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
    }

    private Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }

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

    private String extractImageUrl(String content){
        String imageUrl;
        String regex = "!\\[.*?\\]\\((.*?)\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        if(matcher.find()){ return matcher.group(1); }
        return null;
    }

    private BooleanBuilder buildDynamicQuery(List<String> tagNames, String contents, Long categoryId) {
        QBoard board = QBoard.board;
        QTagBoard tagBoard = QTagBoard.tagBoard;
        QTag tag = QTag.tag;
        BooleanBuilder builder = new BooleanBuilder();

        if (categoryId != null) {
            builder.and(board.category.id.eq(categoryId));
        }

        if (tagNames != null && !tagNames.isEmpty()) {
            List<Long> tagIds = queryFactory
                    .select(tag.id)
                    .from(tag)
                    .where(tag.name.in(tagNames))
                    .fetch();

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

        if (contents != null && !contents.isEmpty()) {
            builder.and(board.content.containsIgnoreCase(contents));
        }

        return builder;
    }

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

    private Board isAuthorized(Long id, String memberId) {
        Board board = getBoardById(id);
        if (!board.getMember().getId().equals(memberId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        return board;
    }
}
