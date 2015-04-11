package hr.fer.zemris.ecf.lab.view.fer.zemris.ecf.engine.conf.xml;

import hr.fer.zemris.ecf.lab.view.fer.zemris.ecf.engine.conf.ConfigurationService;
import hr.fer.zemris.ecf.lab.view.fer.zemris.ecf.engine.param.AlgGenRegUser;

import java.io.File;

public class XmlReadTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//reading
		//AlgGenRegInit sTANKOVIC_NAPRAVI_NESTO_S_OVIM =XmlReading.readInitial("lib/parameters.xml");
		
		//creating xml and writing in it
		//AlgGenRegInit agr = sTANKOVIC_NAPRAVI_NESTO_S_OVIM;
		
//		AlgGenReg4Writing sTANKOVIC_OVO_MI_TREBAS_STVORIT_SA_PARAMETRIMA_KOJE_KORISNIK_IZABERE
//		= new AlgGenReg4Writing(agr.algorithms, createGeotypesForTesting(agr.genotypes), agr.registry);
		AlgGenRegUser s = (new XmlConfigurationReader()).readArchive("test/pSta.txt");
		ConfigurationService.getInstance().getWriter().write(new File("test/out.xml"), s);

	}

//	private static List<List<Genotype>> createGeotypesForTesting(List<Genotype> genotypes) {
//		List<List<Genotype>> genSomething = new ArrayList<>();
//		genSomething.add(genotypes);
//		genSomething.add(genotypes);
//		return genSomething;
//	}

}
