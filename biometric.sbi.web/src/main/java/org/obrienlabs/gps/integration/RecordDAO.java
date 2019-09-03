package org.obrienlabs.gps.integration;

import org.obrienlabs.gps.business.entity.Record;

public interface RecordDAO extends DAORoot<Record, Long> {
	Record read(Long id);

}
