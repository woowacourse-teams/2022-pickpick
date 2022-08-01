package com.pickpick.message.ui;

import com.pickpick.message.application.BookmarkService;
import com.pickpick.message.ui.dto.BookmarkRequest;
import com.pickpick.utils.AuthorizationExtractor;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void save(HttpServletRequest request,
                     @RequestBody BookmarkRequest bookmarkRequest) {
        String memberId = AuthorizationExtractor.extract(request);
        bookmarkService.save(Long.parseLong(memberId), bookmarkRequest);
    }
}
