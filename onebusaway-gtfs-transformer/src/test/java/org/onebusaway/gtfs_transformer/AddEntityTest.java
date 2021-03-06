/**
 * Copyright (C) 2011 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onebusaway.gtfs_transformer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;
import org.onebusaway.gtfs.model.AgencyAndId;
import org.onebusaway.gtfs.model.Frequency;
import org.onebusaway.gtfs.serialization.mappings.StopTimeFieldMappingFactory;
import org.onebusaway.gtfs.services.GtfsRelationalDao;

public class AddEntityTest extends AbstractTestSupport {

  @Test
  public void test() throws IOException {
    _gtfs.putDefaultStopTimes();
    addModification("{'op':'add','obj':{'class':'Frequency','trip':'T10-0','startTime':'08:00:00','endTime':'10:00:00','headwaySecs':600}}");
    GtfsRelationalDao dao = transform();

    Collection<Frequency> frequencies = dao.getAllFrequencies();
    assertEquals(1, frequencies.size());

    Frequency frequency = frequencies.iterator().next();
    assertSame(dao.getTripForId(new AgencyAndId("1", "T10-0")),
        frequency.getTrip());
    assertEquals(StopTimeFieldMappingFactory.getStringAsSeconds("08:00:00"),
        frequency.getStartTime());
    assertEquals(StopTimeFieldMappingFactory.getStringAsSeconds("10:00:00"),
        frequency.getEndTime());
    assertEquals(600, frequency.getHeadwaySecs());
  }

}
