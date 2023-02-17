package ru.min.coursework3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.min.coursework3.service.FilesService;
import ru.min.coursework3.service.SockService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/files")
@Tag(name = "API для работы с файлами")
public class FilesController {

    private final FilesService filesService;
    private final SockService service;

    public FilesController(FilesService filesService, SockService service) {
        this.filesService = filesService;
        this.service = service;
    }

    @GetMapping("/export")
    @Operation(
            summary = "Скачать файл с носками",
            description = "позволяет сохранить файл на компьютер"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Файл сохранен"
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Запрос корректен, но не найден файл на сервере"
            )
    })
    public ResponseEntity<InputStreamResource> downloadFile() throws FileNotFoundException {
        File file = filesService.getSocksDataFile();
        if (file.exists()) {
            InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Socks.json\"")
                    .body(inputStreamResource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Загрузить файл с носками",
            description = "позволяет загрузить файл на сервер, перезатрет старый файл"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Файл загружен"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка со стороны сервера"
            )
    })
    public ResponseEntity<Void> uploadRecipeFile(@RequestParam MultipartFile multipartFile) {
        filesService.cleanSocksDataFile();
        File dataFile = filesService.getSocksDataFile();
        try (FileOutputStream fos = new FileOutputStream(dataFile)) {
            IOUtils.copy(multipartFile.getInputStream(), fos);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


    @GetMapping("/download")
    @Operation(
            summary = "Скачать файл продажи - поступления",
            description = "скачать файл отформатированный для удобного чтения"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "файл сформирован и готов к скачиванию"
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "файл пуст, нет сохраненных рецептов"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "ошибка со стороны сервера"
            )
    })
    public ResponseEntity<Object> download() {
        try {
            Path path = service.createInputOutputFile();
            if (Files.size(path) != 0) {
                InputStreamResource ios = new InputStreamResource(new FileInputStream(path.toFile()));
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .contentLength(Files.size(path))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "input_output.txt\"")
                        .body(ios);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/download/input")
    @Operation(
            summary = "Скачать файл поступлений",
            description = "скачать файл поступлений товара на склад"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "файл сформирован и готов к скачиванию"
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "файл пуст, нет сохраненных рецептов"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "ошибка со стороны сервера"
            )
    })
    public ResponseEntity<Object> downloadInputFile() throws FileNotFoundException {
        File file = filesService.getInputDataFile();
        if (file.exists()) {
            InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Socks_input.json\"")
                    .body(inputStreamResource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/download/output")
    @Operation(
            summary = "Скачать файл продаж",
            description = "скачать файл продаж товара со склада"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "файл сформирован и готов к скачиванию"
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "файл пуст, нет сохраненных рецептов"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "ошибка со стороны сервера"
            )
    })
    public ResponseEntity<Object> downloadOutputFile() throws FileNotFoundException {
        File file = filesService.getOutputDataFile();
        if (file.exists()) {
            InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Socks_output.json\"")
                    .body(inputStreamResource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(value = "/import/input", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Загрузить файл с поступлением носков",
            description = "позволяет загрузить файл на сервер"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Файл загружен"
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Файл пуст, но запрос корректен"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка со стороны сервера"
            )
    })
    public ResponseEntity<Void> uploadInputFile(@RequestParam MultipartFile multipartFile) {
        filesService.cleanInputFile();
        File dataFile = filesService.getInputDataFile();
        try (FileOutputStream fos = new FileOutputStream(dataFile)) {
            IOUtils.copy(multipartFile.getInputStream(), fos);
            //service.updateStockByInputData();
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping(value = "/import/output", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Загрузить файл с продажей носков",
            description = "позволяет загрузить файл на сервер"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Файл загружен"
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Файл пуст, но запрос корректен"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка со стороны сервера"
            )
    })
    public ResponseEntity<Void> uploadOutputFile(@RequestParam MultipartFile multipartFile) {
        filesService.cleanOutputFile();
        File dataFile = filesService.getOutputDataFile();
        try (FileOutputStream fos = new FileOutputStream(dataFile)) {
            IOUtils.copy(multipartFile.getInputStream(), fos);
            //service.updateStockByOutputData();
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/sync")
    @Operation(
            summary = "Синхронизировать данные на складе",
            description = "позволяет загрузить файл на сервер, произойдет продажа/оприходование товара в соответствии с данными " +
                    "в файлах input.json/output.json"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Синхронизация успешна"
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Файл или оба файла пусты, но запрос корректен"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка со стороны сервера"
            )
    })
    public ResponseEntity<Void> updateDataByFiles(){
        service.updateStockByInputData();
        service.updateStockByOutputData();
        return ResponseEntity.ok().build();
    }
}
