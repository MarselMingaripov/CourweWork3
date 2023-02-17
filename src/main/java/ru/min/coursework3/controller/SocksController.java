package ru.min.coursework3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.min.coursework3.entity.Sock;
import ru.min.coursework3.service.SockService;

import java.util.Set;

@RestController
@RequestMapping("/api/socks")
@Tag(name = "API для работы с носками")
public class SocksController {

    private final SockService service;

    public SocksController(SockService service) {
        this.service = service;
    }

    @PostMapping("/")
    @Operation(
            summary = "Регистрирует приход товара на склад",
            description = "Параметры запроса передаются в теле запроса в виде JSON-объекта"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "успешная регистрация прихода товара на склад",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = Sock.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ошибка валидации"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "ошибка со стороны сервера"
            )
    })
    public ResponseEntity<Sock> addSockToStock(@RequestBody Sock sock){
        return ResponseEntity.ok().body(service.addSockToStock(sock));
    }

    @PutMapping("/")
    @Operation(
            summary = "Регистрирует отпуск носков со склада",
            description = "Параметры запроса передаются в теле запроса в виде JSON-объекта"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "успешная продажа товара со склада",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = Sock.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ошибка валидации"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "не хватает на складе"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "ошибка со стороны сервера"
            )
    })
    public ResponseEntity<Sock> sellSockFromStock(@RequestBody Sock sock){
        Sock sock1 = service.sellSockFromStock(sock);
        return ResponseEntity.ok().body(sock1);
    }

    @GetMapping("/")
    @Operation(
            summary = "Возвращает общее количество носков на складе, соответствующих переданным в параметрах критериям запроса",
            description = "Параметры запроса передаются в URL"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "успешно обработан запрос",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = Sock.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "нет на складе"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "ошибка со стороны сервера"
            )
    })
    public ResponseEntity<Integer> getSockByParams(@RequestParam(required = true) String color,
                                                   @RequestParam(required = true) int size,
                                                   @RequestParam(required = false, defaultValue = "0") int minCottonValue,
                                                   @RequestParam(required = false, defaultValue = "100") int maxCottonValue){
        int min = Integer.parseInt(String.valueOf(minCottonValue));
        int max = Integer.parseInt(String.valueOf(maxCottonValue));
        int totalCount = service.getTotalSockByParams(color, size, minCottonValue, maxCottonValue);
        return ResponseEntity.ok().body(totalCount);
    }

    @DeleteMapping("/")
    @Operation(
            summary = "Регистрирует списание испорченных (бракованных) носков",
            description = "Параметры запроса передаются в теле запроса в виде JSON-объекта"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "носки успешно списаны",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = Sock.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ошибка валидации"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "носков по заданным параметрам нет на складе"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "ошибка со стороны сервера"
            )
    })
    public ResponseEntity<Void> deleteFromStock(@RequestBody Sock sock){
        if (service.deleteSockFromStock(sock)){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    @Operation(
            summary = "Выводит список всех носков"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "носки успешно выведены",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = Sock.class)
                            )
                    }
            )
    })
    public Set<Sock> showAll(){
        return service.showAllSocks();
    }
}
