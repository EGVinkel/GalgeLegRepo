package brugerautorisation.transport.soap;

import brugerautorisation.data.Bruger;
import brugerautorisation.data.Diverse;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author j
 */
public class Brugeradminklient {
	public static void main(String[] args) throws MalformedURLException {
		//URL url = new URL("http://localhost:9901/brugeradmin?wsdl");
		URL url = new URL("http://javabog.dk:9901/brugeradmin?wsdl");
		QName qname = new QName("http://soap.transport.brugerautorisation/", "BrugeradminImplService");
		Service service = Service.create(url, qname);
		Brugeradmin ba = service.getPort(Brugeradmin.class);

		Bruger off = ba.hentBrugerOffentligt("s175107");
		System.out.println("Fik offentlige data " + Diverse.toString(off));

    //ba.sendGlemtAdgangskodeEmail("s123456", "Dette er en test, husk at skifte kode");
		//ba.ændrAdgangskode("s123456", "kode1xyz", "kode1xyz");
		Bruger b = ba.hentBruger("s175107", "liam");
		System.out.println("Fik bruger = " + b);
		System.out.println("Data: " + Diverse.toString(b));
		// ba.sendEmail("jacno", "xxx", "Hurra det virker!", "Jeg er så glad");

		Object ekstraFelt = ba.getEkstraFelt("s175107", "liam", "hobby");
		System.out.println("Brugerens hobby er: " + ekstraFelt);

	}
}
