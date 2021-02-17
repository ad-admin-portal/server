package com.insutil.admin.controller;

import com.insutil.admin.model.Holiday;
import com.insutil.admin.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class HolidayHandler {
    private final HolidayRepository holidayRepository;

    /*
    holiday 생성
    holidayDate, afterWorkingDate, holidayDescription, registUser 는 mandatory
    성공 시 status 201과 holiday entity 반환
    실패 시 status 400 반환
    실패 조건 : Duplicate entry(holidayDate), InvalidFormatException,
 */
    
    public Mono<ServerResponse> createHoliday(ServerRequest request) {

        return request.bodyToMono(Holiday.class)
                .flatMap(holiday -> {
                    holiday.setHolidayDate(holiday.getHolidayDate().replaceAll("-", ""));
                    holiday.setAfterWorkingDate(LocalDate.parse(holiday.getHolidayDate(), DateTimeFormatter.ofPattern("yyyyMMdd")).plusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    return holidayRepository.getHolidayByDate(holiday.getAfterWorkingDate())
                            .switchIfEmpty(Mono.just(holiday))
                            .map(it -> {
                                if (!StringUtils.isEmpty(it.getHolidayDate()))
                                    holiday.setAfterWorkingDate(it.getAfterWorkingDate());
                                return holiday;
                            }).onErrorResume(throwable -> {
                                throwable.printStackTrace();
                                return Mono.error(throwable);
                            });
                })
                .flatMap(holidayRepository::save)
                .flatMap(holiday ->
                        holidayRepository.getHolidaysByAfterWorkingDate(holiday.getHolidayDate())
                                .map(it -> {
                                    it.setUpdateUser(holiday.getRegistUser());
                                    it.setAfterWorkingDate(holiday.getAfterWorkingDate());
                                    return it;
                                }).flatMap(holidayRepository::save)
                                .then(Mono.just(holiday))
                ).flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue)
                .onErrorResume(error -> {
                    error.printStackTrace();
                    return ServerResponse.badRequest().bodyValue(error.getMessage());
                })
                .log();
    }

    /*
        enabled 가 true 인 holiday entity 반환
     */
    
    public Mono<ServerResponse> getEnabledHolidays(ServerRequest request) {
        return holidayRepository.findByEnabled()
                .collectList()
                .flatMap(holidays -> ServerResponse.ok().bodyValue(holidays));
    }

    
    public Mono<ServerResponse> updateHolidayDescription(ServerRequest request) {
        String id = request.pathVariable("holidayId");
        if (!NumberUtils.isDigits(id)) {
            return ServerResponse.badRequest().bodyValue(id);
        }

        return holidayRepository.getHolidayById(Long.valueOf(id))
            .flatMap(origin ->
                request.bodyToMono(Holiday.class)
                    .map(origin::update)
                    .flatMap(holidayRepository::save)
            )
            .flatMap(it -> ServerResponse.ok().build())
            .switchIfEmpty(ServerResponse.notFound().build());
    }


    /*
	실재로 holiday 를 삭제하지 않고 enabled 를 false 로 바꿈
	성공 시 status 200과 삭제(수정)한 holiday entity 반환
	id 가 없다면 status 404 반환
 */
    
    public Mono<ServerResponse> disableHoliday(ServerRequest request) {
        String id = request.pathVariable("holidayId");
        if (!NumberUtils.isDigits(id)) {
            return ServerResponse.badRequest().bodyValue(id);
        }

        String updateUser = request.attribute("id)").orElseThrow().toString();
        return holidayRepository.getHolidayById(Long.valueOf(id))
                .flatMap(holiday -> holidayRepository.getHolidaysByAfterWorkingDate(holiday.getAfterWorkingDate())
                        .filter(it -> LocalDate.parse(it.getHolidayDate(), DateTimeFormatter.ofPattern("yyyyMMdd")).isBefore(LocalDate.parse(holiday.getHolidayDate(), DateTimeFormatter.ofPattern("yyyyMMdd"))))
                        .flatMap(it -> {
                            it.setUpdateUser(Long.valueOf(updateUser));
                            it.setAfterWorkingDate(holiday.getHolidayDate());
                            return holidayRepository.save(it);
                        })
                        .then(Mono.just(holiday))
                        .flatMap(it -> holidayRepository.disableHoliday(Long.valueOf(id), Long.valueOf(updateUser))))
                .flatMap(ServerResponse.ok()::bodyValue)
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
