package com.pickpick.message.ui;

import com.pickpick.auth.support.AuthenticationPrincipal;
import com.pickpick.message.application.ReminderService;
import com.pickpick.message.ui.dto.ReminderResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {


    private final ReminderService reminderService;

    public ReminderController(final ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @GetMapping
    public ReminderResponses find(final @AuthenticationPrincipal Long memberId,
                                  final @RequestParam(required = false) Long reminderId) {
        return reminderService.find(reminderId, memberId);
    }
}
