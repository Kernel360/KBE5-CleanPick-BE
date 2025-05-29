package com.kdev5.cleanpick.manager.infra.querydsl;

import java.util.List;
import java.util.Map;

public interface ManagerAvailableCleaningRepositoryCustom {

    Map<Long, List<String>> loadCleanings(List<Long> managerIds);
}
