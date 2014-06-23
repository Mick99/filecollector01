package filecollector;

import java.util.EnumSet;

import filecollector.model.FileAttributesEnum;

public class Main {
	public static void main (String[] args) {
		EnumSet<FileAttributesEnum> fa = EnumSet.of (FileAttributesEnum.CAN_EXECUTE, FileAttributesEnum.IS_HIDDEN);
		
		System.out.println ("Bla" + fa.toString ());
		for (FileAttributesEnum f : fa) {
			System.out.println (f.ordinal () + f.name () + f.printAttributeDescription ());
		}
	}
}
