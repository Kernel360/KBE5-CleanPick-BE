package com.kdev5.cleanpick.manager.infra.querydsl;

import java.util.List;
import java.util.Map;

public interface ManagerAvailableRegionRepositoryCustom {

    Map<Long, List<String>> loadRegions(List<Long> managerIds);
}
