package de.neiss.productdb.test;

import de.neiss.productdb.data.DBReader;
import de.neiss.productdb.model.Person;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DBReaderTest
{
	int expectedNumberOfPersons = 6;
	String expectedEntry = "Kyle Kelly";

	@Test
	public void testValidEntryCount() //Checks if the entries are valid(right amount of arguments) and no duplicates
	// are scanned
	{
		DBReader reader = new DBReader("dbreaderTestDB.txt");
		reader.readData();
		assertEquals("Duplicates or invalid entries not ignored!", expectedNumberOfPersons,
				reader.getPersons().size());
	}

	@Test
	public void testCorrectEntryFormat() //Checks if scanned entries are trimmed
	{
		DBReader reader = new DBReader("dbreaderTestDB.txt");
		reader.readData();
		assertEquals("Unnecessary spaces are not removed correctly", expectedEntry,
				reader.getPersons().get(2).getName());
	}

	@Test
	public void testFriendshipRelationships() //Checks if friendships work as intended and are always mutual
	{
		DBReader dbReader = new DBReader("dbreaderTestDB.txt");
		dbReader.readData();

		List<Person> ellisFriends = dbReader.getPersons().get(0).getFriends();
		assertEquals("Friends are not mutual or not distinct!", "[Mike Houston, Kyle Kelly, Rafael Fowler]",
				ellisFriends.stream()
						.map(Person::getName)
						.toList()
						.toString());
	}

}
