package filecollector.test.filemember;

import static org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import filecollector.model.filemember.DirectoryMember;
import filecollector.model.filemember.FileSystemMember.FileTimes;

// Dringend Tear, before after nutzen
public class EqualTest {

	Path p1 = Paths.get("D:/test");
	Path p2 = Paths.get("D:/test");
	Path p3 = Paths.get("D:/other");
	DirectoryMember d1 = new DirectoryMember(p1);
	DirectoryMember d2 = new DirectoryMember(p2);
	DirectoryMember d3 = new DirectoryMember(p3);
	FileTime ft1 = FileTime.from(5, TimeUnit.DAYS);
	FileTime ft2 = FileTime.from(2, TimeUnit.DAYS);
	
//	@Test
//	public void test() {
//		fail("Not yet implemented");
//	}
	@Test
	public void sameContent() {
		d1.setFileTimes(d1.new FileTimes(ft1, ft1, ft1));
		d2.setFileTimes(d2.new FileTimes(ft1, ft1, ft1));
		assertTrue(d1.equals(d2));
	}
	@Test
	public void notSameContent() {
		// diff path
		d1.setFileTimes(d1.new FileTimes(ft1, ft1, ft1));
		d3.setFileTimes(d3.new FileTimes(ft1, ft1, ft1));
		assertFalse(d1.equals(d3));
		// diff time
		d2.setFileTimes(d1.new FileTimes(ft1, ft1, ft2));
		assertFalse(d1.equals(d2));
	}
	@Test
	public void compare() {
		// gleich
		d1.setFileTimes(d1.new FileTimes(ft1, ft1, ft1));
		d2.setFileTimes(d2.new FileTimes(ft1, ft1, ft1));
		assertEquals(0, d1.compareTo(d2));
		// time 5 > 2
		d2.setFileTimes(d2.new FileTimes(ft1, ft1, ft2));
		assertEquals(1, d1.compareTo(d2));
		// path test > other
		d3.setFileTimes(d3.new FileTimes(ft1, ft1, ft1));
		assertEquals(1, d1.compareTo(d2));
	}
}
