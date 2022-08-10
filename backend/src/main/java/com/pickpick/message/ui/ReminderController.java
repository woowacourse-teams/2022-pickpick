package com.pickpick.message.ui;

import com.pickpick.auth.support.AuthenticationPrincipal;
import com.pickpick.message.application.ReminderService;
import com.pickpick.message.ui.dto.ReminderRequest;
import com.pickpick.message.ui.dto.ReminderResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(final ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void save(final @AuthenticationPrincipal Long memberId, final @RequestBody ReminderRequest reminderRequest) {
        reminderService.save(memberId, reminderRequest);
    }


    @GetMapping
    public ReminderResponses find(final @AuthenticationPrincipal Long memberId,
                                  final @RequestParam(required = false) Long reminderId) {
        return reminderService.find(reminderId, memberId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    public void delete(final @AuthenticationPrincipal Long memberId,
                       final @RequestParam Long messageId) {
        reminderService.delete(messageId, memberId);
    }
}
