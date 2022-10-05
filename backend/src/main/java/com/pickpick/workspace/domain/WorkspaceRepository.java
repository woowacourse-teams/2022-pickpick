package com.pickpick.workspace.domain;

import com.pickpick.exception.workspace.WorkspaceNotFoundException;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface WorkspaceRepository extends Repository<Workspace, Long> {

    Workspace save(Workspace workspace);

    Optional<Workspace> findBySlackId(String slackId);

    default Workspace getBySlackId(final String slackId) {
        return findBySlackId(slackId)
                .orElseThrow(() -> new WorkspaceNotFoundException(slackId));
    }
}
