package entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.Constants;

public class TestPersonPool {
    @Test
    public void testPeopleSizeInitialState() {
        PersonPool personPool = PersonPool.getInstance();
        Assertions.assertEquals(Constants.CITY_PERSON_SIZE, personPool.getPeopleSize(-1));
    }
}
