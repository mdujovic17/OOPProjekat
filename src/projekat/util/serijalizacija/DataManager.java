package projekat.util.serijalizacija;

import projekat.knjiga.Autor;
import projekat.knjiga.Izdavac;
import projekat.knjiga.Knjiga;
import projekat.osoba.Administrator;
import projekat.osoba.Clan;
import projekat.osoba.Pozajmljivanje;
import projekat.osoba.Sifra;
import projekat.sistem.PravilaBiblioteke;
import projekat.util.TokProgramaException;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**Staticka klasa koja sadrzi metode za serijalizaciju i deserijalizcaju objekata.*/
public class DataManager {

	private static final String FOLDER = "data//";

	private static BufferedReader br;
	private static BufferedWriter bw;

	/**Vrsi upisivanje Stringa u fajl. String sadrzi podatke objekta koji treba da se "Serijalizuje".
	 * @param object Objekat koji je formatiran pomocu toStringSerializable()
	 * @param fileName Ime fajla gde treba da se cuva objekat.
	 * @throws IOException u koliko upisivanje objekta nije uspelo*/
	public static void serializeString(String object, String fileName) throws IOException {
		if (new File(fileName).exists()) {
			bw = new BufferedWriter(new FileWriter(FOLDER + fileName, true));
		}
		else {
			bw = new BufferedWriter(new FileWriter(FOLDER + fileName));
		}

		bw.write(object + '\n');

		bw.flush();
		bw.close();
	}

	/**Vrsi upisivanje Stringa u fajl. String sadrzi podatke objekta koji treba da se "Serijalizuje".
	 * @param object Objekat koji je formatiran pomocu toStringSerializable()
	 * @param fileName Ime fajla gde treba da se cuva objekat.
	 * @param append Da li da se sadrzaj dopisuje na kraj fajla, ili da pise fajl od pocetka
	 * @throws IOException u koliko upisivanje objekta nije uspelo*/
	public static void serializeString(String object, String fileName, boolean append) throws IOException {
		if (new File(fileName).exists()) {
			bw = new BufferedWriter(new FileWriter(FOLDER + fileName, append));
		}

		bw.write(object + '\n');

		bw.flush();
		bw.close();
	}

	/**Vrsi serijalizaciju liste stringova koji sadrze informacije o objektu.
	 * @param objects Lista stringova
	 * @param fileName Datoteka u kojoj ce se cuvati podaci
	 * @param append Da li da obrise sve iz fajla i napise sadrzaj liste, ili da doda sadrzaj liste ispod postojeceg sadrzaja
	 * @throws IOException u koliko ne moze da se upisu podaci u fajl.*/
	public static void serializeString(ArrayList<String> objects, String fileName, boolean append) throws IOException {
		bw = new BufferedWriter(new FileWriter(FOLDER + fileName, append));

		for (String object : objects) {
			bw.write(object + '\n');
		}

		bw.flush();
		bw.close();
	}

	/**Vrsi deserijalizaciju administratora. Zahteva da se pre toga deserijalizuju sifre i dozvole*/
	public static ArrayList<Administrator> deserializeAdmins(ArrayList<Sifra> passwds, ArrayList<Administrator.Dozvole> dozvole) throws IOException, TokProgramaException {
		if (passwds == null || passwds.size() == 0) {
			throw new TokProgramaException("Sifre moraju biti deserijalizovane pre administratora!");
		} else if (dozvole == null || dozvole.size() == 0) {
			throw new TokProgramaException("Dozvole moraju biti deserijalizovane pre administratora!");
		}
		String line;
		String[] lista;
		ArrayList<Administrator> administratori = new ArrayList<>();
		br = new BufferedReader(new FileReader(FOLDER + "administrator.tdb"));

		while((line = br.readLine()) != null) {
			Administrator a = new Administrator();
			lista = line.split("~");
			a.setUUID(lista[0]);
			a.setIme(lista[1]);
			a.setPrezime(lista[2]);
			a.setAdresa(lista[3]);
			a.setBrTelefona(lista[4]);
			a.setEmail(lista[5]);
			a.setPol(Integer.parseInt(lista[6]));
			a.setUsername(lista[7]);

			for (Sifra s : passwds) {
				if (s.getKorisnickiUUID().equals(a.getUUID())) {
					a.setPassword(s);
					break;
				}
			}
			for (Administrator.Dozvole d : dozvole) {
				if (d.getUserUUID().equals(a.getUUID())) {
					a.setDozvole(d);
					break;
				}
			}

			administratori.add(a);
		}

		br.close();
		return administratori;
	}

