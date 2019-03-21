package brugerautorisation.transport.rmi;

import brugerautorisation.data.Bruger;
import brugerautorisation.data.Diverse;

import java.rmi.Naming;

public class Brugeradminklient {
	public static void main(String[] arg) throws Exception {
//		Brugeradmin ba =(Brugeradmin) Naming.lookup("rmi://localhost/brugeradmin");
		Brugeradmin ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");

		Bruger off = ba.hentBrugerOffentligt("s175107");
		System.out.println("Fik offentlige data " + Diverse.toString(off));

    //ba.sendGlemtAdgangskodeEmail("s123456", "Dette er en test, husk at skifte kode");
	//	ba.ændrAdgangskode("s175107", "kodeiq6hwt", "liam");
		Bruger b = ba.hentBruger("s175107", "liam");
		System.out.println("Fik bruger " + b);
		System.out.println("med data " + Diverse.toString(b));
		// ba.sendEmail("jacno", "xxx", "Hurra det virker!", "Jeg er så glad");

		Object ekstraFelt = ba.getEkstraFelt("s175107", "liam", "hobby");
		System.out.println("Brugerens hobby er: " + ekstraFelt);

		ba.setEkstraFelt("s175107", "liam", "hobby", "Programmering og familie"); // Skriv noget andet her

		String webside = (String) ba.getEkstraFelt("s175107", "liam", "webside");
		System.out.println("Brugerens webside er: " + webside);
	}
}
