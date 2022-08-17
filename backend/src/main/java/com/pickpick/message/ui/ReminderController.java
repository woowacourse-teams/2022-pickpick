package com.pickpick.message.ui;

import com.pickpick.auth.support.AuthenticationPrincipal;
import com.pickpick.message.application.ReminderService;
import com.pickpick.message.ui.dto.ReminderFindRequest;
import com.pickpick.message.ui.dto.ReminderSaveRequest;
import com.pickpick.message.ui.dto.ReminderResponse;
import com.pickpick.message.ui.dto.ReminderResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public void save(@AuthenticationPrincipal final Long memberId, @RequestBody final ReminderSaveRequest reminderSaveRequest) {
        reminderService.save(memberId, reminderSaveRequest);
    }

    @GetMapping(params = "messageId")
    public ReminderResponse findOne(final @AuthenticationPrincipal Long memberId, final @RequestParam Long messageId) {
        return reminderService.findOne(messageId, memberId);
    }

    @GetMapping
    public ReminderResponses find(@AuthenticationPrincipal final Long memberId, final ReminderFindRequest request) {
        return reminderService.find(request, memberId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    public void delete(@AuthenticationPrincipal final Long memberId, @RequestParam final Long messageId) {
        reminderService.delete(messageId, memberId);
    }

    @PutMapping
    public void update(@AuthenticationPrincipal final Long memberId,
                       @RequestBody final ReminderSaveRequest request) {
        reminderService.update(memberId, request);
    }
}
