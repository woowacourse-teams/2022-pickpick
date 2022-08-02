package com.pickpick.message.ui;

import com.pickpick.message.application.BookmarkService;
import com.pickpick.message.ui.dto.BookmarkRequest;
import com.pickpick.message.ui.dto.BookmarkResponses;
import com.pickpick.utils.AuthorizationExtractor;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(final BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void save(final HttpServletRequest request,
                     final @RequestBody BookmarkRequest bookmarkRequest) {
        String memberId = AuthorizationExtractor.extract(request);
        bookmarkService.save(Long.parseLong(memberId), bookmarkRequest);
    }

    @GetMapping
    public BookmarkResponses find(final HttpServletRequest request,
                                  final @RequestParam(required = false) Long bookmarkId) {
        String memberId = AuthorizationExtractor.extract(request);
        return bookmarkService.find(bookmarkId, Long.parseLong(memberId));
    }

    @DeleteMapping("/{bookmarkId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(final HttpServletRequest request,
                       final @PathVariable Long bookmarkId) {
        String memberId = AuthorizationExtractor.extract(request);
        bookmarkService.delete(bookmarkId, Long.parseLong(memberId));
    }
}
