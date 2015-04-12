package hr.fer.zemris.ecf.lab.engine.conf.xml;

import hr.fer.zemris.ecf.lab.engine.conf.ConfigurationReader;
import hr.fer.zemris.ecf.lab.engine.param.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is only used for reading xml parameters files. It uses static
 * methods for it. It has 2 specific static methods: readInitial(String file)
 * for reading initial parameters dumped by ECF and readArchive(String file) for
 * reading parameter files that have been created by the used and stored into
 * some kind of archive.
 * 
 * @version 1.0
 * 
 */
public class XmlConfigurationReader implements ConfigurationReader {

	/**
	 * This class is used for parsing initial parameters set given by ECF.
	 * 
	 * @param file
	 *            path to the parameters (.xml) given by ECF.
	 * @return AlgGenRegList class filed with necessary data.
	 */
	public ParametersList readInitial(File file) {
		try {
			return readingInitial(file);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			System.err.println("Error ocured while trying to gather initial data given by ECF in xml form.");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This class is used for parsing parameters that was user defined earlier
	 * and saved within an archive.
	 * 
	 * @param file
	 *            parameters file (.xml)
	 * @return AlgGenReg4Writing class filled with necessary data.
	 */
	public AlgGenRegUser readArchive(File file) {
		try {
			return readingArchive(file);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			System.err.println("Error ocured while trying to gather initial data given by ECF in xml form.");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This class is used for parsing initial parameters set given by ECF.
	 * 
	 * @param file
	 *            path to the parameters given by ECF.
	 * @throws org.xml.sax.SAXException
	 *             in case of problem.
	 * @throws java.io.IOException
	 *             in case of problem.
	 * @throws javax.xml.parsers.ParserConfigurationException
	 *             in case of problem.
	 */
	private ParametersList readingInitial(File file) throws SAXException, IOException, ParserConfigurationException {
		ParametersList agrList = new ParametersList();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(file);
		doc.getDocumentElement().normalize();
		NodeList ecf = doc.getChildNodes();
		Node tempNode = ecf.item(0);
		NodeList algGenReg = tempNode.getChildNodes();
		for (int count = 0; count < algGenReg.getLength(); count++) {
			switch (algGenReg.item(count).getNodeName()) {
			case "Algorithm":
				// System.out.println("a");
				algorithm(algGenReg.item(count), agrList.algorithms);
				break;
			case "Genotype":
				// System.out.println("g");
				genotype(algGenReg.item(count), agrList.genotypes);
				break;
			case "Registry":
				// System.out.println("r");
				agrList.registry = new Registry();
				registry(algGenReg.item(count), agrList.registry);
				break;
			default:
				// No default, for your own good don't write here.
				break;
			}
		}
		return agrList;
	}

	/**
	 * This class is used for parsing parameters that was user defined earlier
	 * and saved within an archive.
	 * 
	 * @param file
	 *            parameters file.
	 * @throws org.xml.sax.SAXException
	 *             in case of problem.
	 * @throws java.io.IOException
	 *             in case of problem.
	 * @throws javax.xml.parsers.ParserConfigurationException
	 *             in case of problem.
	 */
	private AlgGenRegUser readingArchive(File file) throws SAXException, IOException, ParserConfigurationException {
		AlgGenRegUser agru = new AlgGenRegUser();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(file);
		doc.getDocumentElement().normalize();
		NodeList ecf = doc.getChildNodes();

		Node comment = ecf.item(0);
		Node tempNode;
		if (comment instanceof Comment) {
			agru.userComment = comment.getNodeValue();
			tempNode = ecf.item(1);
		} else {
			agru.userComment = agru.getUserComment();
			tempNode = ecf.item(0);
		}

		NodeList algGenReg = tempNode.getChildNodes();
		ArrayList<EntryBlock> genList = new ArrayList<>();
		agru.genotypes.add(genList);
		for (int count = 0; count < algGenReg.getLength(); count++) {
			switch (algGenReg.item(count).getNodeName()) {
			case "Algorithm":
				algorithm(algGenReg.item(count), agru.algorithms);
				break;
			case "Genotype":
				genotype(algGenReg.item(count), genList);
				break;
			case "Registry":
				registry(algGenReg.item(count), agru.registry);
				break;
			default:
				break;
			}
		}
		return agru;
	}

	/**
	 * This method is used to fill the given {@link Registry} with given
	 * {@link org.w3c.dom.Node}.
	 * 
	 * @param item
	 *            node containing the registry.
	 * @param registry
	 *            {@link Registry} class to be filled from node.
	 */
	private void registry(Node item, Registry registry) {
		NodeList registries = item.getChildNodes();

		for (int i = 0; i < registries.getLength(); i++) {
			Node param = registries.item(i);
			if (param.getNodeType() == Node.ELEMENT_NODE) {
				Entry entry = new Entry();
				entry.key = ((Element) param).getAttribute("key");
				entry.desc = ((Element) param).getAttribute("desc");
				entry.value = param.getTextContent();
				registry.getEntryList().add(entry);
				// System.out.print("Node Atribute =" +entry.key
				// +" "+entry.desc);
				// System.out.println("   Node Value =" + entry.value);
			}
		}

	}

	/**
	 * This method is used to fill the given genotype list with given
	 * {@link org.w3c.dom.Node}.
	 * 
	 * @param item
	 *            node containing the genotype block.
	 * @param genList
	 *            Genotype list to be filled from node.
	 */
	private void genotype(Node item, List<EntryBlock> genList) {
		NodeList genotypes = item.getChildNodes();

		for (int j = 0; j < genotypes.getLength(); j++) {
			Node genotype = genotypes.item(j);

			if (genotype.getNodeType() == Node.ELEMENT_NODE) {
				EntryBlock gen = new EntryBlock(genotype.getNodeName());

				// System.out.println(gen.getName());

				NodeList genotypeParams = genotype.getChildNodes();

				for (int i = 0; i < genotypeParams.getLength(); i++) {
					Node param = genotypeParams.item(i);

					if (param.getNodeType() == Node.ELEMENT_NODE) {
						Entry entry = new Entry();
						entry.key = ((Element) param).getAttribute("key");
						entry.desc = ((Element) param).getAttribute("desc");
						entry.value = param.getTextContent();
						gen.getEntryList().add(entry);
						// System.out.print("Node Atribute =" +
						// entry.key+" "+entry.desc);
						// System.out.println("   Node Value =" + entry.value);
					}
				}
				genList.add(gen);
			}
		}
	}

	/**
	 * This method is used to fill the given algorithm list with given
	 * {@link org.w3c.dom.Node}.
	 * 
	 * @param item
	 *            node containing the algorithms block.
	 * @param algorithmsList
	 *            Algorithm list to be filled from node.
	 */
	private void algorithm(Node item, List<EntryBlock> algorithmsList) {
		NodeList algorithms = item.getChildNodes();

		for (int j = 0; j < algorithms.getLength(); j++) {
			Node algorithm = algorithms.item(j);

			if (algorithm.getNodeType() == Node.ELEMENT_NODE) {
				EntryBlock alg = new EntryBlock(algorithm.getNodeName());
				// System.out.println(alg.getName());

				NodeList algorithmParams = algorithm.getChildNodes();

				for (int i = 0; i < algorithmParams.getLength(); i++) {
					Node param = algorithmParams.item(i);

					if (param.getNodeType() == Node.ELEMENT_NODE) {
						Entry entry = new Entry();
						entry.key = ((Element) param).getAttribute("key");
						entry.desc = ((Element) param).getAttribute("desc");
						entry.value = param.getTextContent();
						alg.getEntryList().add(entry);
						// System.out.print("Node Atribute =" +
						// entry.key+" "+entry.desc);
						// System.out.println("   Node Value =" + entry.value);
					}
				}
				algorithmsList.add(alg);
			}
		}
	}
}
