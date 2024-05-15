package com.webprog.documentFlow.controllers;

import com.webprog.documentFlow.models.Document;
import com.webprog.documentFlow.models.DocumentType;
import com.webprog.documentFlow.services.DocumentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.UriUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Контроллер для управления документами.
 */
@Controller
@RequestMapping("/documents")
public class DocumentController {
    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * Получает страницу для управления документами.
     *
     * @param model  модель для передачи данных в представление
     * @param search параметр поиска по номеру документа
     * @return представление для управления документами
     */
    @GetMapping("/managing")
    public String managing(Model model, @RequestParam(name = "search", required = false) String search) {
        Iterable<Document> documents;
        if (search != null && !search.isEmpty()) {
            documents = documentService.findDocumentsByNumber(search);
        } else {
            documents = documentService.getAllDocuments();
        }
        model.addAttribute("title", "Управление документами");
        model.addAttribute("documents", documents);
        return "managing";
    }

    /**
     * Отображает форму для добавления документа.
     *
     * @param model модель для передачи данных в представление
     * @return представление для добавления документа
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("document", new Document());

        return "addDocument";
    }

    /**
     * Обрабатывает добавление нового документа.
     *
     * @param document данные нового документа
     * @param request  запрос
     * @param model    модель для передачи данных в представление
     * @return представление для управления документами или форму для добавления документа с сообщением об ошибке
     */
    @PostMapping("/add")
    public String addDocument(@ModelAttribute Document document,
                              HttpServletRequest request,
                              Model model) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");

        if (file != null && !file.isEmpty()) {
            try {
                document.setFileName(file.getOriginalFilename());
                document.setFileData(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        boolean documentExists = documentService.documentExists(document);
        if (documentExists) {
            model.addAttribute("errorMessage", "Документ с такими данными уже существует");
            return "addDocument";
        } else {
            documentService.addDocument(document);
            return "redirect:/documents/managing";
        }
    }

    /**
     * Отображает форму для редактирования документа.
     *
     * @param id    идентификатор документа для редактирования
     * @param model модель для передачи данных в представление
     * @return представление для редактирования документа
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Document document = documentService.getDocumentById(id);
        model.addAttribute("document", document);
        return "editDocument";
    }

    /**
     * Обрабатывает редактирование существующего документа.
     *
     * @param id              идентификатор документа для редактирования
     * @param updatedDocument обновленные данные документа
     * @param request         запрос
     * @param model           модель для передачи данных в представление
     * @return представление для управления документами или форму для редактирования документа с сообщением об ошибке
     */
    @PostMapping("/edit/{id}")
    public String editDocument(@PathVariable Long id, @ModelAttribute Document updatedDocument, HttpServletRequest request, Model model) {
        Document existingDocument = documentService.getDocumentById(id);
        List<Document> existingDocuments = documentService.getAllDocuments();
        for (Document doc : existingDocuments) {
            if (doc.getId().equals(existingDocument.getId())) {
                continue;
            }
            if (doc.getNumber().equals(updatedDocument.getNumber())
                    && doc.getTitle().equals(updatedDocument.getTitle())
                    && doc.getDate().equals(updatedDocument.getDate())
                    && doc.getType().equals(updatedDocument.getType())) {
                model.addAttribute("errorMessage", "Невозможно " +
                        "обновить документ на такой же, какой уже существует");
                model.addAttribute("document", existingDocument);
                return "editDocument";
            }
        }
        existingDocument.setNumber(updatedDocument.getNumber());
        existingDocument.setTitle(updatedDocument.getTitle());
        existingDocument.setDate(updatedDocument.getDate());
        existingDocument.setType(updatedDocument.getType());

        if (request instanceof MultipartHttpServletRequest) {
            MultipartFile newFile = ((MultipartHttpServletRequest) request).getFile("file");
            if (newFile != null && !newFile.isEmpty()) {
                try {
                    existingDocument.setFileName(newFile.getOriginalFilename());
                    existingDocument.setFileData(newFile.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        documentService.updateDocument(existingDocument);
        return "redirect:/documents/managing";
    }

    /**
     * Обрабатывает удаление документа.
     *
     * @param id идентификатор документа для удаления
     * @return представление для управления документами
     */
    @PostMapping("/delete/{id}")
    public String deleteDocument(@PathVariable Long id) {
        documentService.deleteDocumentById(id);
        return "redirect:/documents/managing";
    }

    /**
     * Обрабатывает скачивание файла, прикрепленного к документу.
     *
     * @param id идентификатор документа, к которому прикреплен файл
     * @return объект ResponseEntity с файлом в теле ответа
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String id) {
        Long documentId = Long.parseLong(id);
        Document document = documentService.getDocumentById(documentId);
        String encodedFileName = UriUtils.encode(document.getFileName(), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=UTF-8''" + encodedFileName)
                .body(new ByteArrayResource(document.getFileData()));
    }
}
