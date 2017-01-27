package com.obrienlabs.gps.integration;

import com.obrienlabs.gps.business.entity.Record;

public interface RecordDAO extends DAORoot<Record, Long> {
	Record read(Long id);

}
