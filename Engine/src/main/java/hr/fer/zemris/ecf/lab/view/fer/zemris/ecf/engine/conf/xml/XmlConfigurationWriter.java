package hr.fer.zemris.ecf.lab.view.fer.zemris.ecf.engine.conf.xml;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import hr.fer.zemris.ecf.lab.view.fer.zemris.ecf.engine.conf.ConfigurationWriter;
import hr.fer.zemris.ecf.lab.view.fer.zemris.ecf.engine.param.AlgGenRegUser;
import hr.fer.zemris.ecf.lab.view.fer.zemris.ecf.engine.param.Algorithm;
import hr.fer.zemris.ecf.lab.view.fer.zemris.ecf.engine.param.Entry;
import hr.fer.zemris.ecf.lab.view.fer.zemris.ecf.engine.param.Genotype;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class is only used for writing (creating) xml parameters files.
 * It has one public static method that writes given parameters in form of a {@link hr.fer.zemris.ecf.lab.view.fer.zemris.ecf.engine.param.AlgGenRegUser} to given file path.
 * @version 1.0
 *
 */
public class XmlConfigurationWriter implements ConfigurationWriter {

	/**
	 * This method is used for writing given parameters and user comments to the file.
	 * @param file path to write parameters to.
	 * @param agrwGet class filled with needed parameters to give to ECF.
	 */
	public void write(File file, AlgGenRegUser agrwGet) {
		try {
			writing(file, agrwGet);
		} catch (ParserConfigurationException | TransformerException e) {
			System.err.println("Error ocured while trying to create a parameters xml for ECF.");
			e.printStackTrace();
		}
	}

	/**
	 * This method is used for writing parameters and user comments to the file.
	 * @param file path to write parameters to.
	 * @param agrw
	 * @throws javax.xml.parsers.ParserConfigurationException in case of problem.
	 * @throws javax.xml.transform.TransformerException in case of problem.
	 */
	private void writing(File file, AlgGenRegUser agrw) throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		Document doc = docBuilder.newDocument();
		Comment coment = doc.createComment(agrw.getUserComment());
		doc.appendChild(coment);
		Element rootElement = doc.createElement("ECF");
		doc.appendChild(rootElement);
		
		if(!agrw.algorithm.isEmpty()){
			Element algorithms = doc.createElement("Algorithm");
			algorithm(algorithms, doc, agrw);
			rootElement.appendChild(algorithms);
		}
		
		
		for (List<Genotype> gList : agrw.genotypes){
			if(!gList.isEmpty()){
				Element genotypes = doc.createElement("Genotype");
				genotype(genotypes, doc,gList);
				rootElement.appendChild(genotypes);
			}
		}
		
		if(!agrw.registry.getEntryList().isEmpty()){
			Element registry = doc.createElement("Registry");
			registry(registry, doc, agrw);
			rootElement.appendChild(registry);
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(file);
		//StreamResult result = new StreamResult(System.out); //  For testing.
		transformer.transform(source, result);
		
	}

	/**
	 * This method is used to write the {@link hr.fer.zemris.ecf.lab.view.fer.zemris.ecf.engine.param.Registry} with given {@link org.w3c.dom.Document}.
	 * @param registry {@link org.w3c.dom.Element} class to be written on the document.
	 * @param doc document to be written on.
	 */
	private void registry(Element registry, Document doc, AlgGenRegUser agrw) {
		List<Entry> eList = agrw.registry.getEntryList();
		for(int i=0; i<eList.size(); i++){
			Entry e = eList.get(i);
			Element entry = doc.createElement("Entry");
			entry.setAttribute("key", e.key);
			entry.setAttribute("desc", e.desc);
			entry.appendChild(doc.createTextNode(e.value));
			registry.appendChild(entry);
			
		}
	}

	/**
	 * This method is used to write the {@link Genotype} list with given {@link org.w3c.dom.Document}.
	 * @param genotypes genotype {@link org.w3c.dom.Element} class to be written on the document.
	 * @param doc document to be written on.
	 * @param gList given {@link Genotype} list.
	 */
	private void genotype(Element genotypes, Document doc, List<Genotype> gList) {
		
		for(Genotype genotype : gList) {
			Element genType = doc.createElement(genotype.getName());
			
			List<Entry> eList = genotype.getEntryList();
			for(int i=0; i<eList.size(); i++){
				Entry e = eList.get(i);
				Element entry = doc.createElement("Entry");
				entry.setAttribute("key", e.key);
				entry.setAttribute("desc", e.desc);
				entry.appendChild(doc.createTextNode(e.value));
				genType.appendChild(entry);
				
			}
			
			genotypes.appendChild(genType);
		}
		
	}

	/**
	 * This method is used to write the {@link hr.fer.zemris.ecf.lab.view.fer.zemris.ecf.engine.param.Algorithm} list with given {@link org.w3c.dom.Document}.
	 * @param algorithms algorithm {@link org.w3c.dom.Element} class to be written on the document.
	 * @param doc document to be written on.
	 */
	private void algorithm(Element algorithms, Document doc, AlgGenRegUser agrw) {
		
		List<Algorithm> aList = agrw.algorithm;
		for(Algorithm algorithm : aList){
			Element algType = doc.createElement(algorithm.getName());
			
			List<Entry> eList = algorithm.getEntryList();
			for(int i=0; i<eList.size(); i++){
				Entry e = eList.get(i);
				Element entry = doc.createElement("Entry");
				entry.setAttribute("key", e.key);
				entry.setAttribute("desc", e.desc);
				entry.appendChild(doc.createTextNode(e.value));
				algType.appendChild(entry);
				
			}
			
			algorithms.appendChild(algType);
		}
		
	}	

}
