package com.bradsbrain.simpleastronomy;

import static com.bradsbrain.simpleastronomy.BaseUtils.formatDateAsShortDateLocalTime;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

public class NewMoonFinderTest {

    private static final ZoneId chicagoTimeZone = ZoneId.of("America/Chicago");

    @Test
    public void testFindNewMoonFollowing() {
        ZonedDateTime cal = ZonedDateTime.of(2010, 10, 20, 0, 0, 0, 0, chicagoTimeZone);

        ZonedDateTime newMoonDate = MoonPhaseFinder.findNewMoonFollowing(cal);
        assertThat(newMoonDate, is(not(nullValue())));
        assertThat(formatDateAsShortDateLocalTime(newMoonDate, chicagoTimeZone), equalTo("2010-11-06"));
    }

}
