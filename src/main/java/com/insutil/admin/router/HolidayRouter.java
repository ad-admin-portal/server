package com.insutil.admin.router;

import com.insutil.admin.controller.HolidayHandler;
import com.insutil.admin.filter.HandlerFunctionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class HolidayRouter {
	private final HandlerFunctionFilter handlerFunctionFilter;

	@Bean
	public RouterFunction<ServerResponse> holidayRoute(HolidayHandler handler) {
		return RouterFunctions.route()
			.GET("/api/management/holiday", handler::getEnabledHolidays)
            .POST("/api/management/holiday", handler::createHoliday)
			.PUT("/api/management/holiday/{holidayId}", handler::updateHolidayDescription)
			.DELETE("/api/management/holiday/{holidayId}", handler::disableHoliday)
			.filter(handlerFunctionFilter)
			.build();
	}
}
