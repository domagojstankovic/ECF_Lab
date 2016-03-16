package hr.fer.zemris.ecf.lab.view.layout;

import hr.fer.zemris.ecf.lab.engine.param.Configuration;
import hr.fer.zemris.ecf.lab.engine.param.Entry;
import hr.fer.zemris.ecf.lab.engine.param.EntryBlock;
import hr.fer.zemris.ecf.lab.model.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dstankovic on 3/1/16.
 */
public class DefaultConfigurationsCreator implements ConfigurationsCreator {
  private static class EntryPair {
    MultiEntry multiEntry;
    Entry entry; // reference matters (way to change template configuration)

    public EntryPair(MultiEntry multiEntry, Entry entry) {
      this.multiEntry = multiEntry;
      this.entry = entry;
    }
  }

  @Override
  public List<Pair<Configuration, List<Pair<String, String>>>> createConfigurations(List<MultiEntryBlock> algorithmList,
                                                                                    List<List<MultiEntryBlock>> genotypeListBlock,
                                                                                    MultiEntryBlock registry) {
    List<EntryPair> multiEntryPairs = new ArrayList<>();

    Configuration configuration = createTemplateConfiguration(
        multiEntryPairs, algorithmList, genotypeListBlock, registry
    );

    return createAllConfigurations(configuration, multiEntryPairs);
  }

  private static List<Pair<Configuration, List<Pair<String, String>>>> createAllConfigurations(
      Configuration configuration,
      List<EntryPair> multiEntryPairs) {

    List<Pair<Configuration, List<Pair<String, String>>>> configurations = new ArrayList<>();
    solve(configurations, configuration, multiEntryPairs, new LinkedList<>(), 0);
    return configurations;
  }

  private static void solve(List<Pair<Configuration, List<Pair<String, String>>>> configurations,
                            Configuration configuration,
                            List<EntryPair> multiEntryPairs,
                            List<Pair<String, String>> descriptors,
                            int index) {
    if (index >= multiEntryPairs.size()) {
      Configuration config = configuration.copy();
      List<Pair<String, String>> descriptorList = new ArrayList<>(descriptors.size());
      for (Pair<String, String> desc : descriptors) {
        descriptorList.add(desc.shallowCopy());
      }
      configurations.add(new Pair<>(config, descriptorList));
      return;
    }
    EntryPair pair = multiEntryPairs.get(index);
    List<String> values = pair.multiEntry.values;
    for (String value : values) {
      Entry entry = pair.entry;
      entry.value = value;
      descriptors.add(new Pair<>(pair.multiEntry.key, value));
      solve(configurations, configuration, multiEntryPairs, descriptors, index + 1);
      descriptors.remove(descriptors.size() - 1);
    }
  }

  private static Configuration createTemplateConfiguration(List<EntryPair> multiEntryPairs,
                                                           List<MultiEntryBlock> algorithmList,
                                                           List<List<MultiEntryBlock>> genotypeListBlock,
                                                           MultiEntryBlock registry) {
    Configuration configuration = new Configuration();

    for (MultiEntryBlock algorithmBlock : algorithmList) {
      EntryBlock entryBlock = extractBlock(algorithmBlock, multiEntryPairs);
      configuration.algorithms.add(entryBlock);
    }

    for (List<MultiEntryBlock> genotypeBlock : genotypeListBlock) {
      List<EntryBlock> blocks = new ArrayList<>();
      for (MultiEntryBlock algorithmBlock : genotypeBlock) {
        EntryBlock entryBlock = extractBlock(algorithmBlock, multiEntryPairs);
        blocks.add(entryBlock);
      }
      configuration.genotypes.add(blocks);
    }

    EntryBlock entryBlock = extractBlock(registry, multiEntryPairs);
    configuration.registry = entryBlock;

    return configuration;
  }

  private static EntryBlock extractBlock(MultiEntryBlock multiEntryBlock, List<EntryPair> multiEntryPairs) {
    List<Entry> entryList = new ArrayList<>(multiEntryBlock.getEntries().size());
    EntryBlock entryBlock = new EntryBlock(multiEntryBlock.getName(), entryList);
    for (MultiEntry multiEntry : multiEntryBlock.getEntries()) {
      List<String> values = multiEntry.values;
      if (values.size() == 0) {
        continue;
      }
      Entry entry = new Entry(multiEntry.key, multiEntry.desc, multiEntry.values.get(0));
      if (values.size() > 1) {
        multiEntryPairs.add(new EntryPair(multiEntry, entry));
      }
      entryList.add(entry);
    }
    return entryBlock;
  }
}
