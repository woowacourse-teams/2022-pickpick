package com.pickpick.message.ui;

import com.pickpick.auth.support.AuthenticationPrincipal;
import com.pickpick.message.application.BookmarkService;
import com.pickpick.message.ui.dto.BookmarkRequest;
import com.pickpick.message.ui.dto.BookmarkResponses;
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
    public void save(final @AuthenticationPrincipal Long memberId,
                     final @RequestBody BookmarkRequest bookmarkRequest) {
        bookmarkService.save(memberId, bookmarkRequest);
    }

    @GetMapping
    public BookmarkResponses find(final @AuthenticationPrincipal Long memberId,
                                  final @RequestParam(required = false) Long bookmarkId) {
        return bookmarkService.find(bookmarkId, memberId);
    }

    @DeleteMapping("/{bookmarkId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(final @AuthenticationPrincipal Long memberId,
                       final @PathVariable Long bookmarkId) {
        bookmarkService.delete(bookmarkId, memberId);
    }
}
