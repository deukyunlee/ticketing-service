package com.ticketing.ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@Schema(description = "공연 생성 요청")
public class CreateEventRequest {

    @NotBlank
    @Schema(description = "공연 제목", example = "봄 콘서트")
    private String title;

    @Schema(description = "설명")
    private String description;

    @NotBlank
    @Schema(description = "공연장", example = "올림픽홀")
    private String venue;

    @NotNull
    @Future
    @Schema(description = "공연 일시")
    private LocalDateTime eventDate;

    @Min(1)
    @Max(1000)
    @Schema(description = "총 좌석 수(생성될 좌석 개수)", example = "100")
    private int totalSeats;

    @Min(0)
    @Schema(description = "좌석당 가격(원)", example = "50000")
    private long price;
}
