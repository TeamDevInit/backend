package com.team3.devinit_back.hub.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.devinit_back.hub.dto.HubProfileResponseDto;
import com.team3.devinit_back.profile.entity.QProfile;
import com.team3.devinit_back.resume.entity.QResume;
import com.team3.devinit_back.resume.entity.QSkill;
import com.team3.devinit_back.resume.entity.QSkillTag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class HubCustomRepositoryImpl implements HubCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<HubProfileResponseDto> findProfilesByFilters(List<String> skillNames, String employmentPeriod, String sortType, Pageable pageable) {
        QProfile profile = QProfile.profile;
        QResume resume = QResume.resume;
        QSkill skill = QSkill.skill;
        QSkillTag skillTag = QSkillTag.skillTag;

        BooleanBuilder builder = new BooleanBuilder();

        if (employmentPeriod != null && !employmentPeriod.isEmpty()) {
            builder.and(resume.information.employmentPeriod.eq(employmentPeriod));
        }

        if (skillNames != null && !skillNames.isEmpty()) {
            for (String skillName : skillNames) {
                builder.and(
                        queryFactory
                                .selectOne()
                                .from(skill)
                                .join(skill.skillTag, skillTag)
                                .where(skill.resume.eq(resume)
                                        .and(skillTag.name.eq(skillName)))
                                .exists()
                );
            }
        }

        OrderSpecifier<?> sortOrder = "popular".equals(sortType)
                ? profile.followerCount.desc()
                : profile.createdAt.desc();

        long totalCount = Optional.ofNullable(queryFactory
                .select(profile.count())
                .from(profile)
                .leftJoin(profile.member.resume, resume)
                .where(builder)
                .fetchOne())
                .orElse(0L);

        List<HubProfileResponseDto> result = queryFactory
                .selectDistinct(profile)
                .from(profile)
                .leftJoin(profile.member.resume, resume)
                .where(builder)
                .orderBy(sortOrder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(p -> new HubProfileResponseDto(
                        p.getId(),
                        p.getMember().getNickName(),
                        p.getAbout(),
                        p.getMember().getProfileImage(),
                        p.getMember().getResume() != null && p.getMember().getResume().getInformation() != null
                                ? p.getMember().getResume().getInformation().getEmploymentPeriod() : "정보 없음",
                        p.getMember().getResume() != null
                                ? p.getMember().getResume().getSkills().stream()
                                    .map(s -> s.getSkillTag().getName())
                                    .collect(Collectors.toList())
                                : List.of()
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(result, pageable, totalCount);
    }
}