	/**Vrsi deserijalizaciju clanova. Zahteva da se pre toga deserijalizuje lista pozajmljivanja*/
	public static ArrayList<Clan> deserializeClanovi(ArrayList<Pozajmljivanje> pozajmljivanja) throws IOException, TokProgramaException {
		if (pozajmljivanja == null || pozajmljivanja.size() == 0) {
			throw new TokProgramaException("Lista pozajmljivanja mora biti deserijalizovana pre clanova biblioteke!");
		}

		String line;
		String[] lista;
		ArrayList<Clan> clanovi = new ArrayList<>();
		br = new BufferedReader(new FileReader(FOLDER + "clan.tdb"));

		while((line = br.readLine()) != null) {
			Clan c = new Clan();
			lista = line.split("~");

			c.setUUID(lista[0]);
			c.setIme(lista[1]);
			c.setPrezime(lista[2]);
			c.setAdresa(lista[3]);
			c.setBrTelefona(lista[4]);
			c.setPol(Integer.parseInt(lista[5]));
			c.setEmail(lista[6]);

			for (Pozajmljivanje p : pozajmljivanja) {
				if (p.getClanUUID().equals(c.getUUID())) {
					c.getPozajmljivanje().add(p);
				}
			}

			clanovi.add(c);
		}

		br.close();
		return clanovi;
	}
	/**Vrsi deserijalizaciju sifara. Ne zahteva prethodne deserijalizacije*/
	public static ArrayList<Sifra> deserializeSifre() throws IOException {

		String line;
		String[] lista;
		ArrayList<Sifra> sifre = new ArrayList<>();
		br = new BufferedReader(new FileReader(FOLDER + "sifra.tdb"));

		while((line = br.readLine()) != null) {
			Sifra s = new Sifra();
			lista = line.split("~");

			s.setKorisnickiUUID(lista[0]);
			s.setSifra(lista[1]);

			sifre.add(s);
		}

		br.close();
		return sifre;
	}

	/**Vrsi deserijalizaciju clanova. Zahteva da se pre toga deserijalizuje lista autora i izdavaca*/
	public static ArrayList<Knjiga> deserializeKnjige(ArrayList<Autor> autori, ArrayList<Izdavac> izdavaci) throws IOException, TokProgramaException {

		if (autori == null || autori.size() == 0) {
			throw new TokProgramaException("Lista autora mora biti deserijalizovana pre knjiga!");
		}
		else if (izdavaci == null || izdavaci.size() == 0) {
			throw new TokProgramaException("Lista izdavaca mora biti deserijalizovana pre knjiga");
		}

		String line;
		String[] lista;
		ArrayList<Knjiga> knjige = new ArrayList<>();
		br = new BufferedReader(new FileReader(FOLDER + "knjiga.tdb"));

		while((line = br.readLine()) != null) {
			String[] zanrovi, autors;
			Knjiga k = new Knjiga();
			lista = line.split("~");

			k.setId(lista[0]);
			k.setImeKnjige(lista[1]);
			k.setGodinaObjavljivanja(Integer.parseInt(lista[4]));
			k.setIzdanje(Integer.parseInt(lista[5]));
			k.setBrStrana(Integer.parseInt(lista[6]));
			k.setISBN(lista[7]);
			k.setKategorija(Integer.parseInt(lista[8]));
			k.setKolicina(Integer.parseInt(lista[9]));

			zanrovi = lista[10].replace("(", "").replace(")", "").split(";");
			int[] zanrList = new int[zanrovi.length];

			for (int i = 0; i < zanrList.length; i++) {
				if (zanrovi[i] != null && !zanrovi[i].equals("")) {
					zanrList[i] = Integer.parseInt(zanrovi[i]);
				}
			}
			k.setZanrovi(zanrList);

			autors = lista[2].replace("(", "").replace(")", "").split(";");
			for (Autor a : autori) {
				for (int i = 0; i < autors.length; i++) {
					if (autors[i] != null && !autors[i].equals("")) {
						if (a.getId().equals(autors[i])) {
							k.getAutori().add(a);
						}
					}
				}
			}

			for (Izdavac i : izdavaci) {
				if (i.getId().equals(lista[3])) {
					k.setIzdavac(i);
					break;
				}
			}

			knjige.add(k);
		}

		br.close();
		return knjige;
	}

	/**Vrsi deserijalizaciju dozvola administratora. Ne zahteva prethodne deserijalizacije*/
	public static ArrayList<Administrator.Dozvole> deserializeDozvole() throws IOException{
		String line;
		String[] lista;
		ArrayList<Administrator.Dozvole> dozvole = new ArrayList<>();
		br = new BufferedReader(new FileReader(FOLDER + "dozvole.tdb"));

		while((line = br.readLine()) != null) {
			Administrator.Dozvole d = new Administrator.Dozvole();
			lista = line.split("~");

			d.setUserUUID(lista[0]);
			d.setAdmin(Boolean.parseBoolean(lista[1]));
			d.setCanAddAdmins(Boolean.parseBoolean(lista[2]));
			d.setCanAddMembers(Boolean.parseBoolean(lista[3]));
			d.setCanAddBooks(Boolean.parseBoolean(lista[4]));
			d.setMasterRule(Boolean.parseBoolean(lista[5]));
			d.setCanLoanBooks(Boolean.parseBoolean(lista[6]));
			d.setCanDeleteAdmins(Boolean.parseBoolean(lista[7]));
			d.setCanDeleteMembers(Boolean.parseBoolean(lista[8]));
			d.setCanDeleteBooks(Boolean.parseBoolean(lista[9]));
			d.setCanAlterRules(Boolean.parseBoolean(lista[10]));
			d.setCanEditAdmins(Boolean.parseBoolean(lista[11]));
			d.setCanEditMembers(Boolean.parseBoolean(lista[12]));
			d.setCanEditBooks(Boolean.parseBoolean(lista[13]));

			dozvole.add(d);
		}

		br.close();
		return dozvole;
	}

