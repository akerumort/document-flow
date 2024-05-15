package com.webprog.documentFlow.services;

import com.webprog.documentFlow.models.Document;
import com.webprog.documentFlow.models.DocumentType;
import com.webprog.documentFlow.repos.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Сервис для работы с документами.
 */
@Service
public class DocumentService {
    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    /**
     * Добавляет новый документ.
     *
     * @param document Документ для добавления
     * @return Добавленный документ
     * @throws IllegalArgumentException Если документ с такими данными уже существует
     */
    public Document addDocument(Document document) {
        List<Document> existingDocuments = documentRepository.finByAll(
                document.getNumber(), document.getTitle(), document.getDate(), document.getType());
        if (!existingDocuments.isEmpty()) {
            throw new IllegalArgumentException("Документ с такими данными уже существует");
        }
        return documentRepository.save(document);
    }

    /**
     * Возвращает список всех документов.
     *
     * @return Список всех документов
     */
    public List<Document> getAllDocuments() {
        return (List<Document>) documentRepository.findAll();
    }

    /**
     * Возвращает документ по его идентификатору.
     *
     * @param id Идентификатор документа
     * @return Документ с указанным идентификатором
     * @throws IllegalArgumentException Если недопустимый идентификатор документа
     */
    public Document getDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Недопустимый идентификатор документа:" + id));
    }

    /**
     * Обновляет информацию о документе.
     *
     * @param document Обновленная информация о документе
     */
    public void updateDocument(Document document) {
        documentRepository.save(document);
    }

    /**
     * Удаляет документ по его идентификатору.
     *
     * @param id Идентификатор документа
     */
    public void deleteDocumentById(Long id) {
        documentRepository.deleteById(id);
    }


    /**
     * Проверяет существование документа.
     *
     * @param document Документ для проверки
     * @return true, если документ существует, в противном случае - false
     */
    public boolean documentExists(Document document) {
        List<Document> existingDocuments = documentRepository.finByAll(
                document.getNumber(), document.getTitle(), document.getDate(), document.getType());

        if (!existingDocuments.isEmpty()) {
            for (Document existingDocument : existingDocuments) {
                if (existingDocument.getNumber().equals(document.getNumber())
                        && existingDocument.getTitle().equals(document.getTitle())
                        && existingDocument.getDate().equals(document.getDate())
                        && existingDocument.getType().equals(document.getType())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Поиск документов по их номеру (частичное совпадение).
     *
     * @param number Номер документа или его часть
     * @return Список документов, чей номер содержит указанную строку
     */
    public List<Document> findDocumentsByNumber(String number) {
        return documentRepository.findByNumberContaining(number);
    }

    /**
     * Получение документов по их типу.
     *
     * @param documentType Тип документа
     * @return Список документов указанного типа
     */
    public List<Document> getDocumentsByType(DocumentType documentType) {
        return documentRepository.findByType(documentType);
    }
}
