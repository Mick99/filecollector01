package filecollector.test.filemember;

import static org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import filecollector.model.filemember.DirectoryMember;

// Dringend Tear, before after nutzen
public class EqualTest {

	Path p1 = Paths.get("D:/test");
	Path p2 = Paths.get("D:/test");
	Path p3 = Paths.get("D:/other");
	DirectoryMember d1 = new DirectoryMember(p1);
	DirectoryMember d2 = new DirectoryMember(p2);
	DirectoryMember d3 = new DirectoryMember(p3);
//	DirectoryMember d3 = d1;
	FileTime ft1 = FileTime.from(5, TimeUnit.DAYS);
	FileTime ft2 = FileTime.from(2, TimeUnit.DAYS);
	FileTime ft3 = FileTime.from(5, TimeUnit.DAYS);
	
	@Test
	public void objectsNotSame() {
		assertFalse(d1 == d2);
		assertFalse(d1 == d3);
		assertFalse(d2 == d3);
		assertFalse(ft1 == ft2);
		assertFalse(ft1 == ft3);
		assertFalse(ft2 == ft3);
	}
	@Test
	public void equalsContent() {
		// same
		d1.setFileTimes_OnlyForJUnitTests(ft1, ft1, ft1);
		d2.setFileTimes_OnlyForJUnitTests(ft1, ft1, ft1);
		assertTrue(d1.equals(d2));
		// diff path
		d3.setFileTimes_OnlyForJUnitTests(ft1, ft1, ft1);
		assertFalse(d1.equals(d3));
		// diff time
		d2.setFileTimes_OnlyForJUnitTests(ft1, ft1, ft2);
		assertFalse(d1.equals(d2));
	}
//	@Test
	public void notSameContentEquals() {
		// diff path
		d1.setFileTimes_OnlyForJUnitTests(ft1, ft1, ft1);
		d3.setFileTimes_OnlyForJUnitTests(ft1, ft1, ft1);
		assertFalse(d1.equals(d3));
		// diff time
		d2.setFileTimes_OnlyForJUnitTests(ft1, ft1, ft2);
		assertFalse(d1.equals(d2));
	}
	@Test
	public void compareContent() {
		// gleich
		d1.setFileTimes_OnlyForJUnitTests(ft1, ft1, ft1);
		d2.setFileTimes_OnlyForJUnitTests(ft1, ft1, ft1);
		assertEquals(0, d1.compareTo(d2));
		// time 5 > 2
		d2.setFileTimes_OnlyForJUnitTests(ft1, ft1, ft2);
		assertEquals(1, d1.compareTo(d2));
		// path test > other
		d3.setFileTimes_OnlyForJUnitTests(ft1, ft1, ft1);
		assertEquals(1, d1.compareTo(d2));
		// only lastModifiedTime will compared
		d2.setFileTimes_OnlyForJUnitTests(ft2, ft2, ft1);
		assertEquals(0, d1.compareTo(d2));
	}
	@Test
	public void hashCodeTests() {
		// FileTimes = null
		assertTrue(d1.hashCode() == d2.hashCode());
		// same content
		d1.setFileTimes_OnlyForJUnitTests(ft1, ft1, ft1);
		d2.setFileTimes_OnlyForJUnitTests(ft1, ft1, ft1);
		assertTrue(d1.hashCode() == d2.hashCode());
		// diff Path
		d3.setFileTimes_OnlyForJUnitTests(ft1, ft1, ft1);
		assertFalse(d1.hashCode() == d3.hashCode());
		// diff Times
		d2.setFileTimes_OnlyForJUnitTests(ft1, ft2, ft1);
		assertFalse(d1.hashCode() == d2.hashCode());
	}
}
