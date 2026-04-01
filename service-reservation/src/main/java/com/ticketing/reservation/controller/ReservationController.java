package com.ticketing.reservation.controller;

import com.ticketing.reservation.dto.ReservationRequest;
import com.ticketing.reservation.dto.ReservationResponse;
import com.ticketing.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "예약", description = "예약 생성·조회 API (Gateway가 `X-User-Id` 헤더를 전달한다)")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    @Operation(summary = "예약 생성")
    public ResponseEntity<ReservationResponse> createReservation(
            @Parameter(
                    name = "X-User-Id",
                    description = "사용자 ID (Gateway가 JWT에서 추출해 설정)",
                    in = ParameterIn.HEADER,
                    required = true)
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody ReservationRequest request
    ) {
        return ResponseEntity.ok(reservationService.createReservation(userId, request));
    }

    @GetMapping("/{reservationId}")
    @Operation(summary = "예약 단건 조회")
    public ResponseEntity<ReservationResponse> getReservation(
            @Parameter(description = "예약 ID") @PathVariable String reservationId) {
        return ResponseEntity.ok(reservationService.getReservation(reservationId));
    }

    @GetMapping("/me")
    @Operation(summary = "내 예약 목록 조회")
    public ResponseEntity<List<ReservationResponse>> getMyReservations(
            @Parameter(
                    name = "X-User-Id",
                    description = "사용자 ID (Gateway가 JWT에서 추출해 설정)",
                    in = ParameterIn.HEADER,
                    required = true)
            @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(reservationService.getReservationsByUser(userId));
    }
}