	public static ArrayList<Autor> deserializeAutore() throws IOException{
		String line;
		String[] lista;
		ArrayList<Autor> autori = new ArrayList<>();
		br = new BufferedReader(new FileReader(FOLDER + "autor.tdb"));

		while((line = br.readLine()) != null) {
			Autor a = new Autor();
			lista = line.split("~");

			a.setId(lista[0]);
			a.setIme(lista[1]);
			a.setPrezime(lista[2]);

			autori.add(a);
		}

		br.close();
		return autori;
	}

	public static ArrayList<Izdavac> deserializeIzdavace() throws IOException{
		String line;
		String[] lista;
		ArrayList<Izdavac> izdavaci = new ArrayList<>();
		br = new BufferedReader(new FileReader(FOLDER + "izdavac.tdb"));

		while((line = br.readLine()) != null) {
			Izdavac i = new Izdavac();
			lista = line.split("~");

			i.setId(lista[0]);
			i.setImeIzdavaca(lista[1]);
			i.setZemljaPorekla(lista[2]);

			izdavaci.add(i);
		}

		br.close();
		return izdavaci;
	}

	public static ArrayList<Pozajmljivanje> deserializePozajmljivanje(ArrayList<Knjiga> knjige) throws IOException, ParseException, TokProgramaException {

		if (knjige == null || knjige.size() == 0) {
			throw new TokProgramaException("Lista knjiga mora biti deserijalizovana pre liste pozajmljivanja!");
		}

		String line;
		String[] lista;
		ArrayList<Pozajmljivanje> pozajmljivanjaArrayList = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		br = new BufferedReader(new FileReader(FOLDER + "pozajmljivanja.tdb"));

		while((line = br.readLine()) != null) {
			Pozajmljivanje p = new Pozajmljivanje();
			lista = line.split("~");

			p.setClanUUID(lista[0]);
			p.setDug(Double.parseDouble(lista[1]));

			for (Knjiga k : knjige) {
				if (k.getId().equals(lista[2])) {
					p.setPozajmljenaKnjiga(k);
				}
			}

			cal.setTime(dateFormat.parse(lista[3]));
			p.setDatumPozajmljivanja(cal);

			cal.setTime(dateFormat.parse(lista[4]));
			p.setDatumVracanja(cal);

			p.setRazreseno(Boolean.parseBoolean(lista[5]));

			pozajmljivanjaArrayList.add(p);
		}

		br.close();
		return pozajmljivanjaArrayList;
	}

	public static PravilaBiblioteke deserializePravila() throws IOException{
		String line;
		String[] lista;
		PravilaBiblioteke pravila = new PravilaBiblioteke();
		br = new BufferedReader(new FileReader(FOLDER + "pravila.tdb"));

		if ((line = br.readLine()) != null) {
			lista = line.split("~");
			pravila.maxPeriod(Integer.parseInt(lista[0]))
					.maxReloan(Integer.parseInt(lista[1]))
					.multiplier(Double.parseDouble(lista[2]))
					.loanMultipleAtOnce(Boolean.parseBoolean(lista[3]))
					.setLoanBeforeReturningPrevious(Boolean.parseBoolean(lista[4]));
		}

		br.close();
		return pravila;
	}

	/**Brise sve fajlove iz 'data' foldera
	 * @param potvrda Potvrda da li da se brisu svi fajlovi.*/
	public static void resetSystem(boolean potvrda) {
		File folder;
		if (potvrda) {
			if ((folder = new File(FOLDER)).exists()) {
				for ( File fajl : folder.listFiles() ) {
					String temp = fajl.getName();
					if (!temp.contains("pravila")) {
						if(fajl.delete()) {
							System.out.println(String.format("Obrisan fajl '%s'", temp));
						}
						else {
							System.out.println(String.format("Nemoguce obrisati fajl '%s'", temp));
						}
					}
				}
			}
		}
	}

	public static void createFolder() {
		File folder = new File(FOLDER);
		if (!folder.exists()) {
			if (folder.mkdir()) {
				System.out.println("Kreiran folder data/");
			}
		}

	}

	public static void createFileEntries() throws IOException {
		createFolder();
		ArrayList<String> files = new ArrayList<String>() {{
			add("administrator.tdb");
			add("clan.tdb");
			add("knjiga.tdb");
			add("autor.tdb");
			add("izdavac.tdb");
			add("dozvole.tdb");
			add("pravila.tdb");
			add("sifra.tdb");
			add("pozajmljivanje.tdb");
		}};

		for (String s : files) {
			File file = new File(FOLDER + s);
			if (file.createNewFile()) {
				System.out.printf("Kreiran fajl '%s'%n", s);
			}
		}
	}
}
