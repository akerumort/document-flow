package com.webprog.documentFlow.controllers;


import com.webprog.documentFlow.models.Document;
import com.webprog.documentFlow.models.DocumentType;
import com.webprog.documentFlow.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Контроллер для обработки запросов главной страницы.
 */
@Controller
public class MainController {
    private final DocumentService documentService;

    @Autowired
    public MainController(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * Отображает главную страницу приложения.
     *
     * @param model        модель для передачи данных в представление
     * @param search       параметр поиска по номеру документа
     * @param documentType тип документа для фильтрации
     * @return представление главной страницы
     */
    @GetMapping("/")
    public String home(Model model,
                       @RequestParam(name = "search", required = false) String search,
                       @RequestParam(name = "documentType", required = false) DocumentType documentType) {
        Iterable<Document> documents;
        if (search != null && !search.isEmpty()) {
            documents = documentService.findDocumentsByNumber(search);
        } else if (documentType != null) {
            documents = documentService.getDocumentsByType(documentType);
        } else {
            documents = documentService.getAllDocuments();
        }
        model.addAttribute("title", "Главная страница");
        model.addAttribute("documents", documents);
        return "home";
    }
}