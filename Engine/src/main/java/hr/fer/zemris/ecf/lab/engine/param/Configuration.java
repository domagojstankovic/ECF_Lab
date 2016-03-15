package hr.fer.zemris.ecf.lab.engine.param;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the container for the configuration file that will be given to the ECF by the user.
 *
 * @version 1.0
 */
public class Configuration {

    /**
     * List of algorithms to be written in the algorithms block.
     */
    public List<EntryBlock> algorithms;
    /**
     * List of list of genotype to be written in the multiple genotype blocks.
     * One list of genotype in one genotype block.
     */
    public List<List<EntryBlock>> genotypes;
    /**
     * Registry to be written into registry block.
     */
    public EntryList registry;
    /**
     * User comments to be written at the beginning of the parameters file as the xml comment.
     */
    public String userComment = "";

    /**
     * Constructor it initializes genotypes outer list to new array list.
     */
    public Configuration() {
        algorithms = new ArrayList<>();
        genotypes = new ArrayList<>();
        registry = new EntryList();
    }

    /**
     * Constructor it gets the reference to algorithms, genotypes blocks, genotypes blocks.
     *
     * @param algorithms given algorithms.
     * @param genotypes  given genotypes blocks.
     * @param registry   given registry.
     */
    public Configuration(List<EntryBlock> algorithms, List<List<EntryBlock>> genotypes, EntryList registry) {
        this.algorithms = algorithms;
        this.genotypes = genotypes;
        this.registry = registry;
    }

    /**
     * This method is used for retrieving user comment.	 *
     *
     * @return user comment or text: "There are no user comments for this parameters."
     */
    public String getUserComment() {
        if (!userComment.isEmpty()) {
            return userComment;
        }
        return "There are no user comments for this parameters.";
    }

    public Configuration copy() {
        List<EntryBlock> newAlgorithms = copyEntryBlockList(algorithms);

        List<List<EntryBlock>> newGenotypes = new ArrayList<>(1);
        newGenotypes.add(copyEntryBlockList(genotypes.get(0)));

        EntryList newRegistry = registry.copy();

        return new Configuration(newAlgorithms, newGenotypes, newRegistry);
    }

    private static List<EntryBlock> copyEntryBlockList(List<EntryBlock> blocks) {
        List<EntryBlock> entryBlocks = new ArrayList<>(blocks.size());
        for (EntryBlock block : blocks) {
            entryBlocks.add(block.copy());
        }
        return entryBlocks;
    }
}
